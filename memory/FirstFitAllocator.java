package memory;

import java.util.ListIterator;
import java.util.List;

public class FirstFitAllocator extends MemoryAllocator {

    public FirstFitAllocator(List<MemoryBlock> initialBlocks) {
        super(initialBlocks);
    }

    public FirstFitAllocator() {
        super();
    }

    @Override
    public boolean allocate(int processId, int processSize) {
        if (processSize <= 0) {
            allocationLog.add("FirstFit allocate failed: requested size must be > 0");
            return false;
        }

        // First Fit: list ke start se first suitable block choose karna.
        ListIterator<MemoryBlock> it = blocks.listIterator();
        while (it.hasNext()) {
            MemoryBlock block = it.next();
            if (!block.isFree()) {
                continue;
            }
            if (block.getSize() < processSize) {
                continue;
            }

            if (block.getSize() == processSize) {
                // Perfect fit: split ki zarurat nahi.
                block.setAllocatedProcessId(processId);
                block.setRequestedSize(processSize);
                allocationLog.add("FirstFit allocated pid=" + processId + " into blockId=" + block.getBlockId() +
                        " (start=" + block.getStartAddress() + ", size=" + processSize + ")");
                return true;
            }

            // Split: allocated part + leftover free part.
            int originalSize = block.getSize();
            int remainingSize = originalSize - processSize;
            int newFreeStart = block.getStartAddress() + processSize;

            block.setSize(processSize);
            block.setAllocatedProcessId(processId);
            block.setRequestedSize(processSize);

            int newBlockId = nextBlockId();
            MemoryBlock remainder = new MemoryBlock(newBlockId, newFreeStart, remainingSize);
            remainder.setFree(true);

            it.add(remainder);

            allocationLog.add("FirstFit allocated pid=" + processId + " by splitting blockId=" + block.getBlockId() +
                    " (start=" + block.getStartAddress() + ", size=" + processSize + ")" +
                    "; created free blockId=" + newBlockId + " (start=" + newFreeStart + ", size=" + remainingSize + ")");
            return true;
        }

        allocationLog.add("FirstFit allocation failed for pid=" + processId + " (size=" + processSize + "): no block fits");
        return false;
    }

    @Override
    public boolean deallocate(int processId) {
        // Same processId wale blocks free karne hain.
        boolean freedAny = false;

        for (MemoryBlock block : blocks) {
            Integer pid = block.getAllocatedProcessId();
            if (pid != null && pid == processId) {
                block.setFree(true);
                freedAny = true;
            }
        }

        if (!freedAny) {
            allocationLog.add("FirstFit deallocate: pid=" + processId + " not found");
            return false;
        }

        mergeAdjacentFreeBlocks();
        // Merge se external fragmentation kam hota hai.
        allocationLog.add("FirstFit deallocate: freed pid=" + processId);
        return true;
    }

    private int nextBlockId() {
        int max = -1;
        for (MemoryBlock b : blocks) {
            if (b.getBlockId() > max) {
                max = b.getBlockId();
            }
        }
        return max + 1;
    }

    private void mergeAdjacentFreeBlocks() {
        // Adjacent free blocks ko ek saath merge karte hain.
        if (blocks.isEmpty()) {
            return;
        }

        ListIterator<MemoryBlock> it = blocks.listIterator();
        MemoryBlock prev = null;
        while (it.hasNext()) {
            MemoryBlock current = it.next();
            if (prev == null) {
                prev = current;
                continue;
            }

            if (prev.isFree() && current.isFree() &&
                    prev.getStartAddress() + prev.getSize() == current.getStartAddress()) {
                // merge current into prev
                prev.setSize(prev.getSize() + current.getSize());
                it.remove();
                // keep prev as-is and continue merging with the next
            } else {
                prev = current;
            }
        }
    }
}

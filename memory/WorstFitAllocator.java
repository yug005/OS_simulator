package memory;

import java.util.ListIterator;
import java.util.List;

public class WorstFitAllocator extends MemoryAllocator {

    public WorstFitAllocator(List<MemoryBlock> initialBlocks) {
        super(initialBlocks);
    }

    public WorstFitAllocator() {
        super();
    }

    @Override
    public boolean allocate(int processId, int processSize) {
        if (processSize <= 0) {
            allocationLog.add("WorstFit allocate failed: requested size must be > 0");
            return false;
        }

        // Worst Fit: largest free block choose karte hain (future big reqs ke liye).
        int worstIndex = -1;
        int worstBlockSize = -1;
        int index = 0;
        for (MemoryBlock block : blocks) {
            if (block.isFree() && block.getSize() >= processSize && block.getSize() > worstBlockSize) {
                worstIndex = index;
                worstBlockSize = block.getSize();
            }
            index++;
        }

        if (worstIndex == -1) {
            allocationLog.add("WorstFit allocation failed for pid=" + processId + " (size=" + processSize + "): no block fits");
            return false;
        }

        // Selected block par allocate karo, zarurat ho to split.
        ListIterator<MemoryBlock> it = blocks.listIterator();
        int i = 0;
        while (it.hasNext()) {
            MemoryBlock block = it.next();
            if (i != worstIndex) {
                i++;
                continue;
            }

            if (!block.isFree() || block.getSize() < processSize) {
                allocationLog.add("WorstFit allocation failed for pid=" + processId + " (size=" + processSize + "): block changed");
                return false;
            }

            if (block.getSize() == processSize) {
                // Perfect fit: no split needed.
                block.setAllocatedProcessId(processId);
                block.setRequestedSize(processSize);
                allocationLog.add("WorstFit allocated pid=" + processId + " into blockId=" + block.getBlockId() +
                        " (start=" + block.getStartAddress() + ", size=" + processSize + ")");
                return true;
            }

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

            allocationLog.add("WorstFit allocated pid=" + processId + " by splitting blockId=" + block.getBlockId() +
                    " (start=" + block.getStartAddress() + ", size=" + processSize + ")" +
                    "; created free blockId=" + newBlockId + " (start=" + newFreeStart + ", size=" + remainingSize + ")");
            return true;
        }

        allocationLog.add("WorstFit allocation failed for pid=" + processId + " (size=" + processSize + "): internal error");
        return false;
    }

    @Override
    public boolean deallocate(int processId) {
        // Free + merge to reduce fragmentation.
        boolean freedAny = false;

        for (MemoryBlock block : blocks) {
            Integer pid = block.getAllocatedProcessId();
            if (pid != null && pid == processId) {
                block.setFree(true);
                freedAny = true;
            }
        }

        if (!freedAny) {
            allocationLog.add("WorstFit deallocate: pid=" + processId + " not found");
            return false;
        }

        mergeAdjacentFreeBlocks();
        allocationLog.add("WorstFit deallocate: freed pid=" + processId);
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
        // Adjacent free blocks ko merge karte hain.
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
                prev.setSize(prev.getSize() + current.getSize());
                it.remove();
            } else {
                prev = current;
            }
        }
    }
}

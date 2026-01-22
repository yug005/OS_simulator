package memory;

import java.util.ListIterator;
import java.util.List;

public class BestFitAllocator extends MemoryAllocator {

    public BestFitAllocator(List<MemoryBlock> initialBlocks) {
        super(initialBlocks);
    }

    public BestFitAllocator() {
        super();
    }

    @Override
    public boolean allocate(int processId, int processSize) {
        if (processSize <= 0) {
            allocationLog.add("BestFit allocate failed: requested size must be > 0");
            return false;
        }

        // Best Fit: smallest sufficient block choose karte hain (waste kam karne ke liye).
        int bestIndex = -1;
        int bestBlockSize = Integer.MAX_VALUE;
        int index = 0;
        for (MemoryBlock block : blocks) {
            if (block.isFree() && block.getSize() >= processSize && block.getSize() < bestBlockSize) {
                bestIndex = index;
                bestBlockSize = block.getSize();
            }
            index++;
        }

        if (bestIndex == -1) {
            allocationLog.add("BestFit allocation failed for pid=" + processId + " (size=" + processSize + "): no block fits");
            return false;
        }

        // Allocate at bestIndex, splitting if needed.
        ListIterator<MemoryBlock> it = blocks.listIterator();
        int i = 0;
        while (it.hasNext()) {
            MemoryBlock block = it.next();
            if (i != bestIndex) {
                i++;
                continue;
            }

            if (!block.isFree() || block.getSize() < processSize) {
                allocationLog.add("BestFit allocation failed for pid=" + processId + " (size=" + processSize + "): block changed");
                return false;
            }

            if (block.getSize() == processSize) {
                // Perfect fit case.
                block.setAllocatedProcessId(processId);
                block.setRequestedSize(processSize);
                allocationLog.add("BestFit allocated pid=" + processId + " into blockId=" + block.getBlockId() +
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

            allocationLog.add("BestFit allocated pid=" + processId + " by splitting blockId=" + block.getBlockId() +
                    " (start=" + block.getStartAddress() + ", size=" + processSize + ")" +
                    "; created free blockId=" + newBlockId + " (start=" + newFreeStart + ", size=" + remainingSize + ")");
            return true;
        }

        allocationLog.add("BestFit allocation failed for pid=" + processId + " (size=" + processSize + "): internal error");
        return false;
    }

    @Override
    public boolean deallocate(int processId) {
        // Free karne ke baad merge, taaki fragmentation kam ho.
        boolean freedAny = false;

        for (MemoryBlock block : blocks) {
            Integer pid = block.getAllocatedProcessId();
            if (pid != null && pid == processId) {
                block.setFree(true);
                freedAny = true;
            }
        }

        if (!freedAny) {
            allocationLog.add("BestFit deallocate: pid=" + processId + " not found");
            return false;
        }

        mergeAdjacentFreeBlocks();
        allocationLog.add("BestFit deallocate: freed pid=" + processId);
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
        // Adjacent free blocks merge to reduce external fragmentation.
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

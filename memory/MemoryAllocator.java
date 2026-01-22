package memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public abstract class MemoryAllocator {

    protected final LinkedList<MemoryBlock> blocks;
    protected final List<String> allocationLog;

    protected MemoryAllocator(List<MemoryBlock> initialBlocks) {
        // LinkedList use kiya hai kyunki insert/remove between me hota hai (splitting).
        if (initialBlocks == null) {
            this.blocks = new LinkedList<>();
        } else {
            this.blocks = new LinkedList<>(initialBlocks);
        }
        this.allocationLog = new ArrayList<>();
    }

    protected MemoryAllocator() {
        this(null);
    }

    public List<MemoryBlock> getBlocks() {
        // External code ko safe read-only list milti hai.
        return Collections.unmodifiableList(blocks);
    }

    public List<String> getAllocationLog() {
        return Collections.unmodifiableList(allocationLog);
    }

    /**
     * Allocate memory for a process.
     */
    public abstract boolean allocate(int processId, int processSize);

    /**
     * Deallocate (free) memory for a process.
     */
    public abstract boolean deallocate(int processId);

    /**
     * Generate a report with totals and fragmentation metrics.
     */
    public MemoryReport generateReport() {
        // Total/used/free + fragmentation metrics nikalte hain.
        long total = 0;
        long used = 0;
        long free = 0;
        int largestFree = 0;
        int internalFragmentation = 0;
        for (MemoryBlock block : blocks) {
            total += block.getSize();
            if (!block.isFree()) {
                used += block.getSize();
                Integer requested = block.getRequestedSize();
                if (requested != null && requested <= block.getSize()) {
                    // Internal fragmentation = allocated size - requested size.
                    internalFragmentation += (block.getSize() - requested);
                }
            }
            if (block.isFree()) {
                free += block.getSize();
                if (block.getSize() > largestFree) {
                    largestFree = block.getSize();
                }
            }
        }
        if (free == 0) {
            largestFree = 0;
        }

        // External fragmentation = free memory split ho gayi hai (largest free se compare).
        int externalFragmentation = 0;
        if (free > 0 && free > largestFree) {
            externalFragmentation = (int) (free - largestFree);
        }

        return new MemoryReport(
                (int) total,
                (int) used,
                (int) free,
                internalFragmentation,
                externalFragmentation,
                allocationLog
        );
    }

    /**
     * Compact memory by shifting allocated blocks to the lowest addresses
     * and merging all free space into one block at the end.
     */
    public void compactMemory() {
        // Compaction: allocated blocks ko left shift, free ko end me merge.
        if (blocks.isEmpty()) {
            allocationLog.add("Compaction skipped: no blocks");
            return;
        }

        List<MemoryBlock> ordered = new ArrayList<>(blocks);
        ordered.sort(Comparator.comparingInt(MemoryBlock::getStartAddress));

        LinkedList<MemoryBlock> compacted = new LinkedList<>();
        int nextStart = ordered.get(0).getStartAddress();
        int totalMemory = 0;
        int usedMemory = 0;
        int maxBlockId = -1;

        for (MemoryBlock block : ordered) {
            totalMemory += block.getSize();
            if (block.getBlockId() > maxBlockId) {
                maxBlockId = block.getBlockId();
            }
            if (!block.isFree()) {
                // Allocated block ko nextStart par shift kar rahe hain.
                MemoryBlock allocated = block;
                allocated.setStartAddress(nextStart);
                nextStart += allocated.getSize();
                usedMemory += allocated.getSize();
                compacted.add(allocated);
            }
        }

        int freeMemory = totalMemory - usedMemory;
        if (freeMemory > 0) {
            // Total free ko single block me merge.
            int newFreeId = maxBlockId + 1;
            MemoryBlock freeBlock = new MemoryBlock(newFreeId, nextStart, freeMemory);
            freeBlock.setFree(true);
            compacted.add(freeBlock);
        }

        blocks.clear();
        blocks.addAll(compacted);

        allocationLog.add("Compaction completed: allocated=" + usedMemory + ", free=" + freeMemory +
                ", start=" + ordered.get(0).getStartAddress() + ", blocks=" + blocks.size());
    }
}

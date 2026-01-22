package memory;

import java.util.ArrayList;
import java.util.List;

public class MemoryValidation {

    public static void main(String[] args) {
        List<MemoryBlock> initialBlocks = new ArrayList<>();
        initialBlocks.add(new MemoryBlock(0, 0, 100));

        runScenario("First Fit", new FirstFitAllocator(cloneBlocks(initialBlocks)));
        runScenario("Best Fit", new BestFitAllocator(cloneBlocks(initialBlocks)));
        runScenario("Worst Fit", new WorstFitAllocator(cloneBlocks(initialBlocks)));
    }

    private static void runScenario(String name, MemoryAllocator allocator) {
        System.out.println("\n==== " + name + " ====");

        allocator.allocate(1, 20);
        allocator.allocate(2, 15);
        allocator.allocate(3, 25);
        allocator.deallocate(2);

        System.out.println("Before compaction:");
        printBlocks(allocator.getBlocks());
        printReport(allocator.generateReport());

        allocator.compactMemory();

        System.out.println("After compaction:");
        printBlocks(allocator.getBlocks());
        printReport(allocator.generateReport());
    }

    private static List<MemoryBlock> cloneBlocks(List<MemoryBlock> blocks) {
        List<MemoryBlock> copy = new ArrayList<>();
        for (MemoryBlock block : blocks) {
            MemoryBlock clone = new MemoryBlock(block.getBlockId(), block.getStartAddress(), block.getSize());
            clone.setFree(true);
            copy.add(clone);
        }
        return copy;
    }

    private static void printBlocks(List<MemoryBlock> blocks) {
        // Current blocks ko print karna, layout samajhne ke liye.
        for (MemoryBlock block : blocks) {
            System.out.println("  " + block);
        }
    }

    private static void printReport(MemoryReport report) {
        // Report metrics: totals + fragmentation.
        System.out.println("  Total Memory: " + report.getTotalMemory());
        System.out.println("  Used Memory: " + report.getUsedMemory());
        System.out.println("  Free Memory: " + report.getFreeMemory());
        System.out.println("  Internal Fragmentation: " + report.getInternalFragmentation());
        System.out.println("  External Fragmentation: " + report.getExternalFragmentation());
    }
}

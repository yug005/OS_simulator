package disk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiskComparison {

    public static void main(String[] args) {
        // Fixed workload se algorithms ka fair comparison.
        int headStart = 53;
        int diskSize = 200;
        List<Integer> requests = Arrays.asList(98, 183, 37, 122, 14, 124, 65, 67);

        List<AlgorithmResult> results = new ArrayList<>();
        results.add(run("FCFS", new FCFSDiskScheduler(), headStart, requests, diskSize));
        results.add(run("SSTF", new SSTFDiskScheduler(), headStart, requests, diskSize));
        results.add(run("SCAN", new SCANDiskScheduler(), headStart, requests, diskSize));
        results.add(run("C-SCAN", new CSCANDiskScheduler(), headStart, requests, diskSize));

        printReport(results, requests.size());
    }

    private static AlgorithmResult run(String name, DiskScheduler scheduler, int headStart, List<Integer> requests, int diskSize) {
        // Scheduler run karke total seek capture.
        DiskScheduleResult result = scheduler.schedule(headStart, requests, diskSize);
        return new AlgorithmResult(name, result.getTotalHeadMovement());
    }

    private static void printReport(List<AlgorithmResult> results, int requestCount) {
        // Avg seek = total / requests count.
        System.out.println("Algorithm   Total Seek   Avg Seek");
        for (AlgorithmResult result : results) {
            double avg = requestCount == 0 ? 0.0 : (double) result.totalSeek / requestCount;
            System.out.printf("%-10s %-11d %.1f%n", result.name, result.totalSeek, avg);
        }
    }

    private static class AlgorithmResult {
        private final String name;
        private final int totalSeek;

        private AlgorithmResult(String name, int totalSeek) {
            this.name = name;
            this.totalSeek = totalSeek;
        }
    }
}

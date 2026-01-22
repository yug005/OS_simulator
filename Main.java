import java.util.ArrayList;
import java.util.List;

import models.Process;
import scheduling.FCFS;
import scheduling.RoundRobin;


public class Main {
    public static void main(String[] args) {
        // Entry point: yahin se demo run hota hai.
        System.out.println("OS Simulator starting...");

        // Hardcoded processes = quick demo ke liye, input parsing avoid.
        List<Process> processes = createHardcodedProcesses();

        // FCFS scheduling demo
        FCFS fcfs = new FCFS();
        FCFS.Result result = fcfs.simulate(processes);

        System.out.println();
        System.out.println("FCFS summary:");
        printProcessTable(result);

        System.out.println();
        System.out.println("Average waiting time: " + result.getAverageWaitingTime());
        System.out.println("Average turnaround time: " + result.getAverageTurnaroundTime());

        System.out.println();
        System.out.println("Gantt Chart (FCFS):");
        System.out.println(FCFS.formatGanttChart(result));

        // Round Robin
        int timeQuantum = 2;
        // RR me quantum fixed rakha hai taaki output stable rahe.
        RoundRobin rr = new RoundRobin();
        RoundRobin.Result rrResult = rr.simulate(processes, timeQuantum);

        System.out.println();
        System.out.println("Round Robin summary (q=" + timeQuantum + "):");
        printProcessTable(rrResult);

        System.out.println();
        System.out.println("Average waiting time: " + rrResult.getAverageWaitingTime());
        System.out.println("Average turnaround time: " + rrResult.getAverageTurnaroundTime());

        System.out.println();
        System.out.println("Gantt Chart (Round Robin):");
        System.out.println(RoundRobin.formatGanttChart(rrResult));
    }

    private static void printProcessTable(FCFS.Result result) {
        // Table format: PID, AT, BT, CT, WT, TAT
        System.out.printf("%-5s %-5s %-5s %-5s %-5s %-5s%n", "PID", "AT", "BT", "CT", "WT", "TAT");

        for (FCFS.ProcessStats s : result.getStats()) {
            System.out.printf(
                    "%-5d %-5d %-5d %-5d %-5d %-5d%n",
                    s.getProcessId(),
                    s.getArrivalTime(),
                    s.getBurstTime(),
                    s.getCompletionTime(),
                    s.getWaitingTime(),
                    s.getTurnaroundTime()
            );
        }
    }

    private static void printProcessTable(RoundRobin.Result result) {
        // RR stats bhi same table format me print hote hain.
        System.out.printf("%-5s %-5s %-5s %-5s %-5s %-5s%n", "PID", "AT", "BT", "CT", "WT", "TAT");

        for (RoundRobin.ProcessStats s : result.getStats()) {
            System.out.printf(
                    "%-5d %-5d %-5d %-5d %-5d %-5d%n",
                    s.getProcessId(),
                    s.getArrivalTime(),
                    s.getBurstTime(),
                    s.getCompletionTime(),
                    s.getWaitingTime(),
                    s.getTurnaroundTime()
            );
        }
    }

    private static List<Process> createHardcodedProcesses() {
        List<Process> processes = new ArrayList<>();

        // processId, arrivalTime, burstTime, remainingTime, priority
        // Ye values maine isliye choose ki hain taaki varying AT/BT dikh sake.
        processes.add(new Process(1, 0, 5, 5, 2));
        processes.add(new Process(2, 1, 3, 3, 1));
        processes.add(new Process(3, 2, 8, 8, 3));
        processes.add(new Process(4, 3, 6, 6, 2));
        processes.add(new Process(5, 4, 2, 2, 1));

        return processes;
    }
}

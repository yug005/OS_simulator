package scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import models.Process;
import utils.TimeCalculator;

public class FCFS {
    public Result simulate(List<Process> processes) {
        Objects.requireNonNull(processes, "processes");
        if (processes.isEmpty()) {
            return new Result(Collections.emptyList(), 0.0, 0.0);
        }

        // FCFS me arrival order sabse important hai, isliye yaha sort kar rahe hain.
        // Ye step mujhe clarity deta hai ki CPU ko kaun pehle milega.
        List<Process> sorted = new ArrayList<>(processes);
        sorted.sort(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getProcessId));

        List<ProcessStats> stats = new ArrayList<>(sorted.size());

        // currentTime = CPU ka current clock, jaha se next process start hoga.
        int currentTime = 0;
        int[] waitingTimes = new int[sorted.size()];
        int[] turnaroundTimes = new int[sorted.size()];

        for (int i = 0; i < sorted.size(); i++) {
            Process process = sorted.get(i);

            // Agar process late aayi hai to CPU idle ho sakta hai, isliye max use kiya.
            int startTime = Math.max(currentTime, process.getArrivalTime());
            int completionTime = startTime + process.getBurstTime();

            int waitingTime = startTime - process.getArrivalTime();
            int turnaroundTime = completionTime - process.getArrivalTime();

            // Metrics ko Process object me store karna zaroori hai
            // taaki later table/report me same object se values mil jaye.
            process.setCompletionTime(completionTime);
            process.setWaitingTime(waitingTime);
            process.setTurnaroundTime(turnaroundTime);
            process.setRemainingTime(0);

            waitingTimes[i] = waitingTime;
            turnaroundTimes[i] = turnaroundTime;

            stats.add(new ProcessStats(
                    process.getProcessId(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    startTime,
                    completionTime,
                    waitingTime,
                    turnaroundTime
            ));

            currentTime = completionTime;
        }

        // Averages nikalne se overall performance samajh me aata hai.
        double avgWaiting = TimeCalculator.averageWaitingTime(waitingTimes);
        double avgTurnaround = TimeCalculator.averageTurnaroundTime(turnaroundTimes);

        return new Result(stats, avgWaiting, avgTurnaround);
    }

    public static String formatGanttChart(Result result) {
        Objects.requireNonNull(result, "result");
        return formatGanttChartFromStats(result.getStats());
    }

    private static String formatGanttChartFromStats(List<ProcessStats> stats) {
        if (stats == null || stats.isEmpty()) {
            return "(no processes)";
        }

        // Gantt chart ke liye segments bana rahe hain: IDLE + P1/P2...
        List<Segment> segments = new ArrayList<>();

        int lastEnd = 0;
        for (ProcessStats s : stats) {
            if (s.getStartTime() > lastEnd) {
                segments.add(new Segment("IDLE", lastEnd, s.getStartTime()));
            }
            segments.add(new Segment("P" + s.getProcessId(), s.getStartTime(), s.getCompletionTime()));
            lastEnd = s.getCompletionTime();
        }

        StringBuilder barLine = new StringBuilder();
        List<Integer> boundaryPositions = new ArrayList<>(segments.size() + 1);

        for (Segment seg : segments) {
            boundaryPositions.add(barLine.length());
            barLine.append("| ").append(seg.label).append(' ');
        }
        boundaryPositions.add(barLine.length());
        barLine.append('|');

        StringBuilder timeLine = new StringBuilder();
        for (int i = 0; i < barLine.length(); i++) {
            timeLine.append(' ');
        }

        // Time markers boundary par place karte hain taaki timeline readable ho.
        for (int i = 0; i < boundaryPositions.size(); i++) {
            int timeValue;
            if (i == 0) {
                timeValue = segments.get(0).start;
            } else {
                timeValue = segments.get(i - 1).end;
            }
            placeString(timeLine, boundaryPositions.get(i), String.valueOf(timeValue));
        }

        return barLine + System.lineSeparator() + timeLine;
    }

    private static void placeString(StringBuilder sb, int index, String text) {
        if (index < 0) {
            index = 0;
        }

        int needed = index + text.length();
        while (sb.length() < needed) {
            sb.append(' ');
        }

        for (int i = 0; i < text.length(); i++) {
            sb.setCharAt(index + i, text.charAt(i));
        }
    }

    private static final class Segment {
        private final String label;
        private final int start;
        private final int end;

        private Segment(String label, int start, int end) {
            this.label = label;
            this.start = start;
            this.end = end;
        }
    }

    public static final class Result {
        private final List<ProcessStats> stats;
        private final double averageWaitingTime;
        private final double averageTurnaroundTime;

        public Result(List<ProcessStats> stats, double averageWaitingTime, double averageTurnaroundTime) {
            this.stats = stats;
            this.averageWaitingTime = averageWaitingTime;
            this.averageTurnaroundTime = averageTurnaroundTime;
        }

        public List<ProcessStats> getStats() {
            return stats;
        }

        public double getAverageWaitingTime() {
            return averageWaitingTime;
        }

        public double getAverageTurnaroundTime() {
            return averageTurnaroundTime;
        }
    }

    public static final class ProcessStats {
        private final int processId;
        private final int arrivalTime;
        private final int burstTime;
        private final int startTime;
        private final int completionTime;
        private final int waitingTime;
        private final int turnaroundTime;

        public ProcessStats(
                int processId,
                int arrivalTime,
                int burstTime,
                int startTime,
                int completionTime,
                int waitingTime,
                int turnaroundTime
        ) {
            this.processId = processId;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.startTime = startTime;
            this.completionTime = completionTime;
            this.waitingTime = waitingTime;
            this.turnaroundTime = turnaroundTime;
        }

        public int getProcessId() {
            return processId;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getBurstTime() {
            return burstTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getCompletionTime() {
            return completionTime;
        }

        public int getWaitingTime() {
            return waitingTime;
        }

        public int getTurnaroundTime() {
            return turnaroundTime;
        }

        @Override
        public String toString() {
            return "P" + processId +
                    " (AT=" + arrivalTime +
                    ", BT=" + burstTime +
                    ") => start=" + startTime +
                    ", completion=" + completionTime +
                    ", WT=" + waitingTime +
                    ", TAT=" + turnaroundTime;
        }
    }
}

package scheduling;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

import models.Process;
import utils.TimeCalculator;

public class RoundRobin {

    public static String formatGanttChart(Result result) {
        Objects.requireNonNull(result, "result");
        return formatGanttChartFromTimeline(result.getTimeline());
    }

    private static String formatGanttChartFromTimeline(List<Segment> timeline) {
        if (timeline == null || timeline.isEmpty()) {
            return "(no processes)";
        }

        List<Segment> segments = mergeAdjacentSegments(timeline);

        StringBuilder barLine = new StringBuilder();
        List<Integer> boundaryPositions = new ArrayList<>(segments.size() + 1);

        for (Segment seg : segments) {
            boundaryPositions.add(barLine.length());
            barLine.append("| ").append(seg.getLabel()).append(' ');
        }
        boundaryPositions.add(barLine.length());
        barLine.append('|');

        StringBuilder timeLine = new StringBuilder();
        for (int i = 0; i < barLine.length(); i++) {
            timeLine.append(' ');
        }

        for (int i = 0; i < boundaryPositions.size(); i++) {
            int timeValue;
            if (i == 0) {
                timeValue = segments.get(0).getStart();
            } else {
                timeValue = segments.get(i - 1).getEnd();
            }
            placeString(timeLine, boundaryPositions.get(i), String.valueOf(timeValue));
        }

        return barLine + System.lineSeparator() + timeLine;
    }

    private static List<Segment> mergeAdjacentSegments(List<Segment> timeline) {
        List<Segment> merged = new ArrayList<>();
        for (Segment seg : timeline) {
            if (merged.isEmpty()) {
                merged.add(seg);
                continue;
            }

            Segment last = merged.get(merged.size() - 1);
            if (last.getLabel().equals(seg.getLabel()) && last.getEnd() == seg.getStart()) {
                merged.set(merged.size() - 1, new Segment(last.getLabel(), last.getStart(), seg.getEnd()));
            } else {
                merged.add(seg);
            }
        }
        return merged;
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

    public Result simulate(List<Process> processes, int timeQuantum) {
        Objects.requireNonNull(processes, "processes");
        if (timeQuantum <= 0) {
            throw new IllegalArgumentException("timeQuantum must be > 0");
        }

        if (processes.isEmpty()) {
            return new Result(List.of(), List.of(), 0.0, 0.0);
        }

        // Arrival order ke basis par sort kar rahe hain, par original objects hi use honge
        // taaki metrics wapas same Process me store ho sake.
        List<Process> byArrival = new ArrayList<>(processes);
        byArrival.sort(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getProcessId));

        // Per-run state reset isliye, kyunki RR multiple times run ho sakta hai.
        for (Process p : processes) {
            p.setRemainingTime(p.getBurstTime());
            p.setCompletionTime(-1);
            p.setWaitingTime(-1);
            p.setTurnaroundTime(-1);
        }

        int n = byArrival.size();
        int[] waitingTimes = new int[n];
        int[] turnaroundTimes = new int[n];

        // Ready queue = RR ka heart, yahi round robin rotation maintain karta hai.
        Queue<Process> readyQueue = new ArrayDeque<>();
        List<Segment> timeline = new ArrayList<>();

        int currentTime = 0;
        int arrivalIndex = 0;
        int completed = 0;

        Map<Integer, Integer> indexByProcessId = new HashMap<>();
        for (int i = 0; i < byArrival.size(); i++) {
            indexByProcessId.put(byArrival.get(i).getProcessId(), i);
        }

        // Agar first arrival time 0 se late hai, to CPU idle time add karna zaroori hai.
        int firstArrival = byArrival.get(0).getArrivalTime();
        if (firstArrival > 0) {
            timeline.add(new Segment("IDLE", 0, firstArrival));
            currentTime = firstArrival;
        }

        while (arrivalIndex < n && byArrival.get(arrivalIndex).getArrivalTime() <= currentTime) {
            readyQueue.add(byArrival.get(arrivalIndex));
            arrivalIndex++;
        }

        // Main RR loop: jab tak sab processes complete na ho jaye.
        while (completed < n) {
            if (readyQueue.isEmpty()) {
                // CPU is idle until the next process arrives.
                int nextArrival = byArrival.get(arrivalIndex).getArrivalTime();
                if (nextArrival > currentTime) {
                    timeline.add(new Segment("IDLE", currentTime, nextArrival));
                    currentTime = nextArrival;
                }

                while (arrivalIndex < n && byArrival.get(arrivalIndex).getArrivalTime() <= currentTime) {
                    readyQueue.add(byArrival.get(arrivalIndex));
                    arrivalIndex++;
                }
                continue;
            }

            Process current = readyQueue.remove();

            int remainingBefore = current.getRemainingTime();
            // Slice = min(remaining, quantum) — RR ka basic rule.
            int slice = remainingBefore > timeQuantum ? timeQuantum : remainingBefore;
            int start = currentTime;
            int end = currentTime + slice;

            timeline.add(new Segment("P" + current.getProcessId(), start, end));

            current.setRemainingTime(remainingBefore - slice);
            currentTime = end;

            // Jo processes is slice ke beech me aa gaye, unko queue me add karo.
            while (arrivalIndex < n && byArrival.get(arrivalIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(byArrival.get(arrivalIndex));
                arrivalIndex++;
            }

            if (remainingBefore > timeQuantum) {
                // remainingTime > quantum -> requeue (same process ko last me bhej do)
                readyQueue.add(current);
            } else {
                // Finished -> completion time store karna hai
                current.setCompletionTime(currentTime);
                int turnaround = current.getCompletionTime() - current.getArrivalTime();
                int waiting = turnaround - current.getBurstTime();
                current.setTurnaroundTime(turnaround);
                current.setWaitingTime(waiting);

                Integer idx = indexByProcessId.get(current.getProcessId());
                if (idx != null) {
                    waitingTimes[idx] = waiting;
                    turnaroundTimes[idx] = turnaround;
                }

                completed++;
            }
        }

        // Averages = overall RR performance ka quick summary.
        double avgWaiting = TimeCalculator.averageWaitingTime(waitingTimes);
        double avgTurnaround = TimeCalculator.averageTurnaroundTime(turnaroundTimes);

        List<ProcessStats> stats = new ArrayList<>(n);
        for (Process p : byArrival) {
            stats.add(new ProcessStats(
                    p.getProcessId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getCompletionTime(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime()
            ));
        }

        return new Result(stats, timeline, avgWaiting, avgTurnaround);
    }

    public static final class Result {
        private final List<ProcessStats> stats;
        private final List<Segment> timeline;
        private final double averageWaitingTime;
        private final double averageTurnaroundTime;

        public Result(List<ProcessStats> stats, List<Segment> timeline, double averageWaitingTime, double averageTurnaroundTime) {
            this.stats = stats;
            this.timeline = timeline;
            this.averageWaitingTime = averageWaitingTime;
            this.averageTurnaroundTime = averageTurnaroundTime;
        }

        public List<ProcessStats> getStats() {
            return stats;
        }

        public List<Segment> getTimeline() {
            return timeline;
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
        private final int completionTime;
        private final int waitingTime;
        private final int turnaroundTime;

        public ProcessStats(
                int processId,
                int arrivalTime,
                int burstTime,
                int completionTime,
                int waitingTime,
                int turnaroundTime
        ) {
            this.processId = processId;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
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
                    ") => CT=" + completionTime +
                    ", WT=" + waitingTime +
                    ", TAT=" + turnaroundTime;
        }
    }

    public static final class Segment {
        private final String label;
        private final int start;
        private final int end;

        public Segment(String label, int start, int end) {
            this.label = label;
            this.start = start;
            this.end = end;
        }

        public String getLabel() {
            return label;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return label + ": " + start + " -> " + end;
        }
    }
}

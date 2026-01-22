package utils;

import java.util.List;

public final class TimeCalculator {
    private TimeCalculator() {
        // Utility class: instantiation avoid karna hai.
    }

    public static double averageWaitingTime(int[] waitingTimes) {
        // Waiting time ka average nikalna scheduling summary ke liye helpful.
        return average(waitingTimes);
    }

    public static double averageTurnaroundTime(int[] turnaroundTimes) {
        // Turnaround time ka average overall performance show karta hai.
        return average(turnaroundTimes);
    }

    public static double averageWaitingTime(List<Integer> waitingTimes) {
        // List version: same logic, just different input type.
        return average(waitingTimes);
    }

    public static double averageTurnaroundTime(List<Integer> turnaroundTimes) {
        // List version: for flexibility in callers.
        return average(turnaroundTimes);
    }

    private static double average(int[] values) {
        // Null/empty guard: avoid divide by zero.
        if (values == null || values.length == 0) {
            return 0.0;
        }

        long sum = 0;
        for (int value : values) {
            sum += value;
        }
        // double division for precision.
        return sum / (double) values.length;
    }

    private static double average(List<Integer> values) {
        // Null/empty guard: avoid divide by zero.
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        long sum = 0;
        for (Integer value : values) {
            if (value != null) {
                sum += value;
            }
        }
        // Note: null values ignore kiye gaye.
        return sum / (double) values.size();
    }
}

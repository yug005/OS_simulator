package paging;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class FIFOPageReplacement {

    public PageReplacementResult simulate(int[] pageReferenceString, int frameCount) {
        // FIFO = jo pehle aaya, wahi pehle niklega (queue logic).
        if (pageReferenceString == null) {
            throw new IllegalArgumentException("pageReferenceString cannot be null");
        }
        if (frameCount <= 0) {
            throw new IllegalArgumentException("frameCount must be > 0");
        }

        // Queue maintain karta hai insertion order, set quick lookup ke liye.
        Queue<Integer> fifoOrder = new ArrayDeque<>(frameCount);
        Set<Integer> inFrames = new HashSet<>(frameCount);
        List<String> stepLogs = new ArrayList<>(pageReferenceString.length);

        int hits = 0;
        int faults = 0;

        for (int page : pageReferenceString) {
            boolean hit;

            if (inFrames.contains(page)) {
                // Page already present => hit.
                hits++;
                hit = true;
            } else {
                // Page missing => fault, FIFO eviction if full.
                faults++;
                hit = false;

                if (fifoOrder.size() == frameCount) {
                    Integer evicted = fifoOrder.poll();
                    if (evicted != null) {
                        inFrames.remove(evicted);
                    }
                }

                fifoOrder.add(page);
                inFrames.add(page);
            }

            // Step log format consistent rakhte hain for formatter.
            stepLogs.add(formatStepLog(page, fifoOrder, hit));
        }

        return new PageReplacementResult(faults, hits, stepLogs);
    }

    private static String formatStepLog(int page, Queue<Integer> fifoOrder, boolean hit) {
        // Simple readable line: Page X -> Frames [..] -> Hit/Fault
        return "Page: " + page + " → Frames: " + toFrameListString(fifoOrder) + " → " + (hit ? "Hit" : "Fault");
    }

    private static String toFrameListString(Queue<Integer> fifoOrder) {
        // Queue ko list-like string me convert for log output.
        if (fifoOrder == null || fifoOrder.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (Integer page : fifoOrder) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(page);
        }
        sb.append(']');
        return sb.toString();
    }
}

package paging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LRUPageReplacement {

    public PageReplacementResult simulate(int[] pageReferenceString, int frameCount) {
        // LRU = least recently used page ko replace karna.
        if (pageReferenceString == null) {
            throw new IllegalArgumentException("pageReferenceString cannot be null");
        }
        if (frameCount <= 0) {
            throw new IllegalArgumentException("frameCount must be > 0");
        }

        // frames = current pages in memory, logging ke liye stable order maintain.
        List<Integer> frames = new ArrayList<>(frameCount);
        Set<Integer> inFrames = new HashSet<>(frameCount);

        // MANDATORY DSA: page -> last used time (LRU decide karne ke liye)
        Map<Integer, Integer> lastUsedTime = new HashMap<>();
        List<String> stepLogs = new ArrayList<>(pageReferenceString.length);

        int hits = 0;
        int faults = 0;
        int time = 0;

        for (int page : pageReferenceString) {
            boolean hit;

            if (inFrames.contains(page)) {
                // Hit: last used time update.
                hits++;
                hit = true;
                lastUsedTime.put(page, time);
            } else {
                // Fault: agar space hai to add, warna LRU victim choose.
                faults++;
                hit = false;

                if (frames.size() < frameCount) {
                    frames.add(page);
                    inFrames.add(page);
                    lastUsedTime.put(page, time);
                } else {
                    // Victim = least recently used page.
                    int victimIndex = findLeastRecentlyUsedIndex(frames, lastUsedTime);
                    int victimPage = frames.get(victimIndex);

                    inFrames.remove(victimPage);
                    lastUsedTime.remove(victimPage);

                    frames.set(victimIndex, page);
                    inFrames.add(page);
                    lastUsedTime.put(page, time);
                }
            }

            stepLogs.add(formatStepLog(page, frames, hit));
            time++;
        }

        return new PageReplacementResult(faults, hits, stepLogs);
    }

    private static int findLeastRecentlyUsedIndex(List<Integer> frames, Map<Integer, Integer> lastUsedTime) {
        // frames me jis page ka lastUsed sabse purana hai, wahi victim banega.
        int victimIndex = 0;
        int oldestTime = Integer.MAX_VALUE;

        for (int i = 0; i < frames.size(); i++) {
            int page = frames.get(i);
            Integer usedAt = lastUsedTime.get(page);
            int usedTime = usedAt == null ? Integer.MIN_VALUE : usedAt;

            if (usedTime < oldestTime) {
                oldestTime = usedTime;
                victimIndex = i;
            }
        }

        return victimIndex;
    }

    private static String formatStepLog(int page, List<Integer> frames, boolean hit) {
        // Output ko formatter ke expected format me rakhte hain.
        return "Page: " + page + " → Frames: " + frames + " → " + (hit ? "Hit" : "Fault");
    }
}

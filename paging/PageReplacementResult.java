package paging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PageReplacementResult {
    private final int totalPageFaults;
    private final int totalHits;
    private final List<String> stepLogs;

    public PageReplacementResult(int totalPageFaults, int totalHits, List<String> stepLogs) {
        // Result object: faults, hits, aur per-step logs.
        this.totalPageFaults = totalPageFaults;
        this.totalHits = totalHits;

        if (stepLogs == null) {
            this.stepLogs = Collections.emptyList();
        } else {
            this.stepLogs = Collections.unmodifiableList(new ArrayList<>(stepLogs));
        }
    }

    public int getTotalPageFaults() {
        return totalPageFaults;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public List<String> getStepLogs() {
        // Logs UI/formatter ke liye helpful hain.
        return stepLogs;
    }

    @Override
    public String toString() {
        return "PageReplacementResult{" +
                "totalPageFaults=" + totalPageFaults +
                ", totalHits=" + totalHits +
                ", stepLogs=" + stepLogs.size() +
                '}';
    }
}

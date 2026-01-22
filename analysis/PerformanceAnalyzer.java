package analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerformanceAnalyzer {
    // All module results yahi store hote hain.
    private final List<AlgorithmResult> results;

    public PerformanceAnalyzer() {
        this.results = new ArrayList<>();
    }

    public void addResult(AlgorithmResult result) {
        // Null guard to avoid accidental crashes.
        if (result != null) {
            results.add(result);
        }
    }

    public List<AlgorithmResult> getByModule(String module) {
        // Module name ke basis par filter.
        if (module == null) {
            return Collections.emptyList();
        }
        List<AlgorithmResult> filtered = new ArrayList<>();
        for (AlgorithmResult result : results) {
            if (module.equalsIgnoreCase(result.getModuleName())) {
                filtered.add(result);
            }
        }
        return filtered;
    }

    public List<AlgorithmResult> getAllResults() {
        // Read-only list return so outside code modify na kare.
        return Collections.unmodifiableList(results);
    }
}

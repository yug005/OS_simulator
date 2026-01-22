package analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlgorithmResult {
    // algorithmName = FIFO/LRU/FCFS etc.
    private final String algorithmName;
    // moduleName = Paging/Memory/Disk etc.
    private final String moduleName;
    // metrics = list of Metric objects.
    private final List<Metric> metrics;

    public AlgorithmResult(String algorithmName, String moduleName, List<Metric> metrics) {
        // Immutable result object for cross-module comparison.
        this.algorithmName = algorithmName;
        this.moduleName = moduleName;
        if (metrics == null) {
            this.metrics = Collections.emptyList();
        } else {
            this.metrics = Collections.unmodifiableList(new ArrayList<>(metrics));
        }
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public Metric getMetric(String name) {
        // Case-insensitive match so user input flexible ho.
        if (name == null) {
            return null;
        }
        for (Metric metric : metrics) {
            if (name.equalsIgnoreCase(metric.getName())) {
                return metric;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        // Debug-friendly string.
        return "AlgorithmResult{" +
                "algorithmName='" + algorithmName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", metrics=" + metrics +
                '}';
    }
}

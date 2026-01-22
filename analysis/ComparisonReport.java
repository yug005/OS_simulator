package analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComparisonReport {

    public void printModuleReport(String moduleName, List<AlgorithmResult> results) {
        // Tabular report + best algorithm detection.
        if (results == null || results.isEmpty()) {
            System.out.println("MODULE: " + moduleName);
            System.out.println("No results.");
            return;
        }

        Set<String> metricNames = new LinkedHashSet<>();
        // Saare metric names collect karna, table header ke liye.
        for (AlgorithmResult result : results) {
            for (Metric metric : result.getMetrics()) {
                metricNames.add(metric.getName());
            }
        }

        System.out.println("MODULE: " + moduleName);
        printHeader(metricNames);
        for (AlgorithmResult result : results) {
            printRow(result, metricNames);
        }

        Map<String, AlgorithmResult> bestByMetric = findBestByMetric(results, metricNames);
        // Best per metric print karte hain (lower value wins by default).
        for (Map.Entry<String, AlgorithmResult> entry : bestByMetric.entrySet()) {
            System.out.println("Best: " + entry.getValue().getAlgorithmName() + " (" + entry.getKey() + ")");
        }
    }

    public String toCsv(List<AlgorithmResult> results) {
        // CSV export = industry-friendly data output.
        StringBuilder sb = new StringBuilder();
        sb.append("Module,Algorithm,Metric,Value,Unit\n");
        if (results == null) {
            return sb.toString();
        }
        for (AlgorithmResult result : results) {
            for (Metric metric : result.getMetrics()) {
                sb.append(result.getModuleName()).append(',')
                        .append(result.getAlgorithmName()).append(',')
                        .append(metric.getName()).append(',')
                        .append(metric.getValue()).append(',')
                        .append(metric.getUnit() == null ? "" : metric.getUnit())
                        .append('\n');
            }
        }
        return sb.toString();
    }

    private void printHeader(Set<String> metricNames) {
        // Column alignment for clean console output.
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s", "Algorithm"));
        for (String metric : metricNames) {
            sb.append(String.format(" %-12s", metric));
        }
        System.out.println(sb);
    }

    private void printRow(AlgorithmResult result, Set<String> metricNames) {
        // Each algorithm ka row print hota hai.
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s", result.getAlgorithmName()));
        for (String metricName : metricNames) {
            Metric metric = result.getMetric(metricName);
            if (metric == null) {
                sb.append(String.format(" %-12s", "-"));
            } else {
                sb.append(String.format(" %-12.2f", metric.getValue()));
            }
        }
        System.out.println(sb);
    }

    private Map<String, AlgorithmResult> findBestByMetric(List<AlgorithmResult> results, Set<String> metricNames) {
        // Default rule: smaller metric = better (faults, seek, etc.)
        Map<String, AlgorithmResult> best = new HashMap<>();
        for (String metricName : metricNames) {
            List<AlgorithmResult> candidates = new ArrayList<>();
            for (AlgorithmResult result : results) {
                if (result.getMetric(metricName) != null) {
                    candidates.add(result);
                }
            }
            if (candidates.isEmpty()) {
                continue;
            }
            candidates.sort(Comparator.comparingDouble(r -> r.getMetric(metricName).getValue()));
            best.put(metricName, candidates.get(0));
        }
        return best;
    }
}

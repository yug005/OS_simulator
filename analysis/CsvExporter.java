package analysis;

import java.util.List;

public class CsvExporter {

    public String export(List<AlgorithmResult> results) {
        // Simple wrapper: CSV export ke liye helper method.
        ComparisonReport report = new ComparisonReport();
        return report.toCsv(results);
    }
}

package analysis;

public class Metric {
    // name = metric ka naam (e.g., Page Faults, Hit Ratio)
    private final String name;
    // value = numeric value, double for flexibility.
    private final double value;
    // unit = optional unit (%, ms, faults, etc.)
    private final String unit;

    public Metric(String name, double value, String unit) {
        // Simple data holder for any module metric.
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        // Human readable format for logs or quick debug.
        return name + ": " + value + (unit == null || unit.isBlank() ? "" : (" " + unit));
    }
}

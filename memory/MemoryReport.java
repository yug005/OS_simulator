package memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MemoryReport {
    private final int totalMemory;
    private final int usedMemory;
    private final int freeMemory;
    private final int internalFragmentation;
    private final int externalFragmentation;
    private final List<String> allocationLog;

    public MemoryReport(
            int totalMemory,
            int usedMemory,
            int freeMemory,
            int internalFragmentation,
            int externalFragmentation,
            List<String> allocationLog
    ) {
        // Report object summary ke liye hai, isse UI/printing easy hota hai.
        this.totalMemory = totalMemory;
        this.usedMemory = usedMemory;
        this.freeMemory = freeMemory;
        this.internalFragmentation = internalFragmentation;
        this.externalFragmentation = externalFragmentation;

        if (allocationLog == null) {
            this.allocationLog = Collections.emptyList();
        } else {
            this.allocationLog = Collections.unmodifiableList(new ArrayList<>(allocationLog));
        }
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public int getUsedMemory() {
        return usedMemory;
    }

    public int getFreeMemory() {
        return freeMemory;
    }

    public int getInternalFragmentation() {
        return internalFragmentation;
    }

    public int getExternalFragmentation() {
        return externalFragmentation;
    }

    public List<String> getAllocationLog() {
        // Allocation log ko read-only return karte hain.
        return allocationLog;
    }

    @Override
    public String toString() {
        return "MemoryReport{" +
                "totalMemory=" + totalMemory +
                ", usedMemory=" + usedMemory +
                ", freeMemory=" + freeMemory +
                ", internalFragmentation=" + internalFragmentation +
                ", externalFragmentation=" + externalFragmentation +
                ", allocationLog=" + allocationLog.size() +
                '}';
    }
}

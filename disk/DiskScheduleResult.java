package disk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DiskScheduleResult {
    // serviceOrder = request sequence served by algorithm.
    private final List<Integer> serviceOrder;
    // totalHeadMovement = total seek distance.
    private final int totalHeadMovement;

    public DiskScheduleResult(List<Integer> serviceOrder, int totalHeadMovement) {
        // Immutable result object.
        if (serviceOrder == null) {
            this.serviceOrder = Collections.emptyList();
        } else {
            this.serviceOrder = Collections.unmodifiableList(new ArrayList<>(serviceOrder));
        }
        this.totalHeadMovement = totalHeadMovement;
    }

    public List<Integer> getServiceOrder() {
        return serviceOrder;
    }

    public int getTotalHeadMovement() {
        return totalHeadMovement;
    }

    @Override
    public String toString() {
        return "DiskScheduleResult{" +
                "serviceOrder=" + serviceOrder +
                ", totalHeadMovement=" + totalHeadMovement +
                '}';
    }
}

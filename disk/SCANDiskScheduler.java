package disk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCANDiskScheduler implements DiskScheduler {

    @Override
    public DiskScheduleResult schedule(int headStart, List<Integer> requests, int diskSize) {
        // SCAN (elevator): ek direction me jao, end pe reverse.
        if (requests == null || requests.isEmpty()) {
            return new DiskScheduleResult(List.of(), 0);
        }

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (Integer req : requests) {
            if (req == null) {
                continue;
            }
            if (req < headStart) {
                left.add(req);
            } else {
                right.add(req);
            }
        }

        Collections.sort(left, Collections.reverseOrder());
        Collections.sort(right);

        List<Integer> order = new ArrayList<>();
        int totalMovement = 0;
        int head = headStart;

        // Direction: RIGHT (default)
        for (int r : right) {
            totalMovement += Math.abs(head - r);
            head = r;
            order.add(r);
        }

        // End tak jao, phir reverse direction.
        int end = diskSize - 1;
        totalMovement += Math.abs(head - end);
        head = end;

        for (int r : left) {
            totalMovement += Math.abs(head - r);
            head = r;
            order.add(r);
        }

        return new DiskScheduleResult(order, totalMovement);
    }
}

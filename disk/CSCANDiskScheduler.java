package disk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSCANDiskScheduler implements DiskScheduler {

    @Override
    public DiskScheduleResult schedule(int headStart, List<Integer> requests, int diskSize) {
        // C-SCAN: sirf ek direction me, end pe jump back to start.
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

        Collections.sort(left);
        Collections.sort(right);

        List<Integer> order = new ArrayList<>();
        int totalMovement = 0;
        int head = headStart;

        // Service right side in ascending order
        for (int r : right) {
            totalMovement += Math.abs(head - r);
            head = r;
            order.add(r);
        }

        // End tak jao, phir 0 pe jump (circular behavior).
        int end = diskSize - 1;
        totalMovement += Math.abs(head - end);
        head = end;
        totalMovement += end; // jump from end to 0
        head = 0;

        // Service left side in ascending order (from 0 upwards)
        for (int r : left) {
            totalMovement += Math.abs(head - r);
            head = r;
            order.add(r);
        }

        return new DiskScheduleResult(order, totalMovement);
    }
}

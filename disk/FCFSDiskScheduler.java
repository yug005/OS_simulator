package disk;

import java.util.ArrayList;
import java.util.List;

public class FCFSDiskScheduler implements DiskScheduler {

    @Override
    public DiskScheduleResult schedule(int headStart, List<Integer> requests, int diskSize) {
        if (requests == null || requests.isEmpty()) {
            return new DiskScheduleResult(List.of(), 0);
        }

        List<Integer> order = new ArrayList<>();
        int totalMovement = 0;
        int head = headStart;

        for (Integer req : requests) {
            if (req == null) {
                continue;
            }
            order.add(req);
            totalMovement += Math.abs(req - head);
            head = req;
        }

        return new DiskScheduleResult(order, totalMovement);
    }
}

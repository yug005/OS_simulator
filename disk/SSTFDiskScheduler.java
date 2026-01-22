package disk;

import java.util.ArrayList;
import java.util.List;

public class SSTFDiskScheduler implements DiskScheduler {

    @Override
    public DiskScheduleResult schedule(int headStart, List<Integer> requests, int diskSize) {
        // SSTF: nearest request choose karte hain (greedy).
        if (requests == null || requests.isEmpty()) {
            return new DiskScheduleResult(List.of(), 0);
        }

        List<Integer> pending = new ArrayList<>();
        for (Integer req : requests) {
            if (req != null) {
                pending.add(req);
            }
        }

        List<Integer> order = new ArrayList<>();
        int totalMovement = 0;
        int head = headStart;

        while (!pending.isEmpty()) {
            // Current head se closest request find karo.
            int nearestIndex = 0;
            int nearestValue = pending.get(0);
            int bestDistance = Math.abs(head - nearestValue);

            for (int i = 1; i < pending.size(); i++) {
                int candidate = pending.get(i);
                int distance = Math.abs(head - candidate);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    nearestValue = candidate;
                    nearestIndex = i;
                }
            }

            totalMovement += bestDistance;
            head = nearestValue;
            order.add(nearestValue);
            pending.remove(nearestIndex);
        }

        return new DiskScheduleResult(order, totalMovement);
    }
}

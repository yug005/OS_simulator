package disk;

import java.util.List;

public interface DiskScheduler {
    // Common interface: headStart, requests list, diskSize.
    DiskScheduleResult schedule(int headStart, List<Integer> requests, int diskSize);
}

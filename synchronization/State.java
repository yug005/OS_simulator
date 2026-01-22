package synchronization;

public enum State {
    // READY = process ready hai, CPU milte hi run kar sakta hai.
    READY,
    // RUNNING = currently CPU par chal raha hai.
    RUNNING,
    // WAITING = kisi event/resource ka wait kar raha hai.
    WAITING,
    // BLOCKED = resource unavailable, resume tab hoga jab signal mile.
    BLOCKED
}

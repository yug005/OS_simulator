package synchronization;

public class Semaphore {
    private int value;

    public Semaphore(int initial) {
        // Counting semaphore ka initial value, yahi resources ki count define karta hai.
        if (initial < 0) {
            throw new IllegalArgumentException("initial must be >= 0");
        }
        this.value = initial;
    }

    public synchronized void waitSem() {
        // waitSem = acquire: jab tak resource na mile, wait karo (busy wait nahi).
        while (value <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                // ignore
            }
        }
        // Resource mil gaya, count decrease.
        value--;
    }

    public synchronized void signal() {
        // signal = release: resource wapas add karo aur kisi ko jaga do.
        value++;
        notify();
    }

    public synchronized int getValue() {
        // Debug ke liye current count check kar sakte hain.
        return value;
    }
}

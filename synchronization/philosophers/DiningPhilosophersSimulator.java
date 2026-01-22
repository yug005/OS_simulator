package synchronization.philosophers;

import synchronization.Semaphore;

public class DiningPhilosophersSimulator {

    private static final int PHILOSOPHER_COUNT = 5;
    private static final int EAT_ROUNDS = 2;

    public static void main(String[] args) {
        // Forks ko semaphores se model karte hain (1 = available).
        Semaphore[] forks = new Semaphore[PHILOSOPHER_COUNT];
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            forks[i] = new Semaphore(1);
        }

        Thread[] philosophers = new Thread[PHILOSOPHER_COUNT];
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            int id = i;
            philosophers[i] = new Thread(() -> dine(id, forks));
            philosophers[i].start();
        }

        for (Thread t : philosophers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void dine(int id, Semaphore[] forks) {
        int leftFork = id;
        int rightFork = (id + 1) % PHILOSOPHER_COUNT;

        // Deadlock avoid: hamesha lower-numbered fork pehle uthao.
        int first = Math.min(leftFork, rightFork);
        int second = Math.max(leftFork, rightFork);

        for (int round = 0; round < EAT_ROUNDS; round++) {
            think(id);

            // Ordered acquisition prevents circular wait.
            forks[first].waitSem();
            forks[second].waitSem();

            log("Philosopher " + id + " is eating (round " + (round + 1) + ")");
            sleep(70);

            forks[second].signal();
            forks[first].signal();
        }
    }

    private static void think(int id) {
        log("Philosopher " + id + " is thinking");
        sleep(50);
    }

    private static void log(String message) {
        System.out.println(message);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

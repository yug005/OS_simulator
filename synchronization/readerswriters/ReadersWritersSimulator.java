package synchronization.readerswriters;

import synchronization.Semaphore;

public class ReadersWritersSimulator {

    private static int readCount = 0;

    public static void main(String[] args) {
        // mutex = readCount protect, writeLock = writer exclusive access.
        Semaphore mutex = new Semaphore(1);
        Semaphore writeLock = new Semaphore(1);

        Thread reader1 = new Thread(new Reader("R1", mutex, writeLock));
        Thread reader2 = new Thread(new Reader("R2", mutex, writeLock));
        Thread writer1 = new Thread(new Writer("W1", writeLock));

        reader1.start();
        reader2.start();
        writer1.start();

        try {
            reader1.join();
            reader2.join();
            writer1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class Reader implements Runnable {
        private final String id;
        private final Semaphore mutex;
        private final Semaphore writeLock;

        private Reader(String id, Semaphore mutex, Semaphore writeLock) {
            this.id = id;
            this.mutex = mutex;
            this.writeLock = writeLock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                // Reader entry: first reader writeLock le lega.
                mutex.waitSem();
                readCount++;
                if (readCount == 1) {
                    writeLock.waitSem();
                }
                mutex.signal();

                log("Reader " + id + " is reading");
                sleep(60);

                // Reader exit: last reader writeLock release karega.
                mutex.waitSem();
                readCount--;
                if (readCount == 0) {
                    writeLock.signal();
                }
                mutex.signal();

                sleep(40);
            }
        }
    }

    private static class Writer implements Runnable {
        private final String id;
        private final Semaphore writeLock;

        private Writer(String id, Semaphore writeLock) {
            this.id = id;
            this.writeLock = writeLock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                // Writer ko exclusive access chahiye, isliye writeLock acquire.
                writeLock.waitSem();
                log("Writer " + id + " is writing");
                sleep(80);
                writeLock.signal();

                sleep(50);
            }
        }
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

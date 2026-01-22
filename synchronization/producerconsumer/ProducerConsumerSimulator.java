package synchronization.producerconsumer;

import synchronization.Semaphore;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerSimulator {

    private static final int BUFFER_SIZE = 5;
    private static final int TOTAL_ITEMS = 10;

    public static void main(String[] args) {
        // Bounded buffer: size fixed, overflow/underflow avoid karna hai.
        Queue<Integer> buffer = new LinkedList<>();

        // empty = khaali slots, full = filled slots, mutex = critical section lock.
        Semaphore empty = new Semaphore(BUFFER_SIZE);
        Semaphore full = new Semaphore(0);
        Semaphore mutex = new Semaphore(1);

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= TOTAL_ITEMS; i++) {
                // Producer flow: empty -> mutex -> add -> mutex release -> full.
                empty.waitSem();
                mutex.waitSem();

                buffer.add(i);
                // Logging se runtime behavior easily samajh aata hai.
                log("Producer P1 produced item " + i);
                log("Buffer size: " + buffer.size());

                mutex.signal();
                full.signal();
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= TOTAL_ITEMS; i++) {
                // Consumer flow: full -> mutex -> remove -> mutex release -> empty.
                full.waitSem();
                mutex.waitSem();

                Integer item = buffer.poll();
                log("Consumer C1 consumed item " + item);
                log("Buffer size: " + buffer.size());

                mutex.signal();
                empty.signal();
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void log(String message) {
        System.out.println(message);
    }
}

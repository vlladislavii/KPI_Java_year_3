package org.example.lab.lab1;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    private final MessageQueue messageQueue;
    private static final AtomicInteger messageCounter = new AtomicInteger(0);

    public Producer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = "Повідомлення №" + messageCounter.incrementAndGet();
                messageQueue.produce(message);
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer " + Thread.currentThread().getName() + " був перерваний.");
            }
        }
    }
}
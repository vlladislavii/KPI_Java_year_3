package org.example.lab.lab1;

public class Consumer implements Runnable {
    private final MessageQueue messageQueue;

    public Consumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                messageQueue.consume();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Consumer " + Thread.currentThread().getName() + " був перерваний.");
            }
        }
    }
}
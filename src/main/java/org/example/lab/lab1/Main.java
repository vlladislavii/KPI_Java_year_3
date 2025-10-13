package org.example.lab.lab1;

public class Main {
    public static void main(String[] args) {
        int queueLimit;
        int numberOfProducers;
        int numberOfConsumers;

        queueLimit = 5;
        numberOfProducers = 4;
        numberOfConsumers = 3;

        MessageQueue sharedQueue = new MessageQueue(queueLimit);

        for (int i = 0; i < numberOfProducers; i++) {
            new Thread(new Producer(sharedQueue), "Producer-" + (i + 1)).start();
        }
        for (int i = 0; i < numberOfConsumers; i++) {
            new Thread(new Consumer(sharedQueue), "Consumer-" + (i + 1)).start();
        }
    }
}
package org.example.lab.lab1;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final int limit;

    public MessageQueue(int limit) {
        this.limit = limit;
    }

    // Синхронізовані методи - synchronized
    public synchronized void produce(String message) throws InterruptedException {
        // Якщо черга повна, виробник повинен чекати
        while (queue.size() == limit) {
            System.out.println("Черга повна. Producer " + Thread.currentThread().getName() + " чекає...");
            wait();
        }

        queue.add(message);
        System.out.println("Producer " + Thread.currentThread().getName() + " додав: " + message + " | Розмір черги: " + queue.size());

        // Сповіщаємо один потік, поживача, що в черзі з'явився елемент
        notifyAll();
    }

    public synchronized String consume() throws InterruptedException {
        // Якщо черга порожня, споживач повинен чекати
        while (queue.isEmpty()) {
            System.out.println("Черга порожня. Consumer " + Thread.currentThread().getName() + " чекає...");
            wait();
        }

        String message = queue.remove();
        System.out.println("Consumer " + Thread.currentThread().getName() + " забрав: " + message + " | Розмір черги: " + queue.size());

        // Сповіщаємо один потік, виробника, що в черзі з'явилося вільне місце
        notifyAll();
        return message;
    }
}

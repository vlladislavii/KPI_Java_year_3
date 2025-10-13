package org.example.lab.lab1;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Несинхронізована версія, яка намагається виконувати перевірки,
 * але робить це неправильно, що призводить до стану гонитви.
 */

public class MessageQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final int limit;

    public MessageQueue(int limit) {
        this.limit = limit;
    }

    public void produce(String message) {
        if (queue.size() < limit) {
            queue.add(message);
            System.out.println("Producer " + Thread.currentThread().getName() + " додав: " + message + " | Розмір черги: " + queue.size());
        } else {
            System.out.println("Producer " + Thread.currentThread().getName() + " бачить, що черга повна. Повідомлення відхилено.");
        }
    }

    public String consume() {
        if (!queue.isEmpty()) {
            String message = queue.remove(); // <-- Помилка виникне тут
            System.out.println("Consumer " + Thread.currentThread().getName() + " забрав: " + message + " | Розмір черги: " + queue.size());
            return message;
        }

        return null;
    }
}


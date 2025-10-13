# Лабораторна робота №1: Concurrency в Java

#### Виконав: Кіселар Владислав ІС-32
#### Група: IC-32
#### Номер залікової книжки: 7

## Опис Завдання

Реалізувати чергу повідомлень, а також тих, хто пише повідомлення та отримує їх. Отримувачі очікують на наявність повідомлень в черзі, щоб їх прочитати. Кожне повідомлення може бути прочитане лише раз. Черга повідомлень має обмеження по кількості повідомлень, які може в собі утримувати.
- **C5 = 7 % 5 = 2**
- **C3 = 7 % 3 = 1** (Спосіб створення потоків: тільки інтерфейс `Runnable`)

## Демонстрація некоректної роботи без синхронізації

Для демонстрації проблеми "стану гонитви" (race condition) було створено версію класу `MessageQueueUnsafe`, яка не використовує механізмів синхронізації.

**Код `MessageQueueUnsafe.java`:**
```java
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
            String message = queue.remove();
            System.out.println("Consumer " + Thread.currentThread().getName() + " забрав: " + message + " | Розмір черги: " + queue.size());
            return message;
        }

        return null;
    }
}


```

При запуску симуляції, де споживачі працюють швидше за виробників, черга швидко спустошується. Наступна спроба викликати `consume()` на порожній черзі призводить до винятку `java.util.NoSuchElementException`.

**Результат виконання (помилка):**
```console
Exception in thread "Consumer-3" java.util.NoSuchElementException
	at java.base/java.util.LinkedList.removeFirst(LinkedList.java:274)
	at java.base/java.util.LinkedList.remove(LinkedList.java:689)
	at org.example.lab.lab1.MessageQueue.consume(MessageQueue.java:30)
	at org.example.lab.lab1.Consumer.run(Consumer.java:14)
	at java.base/java.lang.Thread.run(Thread.java:840)
```
Ця помилка доводить необхідність синхронізації доступу до спільного ресурсу.

## Коректна реалізація з механізмами синхронізації

Для вирішення проблеми було реалізовано потокобезпечний клас `MessageQueue` з використанням `synchronized`, `wait()` та `notifyAll()`.

- `synchronized` гарантує атомарний доступ до черги.
- `wait()` блокує потік, якщо черга повна (для виробника) або порожня (для споживача).
- `notifyAll()` пробуджує потоки, що очікують, після зміни стану черги.
- `AtomicInteger` гарантує, що операції над лічильником (наприклад, incrementAndGet()) виконуються атомарно. Це захищає від стану гонитви, коли кілька потоків-виробників одночасно намагаються згенерувати унікальний номер для повідомлення.

**Код `MessageQueue.java`:**
```java
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
```
Ця версія працює стабільно і не призводить до помилок під час виконання.

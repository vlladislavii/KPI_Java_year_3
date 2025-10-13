package org.example.lab.lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MessageQueueTest {

    private MessageQueue messageQueue;
    private final int LIMIT_IN_QUEUE = 5;

    @BeforeEach
    void setUp() {
        messageQueue = new MessageQueue(LIMIT_IN_QUEUE);
    }

    @Test
    @DisplayName("Тест: успішне додавання та отримання одного повідомлення")
    void testProduceAndConsumeSingleMessage() throws InterruptedException {
        String testMessage = "Hello, World!";
        messageQueue.produce(testMessage);
        String consumedMessage = messageQueue.consume();
        assertEquals(testMessage, consumedMessage, "Отримане повідомлення не співпадає з відправленим.");
    }

    @Test
    @DisplayName("Тест: Producer повинен чекати, якщо черга повна")
    @Timeout(2)
    void testProducerWaitsWhenQueueIsFull() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            messageQueue.produce("Message " + i);
        }
        Thread producerThread = new Thread(() -> {
            try {
                messageQueue.produce("Overflow Message");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producerThread.start();
        Thread.sleep(500);
        assertEquals(Thread.State.WAITING, producerThread.getState());

        // Забираємо один елемент, щоб звільнити місце для продюсера
        messageQueue.consume();

        // Чекаємо, поки продюсер завершить свою роботу і додасть "Overflow Message"
        producerThread.join();

        // Тепер черга містить ["Message 1", "Message 2", "Message 3", "Message 4", "Overflow Message"]
        for (int i = 1; i < 5; i++) {
            messageQueue.consume(); // Забираємо "Message 1", "Message 2", і т.д.
        }

        // наступним елементом має бути саме "Overflow Message"
        assertEquals("Overflow Message", messageQueue.consume());
    }


    @Test
    @DisplayName("Тест: Consumer повинен чекати, якщо черга порожня")
    @Timeout(2)
    void testConsumerWaitsWhenQueueIsEmpty() throws InterruptedException {
        // Цей потік спробує отримати елемент з порожньої черги і повинен заблокуватися
        Thread consumerThread = new Thread(() -> {
            try {
                messageQueue.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumerThread.start();

        // Даємо час на блокування
        Thread.sleep(500);

        assertEquals(Thread.State.WAITING, consumerThread.getState(), "Consumer не перейшов у стан очікування, коли черга порожня.");

        // Додаємо елемент, щоб розбудити споживачаS
        messageQueue.produce("Wake Up Message");
        consumerThread.join();
    }

    @Test
    @DisplayName("Тест: Переривання потоку, що очікує на додавання в повну чергу")
    @Timeout(1)
    void testInterruptProducerWhileWaiting() throws InterruptedException {
        MessageQueue queue = new MessageQueue(1);
        queue.produce("Initial message");

        Thread producerThread = new Thread(() -> {
            try {
                // Цей виклик заблокує потік
                queue.produce("This should not be added");
            } catch (InterruptedException e) {
                // Правильна обробка - відновити статус переривання
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        Thread.sleep(200); // Чекаємо, поки потік заблокується

        // Перериваємо потік
        producerThread.interrupt();
        producerThread.join(); // Чекаємо завершення

        assertTrue(producerThread.isInterrupted(), "Статус переривання потоку не був встановлений.");
        assertEquals("Initial message", queue.consume(), "Повідомлення було додано в чергу після переривання.");
    }


    @Test
    @DisplayName("Тест: Багатопотокова робота з кількома Producer та Consumer")
    @Timeout(5)
    void testMultipleProducersAndConsumers() throws InterruptedException {
        final int messagesCount = 100;
        final int producerCount = 3;
        final int consumerCount = 3;
        final ExecutorService executor = Executors.newFixedThreadPool(producerCount + consumerCount);
        final AtomicInteger producedCounter = new AtomicInteger(0);
        final AtomicInteger consumedCounter = new AtomicInteger(0);

        for (int i = 0; i < producerCount; i++) {
            executor.submit(() -> {
                while (producedCounter.get() < messagesCount) {
                    try {
                        int value = producedCounter.incrementAndGet();
                        if (value <= messagesCount) {
                            messageQueue.produce("Message " + value);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        for (int i = 0; i < consumerCount; i++) {
            executor.submit(() -> {
                while (consumedCounter.get() < messagesCount) {
                    try {
                        messageQueue.consume();
                        consumedCounter.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        executor.shutdown();
        boolean terminatedGracefully = executor.awaitTermination(4, TimeUnit.SECONDS);

        // Якщо потоки не завершились самі (бо споживачі "застрягли" в очікуванні),
        // Ми примусово їх зупиняємо, надіславши interrupt.
        if (!terminatedGracefully) {
            executor.shutdownNow();
        }

        // Головна перевірка: чи всі повідомлення були оброблені,
        // навіть якщо потоки довелося зупиняти примусово.
        assertEquals(messagesCount, consumedCounter.get(), "Кількість отриманих повідомлень не дорівнює кількості відправлених.");
    }
}
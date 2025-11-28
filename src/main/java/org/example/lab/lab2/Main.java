package org.example.lab.lab2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main class demonstrating Stream API operations on ChthoniceCreature objects.
 * <p>
 * This class demonstrates the implementation of all tasks from Lab 2:
 * - Task 3: Infinite stream generator
 * - Task 4: Custom stream operation for skipping and collecting
 * - Task 5: Filtering and grouping
 * - Task 6: Custom Collector for statistics
 * - Task 7: Outlier detection using IQR method
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Лабораторна робота №2: Stream API ===");
        System.out.println("Виконав: Кіселар Владислав, ІС-32");
        System.out.println("Варіант: C4 = 7 % 4 = 3 (Хтонічні істоти)\n");

        // Task 3: Create infinite stream generator
        System.out.println("--- Завдання 3: Створення нескінченного генератора ---");
        CreatureGenerator generator = new CreatureGenerator(42);

        // Task 4: Generate 500 creatures, skipping first N of a specific type
        System.out.println("\n--- Завдання 4: Генерація 500 об'єктів зі пропуском ---");
        CreatureType typeToSkip = CreatureType.VAMPIRE;
        int numberOfElementsToSkip = 10;

         List<ChthoniceCreature> creatures = generator.generateInfiniteStream()
                 .gather(SkipByFieldGatherer.skipAndCollect(typeToSkip, numberOfElementsToSkip))
                 .collect(Collectors.toList());

        System.out.printf("Згенеровано %d істот (пропущено перші %d %s)%n",
                creatures.size(), numberOfElementsToSkip, typeToSkip);
        System.out.println("Приклади згенерованих істот:");
        creatures.stream().limit(5).forEach(System.out::println);

        // Task 5: Filter by Parameter B range and group by Field B
        System.out.println("\n--- Завдання 5: Фільтрація та групування ---");
        long minYears = 100;
        long maxYears = 2000;

        Map<CreatureType, List<ChthoniceCreature>> groupedByType = creatures.stream()
                .filter(c -> {
                    long years = c.getYearsSinceFirstMention();
                    return years >= minYears && years <= maxYears;
                })
                .collect(Collectors.groupingBy(ChthoniceCreature::getType));

        System.out.printf("Фільтрація за діапазоном років: %d - %d%n", minYears, maxYears);
        System.out.println("Групування за типом істоти:");
        groupedByType.forEach((type, list) ->
                System.out.printf("  %s: %d істот%n", type, list.size()));

        // Task 6: Statistical analysis using custom Collector
        System.out.println("\n--- Завдання 6: Статистичний аналіз ---");
        CreatureStatistics statistics = creatures.stream()
                .collect(CreatureStatisticsCollector.toStatistics());

        System.out.println(statistics);

        // Task 7: Outlier detection using IQR method
        System.out.println("\n--- Завдання 7: Визначення викидів (IQR метод) ---");
        OutlierAnalyzer.OutlierAnalysisResult outlierAnalysis =
                OutlierAnalyzer.analyzeOutliers(creatures);

        System.out.println(outlierAnalysis);

        // Additional demonstration of showing some outliers
        double lowerBound = outlierAnalysis.getLowerBound();
        double upperBound = outlierAnalysis.getUpperBound();

        System.out.println("\nПриклади викидів:");
        creatures.stream()
                .filter(c -> c.getAttackPower() < lowerBound || c.getAttackPower() > upperBound)
                .limit(5)
                .forEach(c -> System.out.printf("  %s (сила атаки: %d)%n",
                        c.getName(), c.getAttackPower()));

        System.out.println("\nПриклади звичайних значень:");
        creatures.stream()
                .filter(c -> c.getAttackPower() >= lowerBound && c.getAttackPower() <= upperBound)
                .limit(5)
                .forEach(c -> System.out.printf("  %s (сила атаки: %d)%n",
                        c.getName(), c.getAttackPower()));

        // Summary
        System.out.println("\n=== Підсумок ===");
        System.out.printf("Всього згенеровано істот: %d%n", creatures.size());
        System.out.printf("Різних типів істот: %d%n", groupedByType.size());
        System.out.printf("Звичайних значень: %d%n",
                outlierAnalysis.getCategoryCounts().get("data"));
        System.out.printf("Викидів: %d%n",
                outlierAnalysis.getCategoryCounts().get("outliers"));
        System.out.printf("Відсоток викидів: %.2f%%%n",
                (double) outlierAnalysis.getCategoryCounts().get("outliers") / creatures.size() * 100);
    }
}

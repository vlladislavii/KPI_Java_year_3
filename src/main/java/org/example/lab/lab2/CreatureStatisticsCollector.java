package org.example.lab.lab2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Custom Collector for collecting ChthoniceCreature objects into CreatureStatistics.
 * <p>
 * This collector calculates statistical measures for the attack power field:
 * - Minimum attack power
 * - Maximum attack power
 * - Average attack power
 * - Standard deviation
 */
public class CreatureStatisticsCollector implements Collector<ChthoniceCreature, CreatureStatisticsCollector.Accumulator, CreatureStatistics> {

    /**
     * Accumulator class that holds intermediate state during collection.
     */
    static class Accumulator {
        private final List<Integer> attackPowers = new ArrayList<>();

        void add(ChthoniceCreature creature) {
            attackPowers.add(creature.getAttackPower());
        }

        void combine(Accumulator other) {
            attackPowers.addAll(other.attackPowers);
        }

        CreatureStatistics toStatistics() {
            if (attackPowers.isEmpty()) {
                return new CreatureStatistics(0, 0, 0.0, 0.0, 0);
            }

            int min = attackPowers.stream().mapToInt(Integer::intValue).min().orElse(0);
            int max = attackPowers.stream().mapToInt(Integer::intValue).max().orElse(0);
            double average = attackPowers.stream().mapToInt(Integer::intValue).average().orElse(0.0);

            // Calculate sample standard deviation using Bessel's correction (n-1)
            double sumOfSquaredDeviations = attackPowers.stream()
                    .mapToDouble(power -> Math.pow(power - average, 2))
                    .sum();
            double variance = attackPowers.size() > 1
                    ? sumOfSquaredDeviations / (attackPowers.size() - 1)
                    : 0.0;
            double standardDeviation = Math.sqrt(variance);

            return new CreatureStatistics(min, max, average, standardDeviation, attackPowers.size());
        }
    }

    @Override
    public Supplier<Accumulator> supplier() {
        return Accumulator::new;
    }

    @Override
    public BiConsumer<Accumulator, ChthoniceCreature> accumulator() {
        return Accumulator::add;
    }

    @Override
    public BinaryOperator<Accumulator> combiner() {
        return (acc1, acc2) -> {
            acc1.combine(acc2);
            return acc1;
        };
    }

    @Override
    public Function<Accumulator, CreatureStatistics> finisher() {
        return Accumulator::toStatistics;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    /**
     * Creates a new CreatureStatisticsCollector.
     *
     * @return a new collector instance
     */
    public static CreatureStatisticsCollector toStatistics() {
        return new CreatureStatisticsCollector();
    }
}

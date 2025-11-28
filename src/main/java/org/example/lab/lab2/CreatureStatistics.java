package org.example.lab.lab2;

/**
 * Statistical data for a collection of ChthoniceCreature objects.
 * <p>
 * This class contains statistical measures calculated from the attack power
 * values of creatures, including minimum, maximum, average, and standard deviation.
 */
public class CreatureStatistics {
    private final int minAttackPower;
    private final int maxAttackPower;
    private final double averageAttackPower;
    private final double standardDeviation;
    private final int count;

    /**
     * Constructs a new CreatureStatistics object.
     *
     * @param minAttackPower     the minimum attack power value
     * @param maxAttackPower     the maximum attack power value
     * @param averageAttackPower the average attack power value
     * @param standardDeviation  the standard deviation of attack power values
     * @param count              the number of creatures analyzed
     */
    public CreatureStatistics(int minAttackPower, int maxAttackPower,
                              double averageAttackPower, double standardDeviation, int count) {
        this.minAttackPower = minAttackPower;
        this.maxAttackPower = maxAttackPower;
        this.averageAttackPower = averageAttackPower;
        this.standardDeviation = standardDeviation;
        this.count = count;
    }

    /**
     * Gets the minimum attack power value.
     *
     * @return the minimum attack power
     */
    public int getMinAttackPower() {
        return minAttackPower;
    }

    /**
     * Gets the maximum attack power value.
     *
     * @return the maximum attack power
     */
    public int getMaxAttackPower() {
        return maxAttackPower;
    }

    /**
     * Gets the average attack power value.
     *
     * @return the average attack power
     */
    public double getAverageAttackPower() {
        return averageAttackPower;
    }

    /**
     * Gets the standard deviation of attack power values.
     *
     * @return the standard deviation
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * Gets the number of creatures analyzed.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("""
                        Статистика по силі атаки:
                        - Кількість істот: %d
                        - Мінімальна сила атаки: %d
                        - Максимальна сила атаки: %d
                        - Середня сила атаки: %.2f
                        - Стандартне відхилення: %.2f""",
                count, minAttackPower, maxAttackPower, averageAttackPower, standardDeviation);
    }
}

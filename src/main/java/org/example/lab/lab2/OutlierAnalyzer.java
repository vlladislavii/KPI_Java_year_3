package org.example.lab.lab2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Analyzer for detecting outliers in creature attack power values using the IQR method.
 * <p>
 * This class implements outlier detection based on the Interquartile Range (IQR).
 * Values are considered outliers if they fall below Q1 - 1.5*IQR or above Q3 + 1.5*IQR.
 * <p>
 * References:
 * - Interquartile range: https://uk.wikipedia.org/wiki/Міжквартильний_розмах
 * - Outliers: https://uk.wikipedia.org/wiki/Викид_(статистика)
 */
public class OutlierAnalyzer {

    /**
     * Result of outlier analysis containing statistics and categorized data.
     */
    public static class OutlierAnalysisResult {
        private final double q1;
        private final double q2;
        private final double q3;
        private final double iqr;
        private final double lowerBound;
        private final double upperBound;
        private final Map<String, Long> categoryCounts;

        public OutlierAnalysisResult(double q1, double q2, double q3, double iqr,
                                     double lowerBound, double upperBound,
                                     Map<String, Long> categoryCounts) {
            this.q1 = q1;
            this.q2 = q2;
            this.q3 = q3;
            this.iqr = iqr;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.categoryCounts = categoryCounts;
        }

        public double getQ1() {
            return q1;
        }

        public double getQ2() {
            return q2;
        }

        public double getQ3() {
            return q3;
        }

        public double getIqr() {
            return iqr;
        }

        public double getLowerBound() {
            return lowerBound;
        }

        public double getUpperBound() {
            return upperBound;
        }

        public Map<String, Long> getCategoryCounts() {
            return categoryCounts;
        }

        @Override
        public String toString() {
            return String.format("""
                            Аналіз викидів:
                            - Q1 (перший квартиль): %.2f
                            - Q2 (медіана): %.2f
                            - Q3 (третій квартиль): %.2f
                            - IQR (міжквартильний розмах): %.2f
                            - Нижня межа: %.2f
                            - Верхня межа: %.2f
                            - Категорії: %s""",
                    q1, q2, q3, iqr, lowerBound, upperBound, categoryCounts);
        }
    }

    /**
     * Analyzes the list of creatures for outliers in attack power.
     *
     * @param creatures the list of creatures to analyze
     * @return the analysis result with quartiles, IQR, and category counts
     */
    public static OutlierAnalysisResult analyzeOutliers(List<ChthoniceCreature> creatures) {
        // Extract and sort attack power values
        List<Integer> attackPowers = creatures.stream()
                .map(ChthoniceCreature::getAttackPower)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));

        if (attackPowers.isEmpty()) {
            return new OutlierAnalysisResult(0, 0, 0, 0, 0, 0,
                    Map.of("data", 0L, "outliers", 0L));
        }

        // Calculate quartiles
        double q1 = calculateQuartile(attackPowers, 0.25);
        double q2 = calculateQuartile(attackPowers, 0.50);
        double q3 = calculateQuartile(attackPowers, 0.75);

        // Calculate IQR (Interquartile Range)
        double iqr = q3 - q1;

        // Calculate outlier bounds using the 1.5*IQR rule
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        // Categorize creatures and count
        Map<String, Long> categoryCounts = creatures.stream()
                .collect(Collectors.groupingBy(
                        creature -> isOutlier(creature.getAttackPower(), lowerBound, upperBound)
                                ? "outliers" : "data",
                        Collectors.counting()
                ));

        categoryCounts.putIfAbsent("data", 0L);
        categoryCounts.putIfAbsent("outliers", 0L);

        return new OutlierAnalysisResult(q1, q2, q3, iqr, lowerBound, upperBound, categoryCounts);
    }

    /**
     * Calculates a quartile value from a sorted list of values.
     *
     * @param sortedValues the sorted list of values
     * @param percentile   the percentile (0.25 for Q1, 0.50 for Q2, 0.75 for Q3)
     * @return the quartile value
     */
    private static double calculateQuartile(List<Integer> sortedValues, double percentile) {
        if (sortedValues.isEmpty()) {
            return 0.0;
        }

        double index = percentile * (sortedValues.size() - 1);
        int lowerIndex = (int) Math.floor(index);
        int upperIndex = (int) Math.ceil(index);

        if (lowerIndex == upperIndex) {
            return sortedValues.get(lowerIndex);
        }

        double lowerValue = sortedValues.get(lowerIndex);
        double upperValue = sortedValues.get(upperIndex);
        double fraction = index - lowerIndex;

        return lowerValue + (upperValue - lowerValue) * fraction;
    }

    /**
     * Determines if a value is an outlier based on the IQR method.
     *
     * @param value      the value to check
     * @param lowerBound the lower bound (Q1 - 1.5*IQR)
     * @param upperBound the upper bound (Q3 + 1.5*IQR)
     * @return true if the value is an outlier, false otherwise
     */
    private static boolean isOutlier(int value, double lowerBound, double upperBound) {
        return value < lowerBound || value > upperBound;
    }
}

package org.example.lab.lab2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the OutlierAnalyzer class.
 */
class OutlierAnalyzerTest {

    @Test
    @DisplayName("Тест: обчислення квартилів для простого набору даних")
    void testQuartileCalculation() {
        // Create a simple dataset: attack powers from 10 to 90 (step 10)
        List<ChthoniceCreature> creatures = new ArrayList<>();
        for (int power = 10; power <= 90; power += 10) {
            creatures.add(new ChthoniceCreature(
                    "Creature" + power,
                    CreatureType.GHOST,
                    LocalDate.now().minusYears(100),
                    power
            ));
        }

        OutlierAnalyzer.OutlierAnalysisResult result = OutlierAnalyzer.analyzeOutliers(creatures);

        // For values [10, 20, 30, 40, 50, 60, 70, 80, 90]
        // Q1 ≈ 30, Q2 = 50, Q3 ≈ 70
        assertEquals(30.0, result.getQ1(), 5.0);
        assertEquals(50.0, result.getQ2(), 5.0);
        assertEquals(70.0, result.getQ3(), 5.0);
    }

    @Test
    @DisplayName("Тест: обчислення IQR")
    void testIQRCalculation() {
        List<ChthoniceCreature> creatures = new ArrayList<>();
        for (int power = 20; power <= 80; power += 10) {
            creatures.add(new ChthoniceCreature(
                    "Creature" + power,
                    CreatureType.DEMON,
                    LocalDate.now().minusYears(500),
                    power
            ));
        }

        OutlierAnalyzer.OutlierAnalysisResult result = OutlierAnalyzer.analyzeOutliers(creatures);

        double expectedIQR = result.getQ3() - result.getQ1();
        assertEquals(expectedIQR, result.getIqr(), 0.01);
    }

    @Test
    @DisplayName("Тест: визначення викидів")
    void testOutlierDetection() {
        List<ChthoniceCreature> creatures = new ArrayList<>();

        // Normal data: 40-60
        for (int i = 0; i < 20; i++) {
            creatures.add(new ChthoniceCreature(
                    "Normal" + i,
                    CreatureType.VAMPIRE,
                    LocalDate.now().minusYears(200),
                    50
            ));
        }

        // Add clear outliers
        creatures.add(new ChthoniceCreature("VeryWeak", CreatureType.GHOST, LocalDate.now(), 1));
        creatures.add(new ChthoniceCreature("VeryStrong", CreatureType.DRAGON, LocalDate.now(), 99));

        OutlierAnalyzer.OutlierAnalysisResult result = OutlierAnalyzer.analyzeOutliers(creatures);

        Map<String, Long> counts = result.getCategoryCounts();
        assertTrue(counts.get("outliers") > 0, "Should detect outliers");
        assertTrue(counts.get("data") > 0, "Should have normal data points");
        assertEquals(22, counts.get("data") + counts.get("outliers"), "Total count should be 22");
    }

    @Test
    @DisplayName("Тест: порожній список")
    void testEmptyList() {
        List<ChthoniceCreature> emptyList = new ArrayList<>();
        OutlierAnalyzer.OutlierAnalysisResult result = OutlierAnalyzer.analyzeOutliers(emptyList);

        assertEquals(0.0, result.getQ1());
        assertEquals(0.0, result.getQ2());
        assertEquals(0.0, result.getQ3());
        assertEquals(0.0, result.getIqr());
        assertEquals(0L, result.getCategoryCounts().get("data"));
        assertEquals(0L, result.getCategoryCounts().get("outliers"));
    }

    @Test
    @DisplayName("Тест: всі значення однакові (немає викидів)")
    void testUniformData() {
        List<ChthoniceCreature> creatures = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            creatures.add(new ChthoniceCreature(
                    "Same" + i,
                    CreatureType.ZOMBIE,
                    LocalDate.now().minusYears(100),
                    50
            ));
        }

        OutlierAnalyzer.OutlierAnalysisResult result = OutlierAnalyzer.analyzeOutliers(creatures);

        assertEquals(50.0, result.getQ1());
        assertEquals(50.0, result.getQ2());
        assertEquals(50.0, result.getQ3());
        assertEquals(0.0, result.getIqr());
        assertEquals(50L, result.getCategoryCounts().get("data"));
        assertEquals(0L, result.getCategoryCounts().get("outliers"));
    }
}

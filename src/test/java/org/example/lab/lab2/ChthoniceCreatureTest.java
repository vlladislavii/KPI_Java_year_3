package org.example.lab.lab2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ChthoniceCreature class.
 */
class ChthoniceCreatureTest {

    @Test
    @DisplayName("Тест: створення істоти з валідними параметрами")
    void testCreatureCreation() {
        LocalDate date = LocalDate.of(1897, 5, 26);
        ChthoniceCreature creature = new ChthoniceCreature(
                "Дракула",
                CreatureType.VAMPIRE,
                date,
                85
        );

        assertEquals("Дракула", creature.getName());
        assertEquals(CreatureType.VAMPIRE, creature.getType());
        assertEquals(date, creature.getFirstMentionDate());
        assertEquals(85, creature.getAttackPower());
    }

    @Test
    @DisplayName("Тест: обчислення років з моменту першої згадки")
    void testYearsSinceFirstMention() {
        LocalDate date = LocalDate.now().minusYears(100);
        ChthoniceCreature creature = new ChthoniceCreature(
                "Тестова Істота",
                CreatureType.GHOST,
                date,
                50
        );

        assertEquals(100, creature.getYearsSinceFirstMention());
    }

    @Test
    @DisplayName("Тест: перевірка на null ім'я")
    void testNullName() {
        assertThrows(NullPointerException.class, () ->
                new ChthoniceCreature(null, CreatureType.DEMON, LocalDate.now(), 50)
        );
    }

    @Test
    @DisplayName("Тест: перевірка на null тип")
    void testNullType() {
        assertThrows(NullPointerException.class, () ->
                new ChthoniceCreature("Тест", null, LocalDate.now(), 50)
        );
    }

    @Test
    @DisplayName("Тест: перевірка на null дату")
    void testNullDate() {
        assertThrows(NullPointerException.class, () ->
                new ChthoniceCreature("Тест", CreatureType.DRAGON, null, 50)
        );
    }

    @Test
    @DisplayName("Тест: перевірка на недопустиму силу атаки (негативна)")
    void testNegativeAttackPower() {
        assertThrows(IllegalArgumentException.class, () ->
                new ChthoniceCreature("Тест", CreatureType.ZOMBIE, LocalDate.now(), -1)
        );
    }

    @Test
    @DisplayName("Тест: перевірка на недопустиму силу атаки (>100)")
    void testTooHighAttackPower() {
        assertThrows(IllegalArgumentException.class, () ->
                new ChthoniceCreature("Тест", CreatureType.DEMON, LocalDate.now(), 101)
        );
    }

    @Test
    @DisplayName("Тест: граничні значення сили атаки")
    void testBoundaryAttackPower() {
        assertDoesNotThrow(() ->
                new ChthoniceCreature("Слабкий", CreatureType.GHOST, LocalDate.now(), 0)
        );
        assertDoesNotThrow(() ->
                new ChthoniceCreature("Сильний", CreatureType.DRAGON, LocalDate.now(), 100)
        );
    }

    @Test
    @DisplayName("Тест: equals і hashCode")
    void testEqualsAndHashCode() {
        LocalDate date = LocalDate.of(1818, 1, 1);
        ChthoniceCreature creature1 = new ChthoniceCreature("Франкенштейн", CreatureType.ZOMBIE, date, 75);
        ChthoniceCreature creature2 = new ChthoniceCreature("Франкенштейн", CreatureType.ZOMBIE, date, 75);
        ChthoniceCreature creature3 = new ChthoniceCreature("Інша Істота", CreatureType.ZOMBIE, date, 75);

        assertEquals(creature1, creature2);
        assertNotEquals(creature1, creature3);
        assertEquals(creature1.hashCode(), creature2.hashCode());
    }
}

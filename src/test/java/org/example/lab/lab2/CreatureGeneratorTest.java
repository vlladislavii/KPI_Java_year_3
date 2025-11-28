package org.example.lab.lab2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CreatureGenerator class.
 */
class CreatureGeneratorTest {

    @Test
    @DisplayName("Тест: генерація нескінченного стріму")
    void testInfiniteStreamGeneration() {
        CreatureGenerator generator = new CreatureGenerator();
        List<ChthoniceCreature> creatures = generator.generateInfiniteStream()
                .limit(100)
                .collect(Collectors.toList());

        assertEquals(100, creatures.size());
        creatures.forEach(creature -> {
            assertNotNull(creature.getName());
            assertNotNull(creature.getType());
            assertNotNull(creature.getFirstMentionDate());
            assertTrue(creature.getAttackPower() >= 0 && creature.getAttackPower() <= 100);
        });
    }

    @Test
    @DisplayName("Тест: генератор з seed генерує однакові послідовності")
    void testGeneratorWithSeedIsReproducible() {
        CreatureGenerator generator1 = new CreatureGenerator(42);
        CreatureGenerator generator2 = new CreatureGenerator(42);

        List<ChthoniceCreature> creatures1 = generator1.generateInfiniteStream()
                .limit(50)
                .collect(Collectors.toList());

        List<ChthoniceCreature> creatures2 = generator2.generateInfiniteStream()
                .limit(50)
                .collect(Collectors.toList());

        assertEquals(creatures1, creatures2);
    }

    @Test
    @DisplayName("Тест: генератор створює різні типи істот")
    void testGeneratorCreatesVariousTypes() {
        CreatureGenerator generator = new CreatureGenerator();
        List<CreatureType> types = generator.generateInfiniteStream()
                .limit(200)
                .map(ChthoniceCreature::getType)
                .distinct()
                .collect(Collectors.toList());

        // With 200 creatures, we should have multiple different types
        assertTrue(types.size() > 1, "Generator should create various creature types");
    }

    @Test
    @DisplayName("Тест: сила атаки в допустимих межах")
    void testAttackPowerWithinBounds() {
        CreatureGenerator generator = new CreatureGenerator();
        List<ChthoniceCreature> creatures = generator.generateInfiniteStream()
                .limit(500)
                .collect(Collectors.toList());

        creatures.forEach(creature -> {
            int power = creature.getAttackPower();
            assertTrue(power >= 0 && power <= 100,
                    String.format("Attack power %d should be between 0 and 100", power));
        });
    }

    @Test
    @DisplayName("Тест: дати першої згадки реалістичні")
    void testFirstMentionDatesAreRealistic() {
        CreatureGenerator generator = new CreatureGenerator();
        List<ChthoniceCreature> creatures = generator.generateInfiniteStream()
                .limit(100)
                .collect(Collectors.toList());

        creatures.forEach(creature -> {
            long years = creature.getYearsSinceFirstMention();
            assertTrue(years >= 0, "Years since first mention should be non-negative");
            assertTrue(years < 10000, "Years since first mention should be realistic (<10000 years)");
        });
    }
}

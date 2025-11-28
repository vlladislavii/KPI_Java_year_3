package org.example.lab.lab2;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Generator for creating infinite streams of random ChthoniceCreature objects.
 * <p>
 * This class generates creatures with realistic parameters based on their types
 * and historical/mythological context.
 */
public class CreatureGenerator {
    private final Random random;

    // Creature names for each type
    private static final String[][] CREATURE_NAMES = {
            // VAMPIRE
            {"Дракула", "Носферату", "Кармілла", "Влад Цепеш", "Граф Орлок", "Лестат", "Алукард"},
            // DEMON
            {"Асмодей", "Вельзевул", "Люцифер", "Мефістофель", "Белфегор", "Левіафан", "Астарот"},
            // DRAGON
            {"Змій Горинич", "Фафнір", "Смауг", "Дрогон", "Анкалагон", "Нідгоґ", "Тіамат"},
            // GHOST
            {"Біла Пані", "Сірий Монах", "Плачуча Жінка", "Кривава Мері", "Привид Опери"},
            // WEREWOLF
            {"Талбот", "Ван Хельсінг", "Ларрі Талбот", "Ремус Люпін", "Fenrir"},
            // WITCH
            {"Баба Яга", "Моргана", "Цирцея", "Медея", "Гекуба", "Відьма з Блер"},
            // ZOMBIE
            {"Ходячий Мрець", "Зомбі №13", "Інфікований", "Нежить", "Реаніматор"},
            // GHOUL
            {"Упир Нічний", "Гуль з Катакомб", "Трупоїд", "Могильник"},
            // BANSHEE
            {"Віла Плакальниця", "Банші О'Браєн", "Вісниця Смерті", "Скорботна Тінь"},
            // TROLL
            {"Грімзуб", "Камінь-Дроб", "Тролль Гірський", "Великий Угрюм"}
    };

    /**
     * Constructs a new CreatureGenerator with a default random seed.
     */
    public CreatureGenerator() {
        this.random = new Random();
    }

    /**
     * Constructs a new CreatureGenerator with a specified seed for reproducibility.
     *
     * @param seed the seed for random number generation
     */
    public CreatureGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Generates an infinite stream of random ChthoniceCreature objects.
     * <p>
     * The stream generates creatures with:
     * - Random types
     * - Appropriate names for each type
     * - Realistic first mention dates (from ancient times to modern era)
     * - Attack power correlated with creature type
     *
     * @return an infinite stream of ChthoniceCreature objects
     */
    public Stream<ChthoniceCreature> generateInfiniteStream() {
        return Stream.generate(this::generateCreature);
    }

    /**
     * Generates a single random ChthoniceCreature.
     *
     * @return a randomly generated creature
     */
    private ChthoniceCreature generateCreature() {
        CreatureType type = getRandomType();
        String name = getRandomName(type);
        LocalDate firstMention = getRandomFirstMentionDate(type);
        int attackPower = getRandomAttackPower(type);

        return new ChthoniceCreature(name, type, firstMention, attackPower);
    }

    /**
     * Selects a random creature type.
     *
     * @return a random CreatureType
     */
    private CreatureType getRandomType() {
        CreatureType[] types = CreatureType.values();
        return types[random.nextInt(types.length)];
    }

    /**
     * Selects a random name appropriate for the given creature type.
     *
     * @param type the creature type
     * @return a random name
     */
    private String getRandomName(CreatureType type) {
        String[] names = CREATURE_NAMES[type.ordinal()];
        String baseName = names[random.nextInt(names.length)];

        // Sometimes add a number suffix to create variety
        if (random.nextDouble() < 0.3) {
            return baseName + " " + (random.nextInt(50) + 1);
        }
        return baseName;
    }

    /**
     * Generates a realistic first mention date based on creature type.
     * Ancient creatures (dragons, demons) have earlier dates,
     * modern creatures (zombies) have more recent dates.
     *
     * @param type the creature type
     * @return a random first mention date
     */
    private LocalDate getRandomFirstMentionDate(CreatureType type) {
        int currentYear = LocalDate.now().getYear();
        int minYear, maxYear;

        switch (type) {
            case DRAGON, DEMON, GHOST -> {
                // Ancient creatures: 3000 BC to 1900 AD
                minYear = -3000;
                maxYear = 1900;
            }
            case VAMPIRE, WEREWOLF, WITCH -> {
                // Medieval creatures: 500 AD to 1950 AD
                minYear = 500;
                maxYear = 1950;
            }
            case ZOMBIE, GHOUL -> {
                // Modern creatures: 1800 AD to 2000 AD
                minYear = 1800;
                maxYear = 2000;
            }
            case BANSHEE, TROLL -> {
                // Celtic/Norse creatures: 800 AD to 1800 AD
                minYear = 800;
                maxYear = 1800;
            }
            default -> {
                minYear = 1000;
                maxYear = currentYear;
            }
        }

        int year = minYear + random.nextInt(maxYear - minYear + 1);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28); // Use 28 to avoid invalid dates

        return LocalDate.of(year, month, day);
    }

    /**
     * Generates attack power based on creature type.
     * More dangerous creatures (demons, dragons) have higher base attack power.
     * <p>
     * Includes a 5% chance of generating extreme outliers (very weak or very strong)
     * to demonstrate outlier detection with IQR method.
     *
     * @param type the creature type
     * @return attack power (0-100)
     */
    private int getRandomAttackPower(CreatureType type) {
        // 5% chance of generating an extreme outlier
        if (random.nextDouble() < 0.05) {
            return random.nextBoolean() ?
                    random.nextInt(10) :      // Very weak outlier: 0-9
                    95 + random.nextInt(6);   // Very strong outlier: 95-100
        }

        int baseMin, baseMax;

        switch (type) {
            case DRAGON, DEMON -> {
                // Very powerful creatures
                baseMin = 60;
                baseMax = 75;
            }
            case VAMPIRE, WEREWOLF -> {
                // Powerful creatures
                baseMin = 45;
                baseMax = 60;
            }
            case ZOMBIE, GHOUL, TROLL -> {
                // Moderately powerful
                baseMin = 35;
                baseMax = 50;
            }
            case GHOST, BANSHEE, WITCH -> {
                // Variable power
                baseMin = 30;
                baseMax = 55;
            }
            default -> {
                baseMin = 30;
                baseMax = 60;
            }
        }

        return baseMin + random.nextInt(baseMax - baseMin + 1);
    }
}

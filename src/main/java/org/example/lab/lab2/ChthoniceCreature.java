package org.example.lab.lab2;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a chthonic creature (evil spirit/monster) from mythology and literature.
 * <p>
 * This class contains information about mythological creatures including their name,
 * type, first literary mention date, and attack power.
 */
public class ChthoniceCreature {
    private final String name;
    private final CreatureType type;
    private final LocalDate firstMentionDate;
    private final int attackPower;

    /**
     * Constructs a new ChthoniceCreature.
     *
     * @param name             the name of the creature
     * @param type             the type of the creature
     * @param firstMentionDate the date of first mention in literature
     * @param attackPower      the attack power of the creature (0-100)
     */
    public ChthoniceCreature(String name, CreatureType type, LocalDate firstMentionDate, int attackPower) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.firstMentionDate = Objects.requireNonNull(firstMentionDate, "First mention date cannot be null");
        if (attackPower < 0 || attackPower > 100) {
            throw new IllegalArgumentException("Attack power must be between 0 and 100");
        }
        this.attackPower = attackPower;
    }

    /**
     * Gets the name of the creature.
     *
     * @return the creature's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the creature (Field A, Field B).
     *
     * @return the creature type
     */
    public CreatureType getType() {
        return type;
    }

    /**
     * Gets the date of first mention in literature.
     *
     * @return the first mention date
     */
    public LocalDate getFirstMentionDate() {
        return firstMentionDate;
    }

    /**
     * Calculates the number of years since first mention in literature (Parameter B).
     *
     * @return years since first mention
     */
    public long getYearsSinceFirstMention() {
        return ChronoUnit.YEARS.between(firstMentionDate, LocalDate.now());
    }

    /**
     * Gets the attack power of the creature (Field C).
     *
     * @return the attack power (0-100)
     */
    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChthoniceCreature that = (ChthoniceCreature) o;
        return attackPower == that.attackPower &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(firstMentionDate, that.firstMentionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, firstMentionDate, attackPower);
    }

    @Override
    public String toString() {
        return String.format("ChthoniceCreature{name='%s', type=%s, firstMention=%s (%d years ago), attackPower=%d}",
                name, type, firstMentionDate, getYearsSinceFirstMention(), attackPower);
    }
}
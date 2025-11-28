package org.example.lab.lab2;

/**
 * Represents different types of chthonic creatures from mythology and folklore.
 */
public enum CreatureType {
    /**
     * Vampires - blood-drinking undead creatures
     */
    VAMPIRE("Вампір"),

    /**
     * Demons - evil spirits from various mythologies
     */
    DEMON("Демон"),

    /**
     * Dragons - large reptilian legendary creatures
     */
    DRAGON("Дракон"),

    /**
     * Ghosts - spirits of the deceased
     */
    GHOST("Привид"),

    /**
     * Werewolves - shapeshifters that transform into wolves
     */
    WEREWOLF("Перевертень"),

    /**
     * Witches - practitioners of dark magic
     */
    WITCH("Відьма"),

    /**
     * Zombies - reanimated corpses
     */
    ZOMBIE("Зомбі"),

    /**
     * Ghouls - creatures that feed on corpses
     */
    GHOUL("Упир"),

    /**
     * Banshees - wailing spirits that predict death
     */
    BANSHEE("Банші"),

    /**
     * Trolls - large, brutish creatures from Scandinavian folklore
     */
    TROLL("Троль");

    private final String ukrainianName;

    CreatureType(String ukrainianName) {
        this.ukrainianName = ukrainianName;
    }

    /**
     * Gets the Ukrainian name of the creature type.
     *
     * @return Ukrainian name
     */
    public String getUkrainianName() {
        return ukrainianName;
    }

    @Override
    public String toString() {
        return ukrainianName;
    }
}
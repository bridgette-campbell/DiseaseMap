package edu.uw.tacoma.css.diseasemap.disease;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles DiseaseItems
 */
public class DiseaseContent {

    /**
     * ArrayList of all DiseaseItems
     */
    public static final List<DiseaseItem> ITEMS = new ArrayList<>();

    // Add sample DiseaseItems
    static {
        String[] sampleDiseases = {"Swine", "Turtle", "Cow", "Octopus", "Zebra", "Seagull",
                "Monkey", "Panda", "Sloth", "Cat", "Penguin", "Polar Bear", "Shark", "Alien",
                "Regular", "Gluten", "Unknown"};

        for (String s : sampleDiseases)
            addItem(new DiseaseItem(s + " Flu"));
    }

    /**
     * Adds a single DiseaseItem to the list of all DiseaseItems
     *
     * @param item The DiseaseItem to add
     */
    private static void addItem(DiseaseItem item) {
        ITEMS.add(item);
    }

    /**
     * Represents an individual disease
     */
    public static class DiseaseItem {

        /**
         * The DiseaseItem's name
         */
        public final String name;

        /**
         * Constructor
         *
         * @param name The name of the Disease
         */
        public DiseaseItem(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

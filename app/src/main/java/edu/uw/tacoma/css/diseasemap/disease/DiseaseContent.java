package edu.uw.tacoma.css.diseasemap.disease;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
public class DiseaseContent {

    // ArrayList of all Diseases
    public static final List<DiseaseItem> ITEMS = new ArrayList<>();

    // Add sample Diseases
    static {
        String[] sampleDiseases = {"Swine", "Turtle", "Cow", "Octopus", "Zebra", "Seagull",
                "Monkey", "Panda", "Sloth", "Cat", "Penguin", "Polar Bear", "Shark", "Alien",
                "Regular", "Gluten", "Unknown"};

        for (String s : sampleDiseases)
            addItem(new DiseaseItem(s + " Flu"));
    }

    private static void addItem(DiseaseItem item) {
        ITEMS.add(item);
    }

    /**
     * An individual disease
     */
    public static class DiseaseItem {
        public final String name;

        public DiseaseItem(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

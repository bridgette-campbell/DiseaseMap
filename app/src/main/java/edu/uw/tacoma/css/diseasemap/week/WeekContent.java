package edu.uw.tacoma.css.diseasemap.week;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles WeekItems
 */
public class WeekContent {

    /**
     * ArrayList of all WeekItems
     */
    public static final List<WeekItem> ITEMS = new ArrayList<>();

    // Add sample WeekItems
    static {
        String[] sampleWeeks = {"Jan 1-7", "Jan 8-14", "Jan 15-21", "Jan 22-28", "Jan 29-Feb 4",
                "Feb 5-11", "Feb 12-18", "Feb 19-25", "Feb 26-Mar 4", "--Copy/pasting--", "Jan 1-7",
                "Jan 8-14", "Jan 15-21", "Jan 22-28", "Jan 29-Feb 4", "Feb 5-11", "Feb 12-18",
                "Feb 19-25", "Feb 26-Mar 4"};

        for (int i = 0; i < sampleWeeks.length; i++)
            addItem(new WeekItem(i + ": " + sampleWeeks[i]));
    }

    /**
     * Adds a single WeekItem to the list of all WeekItems
     *
     * @param item The WeekItem to add
     */
    private static void addItem(WeekItem item) {
        ITEMS.add(item);
    }

    /**
     * Represents an individual week
     */
    public static class WeekItem {

        /**
         * The WeekItem's week
         */
        public final String week;

        /**
         * Constructor
         *
         * @param week The week
         */
        public WeekItem(String week) {
            this.week = week;
        }

        @Override
        public String toString() {
            return week;
        }
    }
}

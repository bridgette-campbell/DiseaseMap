package edu.uw.tacoma.css.diseasemap.week;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
public class WeekContent {

    // ArrayList of all Weeks
    public static final List<WeekItem> ITEMS = new ArrayList<>();

    // Add sample Weeks
    static {
        String[] sampleWeeks = {"Jan 1-7", "Jan 8-14", "Jan 15-21", "Jan 22-28", "Jan 29-Feb 4",
                "Feb 5-11", "Feb 12-18", "Feb 19-25", "Feb 26-Mar 4", "--Copy/pasting--", "Jan 1-7",
                "Jan 8-14", "Jan 15-21", "Jan 22-28", "Jan 29-Feb 4", "Feb 5-11", "Feb 12-18",
                "Feb 19-25", "Feb 26-Mar 4"};

        for (int i = 0; i < sampleWeeks.length; i++)
            addItem(new WeekItem(i + ": " + sampleWeeks[i]));
    }

    private static void addItem(WeekItem item) {
        ITEMS.add(item);
    }

    /**
     * An individual week
     */
    public static class WeekItem {
        public final String week;

        public WeekItem(String week) {
            this.week = week;
        }

        @Override
        public String toString() {
            return week;
        }
    }
}

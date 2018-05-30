package edu.uw.tacoma.css.diseasemap.database_connection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a general representation of a disease record, as we intend to use for to display in our visual.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public final class DiseaseRecord implements Serializable {

    /*
     * This is a Map<Integer, Map<String, WeekInfo>>, where the outer map's key is the mapping of
     * WeekNumber -> Map<String, WeekInfo>, the Second map is mapping a String -> WeekInfo, where
     * the String is the name of the reporting location.
     */
    private Map<Integer, Map<String, WeekInfo>> weekInfo = new HashMap<>();
    private Map<Integer, WeekInfo> weekMax = new HashMap<>();
    private Map<Integer, WeekInfo> weekMin = new HashMap<>();

    /**
     * This takes the list of WeekInfo and map it into a format that can be more readily used.
     *
     * @param weekInfo the week-by-week data of the disease
     */
    public DiseaseRecord(List<WeekInfo> weekInfo) {

        for (WeekInfo wi : weekInfo) {

            Integer week = wi.getWeek();

            if (this.weekInfo.get(week) == null) {
                this.weekInfo.put(week, new HashMap<String, WeekInfo>());
            }

            if(weekMax.get(week) == null || weekMax.get(week).getCumulativeInfected() < wi.getCumulativeInfected()){
                weekMax.put(week, wi);
            }
            if(weekMin.get(week) == null || wi.getCumulativeInfected() < weekMin.get(week).getCumulativeInfected()){
                weekMin.put(week, wi);
            }

            this.weekInfo.get(wi.getWeek()).put(wi.getReportingArea(), wi);
        }
    }

    /**
     * This will return the Map associated with the specified week.
     *
     * @param week the week of the year
     * @return a {@link Map<String, WeekInfo}>}
     */
    public Map<String, WeekInfo> getInfoForWeek(Integer week) {
        return this.weekInfo.get(week);
    }

    /**
     * Returns the number of weeks covered by this record.
     *
     * @return the {@link Integer} number of weeks
     */
    public Set<Integer> getWeeks() {
        return this.weekInfo.keySet();
    }

    public WeekInfo getMaxForWeek(Integer week){
        return this.weekMax.get(week);
    }

    public WeekInfo getMinForWeek(Integer week){
        return this.weekMin.get(week);
    }

    /**
     * This is a manifestation of a single week's recording for a single location.
     *
     * @author Bridgette Campbell, Daniel McBride, Matt Qunell
     */
    public static class WeekInfo implements Serializable {
        private final Integer year;
        private final Integer week;
        private final Integer infected;
        private final Integer cumulativeInfected;
        private final String reportingArea;

        /**
         * Constructor
         *
         * @param year          the year that the week corresponds to (2018, 2017, etc.)
         * @param week          the week of the year (Week 1, Week 5, etc.)
         * @param infected      the number of reported infected
         * @param reportingArea the location that the report is coming from
         */
        public WeekInfo(Integer year, Integer week, Integer infected, Integer cumulativeInfected, String reportingArea) {
            this.year = year;
            this.week = week;
            this.infected = infected;
            this.cumulativeInfected = cumulativeInfected;
            this.reportingArea = reportingArea;
        }

        public String getReportingArea() {
            return reportingArea;
        }

        public Integer getWeek() {
            return this.week;
        }

        public Integer getYear() {
            return this.year;
        }

        public Integer getInfected() {
            return infected;
        }

        public Integer getCumulativeInfected() {
            return this.cumulativeInfected;
        }
    }

}

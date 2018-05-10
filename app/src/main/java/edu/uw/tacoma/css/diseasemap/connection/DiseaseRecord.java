package edu.uw.tacoma.css.diseasemap.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public final class DiseaseRecord {

    Map<Integer, Map<String, WeekInfo>> weekInfo = new HashMap<>();
    private final String name;

    public DiseaseRecord(String name, List<WeekInfo> weekInfo) {

        this.name = name;

        for (WeekInfo wi : weekInfo) {

            if (this.weekInfo.get(wi.getWeek()) == null) {
                this.weekInfo.put(wi.getWeek(), new HashMap<String, WeekInfo>());
            }

            this.weekInfo.get(wi.getWeek()).put(wi.getReportingArea(), wi);
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, WeekInfo> getInfoForWeek(Integer week) {
        return this.weekInfo.get(week);
    }

    public Integer getWeeks(){
        return this.weekInfo.size();
    }

    public static class WeekInfo {
        private final Integer year;
        private final Integer week;
        private final Integer infected;
        private final String reportingArea;

        public WeekInfo(Integer year, Integer week, Integer infected, String reportingArea) {
            this.year = year;
            this.week = week;
            this.infected = infected;
            this.reportingArea = reportingArea;
        }

        public String getReportingArea() {
            return reportingArea;
        }

        public Integer getWeek() {
            return this.week;
        }

        public Integer getInfected() {
            return infected;
        }
    }

}

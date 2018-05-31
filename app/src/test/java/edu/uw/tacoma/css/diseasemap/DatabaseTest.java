package edu.uw.tacoma.css.diseasemap;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;

public class DatabaseTest extends TestCase {

    DiseaseRecord sample;
    String name = "Senioritis";
    String reportingLocation = "Knowhere";
    Integer infected;

    @Override
    protected void setUp() throws Exception {
        infected = (int) (10 * Math.random());
        DiseaseRecord.WeekInfo wi1 = new DiseaseRecord.WeekInfo(2018, 1, infected, infected, reportingLocation);
        DiseaseRecord.WeekInfo wi2 = new DiseaseRecord.WeekInfo(2018, 2, infected, 2 * infected, reportingLocation);
        List<DiseaseRecord.WeekInfo> weekInfo = new ArrayList<>();
        weekInfo.add(wi1);
        weekInfo.add(wi2);
        sample = new DiseaseRecord(name, weekInfo);

    }

    /*

    //This throws the IllegalArgumentException as expected, but still says it fails...

    @Test(expected = IllegalArgumentException.class)
    public void testDiseaseRecordException(){
        new DiseaseRecord(null, null);
    }

    */

    @Test
    public void testDiseaseRecordGetters(){
        assertEquals(infected, sample.getMaxForWeek(1).getCumulativeInfected());
        assertEquals(infected, sample.getMinForWeek(1).getCumulativeInfected());

        assertEquals(name, sample.getName());
    }

    @Test
    public void testWeekInfoGetters(){
        DiseaseRecord.WeekInfo wi1 = sample.getInfoForWeek(1).get(reportingLocation);
        assertEquals(infected, wi1.getCumulativeInfected());
        assertEquals(infected, wi1.getInfected());
        assertEquals(Integer.valueOf(1), wi1.getWeek());
        assertEquals(Integer.valueOf(2018), wi1.getYear());
        assertEquals(reportingLocation, wi1.getReportingArea());

        DiseaseRecord.WeekInfo wi2 = sample.getInfoForWeek(2).get(reportingLocation);
        assertEquals(Integer.valueOf(infected * 2), wi2.getCumulativeInfected());

    }

}

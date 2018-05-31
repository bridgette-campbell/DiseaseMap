package edu.uw.tacoma.css.diseasemap.database_connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This object creates a connection to the NNDSS database. It can be used to query the NNDSS
 * database for a specified disease.
 * <p>
 * Here is a list of the tables, in a readable format.
 *
 * @see <a href="https://wonder.cdc.gov/nndss/nndss_weekly_tables_menu.asp?mmwr_year=&mmwr_week=">https://wonder.cdc.gov/nndss/nndss_weekly_tables_menu.asp?mmwr_year=&mmwr_week=</a>
 * <p>
 * Actual API datasets can be found:
 * @see <a href="https://data.cdc.gov/browse?category=NNDSS&sortBy=last_modified">https://data.cdc.gov/browse?category=NNDSS&sortBy=last_modified</a>
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public final class NNDSSConnection
        extends AsyncTask<NNDSSConnection.DiseaseTable, Void, List<DiseaseRecord>> {

    /*
     * API constants
     */
    private static String CDC_DATABASE_ADDRESS = "https://data.cdc.gov/resource/";
    private static String APP_TOKEN = "$$app_token=csJoX8FOoaC0U29v2HtsUk4nb";
    private static String JSON_SUFFIX = ".json?";

    private static final String MMWR_YEAR = "mmwr_year";
    private static final String MMWR_WEEK = "mmwr_week";
    private static final String _CURRENT_WEEK = "_current_week";
    private static final String _CURRENT_WEEK_FLAG = "_current_week_flag";
    private static final String _CUM_ = "_cum_";
    private static final String _FLAG = "_flag";
    private static final String REPORTING_AREA = "reporting_area";

    /**
     * This is a list of areas to exclude.
     */
    private static final List<String> EXLUDE_AREAS = new ArrayList<String>(){{
        add("UNITED STATES");
        add("S. ATLANTIC");
        add("PACIFIC");
        add("E.N. CENTRAL");
        add("W.S. CENTRAL");
        add("MOUNTAIN");
        add("MID. ATLANTIC");
        add("NEW ENGLAND");
        add("W.N. CENTRAL");
        add("E.S. CENTRAL");
        add("C.N.M.I.");
    }};

    //A tag.
    private static final String TAG = "NNDSSConnection";

    /**
     * This will return a {@link DiseaseRecord} constructed for the specified {@link DiseaseTable}.
     *
     * @param diseaseTable the {@link DiseaseTable} to query
     * @return the {@link DiseaseRecord}
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public DiseaseRecord getDiseaseFromTable(DiseaseTable diseaseTable)
            throws ExecutionException, InterruptedException {

        List<DiseaseRecord> dList = this.execute(diseaseTable).get();
        assert dList.size() == 1;
        return dList.get(0);
    }

    /**
     * This creates a {@link URL} used to query the specified table.
     *
     * @param diseaseTable the {@link DiseaseTable} to query
     * @return the {@link URL} connection to the table
     * @throws MalformedURLException
     */
    private URL createConnectionURL(DiseaseTable diseaseTable) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();

        sb.append(CDC_DATABASE_ADDRESS);
        sb.append(diseaseTable.getTableName());
        sb.append(JSON_SUFFIX);
        sb.append(APP_TOKEN);

        return new URL(sb.toString());
    }

    /**
     * This queries the database and pulls the information that is relevant to the desired
     * {@link DiseaseRecord}, it then constructs a list of {@link DiseaseRecord.WeekInfo}
     * and constructs a {@link DiseaseRecord} with them.
     *
     * @param diseaseTables the {@link DiseaseTable}(s) to query
     * @return a {@link List<DiseaseRecord>} of disease records
     */
    @Override
    protected List<DiseaseRecord> doInBackground(DiseaseTable... diseaseTables) {

        List<DiseaseRecord> dList = new ArrayList<>();
        try {
            for (DiseaseTable dt : diseaseTables) {


                //Log.i(TAG, "Reading from: " + dt.getTableName());
                HttpURLConnection conn = (HttpURLConnection) createConnectionURL(dt)
                        .openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                conn.connect();

                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                BufferedReader rd = new BufferedReader(isr);

                StringBuilder sb = new StringBuilder();

                String str;
                while ((str = rd.readLine()) != null) {
                    sb.append(str);
                }

                JSONArray jsonArray = new JSONArray(sb.toString());

                List<DiseaseRecord.WeekInfo> weekInfoList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    Integer year = jsonArray.getJSONObject(i).getInt(MMWR_YEAR);

                    Integer week = jsonArray.getJSONObject(i).getInt(MMWR_WEEK);

                    Integer infected;
                    if (jsonArray.getJSONObject(i).has(dt.diseaseName + _CURRENT_WEEK_FLAG)) {
                        infected = 0;
                    } else {
                        infected = jsonArray.getJSONObject(i).getInt(dt.diseaseName
                                + _CURRENT_WEEK);
                    }

                    String yearlyCumulativeFlag = dt.diseaseName + _CUM_ + year.toString() + _FLAG;
                    String yearlyCumulative = dt.diseaseName + _CUM_ + year.toString();

                    Integer cumInfected;

                    if (jsonArray.getJSONObject(i).has(yearlyCumulativeFlag)) {
                        cumInfected = 0;
                    } else {
                        cumInfected = jsonArray.getJSONObject(i).getInt(yearlyCumulative);
                    }



                    String reportingArea = jsonArray.getJSONObject(i).getString(REPORTING_AREA);

                    if(EXLUDE_AREAS.contains(reportingArea)){
                        continue;
                    }

                    //Log.i(TAG, "Parsed week: " + week + " reporting location: " + reportingArea);

                    weekInfoList.add(
                            new DiseaseRecord.WeekInfo(year, week, infected, cumInfected, reportingArea));
                }

                dList.add(new DiseaseRecord(dt.getDiseaseName(), weekInfoList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dList;
    }


    /**
     * These are complex enums that contain information about disease tables.
     * Its just an enum with fields.
     */
    public enum DiseaseTable {
        Babesiosis("nkqh-e867", "babesiosis", "Babesiosis"),
        Campylobacteriosis("nkqh-e867", "campylobacteriosis", "Campylobacteriosis"),
        Chlamydia_Trachomatis("dsz3-9wvn", "chlamydia_trachomatis_infection", "Chlamydia Trachomatis"),
        Coccidioidomycosis("dsz3-9wvn", "Coccidioidomycosis", "Coccidioidomycosis"),
        Meningococcal_Disease("w3an-exa3", "meningococcal_disease_all_serogroups", "Meningococcal Disease"),
        Mumps("w3an-exa3", "mumps", "Mumps"),
        Pertussis("w3an-exa3", "pertussis", "Pertussis"),
        Rabies("j75t-qfp3", "rabies_animal", "Rabies"),
        Rubella_Congenital_Syndrome("j75t-qfp3", "rubella_congenital_syndrome", "Rubella Syndrome"),
        Varicella("v9up-rs3x", "varicella", "Varicella"),
        Salmonellosis("rhry-k9aj", "salmonellosis_excluding_paratyphoid_fever_andtyphoid_fever", "Salmonellosis");

        private static final String TAG = "DiseaseTable";

        //The name of the table that the disease is in.
        private final String tableName;
        //The name of the disease in the table.
        private final String diseaseName;
        //The name as the disease should be displayed.
        private final String displayName;

        /**
         * Basic constructor.
         * @param tableName
         * @param diseaseName
         * @param displayName
         */
        DiseaseTable(String tableName, String diseaseName, String displayName) {
            this.tableName = tableName;
            this.diseaseName = diseaseName;
            this.displayName = displayName;
        }

        /**
         * Returns the name of the table
         * @return
         */
        public String getTableName() {
            return this.tableName;
        }

        /**
         * Returns the name of the disease.
         * @return
         */
        public String getDiseaseName() {
            return this.diseaseName;
        }

        /**
         * returns the name as it should be displayed to the user.
         * @return
         */
        public String getDisplayName() {
            return this.displayName;
        }

        /**
         * Gets the DiseaseTable that matches this name.
         * @param tableName
         * @return
         * @throws IllegalAccessException
         */
        public static DiseaseTable getOfName(String tableName) throws IllegalAccessException {
            for (DiseaseTable dt : DiseaseTable.values()) {
                if (dt.getDiseaseName().equals(tableName)) {
                    return dt;
                }
            }
            throw new IllegalAccessException("No DiseaseTable with the name: \""
                    + tableName + "\"");
        }
    }
}

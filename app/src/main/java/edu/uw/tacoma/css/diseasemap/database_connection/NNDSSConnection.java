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

import edu.uw.tacoma.css.diseasemap.disease.DiseaseRecord;

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
 */
public final class NNDSSConnection extends AsyncTask<NNDSSConnection.DiseaseTable, Void, List<DiseaseRecord>> {

    /*API CONSTANTS*/
    public static final String MMWR_YEAR = "mmwr_year";
    public static final String MMWR_WEEK = "mmwr_week";
    public static final String _CURRENT_WEEK = "_current_week";
    public static final String _CURRENT_WEEK_FLAG = "_current_week_flag";
    public static final String REPORTING_AREA = "reporting_area";

    /**
     * These are complex enums that contain information about disease tables.
     * Its just an enum with fields.
     */
    public enum DiseaseTable {
        Haemophilus_Influenza("cafy-kah2", "haemophilus_influenzae_invasive_all_ages_all_serotypes", "Haemophilus Influenza"),
        Tetanus("n3ub-5wxs", "tetanus", "Tetanus"),
        Chicken_Pox("n3ub-5wxs", "varicella_chickenpox", "Chicken Pox");

        private final String tableName;
        private final String diseaseName;
        private final String displayName;

        DiseaseTable(String tableName, String diseaseName, String displayName) {
            this.tableName = tableName;
            this.diseaseName = diseaseName;
            this.displayName = displayName;
        }

        public String getTableName() {
            return this.tableName;
        }

        public String getDiseaseName() {
            return this.diseaseName;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static DiseaseTable getOfName(String tableName) throws IllegalAccessException {
            for (DiseaseTable dt : DiseaseTable.values()) {
                if (dt.getDiseaseName().equals(tableName)) {
                    return dt;
                } else {
                    Log.e("Weird", "\"" + dt.getDiseaseName() + "\" does not equal \"" + tableName + "\"");
                }
            }
            throw new IllegalAccessException("No DiseaseTable with the name: \"" + tableName + "\"");
        }
    }

    private static String CDC_DATABASE_ADDRESS = "https://data.cdc.gov/resource/";
    private static String APP_TOKEN = "$$app_token=csJoX8FOoaC0U29v2HtsUk4nb";
    private static String JSON_SUFFIX = ".json?";

    /**
     * This will return a {@link DiseaseRecord} constructed for the specified {@link DiseaseTable}.
     *
     * @param diseaseTable
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public DiseaseRecord getDiseaseFromTable(DiseaseTable diseaseTable) throws IOException, ExecutionException, InterruptedException {
        List<DiseaseRecord> dList = this.execute(diseaseTable).get();
        assert dList.size() == 1;
        return dList.get(0);
    }

    /**
     * This creates a {@link URL} used to query the specified table.
     *
     * @param diseaseTable
     * @return
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
     * @param diseaseTables
     * @return
     */
    @Override
    protected List<DiseaseRecord> doInBackground(DiseaseTable... diseaseTables) {
        List<DiseaseRecord> dList = new ArrayList<>();
        try {
            for (DiseaseTable dt : diseaseTables) {

                Log.i("Info", "Reading from: " + dt.getTableName());
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
                        infected = jsonArray.getJSONObject(i).getInt(dt.diseaseName + _CURRENT_WEEK);
                    }
                    String reportingArea = jsonArray.getJSONObject(i).getString(REPORTING_AREA);

                    Log.i("WeekInfo", "Parsed week: " + week + " reporting location: " + reportingArea);

                    weekInfoList.add(new DiseaseRecord.WeekInfo(year, week, infected, reportingArea));
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

}

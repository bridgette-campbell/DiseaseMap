package edu.uw.tacoma.css.diseasemap.connection;

import android.util.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import edu.uw.tacoma.css.diseasemap.disease.Disease;

/**
 * This object creates a connection to the NNDSS database. It can be used to query the specified
 * database.
 * <p>
 * Here is a list of the tables, in a readable format.
 * {@link https://wonder.cdc.gov/nndss/nndss_weekly_tables_menu.asp?mmwr_year=&mmwr_week=}
 *
 * Actual API datasets can be found:
 * {@link https://data.cdc.gov/browse?category=NNDSS&sortBy=last_modified}
 *
 */
public final class NNDSSConnection {


    public enum DiseaseTable {
        Influenza("cafy-kah2");

        private final String tableName;

        DiseaseTable(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return this.tableName;
        }
    }

    private static String CDC_DATABASE_ADDRESS = "https://data.cdc.gov/resource/";
    private static String APP_TOKEN = "app_token=csJoX8FOoaC0U29v2HtsUk4nb";
    private static String JSON_SUFFIX = ".json";

    public Disease getDiseaseFromTable(DiseaseTable diseaseTable) throws IOException {

            HttpURLConnection conn = (HttpURLConnection) createConnectionURL(diseaseTable)
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());

            JsonReader reader = new JsonReader(isr);

            reader.beginArray();

            return null;
    }

    private URL createConnectionURL(DiseaseTable diseaseTable) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();

        sb.append(CDC_DATABASE_ADDRESS);
        sb.append(diseaseTable);
        sb.append(JSON_SUFFIX);
        sb.append(APP_TOKEN);

        return new URL(sb.toString());
    }

}

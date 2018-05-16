package edu.uw.tacoma.css.diseasemap;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.database_connection.NNDSSConnection;
import edu.uw.tacoma.css.diseasemap.disease.DiseaseActivity;

/**
 * The {@link AppCompatActivity} that handles all of the logic for displaying the disease
 * information with the location and other relevant information.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class MapActivity extends AppCompatActivity {

    /**
     * SharedPreferences keys for the user-selected Disease and Week
     */
    public static final String SELECTED_DISEASE = "selected_disease";
    public static final String SELECTED_WEEK = "selected_week";

    private String mSelectedDisease;
    private String mSelectedDiseaseDisplayName;
    private int mSelectedWeek;

    /**
     * UI elements
     */
    private TextView mMapView;
    private FloatingActionButton mDiseaseFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = findViewById(R.id.map_textview);

        mDiseaseFab = findViewById(R.id.disease_fab);
        mDiseaseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapActivity.this, DiseaseActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update the UI
        mSelectedDisease = getSharedPreferences(SELECTED_DISEASE, Context.MODE_PRIVATE)
                .getString(SELECTED_DISEASE, "none");

        mSelectedWeek = getSharedPreferences(SELECTED_WEEK, Context.MODE_PRIVATE)
                .getInt(SELECTED_WEEK, -1);

        if (!mSelectedDisease.equals("none") && mSelectedWeek >= 0) {
            try {
                NNDSSConnection.DiseaseTable table =
                        NNDSSConnection.DiseaseTable.getOfName(mSelectedDisease);

                mSelectedDiseaseDisplayName = table.getDisplayName();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            updateMap();
        }
    }

    /**
     * Updates the map based on the user-selected disease and week.
     * STILL IN PROGRESS - While testing, this simply updates a TextView.
     */
    private void updateMap() {
        // Data is stored in an additional map of locations. This adds up the infections of all
        // locations.
        int infected = 0;

        DiseaseRecord dr;
        try {

            Log.v("Map check", "selected disease: " + mSelectedDisease);
            dr = new NNDSSConnection().getDiseaseFromTable(
                    NNDSSConnection.DiseaseTable.getOfName(mSelectedDisease));

            Map<String, DiseaseRecord.WeekInfo> map = dr.getInfoForWeek(mSelectedWeek);
            if (map != null) {
                for (String s : map.keySet()) {
                    Log.i("Blah","String s: " + s);
                    infected += map.get(s).getInfected();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String output = "Selected disease: " + mSelectedDiseaseDisplayName + "\n";
        output += "Selected week number: " + mSelectedWeek + "\n";
        output += "Total number infected: ";
        output += (infected > 0) ? infected : "Data not yet published by CDC";

        mMapView.setText(output);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Share
            case R.id.share:

                // Make a Toast if a disease and week haven't been selected
                if ("".equals(mMapView.getText().toString())) {
                    Toast.makeText(this, "Select a disease and week before sharing",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    share();
                }
                return true;

            // Sign Out
            case R.id.sign_out:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Shares the displayed information using an implicit Intent.
     */
    private void share() {

        // Set up the necessary Strings
        String type = "text/plain";
        String subject = getString(R.string.app_name);
        String text = mMapView.getText().toString();
        String chooserText = getString(R.string.send_report_via);

        // Build the Intent
        Intent i = ShareCompat.IntentBuilder.from(this)
                .setType(type)
                .setSubject(subject)
                .setText(text)
                .getIntent();

        // Force the chooser to be shown each time
        i = Intent.createChooser(i, chooserText);

        // Start the Intent
        startActivity(i);
    }

    /**
     * Handles signing out of the Google Account and returning to MainActivity.
     */
    private void signOut() {
        // Set the user as not signed in
        getSharedPreferences(MainActivity.SIGNED_IN, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(MainActivity.SIGNED_IN, false)
                .apply();

        Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

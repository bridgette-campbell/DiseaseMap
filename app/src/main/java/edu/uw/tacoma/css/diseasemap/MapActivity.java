package edu.uw.tacoma.css.diseasemap;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;

import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.database_connection.NNDSSConnection;
import edu.uw.tacoma.css.diseasemap.disease.DiseaseActivity;
import edu.uw.tacoma.css.diseasemap.week.WeekActivity;

/**
 * The {@link AppCompatActivity} that handles all of the logic for displaying the disease
 * information with the location and other relevant information.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class MapActivity extends AppCompatActivity {

    /**
     * Request codes
     */
    private static final int RC_DISEASE = 0;
    private static final int RC_WEEK = 1;

    /**
     * UI elements
     */
    private TextView mMapView;
    private Button mButtonDisease;
    private Button mButtonWeek;

    /**
     * User-selected data. Used for displaying specified map data and backing up preferences.
     */
    private String mSelectedDisease;
    private String mSelectedDiseaseDisplayName;
    private Integer mSelectedWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = findViewById(R.id.map_textview);
        mButtonDisease = findViewById(R.id.disease_button);
        mButtonWeek = findViewById(R.id.week_button);
    }

    /**
     * onClick handler for the "Select Disease" button. Starts DiseaseActivity for a result.
     *
     * @param v The containing View
     */
    public void launchDiseases(View v) {
        Intent diseases = new Intent(this, DiseaseActivity.class);
        startActivityForResult(diseases, RC_DISEASE);
    }

    /**
     * onClick handler for the "Select Week" button. Starts WeekActivity for a result.
     *
     * @param v The containing View
     */
    public void launchWeeks(View v) {
        Intent weeks = new Intent(this, WeekActivity.class);
        startActivityForResult(weeks, RC_WEEK);
    }

    /**
     * Handles information coming from other Activities/Fragments using their encapsulated methods,
     * updates the previously-selected button's text, and calls updateMap().
     *
     * @param requestCode   The code that specifies where the data is coming from
     * @param resultCode    Shows whether the result was OK, CANCELLED, etc
     * @param data          The returned Intent that carries data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {

            // DiseaseRecordFragment
            if (requestCode == RC_DISEASE) {
                try {
                    NNDSSConnection.DiseaseTable table =
                            NNDSSConnection.DiseaseTable.getOfName(
                                    DiseaseActivity.getSelectedDisease(data));

                    mSelectedDisease = table.getDiseaseName();
                    mSelectedDiseaseDisplayName = table.getDisplayName();
                    mButtonDisease.setText(getString(R.string.disease_selected,
                            table.getDisplayName()));
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

            // WeekActivity result
            else if (requestCode == RC_WEEK) {
                mSelectedWeek = WeekActivity.getSelectedWeek(data);

                String selectedWeek = String.valueOf(mSelectedWeek);
                mButtonWeek.setText(getString(R.string.week_selected, selectedWeek));
            }

            updateMap();
        }
    }

    /**
     * Updates the map based on the user-selected disease and week.
     * STILL IN PROGRESS - While testing, this simply updates a TextView.
     */
    private void updateMap() {
        if (mSelectedDisease != null && mSelectedWeek != null) {
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
                share();
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
     * Handles signing out of the Google Account and returning to LoginActivity.
     */
    private void signOut() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

package edu.uw.tacoma.css.diseasemap;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import edu.uw.tacoma.css.diseasemap.connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.connection.NNDSSConnection;
import edu.uw.tacoma.css.diseasemap.disease.SelectDiseaseActivity;
import edu.uw.tacoma.css.diseasemap.disease.DiseaseRecordListFragment;
import edu.uw.tacoma.css.diseasemap.disease.ViewDiseaseActivity;
import edu.uw.tacoma.css.diseasemap.week.WeekActivity;

import static edu.uw.tacoma.css.diseasemap.disease.ViewDiseaseActivity.SELECTED_DISEASE;

public class MapActivity extends AppCompatActivity implements DiseaseRecordListFragment.OnListFragmentInteractionListener {

    /**
     * Request codes
     */
    private static final int RC_DISEASE = 0;
    private static final int RC_WEEK = 1;

    /**
     * UI elements
     */
    private Button mButtonDisease;
    private Button mButtonWeek;
    private int mColumnCount = 1;

    /**
     * User-selected data. Used for displaying specified map data and backing up preferences.
     */
    private String mSelectedDisease;
    private String mSelectedWeek;

    private DiseaseRecordListFragment recordListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (findViewById(R.id.diseaserecord_container) != null) {
            recordListFragment = new DiseaseRecordListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.diseaserecord_container, recordListFragment)
                    .commit();
        }

        mButtonDisease = findViewById(R.id.disease_button);
        mButtonWeek = findViewById(R.id.week_button);
    }

    /**
     * onClick handler for the "Select Disease" button. Starts DiseaseActivity for a result.
     *
     * @param v The containing View
     */
    public void launchDiseases(View v) {
        Intent diseases = new Intent(this, SelectDiseaseActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {


            // DiseaseRecordFragment
            if (requestCode == RC_DISEASE) {
                String mSelectedDisease = SelectDiseaseActivity.getSelectedDisease(data);

                Intent diseases = new Intent(this, ViewDiseaseActivity.class);
                diseases.putExtra(SELECTED_DISEASE, mSelectedDisease);
                startActivityForResult(diseases, RC_DISEASE);

            }
            // WeekActivity result
            else if (requestCode == RC_WEEK) {
                String mSelectedWeek = WeekActivity.getSelectedWeek(data);
                mButtonWeek.setText(getString(R.string.week_selected, mSelectedWeek));
            }

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
            case R.id.sign_out:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Handles signing out of the Google Account and returning to LoginActivity.
     */
    private void signOut() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        GoogleSignIn.getClient(this, gso).signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),
                                R.string.signed_out, Toast.LENGTH_SHORT).show();
                    }
                });

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onListFragmentInteraction(DiseaseRecord diseaseRecord) {
    }
}

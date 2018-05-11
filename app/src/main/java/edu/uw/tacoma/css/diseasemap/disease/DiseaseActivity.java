package edu.uw.tacoma.css.diseasemap.disease;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.NNDSSConnection;

public class DiseaseActivity extends AppCompatActivity
        implements DiseaseListFragment.OnListFragmentInteractionListener {

    /**
     * Identifier for the returned String extra
     */
    private static final String SELECTED_DISEASE = "edu.uw.tacoma.css.diseasemap.selected_disease";

    /**
     * Encapsulates the implementation details of DiseaseActivity's returned Intent
     *
     * @param data The Intent returned by DiseaseActivity
     * @return The String extra from the Intent
     */
    public static String getSelectedDisease(Intent data) {
        return (data.getStringExtra(SELECTED_DISEASE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        // disease_fragment_container is activity_disease's empty FrameLayout
        if (findViewById(R.id.disease_fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.disease_fragment_container, new DiseaseListFragment())
                    .commit();
        }
    }

    // Called when an item in the list is selected
    @Override
    public void onListFragmentInteraction(NNDSSConnection.DiseaseTable item) {

        // Create a new Intent with the selected DiseaseRecordFragment's name
        Intent data = new Intent();
        data.putExtra(SELECTED_DISEASE, item.getDiseaseName());

        // Send the Intent back to MapActivity
        setResult(RESULT_OK, data);
        finish();
    }
}

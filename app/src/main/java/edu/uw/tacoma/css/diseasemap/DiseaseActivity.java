package edu.uw.tacoma.css.diseasemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uw.tacoma.css.diseasemap.disease.DiseaseContent;

public class DiseaseActivity extends AppCompatActivity
        implements DiseaseListFragment.OnListFragmentInteractionListener {

    private static final String CHOSEN_DISEASE = "edu.uw.tacoma.css.diseasemap.chosen_disease";

    // Encapsulates the implementation details of DiseaseActivity's returned Intent
    public static String getChosenDisease(Intent data) {
        return (data.getStringExtra(CHOSEN_DISEASE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        // disease_fragment_container is activity_list's empty FrameLayout
        if (findViewById(R.id.disease_fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.disease_fragment_container, new DiseaseListFragment())
                    .commit();
        }
    }

    // Called when an item in the list is selected
    @Override
    public void onListFragmentInteraction(DiseaseContent.DiseaseItem item) {

        // Create a new Intent with the selected Disease's id
        Intent data = new Intent();
        data.putExtra(CHOSEN_DISEASE, item.id);

        // Send the Intent back to MapActivity
        setResult(RESULT_OK, data);
        finish();
    }
}

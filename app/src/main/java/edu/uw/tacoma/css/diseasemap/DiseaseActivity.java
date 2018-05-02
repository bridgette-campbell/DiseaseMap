package edu.uw.tacoma.css.diseasemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uw.tacoma.css.diseasemap.disease.DiseaseContent.DiseaseItem;

public class DiseaseActivity extends AppCompatActivity
        implements DiseaseListFragment.OnListFragmentInteractionListener {

    private static final String SELECTED_DISEASE = "edu.uw.tacoma.css.diseasemap.selected_disease";

    // Encapsulates the implementation details of DiseaseActivity's returned Intent
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
    public void onListFragmentInteraction(DiseaseItem item) {

        // Create a new Intent with the selected Disease's name
        Intent data = new Intent();
        data.putExtra(SELECTED_DISEASE, item.name);

        // Send the Intent back to MapActivity
        setResult(RESULT_OK, data);
        finish();
    }
}

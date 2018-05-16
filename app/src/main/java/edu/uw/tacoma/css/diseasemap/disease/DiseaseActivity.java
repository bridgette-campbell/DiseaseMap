package edu.uw.tacoma.css.diseasemap.disease;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uw.tacoma.css.diseasemap.MapActivity;
import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.NNDSSConnection;
import edu.uw.tacoma.css.diseasemap.week.WeekActivity;

/**
 * The {@link AppCompatActivity} to launch a {@link DiseaseListFragment}
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class DiseaseActivity extends AppCompatActivity
        implements DiseaseListFragment.OnListFragmentInteractionListener {

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
        String key = MapActivity.SELECTED_DISEASE;

        getSharedPreferences(key, Context.MODE_PRIVATE)
                .edit()
                .putString(key, item.getDiseaseName())
                .apply();

        startActivity(new Intent(this, WeekActivity.class));
        finish();
    }
}

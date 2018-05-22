package edu.uw.tacoma.css.diseasemap.week;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uw.tacoma.css.diseasemap.map.MapActivity;
import edu.uw.tacoma.css.diseasemap.R;

/**
 * The {@link AppCompatActivity} to launch a {@link WeekListFragment}.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class WeekActivity extends AppCompatActivity
        implements WeekListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        // Hide the floating action button
        FloatingActionButton diseaseFab = findViewById(R.id.disease_fab);
        diseaseFab.hide();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new WeekListFragment())
                .commit();
    }

    // Called when an item in the list is selected
    @Override
    public void onListFragmentInteraction(int weekNum) {
        String key = MapActivity.SELECTED_WEEK;

        getSharedPreferences(key, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, weekNum)
                .apply();

        finish();
    }
}

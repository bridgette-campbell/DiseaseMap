package edu.uw.tacoma.css.diseasemap.week;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uw.tacoma.css.diseasemap.R;

/**
 * The {@link AppCompatActivity} to launch a {@link WeekListFragment}.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class WeekActivity extends AppCompatActivity
        implements WeekListFragment.OnListFragmentInteractionListener {

    /**
     * Identifier for the returned String extra
     */
    private static final String SELECTED_WEEK = "edu.uw.tacoma.css.diseasemap.selected_week";

    /**
     * Encapsulates the implementation details of WeekActivity's returned Intent
     *
     * @param data The Intent returned by WeekActivity
     * @return The String extra from the Intent
     */
    public static int getSelectedWeek(Intent data) {
        return (data.getIntExtra(SELECTED_WEEK, -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        // week_fragment_container is activity_week's empty FrameLayout
        if (findViewById(R.id.week_fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.week_fragment_container, new WeekListFragment())
                    .commit();
        }
    }

    // Called when an item in the list is selected
    @Override
    public void onListFragmentInteraction(int weekNum) {

        // Create a new Intent with the selected Week's week
        Intent data = new Intent();
        data.putExtra(SELECTED_WEEK, weekNum);

        // Send the Intent back to MapActivity
        setResult(RESULT_OK, data);
        finish();
    }
}

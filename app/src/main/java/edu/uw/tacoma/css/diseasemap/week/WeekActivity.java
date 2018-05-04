package edu.uw.tacoma.css.diseasemap.week;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.week.WeekContent.WeekItem;

public class WeekActivity extends AppCompatActivity
        implements WeekListFragment.OnListFragmentInteractionListener {

    private static final String SELECTED_WEEK = "edu.uw.tacoma.css.diseasemap.selected_week";

    // Encapsulates the implementation details of WeekActivity's returned Intent
    public static String getSelectedWeek(Intent data) {
        return (data.getStringExtra(SELECTED_WEEK));
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
    public void onListFragmentInteraction(WeekItem item) {

        // Create a new Intent with the selected Week's week
        Intent data = new Intent();
        data.putExtra(SELECTED_WEEK, item.week);

        // Send the Intent back to MapActivity
        setResult(RESULT_OK, data);
        finish();
    }
}

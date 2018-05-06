package edu.uw.tacoma.css.diseasemap.disease;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.connection.NNDSSConnection;

public class TimeActivity extends AppCompatActivity
        implements TimeListFragment.OnListFragmentInteractionListener {

    public static final String SELECTED_DISEASE = "edu.uw.tacoma.css.diseasemap.selected_disease";

    // Encapsulates the implementation details of DiseaseActivity's returned Intent
    public static String getSelectedDisease(Intent data) {
        return (data.getStringExtra(SELECTED_DISEASE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        assert savedInstanceState.containsKey(SELECTED_DISEASE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);


        NNDSSConnection nndssConnection = new NNDSSConnection();
        try {
            DiseaseRecord dr = nndssConnection.getDiseaseFromTable(NNDSSConnection.DiseaseTable.getOfName(getIntent().getExtras().getString(SELECTED_DISEASE)));

            // disease_fragment_container is activity_disease's empty FrameLayout
            if (findViewById(R.id.disease_fragment_container) != null) {

                TimeListFragment drlf = new TimeListFragment();
                drlf.setDiseaseRecord(dr);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.disease_fragment_container, drlf)
                        .commit();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    // Called when an item in the list is selected
    @Override
    public void onListFragmentInteraction(DiseaseRecord item) {
        finish();
    }
}

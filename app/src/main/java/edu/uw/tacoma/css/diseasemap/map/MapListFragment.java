package edu.uw.tacoma.css.diseasemap.map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.week.WeekListFragment;

/**
 * Represents a list of MapItems
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class MapListFragment extends Fragment {

    /**
     * Mandatory empty constructor
     */
    public MapListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            DiseaseRecord record = (DiseaseRecord) getArguments().getSerializable("disease");
            int week = getArguments().getInt("week");



            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MapRecyclerViewAdapter(record, week));
        }

        return view;
    }
}

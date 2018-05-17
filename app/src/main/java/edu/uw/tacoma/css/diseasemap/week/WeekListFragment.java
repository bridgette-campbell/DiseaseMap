package edu.uw.tacoma.css.diseasemap.week;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tacoma.css.diseasemap.MapActivity;
import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.database_connection.NNDSSConnection;

/**
 * Represents a list of {@link DiseaseRecord.WeekInfo}s
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class WeekListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor
     */
    public WeekListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            DiseaseRecord dr;
            try {
                String key = MapActivity.SELECTED_DISEASE;
                String diseaseName = getActivity().getSharedPreferences(key, Context.MODE_PRIVATE)
                        .getString(key, "none");

                dr = new NNDSSConnection().getDiseaseFromTable(
                        NNDSSConnection.DiseaseTable.getOfName(diseaseName));

                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(
                        new WeekRecyclerViewAdapter(dr, mListener));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(int weekNum);
    }
}

package edu.uw.tacoma.css.diseasemap.disease;

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
import edu.uw.tacoma.css.diseasemap.database_connection.NNDSSConnection;

/**
 * Represents a list of {@link DiseaseRecord}s
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class DiseaseListFragment extends Fragment {

    private DiseaseListListener mListener;

    /**
     * Mandatory empty constructor
     */
    public DiseaseListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new DiseaseRecyclerViewAdapter(mListener));
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DiseaseListListener) {
            mListener = (DiseaseListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DiseaseListListener");
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
    public interface DiseaseListListener {
        void selectDisease(NNDSSConnection.DiseaseTable item);
    }
}

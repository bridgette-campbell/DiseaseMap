package edu.uw.tacoma.css.diseasemap.disease;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.NNDSSConnection;
import edu.uw.tacoma.css.diseasemap.disease.DiseaseListFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NNDSSConnection.DiseaseTable} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class DiseaseRecyclerViewAdapter
        extends RecyclerView.Adapter<DiseaseRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    public DiseaseRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_disease_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = NNDSSConnection.DiseaseTable.values()[position];
        holder.mNameView.setText(NNDSSConnection.DiseaseTable.values()[position].getDisplayName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the fragment is
                    // attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return NNDSSConnection.DiseaseTable.values().length;
    }

    /**
     * The {@link android.support.v7.widget.RecyclerView.ViewHolder} used in this Adapter.
     *
     * @author Bridgette Campbell, Daniel McBride, Matt Qunell
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public NNDSSConnection.DiseaseTable mItem;

        /**
         * Constructor
         *
         * @param view the {@link View}
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.disease_name);
        }
    }
}

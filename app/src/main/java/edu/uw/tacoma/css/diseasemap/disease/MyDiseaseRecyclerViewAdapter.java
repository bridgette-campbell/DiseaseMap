package edu.uw.tacoma.css.diseasemap.disease;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.disease.DiseaseListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.css.diseasemap.disease.DiseaseContent.DiseaseItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DiseaseItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyDiseaseRecyclerViewAdapter
        extends RecyclerView.Adapter<MyDiseaseRecyclerViewAdapter.ViewHolder> {

    private final List<DiseaseItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyDiseaseRecyclerViewAdapter(List<DiseaseItem> items,
                                        OnListFragmentInteractionListener listener) {
        mValues = items;
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
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;

        public DiseaseItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.item_name);
        }
    }
}

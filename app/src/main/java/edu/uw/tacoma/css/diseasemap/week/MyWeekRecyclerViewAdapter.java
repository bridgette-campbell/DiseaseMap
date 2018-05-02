package edu.uw.tacoma.css.diseasemap.week;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.week.WeekListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.css.diseasemap.week.WeekContent.WeekItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link WeekItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyWeekRecyclerViewAdapter extends RecyclerView.Adapter<MyWeekRecyclerViewAdapter.ViewHolder> {

    private final List<WeekItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyWeekRecyclerViewAdapter(List<WeekItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_week_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mWeekView.setText(mValues.get(position).week);

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
        public final TextView mWeekView;

        public WeekItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWeekView = view.findViewById(R.id.item_week);
        }
    }
}

package edu.uw.tacoma.css.diseasemap.week;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.week.WeekListFragment.WeekListListener;

/**
 * {@link RecyclerView.Adapter} that can display a week number and makes a call to the
 * specified {@link WeekListListener}.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class WeekRecyclerViewAdapter
        extends RecyclerView.Adapter<WeekRecyclerViewAdapter.ViewHolder> {

    private DiseaseRecord mDiseaseRecord;
    private final WeekListListener mListener;

    /**
     * Constructor
     *
     * @param diseaseRecord the {@link DiseaseRecord}
     * @param listener the {@link WeekListListener}
     */
    public WeekRecyclerViewAdapter(DiseaseRecord diseaseRecord,
                                   WeekListListener listener) {
        mDiseaseRecord = diseaseRecord;
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
        List<Integer> keyList = new ArrayList<>(mDiseaseRecord.getWeeks());
        Collections.sort(keyList);

        holder.mItem = mDiseaseRecord.getInfoForWeek(position);
        holder.mWeek.setText("Week " + keyList.get(position));

        int infected = 0;
        Map<String, DiseaseRecord.WeekInfo> map = mDiseaseRecord.getInfoForWeek(keyList.get(position));
        if (map != null) {
            for (String s : map.keySet()) {
                infected += map.get(s).getInfected();
            }
        }

        holder.mInfected.setText(infected > 0
                ? "Infected: " + String.valueOf(infected)
                : "CDC data not yet published");

        final int pos = keyList.get(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the fragment is
                    // attached to one) that an item has been selected.
                    mListener.selectWeek(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDiseaseRecord == null){
            return 0;
        }

        return mDiseaseRecord.getWeeks().size();
    }

    /**
     * The {@link android.support.v7.widget.RecyclerView.ViewHolder} used in this Adapter.
     *
     * @author Bridgette Campbell, Daniel McBride, Matt Qunell
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mWeek;
        public final TextView mInfected;
        public Map<String, DiseaseRecord.WeekInfo> mItem;

        /**
         * Constructor
         *
         * @param view the {@link View}
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWeek = view.findViewById(R.id.week);
            mInfected = view.findViewById(R.id.infected);
        }
    }
}

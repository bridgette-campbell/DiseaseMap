package edu.uw.tacoma.css.diseasemap.disease;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.connection.DiseaseRecord;

public class TimeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRecyclerViewAdapter.ViewHolder> {

    private final TimeListFragment.OnListFragmentInteractionListener mListener;

    private DiseaseRecord diseaseRecord;

    public TimeRecyclerViewAdapter(DiseaseRecord diseaseRecord, TimeListFragment.OnListFragmentInteractionListener listener) {
        mListener = listener;
        this.diseaseRecord = diseaseRecord;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_week_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = this.diseaseRecord.getInfoForWeek(position);

        //Data is stored in an additional map of locations. This adds up the infections of all locations.
        Integer infected = 0;

        Map<String, DiseaseRecord.WeekInfo> map = this.diseaseRecord.getInfoForWeek(position + 1);
        if(map != null){
            for(String s : map.keySet()){
                infected += map.get(s).getInfected();
            }
        }


        holder.mWeek.setText("Week: " + (position + 1));

        if(infected > 0) {
            holder.mInfected.setText("Infected: " + infected.toString());
        } else {
            holder.mInfected.setText("Data not yet published by CDC.");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(diseaseRecord);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.diseaseRecord == null){
            return 0;
        }
        return diseaseRecord.getWeeks();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mWeek;
        public final TextView mInfected;

        public Map<String, DiseaseRecord.WeekInfo> mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mInfected = view.findViewById(R.id.infected);
            mWeek = view.findViewById(R.id.week);
        }
    }
}

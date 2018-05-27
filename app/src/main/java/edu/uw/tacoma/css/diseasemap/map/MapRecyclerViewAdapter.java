package edu.uw.tacoma.css.diseasemap.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.week.WeekListFragment;

/**
 * {@link RecyclerView.Adapter} that can display MapItems
 */
public class MapRecyclerViewAdapter extends RecyclerView.Adapter<MapRecyclerViewAdapter.ViewHolder> {

    private DiseaseRecord mDiseaseRecord;
    private int mWeek;

    public MapRecyclerViewAdapter(DiseaseRecord diseaseRecord, int week) {
        mDiseaseRecord = diseaseRecord;
        mWeek = week;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_map_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Map<String, DiseaseRecord.WeekInfo> map = mDiseaseRecord.getInfoForWeek(mWeek);
        List<String> keyList = new ArrayList<String>(map.keySet());

        holder.locationTextView.setText(keyList.get(position));


    }

    @Override
    public int getItemCount() {
        if(mDiseaseRecord != null) {
            return mDiseaseRecord.getWeeks();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView locationTextView;
        public final TextView seasonTextView;
        public final TextView weekTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.imageView);
            locationTextView = view.findViewById(R.id.locationTextView);
            seasonTextView = view.findViewById(R.id.seasonTextView);
            weekTextView = view.findViewById(R.id.weekTextView);
        }
    }
}

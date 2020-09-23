package com.speant.user.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.speant.user.ui.TrackingActivity;
import com.speant.user.Models.TrackingDetailPojo;
import com.speant.user.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackingStatusAdapter extends RecyclerView.Adapter<TrackingStatusAdapter.ViewHolder> {

    List<TrackingDetailPojo.TrackingDetail> trackList = new ArrayList<>();
    private TrackingActivity mContext;
    private Date date;

    public TrackingStatusAdapter(TrackingActivity activityTracking, List<TrackingDetailPojo.TrackingDetail> trackList) {
        this.trackList = trackList;
        this.mContext = activityTracking;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_track_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.trackDetailDescTxt.setText(trackList.get(position).getDetail());

        Log.e("Nive ", "getStatus: " + trackList.get(position).getStatus());
        Log.e("Nive ", "getStatus: " + trackList.get(position).getCreatedAt());

        DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            String inputText = trackList.get(position).getCreatedAt();
            date = inputFormat.parse(inputText);
            Log.e("Nive ", "date::: " + date);
            holder.trackDetailTimeTxt.setText(outputFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (position != trackList.size() - 1) {

            holder.trackDetailTimeTxt.setTextColor(mContext.getResources().getColor(R.color.grey));
            holder.trackDetailDescTxt.setTextColor(mContext.getResources().getColor(R.color.grey));

        } else {


            holder.trackDetailTimeTxt.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.trackDetailDescTxt.setTextColor(mContext.getResources().getColor(R.color.colorAccent));

        }

    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.track_detail_time_txt)
        TextView trackDetailTimeTxt;
        @BindView(R.id.track_detail_desc_txt)
        TextView trackDetailDescTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

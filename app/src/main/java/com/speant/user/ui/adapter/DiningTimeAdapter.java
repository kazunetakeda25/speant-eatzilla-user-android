package com.speant.user.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.callBacks.DiningCallBack;
import com.speant.user.Models.TimeData;
import com.speant.user.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiningTimeAdapter extends RecyclerView.Adapter<DiningTimeAdapter.ViewHolder> {
    Activity activity;
    List<TimeData> timeList;
    List<TimeData> selectedTimeList = new ArrayList<>();
    DiningCallBack diningCallBack;
    private boolean onBind;
    private int selectedPosition = -1;
    String timevalue;
    String type;


    public DiningTimeAdapter(Activity activity, List<TimeData> timeList, DiningCallBack diningCallBack, String type) {
        this.activity = activity;
        this.timeList = timeList;
        this.diningCallBack = diningCallBack;
        this.type =type;

        selectedTimeList.clear();
        for (TimeData timeData : timeList) {
            if(timeData.getType().equals(type)){
                selectedTimeList.add(timeData);
            }
        }
        Log.e("TAG", "DiningTimeAdapter:selectedTimeList size"+selectedTimeList.size() );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBind = true;
        timevalue = selectedTimeList.get(position).getTime();
        if (position == 0) {
            timevalue = timevalue + "\n" + "Open Time";
        } else if (position == selectedTimeList.size() - 1) {
            timevalue = timevalue + "\n" + "Close Time";
        }
        holder.chkMember.setText(timevalue);

        if (selectedPosition == position) {
            holder.chkMember.setChecked(true);
        } else {
            holder.chkMember.setChecked(false);
        }

        onBind = false;
    }

    @Override
    public int getItemCount() {
        return selectedTimeList.size();
    }

    public void resetAdapter() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.chk_member)
        AppCompatCheckBox chkMember;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chkMember.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!onBind) {
                selectedPosition = getAdapterPosition();
                diningCallBack.OnTimeSelect(selectedTimeList.get(getAdapterPosition()));
                notifyDataSetChanged();
            }
        }
    }
}

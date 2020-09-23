package com.speant.user.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;


import com.speant.user.Common.Global;
import com.speant.user.Common.callBacks.DiningCallBack;
import com.speant.user.Models.DateData;
import com.speant.user.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiningDayAdapter extends RecyclerView.Adapter<DiningDayAdapter.ViewHolder> {
    Activity activity;
    List<DateData> dateList;
    DiningCallBack diningCallBack;
    private boolean onBind;
    private int selectedPosition = -1;

    public DiningDayAdapter(Activity activity, List<DateData> dateList, DiningCallBack diningCallBack) {
        this.activity = activity;
        this.dateList = dateList;
        this.diningCallBack = diningCallBack;
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
        String date = Global.getDateFromString(dateList.get(position).getDate(),"yyyy-MM-dd","MMM d"+"\n"+"EEE");
        holder.chkMember.setText(date);

        if (selectedPosition == position) {
            holder.chkMember.setChecked(true);
        } else {
            holder.chkMember.setChecked(false);
        }

        onBind = false;
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public void resetAdapter() {
        selectedPosition = 0;
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
                diningCallBack.OnDateSelect(dateList.get(getAdapterPosition()));
                notifyDataSetChanged();
            }
        }
    }
}

package com.speant.user.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.callBacks.DiningCallBack;
import com.speant.user.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiningMemberAdapter extends RecyclerView.Adapter<DiningMemberAdapter.ViewHolder> {

    private boolean onBind;
    private int selectedPosition = -1;
    Activity activity;
    List<String> memberList;
    DiningCallBack diningCallBack;

    public DiningMemberAdapter(Activity activity, List<String> memberList, DiningCallBack diningCallBack) {
        this.activity =activity;
        this.memberList =memberList;
        this.diningCallBack = diningCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBind = true;
        holder.chkMember.setText(memberList.get(position));

        if (selectedPosition == position) {
            holder.chkMember.setChecked(true);
        } else {
            holder.chkMember.setChecked(false);
        }

        onBind = false;
    }

    @Override
    public int getItemCount() {
        return memberList.size();
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
                diningCallBack.OnMemberSelect(memberList.get(getAdapterPosition()));
                notifyDataSetChanged();
            }
        }
    }
}

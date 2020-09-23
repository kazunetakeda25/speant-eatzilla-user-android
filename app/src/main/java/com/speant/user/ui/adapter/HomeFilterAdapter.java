package com.speant.user.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.speant.user.Common.CONST;
import com.speant.user.Models.FilterPojo;
import com.speant.user.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFilterAdapter extends RecyclerView.Adapter<HomeFilterAdapter.ViewHolder> {
    private static final String TAG = "HomeFilterAdapter";
    private Context mContext;
    ViewHolder holder;

    List<FilterPojo.Data> filterList = new ArrayList<>();

    public HomeFilterAdapter(Context context, List<FilterPojo.Data> filterList) {
        this.filterList = filterList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filter, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        this.holder = holder;

        holder.listFilterChk.setText(filterList.get(position).getName());
        holder.listFilterChk.setChecked(filterList.get(position).getIsSelect());

        holder.listFilterChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList.get(position).setIsSelect(!filterList.get(position).getIsSelect());

                Log.e(TAG, "onClick: "+ CONST.filterPojo.get(position).getIsSelect());
                Log.e(TAG, "onClick: "+ filterList.get(position).getIsSelect());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
    }

    public void confirmFilter(){
        CONST.filterPojo = filterList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_filter_chk)
        CheckBox listFilterChk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

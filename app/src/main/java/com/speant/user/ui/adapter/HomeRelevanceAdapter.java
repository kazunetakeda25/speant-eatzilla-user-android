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

public class HomeRelevanceAdapter extends RecyclerView.Adapter<HomeRelevanceAdapter.ViewHolder> {
    private static final String TAG = "HomeFilterAdapter";
    private Context mContext;

    List<FilterPojo.Data> relevanceList = new ArrayList<>();

    public HomeRelevanceAdapter(Context context, List<FilterPojo.Data> relevanceList) {
        this.relevanceList = relevanceList;
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

        holder.listFilterChk.setText(relevanceList.get(position).getName());
        holder.listFilterChk.setChecked(relevanceList.get(position).getIsSelect());

        holder.listFilterChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relevanceList.get(position).setIsSelect(!relevanceList.get(position).getIsSelect());
                Log.e(TAG, "onClick: "+ CONST.relevancePojo.get(position).getIsSelect());
                Log.e(TAG, "onClick: "+ relevanceList.get(position).getIsSelect());
            }
        });
    }

    @Override
    public int getItemCount() {
        return relevanceList.size();
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_filter_chk)
        CheckBox listFilterChk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void confirmFilter(){

        CONST.relevancePojo.clear();
        CONST.relevancePojo.addAll(relevanceList);

    }



}

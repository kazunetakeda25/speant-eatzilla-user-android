package com.speant.user.ui.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.speant.user.Common.CONST;
import com.speant.user.Models.PopularBrandsPojo;
import com.speant.user.R;
import com.speant.user.ui.HotelDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopularBrandsAdapter extends RecyclerView.Adapter<PopularBrandsAdapter.ViewHolder> {

    private Context mContext;
    List<PopularBrandsPojo.Data> popularBrandsList = new ArrayList<>();
    String popularBaseStr;
    public PopularBrandsAdapter(Context context, List<PopularBrandsPojo.Data> popularBrandsList,String popularBaseStr) {
        this.mContext = context;
        this.popularBrandsList = popularBrandsList;
        this.popularBaseStr = popularBaseStr;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_popular_brands, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        /*Glide.with(mContext)
                .load(popularBaseStr + popularBrandsList.get(position).getImage())
                .into(holder.popularBrandsImg);*/
        Picasso.get().load(popularBaseStr + popularBrandsList.get(position).getImage()).placeholder(R.drawable.ic_placeholder).into(holder.popularBrandsImg);
        holder.txtBrandName.setText(popularBrandsList.get(position).getName());
        holder.popularBrandsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CONST.restaren_id = String.valueOf(popularBrandsList.get(position).getId());
                Intent intent = new Intent(mContext, HotelDetailActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return popularBrandsList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.popular_brands_img)
        ImageView popularBrandsImg;

        @BindView(R.id.txt_brand_name)
        AppCompatTextView txtBrandName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
    }

}

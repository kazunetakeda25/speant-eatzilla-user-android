package com.speant.user.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speant.user.Common.CONST;
import com.speant.user.Models.BannerPojo;
import com.speant.user.R;
import com.speant.user.ui.HotelDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class HomeSlideAdapter extends RecyclerView.Adapter<HomeSlideAdapter.ViewHolder> {

    private List<BannerPojo.Data> data;
    private String imageBaseStr;
    private Activity activity;

    public HomeSlideAdapter(Activity activity, List<BannerPojo.Data> data, String imageBaseStr) {
        this.data = data;
        this.activity=activity;
        this.imageBaseStr = imageBaseStr;
        Log.e("Giri ", "HomeSlideAdapter:imageBaseStr "+imageBaseStr );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_shop_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Uri url = Uri.parse(imageBaseStr + data.get(position).getImage());
        Log.e("Giri ", "onBindViewHolder:HomeSlideAdapter "+imageBaseStr );
        Picasso.get().load(url).placeholder(R.drawable.ic_placeholder).into(holder.image);
        /*holder.homeSlideHeadTxt.setText(data.get(position).getTitle());
        holder.homeSlideDescTxt.setText(data.get(position).getDescription());*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONST.restaren_id = String.valueOf(data.get(position).getRestaurantId());
                Intent intent = new Intent(activity, HotelDetailActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        AppCompatImageView image;
        @BindView(R.id.home_slide_head_txt)
        AppCompatTextView homeSlideHeadTxt;
        @BindView(R.id.home_slide_desc_txt)
        AppCompatTextView homeSlideDescTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //use to set rounded imageview
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setClipToOutline(true);
            }

        }
    }


}

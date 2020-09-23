package com.speant.user.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.CONST;
import com.speant.user.Models.TrendFoodList;
import com.speant.user.R;
import com.speant.user.ui.HotelDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodaySpecialAdapter extends RecyclerView.Adapter<TodaySpecialAdapter.Viewholder> {
    Activity activity;
    List<TrendFoodList> trendFoodLists;


    public TodaySpecialAdapter(Activity activity, List<TrendFoodList> trendFoodLists) {
        this.activity = activity;
        this.trendFoodLists = trendFoodLists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_today_special_restarent, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if(trendFoodLists.get(position).getImage() != null && !trendFoodLists.get(position).getImage().isEmpty()) {
            Picasso.get().load(trendFoodLists.get(position).getImage()).into(holder.imgDish);
        }
        holder.txtDishName.setText(trendFoodLists.get(position).getName()+"\n\n"+trendFoodLists.get(position).getMenu().getMenu_name());
    }

    @Override
    public int getItemCount() {
        return trendFoodLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_dish)
        AppCompatImageView imgDish;
        @BindView(R.id.txt_dish_name)
        TextView txtDishName;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CONST.restaren_id = String.valueOf(trendFoodLists.get(getAdapterPosition()).getRestaurant_id());
                    Intent intent = new Intent(activity, HotelDetailActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}

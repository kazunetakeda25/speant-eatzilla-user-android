package com.speant.user.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.CONST;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Models.ItemListHistory;
import com.speant.user.Models.UpcomingOrders;
import com.speant.user.R;
import com.speant.user.ui.DiningTrackActivity;
import com.speant.user.ui.TrackingActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.CONST.DOOR_DELIVERY;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;

public class UpcomingOrderAdapter extends RecyclerView.Adapter<UpcomingOrderAdapter.Viewholder> {
    Activity activity;
    List<UpcomingOrders> upcomingOrders;
    List<ItemListHistory> itemLists;
    String ordersMenu;
    StringBuilder stringBuilder;


    private SessionManager sessionManager;

    public UpcomingOrderAdapter(Activity activity, List<UpcomingOrders> upcomingOrders) {
        this.activity = activity;
        this.upcomingOrders = upcomingOrders;
        sessionManager = new SessionManager(activity);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_history_list, parent, false);
        stringBuilder = new StringBuilder();
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        itemLists = upcomingOrders.get(position).getItem_list();
        Log.e("TAG", "onBindViewHolder:itemLists.size " + itemLists.size());
        Log.e("TAG", "onBindViewHolder: " + upcomingOrders.get(position).getRestaurant_name());
        Glide.with(activity)
                .load(upcomingOrders.get(position).getRestaurant_image())
                .into(holder.imgFood);
        holder.hotelName.setText(upcomingOrders.get(position).getRestaurant_name());
        holder.txtAmount.setText(upcomingOrders.get(position).getBill_amount() + "\t" + sessionManager.getCurrency());

        String time = " ";
        try {
            if (upcomingOrders.get(position).getDelivery_type().equals(DOOR_DELIVERY)) {
                time = Global.getDateFromString(upcomingOrders.get(position).getOrdered_on(), "yyyy-MM-dd HH:mm:ss", "EEEE MMM, d hh:mm a");

            } else if (upcomingOrders.get(position).getDelivery_type().equals(DINING) ||
                    upcomingOrders.get(position).getDelivery_type().equals(PICKUP_RESTAURANT)) {

                time = Global.getDateFromString(upcomingOrders.get(position).getPickup_dining_time(), "yyyy-MM-dd HH:mm", "EEEE MMM, d hh:mm a");
            }
        } catch (Exception e) {
            Log.e("TAG", "onBindViewHolder:upcomingOrders Exception " + e);
        }


        holder.txtOrderno.setText(time);

        if (itemLists.size() > 0) {
            holder.layItems.setVisibility(View.VISIBLE);
            holder.layAmount.setVisibility(View.VISIBLE);
            for (int i = 0; i < itemLists.size(); i++) {
                String itemName = itemLists.get(i).getFood_quantity() + " x " + itemLists.get(i).getFood_name() + ",";
                ordersMenu = stringBuilder.append(itemName).toString();
                Log.e("Giri ", "onBindViewHolder: ordersMenu" + ordersMenu);
            }
            holder.txtItems.setText(ordersMenu);
        } else {
            holder.layItems.setVisibility(View.GONE);
            holder.layAmount.setVisibility(View.GONE);
        }
        holder.hotelLoc.setText(upcomingOrders.get(position).getRestaurant_address());

    }

    @Override
    public int getItemCount() {
        return upcomingOrders.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_food)
        AppCompatImageView imgFood;
        @BindView(R.id.hotel_name)
        AppCompatTextView hotelName;
        @BindView(R.id.hotel_loc)
        AppCompatTextView hotelLoc;
        @BindView(R.id.txt_items)
        AppCompatTextView txtItems;
        @BindView(R.id.txt_orderno)
        AppCompatTextView txtOrderno;
        @BindView(R.id.txt_amount)
        AppCompatTextView txtAmount;
        @BindView(R.id.lay_items)
        LinearLayout layItems;
        @BindView(R.id.lay_amount)
        LinearLayout layAmount;
        public Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (upcomingOrders.get(getAdapterPosition()).getDelivery_type().equals(DINING)) {
                        Intent intent = new Intent(activity, DiningTrackActivity.class);
                        intent.putExtra(CONST.REQUEST_ID, upcomingOrders.get(getAdapterPosition()).getRequest_id());
                        activity.startActivity(intent);
                    } else if (upcomingOrders.get(getAdapterPosition()).getDelivery_type().equals(DOOR_DELIVERY) ||
                            upcomingOrders.get(getAdapterPosition()).getDelivery_type().equals(PICKUP_RESTAURANT)) {
                        Intent intent = new Intent(activity, TrackingActivity.class);
                        intent.putExtra(CONST.REQUEST_ID, upcomingOrders.get(getAdapterPosition()).getRequest_id());
                        intent.putExtra(CONST.DELIVERY_TYPE, upcomingOrders.get(getAdapterPosition()).getDelivery_type());
                        activity.startActivity(intent);
                    }

                }
            });
        }
    }
}

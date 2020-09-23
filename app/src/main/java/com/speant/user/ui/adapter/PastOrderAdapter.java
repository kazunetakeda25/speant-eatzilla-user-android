package com.speant.user.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.speant.user.Common.SessionManager;
import com.speant.user.Models.ItemListHistory;
import com.speant.user.ui.DiningTrackActivity;
import com.bumptech.glide.Glide;
import com.speant.user.Common.CONST;
import com.speant.user.Common.Global;
import com.speant.user.Models.PastOrders;
import com.speant.user.R;
import com.speant.user.ui.CompletedOrderActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.CONST.DOOR_DELIVERY;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.Viewholder> {
    Activity activity;
    List<PastOrders> pastOrders;
    List<ItemListHistory> itemLists;



    private String ordersMenu;
    private SessionManager sessionManager;
    private StringBuilder stringBuilder;

    public PastOrderAdapter(Activity activity, List<PastOrders> pastOrders) {
        this.activity = activity;
        this.pastOrders = pastOrders;
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
        Log.e("Giri ", "onBindViewHolder: " + position);
        Log.e("Giri ", "getRestaurant_name: " + pastOrders.get(position).getRestaurant_name());
        Log.e("Giri ", "getBill_amount: " + pastOrders.get(position).getBill_amount());
        itemLists = pastOrders.get(position).getItem_list();
        Glide.with(activity)
                .load(pastOrders.get(position).getRestaurant_image())
                .into(holder.imgFood);
        holder.hotelName.setText(pastOrders.get(position).getRestaurant_name());
        holder.txtAmount.setText(pastOrders.get(position).getBill_amount() + "\t" + sessionManager.getCurrency());
        String time = Global.getDateFromString(pastOrders.get(position).getOrdered_on(), "yyyy-MM-dd HH:mm", "EEEE MMM, d hh:mm a");
        holder.txtOrderno.setText(time);
        if (itemLists.size() > 0) {
            holder.layItems.setVisibility(View.VISIBLE);
            holder.layAmount.setVisibility(View.VISIBLE);
            for (int i = 0; i < itemLists.size(); i++) {
                String itemName = itemLists.get(i).getFood_quantity() + " x " + itemLists.get(i).getFood_name() + ",";
                ordersMenu = stringBuilder.append(itemName).toString();
                Log.e("Giri ", "onBindViewHolder:PastOrderAdapter ordersMenu" + ordersMenu);
            }
            holder.txtItems.setText(ordersMenu);
        } else {
            holder.layItems.setVisibility(View.GONE);
            holder.layAmount.setVisibility(View.GONE);
        }
        holder.hotelLoc.setText(pastOrders.get(position).getRestaurant_address());
    }

    @Override
    public int getItemCount() {
        return pastOrders.size();
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

                    if (pastOrders.get(getAdapterPosition()).getDelivery_type().equals(DINING)) {
                        Intent intent = new Intent(activity, DiningTrackActivity.class);
                        intent.putExtra(CONST.REQUEST_ID, pastOrders.get(getAdapterPosition()).getRequest_id());
                        activity.startActivity(intent);
                    } else if (pastOrders.get(getAdapterPosition()).getDelivery_type().equals(DOOR_DELIVERY) ||
                            pastOrders.get(getAdapterPosition()).getDelivery_type().equals(PICKUP_RESTAURANT)) {
                        Intent intent = new Intent(activity, CompletedOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CONST.PAST_ORDER_DETAIL, pastOrders.get(getAdapterPosition()));
                        bundle.putParcelableArrayList(CONST.PAST_ORDER_ITEMS, (ArrayList<? extends Parcelable>) pastOrders.get(getAdapterPosition()).getItem_list());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }
}

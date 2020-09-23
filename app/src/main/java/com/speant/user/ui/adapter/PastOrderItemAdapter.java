package com.speant.user.ui.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.speant.user.Common.CONST;
import com.speant.user.Models.ItemListHistory;
import com.speant.user.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static io.fabric.sdk.android.Fabric.TAG;

public class PastOrderItemAdapter extends RecyclerView.Adapter<PastOrderItemAdapter.ViewHolder> {

    private final String currencyStr;
    Activity activity;
    List<ItemListHistory> item_list;
    String quantityAddOn = "";



    public PastOrderItemAdapter(Activity activity, List<ItemListHistory> item_list, String currencyStr) {
        this.activity = activity;
        this.item_list = item_list;
        this.currencyStr = currencyStr;
        Log.e("Giri ", "PastOrderItemAdapter:currencyStr " + this.currencyStr);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_past_order_itemlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double itemPrice = 0.0;
        if (item_list.get(position).getIs_veg().equals(CONST.VEG)) {
            Picasso.get().load(R.drawable.ic_green_agmark).into(holder.itemAgmark);
        } else {
            Picasso.get().load(R.drawable.ic_red_agmark).into(holder.itemAgmark);
        }

        holder.itemName.setText(item_list.get(position).getFood_quantity() + " X " + item_list.get(position).getFood_name());
        Log.e("Giri ", "onBindViewHolder: " + currencyStr + item_list.get(position).getItem_price());

        Log.e(TAG, "onBindViewHolder:getFood_size "+ item_list.get(position).getFood_size().size());
        if (item_list.get(position).getFood_size().size() > 0) {

            quantityAddOn = "Quantity : " + item_list.get(position).getFood_size().get(0).getName();
            itemPrice += Double.parseDouble(item_list.get(position).getFood_size().get(0).getPrice());
            Log.e(TAG, "onBindViewHolder:getFood_size "+ quantityAddOn);
            Log.e(TAG, "onBindViewHolder:getFood_size itemPrice "+ itemPrice);
            Log.e(TAG, "onBindViewHolder:getFood_size quantityPrice "+ item_list.get(position).getFood_size().get(0).getPrice());
        }else{
            itemPrice += Double.parseDouble(item_list.get(position).getItem_price());
        }

        if (item_list.get(position).getAdd_ons().size() > 0) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < item_list.get(position).getAdd_ons().size(); i++) {
                itemPrice += Double.parseDouble(item_list.get(position).getAdd_ons().get(i).getPrice());
                Log.e(TAG, "onBindViewHolder:getAdd_ons itemPrice "+ itemPrice);
                Log.e(TAG, "onBindViewHolder:getAdd_ons Add_onPrice "+ item_list.get(position).getAdd_ons().get(i).getPrice());
                if (i == 0) {
                    str.append(activity.getString(R.string.addOns) + item_list.get(position).getAdd_ons().get(i).getName());
                    if (item_list.get(position).getAdd_ons().size() > 1) {
                        str.append(",");
                    }
                } else if (i == item_list.get(position).getAdd_ons().size() - 1) {
                    str.append(item_list.get(position).getAdd_ons().get(i).getName());
                } else {
                    str.append(item_list.get(position).getAdd_ons().get(i).getName());
                    str.append(",");
                }
            }

            if (quantityAddOn.contains("Quantity")) {
                quantityAddOn = quantityAddOn + "\n" + str.toString();
            } else {
                quantityAddOn = str.toString();
            }


        }

        if (quantityAddOn.isEmpty()) {
            holder.itemAddonTxt.setVisibility(View.GONE);
        } else {
            holder.itemAddonTxt.setVisibility(View.VISIBLE);
            holder.itemAddonTxt.setText(quantityAddOn);
        }

        holder.itemAmt.setText(currencyStr + itemPrice);

    }


    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_agmark)
        AppCompatImageView itemAgmark;
        @BindView(R.id.item_name)
        AppCompatTextView itemName;
        @BindView(R.id.item_amt)
        AppCompatTextView itemAmt;
        @BindView(R.id.item_addon_txt)
        TextView itemAddonTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

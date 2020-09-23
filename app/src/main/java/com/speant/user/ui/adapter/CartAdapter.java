package com.speant.user.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.user.Common.callBacks.CheckLoginCallBack;
import com.bumptech.glide.Glide;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.localDb.FoodItemDb;
import com.speant.user.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    Activity activity;

    List<CartDetailsDb> cartDetailsDbList;
    List<FoodItemDb> foodItemDbList;
    ArrayList<StringBuilder> combineItemList;
    CheckLoginCallBack checkLoginCallBack;
    String items;

    int pos;


    public CartAdapter(Activity activity, List<CartDetailsDb> cartDetailsDbList, List<FoodItemDb> foodItemDbList, ArrayList<StringBuilder> combineItemList, CheckLoginCallBack checkLoginCallBack) {
        this.activity = activity;
        this.cartDetailsDbList = cartDetailsDbList;
        this.foodItemDbList = foodItemDbList;
        this.combineItemList = combineItemList;
        this.checkLoginCallBack = checkLoginCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.list_cart_items_txt.setText(combineItemList.get(0));


        holder.list_cart_hotel_loc_txt.setText(cartDetailsDbList.get(position).getRestaurant_address());
        holder.list_cart_total_amt_txt.setText(activity.getResources().getString(R.string.rs) + " " + String.valueOf(cartDetailsDbList.get(position).getTotalAmount()));
        holder.list_cart_hotel_name_txt.setText(cartDetailsDbList.get(position).getRestaurant_name());
        Glide.with(activity).load(cartDetailsDbList.get(position).getRestaurant_image()).into(holder.list_cart_hotel_img);




        /*//convert Time and Date

        String dateFormat = holder.order_date.getText().toString();

        String dateSplit[] = dateFormat.split(" ");
        String splitOne = dateSplit[0];
        String splitTwo = dateSplit[1];

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd ");

        try {

            dateFormat = sdf.format(new SimpleDateFormat("yyyy-M-dd").parse(splitOne));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.order_date.setText(dateFormat + "," + Global.convert(splitTwo));*/


    }

    @Override
    public int getItemCount() {
        return cartDetailsDbList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView list_cart_hotel_img;
        TextView list_cart_hotel_name_txt, list_cart_hotel_loc_txt, list_cart_total_amt_txt, list_cart_items_txt, order_date;
        LinearLayout layDate;

        public ViewHolder(View itemView) {
            super(itemView);
            list_cart_hotel_img = (ImageView) itemView.findViewById(R.id.list_cart_hotel_img);
            list_cart_hotel_name_txt = (TextView) itemView.findViewById(R.id.list_cart_hotel_name_txt);
            order_date = (TextView) itemView.findViewById(R.id.order_date);
            list_cart_hotel_loc_txt = (TextView) itemView.findViewById(R.id.list_cart_hotel_loc_txt);
            list_cart_total_amt_txt = (TextView) itemView.findViewById(R.id.list_cart_total_amt_txt);
            list_cart_items_txt = (TextView) itemView.findViewById(R.id.list_cart_items_txt);
            layDate = (LinearLayout) itemView.findViewById(R.id.lay_date);
            layDate.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkLoginCallBack.loginCheck();
                }
            });
        }
    }
}

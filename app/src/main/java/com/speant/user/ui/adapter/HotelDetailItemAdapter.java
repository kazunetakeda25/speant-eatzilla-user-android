package com.speant.user.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.user.ui.HotelDetailActivity;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Common.CONST;
import com.speant.user.Common.SessionManager;
import com.speant.user.Models.CheckCartPojo;
import com.speant.user.Models.HotelDetailPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelDetailItemAdapter extends RecyclerView.Adapter<HotelDetailItemAdapter.ViewHolder> {

    private static final String TAG = "HotelDetailItemAdapter";
    List<HotelDetailPojo.Restaurants.FoodList> foodLists = new ArrayList<>();
    Context context;
    SessionManager sessionManager;
    APIInterface apiInterface;
    private String foodIdStr;
    private String qtyStr;

    public HotelDetailItemAdapter(HotelDetailActivity context, List<HotelDetailPojo.Restaurants.FoodList> foodLists) {
        this.foodLists = foodLists;
        this.context = context;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recomended, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final HotelDetailPojo.Restaurants.FoodList pojo = foodLists.get(position);

        holder.itemNameTxt.setText(pojo.getName());
        holder.itemAmtTxt.setText("" + pojo.getPrice());
        holder.itemDescTxt.setText(pojo.getDescription());

        Drawable non_veg = context.getResources().getDrawable(R.drawable.ic_red_agmark);
        Drawable veg = context.getResources().getDrawable(R.drawable.ic_green_agmark);

        if (pojo.getIsVeg() == 1) {
            holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(veg, null, null, null);
        } else {
            holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(non_veg, null, null, null);
        }
        if (pojo.getItemCount() > 0) {
            holder.itemAddTxt.setVisibility(View.GONE);
            holder.itemQtyLinear.setVisibility(View.VISIBLE);
            holder.itemQtyTxtLinear.setText("" + pojo.getItemCount());
        } else {
            holder.itemAddTxt.setVisibility(View.VISIBLE);
            holder.itemQtyLinear.setVisibility(View.GONE);
            holder.itemQtyTxtLinear.setText("" + pojo.getItemCount());
        }

        holder.itemAddTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemAddTxt.setVisibility(View.GONE);
                holder.itemQtyLinear.setVisibility(View.VISIBLE);
                int count = pojo.getItemCount() + 1;
                pojo.setItemCount(count);
                holder.itemQtyTxtLinear.setText("" + pojo.getItemCount());

                foodIdStr = String.valueOf(pojo.getFoodId());
                qtyStr = "" + pojo.getItemCount();

                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.food_id, String.valueOf(pojo.getFoodId()));
                map.put(CONST.Params.quantity, "" + pojo.getItemCount());
                map.put(CONST.Params.restaurant_id, CONST.restaren_id);
                map.put(CONST.Params.force_insert, "0");
                jsonValueAdd(map, position);
            }
        });

        holder.itemQtyPlusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = pojo.getItemCount() + 1;
                pojo.setItemCount(count);
                holder.itemQtyTxtLinear.setText("" + pojo.getItemCount());

                foodIdStr = String.valueOf(pojo.getFoodId());
                qtyStr = "" + pojo.getItemCount();

                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.food_id, String.valueOf(pojo.getFoodId()));
                map.put(CONST.Params.quantity, "" + pojo.getItemCount());
                map.put(CONST.Params.restaurant_id, CONST.restaren_id);
                map.put(CONST.Params.force_insert, "0");
                jsonValueAdd(map, position);
            }
        });

        holder.itemQtyMinusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pojo.getItemCount() > 1) {
                    int count = pojo.getItemCount() - 1;
                    pojo.setItemCount(count);
                    holder.itemQtyTxtLinear.setText("" + pojo.getItemCount());
                } else {
                    pojo.setItemCount(0);
                    holder.itemAddTxt.setVisibility(View.VISIBLE);
                    holder.itemQtyLinear.setVisibility(View.GONE);
                    holder.itemQtyTxtLinear.setText("" + pojo.getItemCount());
                }

                foodIdStr = String.valueOf(pojo.getFoodId());
                qtyStr = "" + pojo.getItemCount();

                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.food_id, String.valueOf(pojo.getFoodId()));
                map.put(CONST.Params.quantity, "" + pojo.getItemCount());
                map.put(CONST.Params.restaurant_id, CONST.restaren_id);
                map.put(CONST.Params.force_insert, "0");
                jsonValueMinus(map);

            }
        });

    }

    public void jsonValueAdd(HashMap<String, String> map, final int position) {

        Call<SuccessPojo> call = apiInterface.addToCart(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {
                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        foodLists.get(position).setItemCount(Integer.parseInt(qtyStr));
                        notifyDataChanged();
                        jsonCheckCart();
                    } else {
                        if (response.body().getErrorCode() == 102) {
                            alertDialogue(response.body().getMessage(), position);

                            if (position >= 0) {
                                foodLists.get(position).setItemCount(0);
                                notifyDataChanged();
                            }
                        }
                    }

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonValueMinus(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.reduceFromCart(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        jsonCheckCart();
                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonCheckCart() {

        Call<CheckCartPojo> call = apiInterface.checkCart(sessionManager.getHeader());
        call.enqueue(new Callback<CheckCartPojo>() {
            @Override
            public void onResponse(Call<CheckCartPojo> call, Response<CheckCartPojo> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {

                        CheckCartPojo.Cart pojo = response.body().getCart().get(0);

                        ((HotelDetailActivity) context).cartValue(pojo.getAmount(), pojo.getQuantity());

                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onFailure(Call<CheckCartPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                ((HotelDetailActivity) context).cartValue(0, 0);
            }
        });

    }

    public void alertDialogue(String message, final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // set title
        alertDialogBuilder.setTitle("Replace Cart Item");
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        HashMap<String, String> map = new HashMap<>();
                        map.put(CONST.Params.food_id, foodIdStr);
                        map.put(CONST.Params.quantity, qtyStr);
                        map.put(CONST.Params.restaurant_id, "" + CONST.restaren_id);
                        map.put(CONST.Params.force_insert, "1");
                        jsonValueAdd(map, position);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    @Override
    public int getItemCount() {
        return foodLists.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_add_txt)
        TextView itemAddTxt;
        @BindView(R.id.item_qty_plus_img)
        ImageView itemQtyPlusImg;
        @BindView(R.id.item_qty_txt_linear)
        TextView itemQtyTxtLinear;
        @BindView(R.id.item_qty_minus_img)
        ImageView itemQtyMinusImg;
        @BindView(R.id.item_qty_linear)
        LinearLayout itemQtyLinear;
        @BindView(R.id.add_linear)
        LinearLayout addLinear;
        @BindView(R.id.item_name_txt)
        TextView itemNameTxt;
        @BindView(R.id.item_amt_txt)
        TextView itemAmtTxt;
        @BindView(R.id.item_desc_txt)
        TextView itemDescTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

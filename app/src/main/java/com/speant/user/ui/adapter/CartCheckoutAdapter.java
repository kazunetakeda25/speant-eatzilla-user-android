package com.speant.user.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.onCartUpdate;
import com.speant.user.Common.localDb.AddOnDb;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.localDb.DbStorage;
import com.speant.user.Common.localDb.FoodItemDb;
import com.speant.user.Common.localDb.GroupDb;
import com.speant.user.Common.localDb.QuantityDb;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.R;
import com.speant.user.ui.ViewCartActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.speant.user.Common.CONST.ADD_DATA;
import static com.speant.user.Common.CONST.N0_ADDON;
import static com.speant.user.Common.CONST.NO_QUANTITY_ID;
import static com.speant.user.Common.CONST.REDUCE_DATA;

public class CartCheckoutAdapter extends RecyclerView.Adapter<CartCheckoutAdapter.ViewHolder> {

    private static final String TAG = "CartCheckoutAdapter";
    List<FoodItemDb> foodDetailList;
    SessionManager sessionManager;
    APIInterface apiInterface;

    private Activity context;
    onCartUpdate oncartUpdate;
    List<CartDetailsDb> listCartDetailsDb;
    List<GroupDb> groupDbList;
    DbStorage dbStorage;
    private String ACTION_TYPE;
    double reduceAmount;

    /*public CartCheckoutAdapter(Activity activity, List<FoodItemDb> foodDetailList, List<CartDetailsDb> cartDetailsDbList, onCartUpdate oncartUpdate) {
        this.context = activity;
        listCartDetailsDb = cartDetailsDbList;
        this.foodDetailList = foodDetailList;
        this.oncartUpdate = oncartUpdate;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);

    }*/

    public CartCheckoutAdapter(ViewCartActivity activity, List<GroupDb> groupDbList, onCartUpdate oncartUpdate) {
        this.context = activity;
        this.groupDbList = groupDbList;
        this.oncartUpdate = oncartUpdate;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        dbStorage = new DbStorage();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        GroupDb groupDb = groupDbList.get(position);
        String addOnText = "";
        Log.e(TAG, "onBindViewHolder: " + groupDb.getQuantity_id());
        Log.e(TAG, "onBindViewHolder: " + groupDb.getGroupData());
        Log.e(TAG, "onBindViewHolder: " + groupDbList.size());

        holder.itemQtyTxt.setText("" + groupDb.getGroupCount());

        holder.listCartItemAmt.setText("" + sessionManager.getCurrency() + " " + groupDb.getGroupamount());

        Drawable non_veg = context.getResources().getDrawable(R.drawable.ic_red_agmark);
        Drawable veg = context.getResources().getDrawable(R.drawable.ic_green_agmark);


        List<FoodItemDb> foodItemDb = dbStorage.getAddedFoodDetailsQuery(groupDb.getFood_id());
        for (FoodItemDb itemDb : foodItemDb) {
            Log.e(TAG, "onBindViewHolder:foodItemDb " + itemDb.getFood_name());
            Log.e(TAG, "onBindViewHolder: " + itemDb.getFood_id());
        }
        if (foodItemDb.size() > 0) {
            holder.itemNameTxt.setText(foodItemDb.get(0).getFood_name());
            if (foodItemDb.get(0).getFood_type() == 1) {
                holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(veg, null, null, null);
            } else {
                holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(non_veg, null, null, null);
            }
        }

        if (!groupDb.getGroupData().equals(N0_ADDON)) {
            List<AddOnDb> addOnDbList = dbStorage.getAddedAddOnDetailsQuery(groupDb.getGroupData(), groupDb.getFood_id());
            if (addOnDbList.size() > 0) {
                for (AddOnDb addOnDb : addOnDbList) {
                    Log.e(TAG, "onBindViewHolder:addOnDbList " + addOnDb.getName());
                    Log.e(TAG, "onBindViewHolder: " + addOnDb.getId());
                }
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < addOnDbList.size(); i++) {
                    if (i == 0) {
                        str.append(context.getString(R.string.addOns) + addOnDbList.get(i).getName());
                    } else if (i == addOnDbList.size() - 1) {
                        str.append(addOnDbList.get(i).getName());
                    } else {
                        str.append(addOnDbList.get(i).getName());
                        str.append(",");
                    }
                }

                addOnText = str.toString();
                Log.e(TAG, "onBindViewHolder:addOnText " + addOnText);
            } else {
                addOnText = "";
            }
        } else {
            addOnText = "";
        }

        List<QuantityDb> quantityDb = dbStorage.getAddedQuantityDetailsQuery(groupDb.getQuantity_id(), groupDb.getFood_id());
        if (quantityDb.size() > 0) {
            for (QuantityDb db : quantityDb) {
                Log.e(TAG, "onBindViewHolder:QuantityDb " + db.getName());
                Log.e(TAG, "onBindViewHolder: " + db.getId());
            }
            if (!groupDb.getQuantity_id().equals(NO_QUANTITY_ID)) {

                if (!addOnText.contains("AddOns")) {
                    addOnText = context.getString(R.string.quantity) + quantityDb.get(0).getName();
                } else {
                    addOnText = addOnText + "\n" + context.getString(R.string.quantity) + quantityDb.get(0).getName();
                }

                Log.e(TAG, "onBindViewHolder:quantityMedium " + addOnText);

            }
        }

        holder.itemAddonTxt.setText(addOnText);

        holder.itemQtyPlusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: itemQtyPlusImg");
                ACTION_TYPE = ADD_DATA;
                dbStorage.setAddOnCartData(groupDb, foodItemDb, quantityDb, ACTION_TYPE, oncartUpdate);
               /* int count = pojo.getFood_qty() + 1;
                pojo.setFood_qty(count);
                holder.itemQtyTxt.setText("" + count);
                setFoodDataQuery(pojo);*/


            }
        });

        holder.itemQtyMinusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: itemQtyMinusImg");
                ACTION_TYPE = REDUCE_DATA;
                dbStorage.setAddOnCartData(groupDb, foodItemDb, quantityDb, ACTION_TYPE, oncartUpdate);
                /*if (pojo.getFood_qty() > 1) {
                    int count = pojo.getFood_qty() - 1;
                    pojo.setFood_qty(count);
                    holder.itemQtyTxt.setText("" + count);
                } else {
                    pojo.setFood_qty(0);
                    holder.itemQtyTxt.setText("0");
                }
                setFoodDataQuery(pojo);*/

            }
        });



        /*final FoodItemDb pojo = foodDetailList.get(position);

        holder.itemQtyTxt.setText("" + pojo.getFood_qty());
        holder.itemNameTxt.setText(foodDetailList.get(position).getFood_name());
        int cost = pojo.getFood_qty() * pojo.getFood_cost();
        holder.listCartItemAmt.setText("" + sessionManager.getCurrency() + " " + cost);

        Drawable non_veg = context.getResources().getDrawable(R.drawable.ic_red_agmark);
        Drawable veg = context.getResources().getDrawable(R.drawable.ic_green_agmark);

        if (pojo.getFood_type() == 1) {
            holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(veg, null, null, null);
        } else {
            holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(non_veg, null, null, null);
        }

        holder.itemQtyPlusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = pojo.getFood_qty() + 1;
                pojo.setFood_qty(count);
                holder.itemQtyTxt.setText("" + count);
                setFoodDataQuery(pojo);
            }
        });

        holder.itemQtyMinusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pojo.getFood_qty() > 1) {
                    int count = pojo.getFood_qty() - 1;
                    pojo.setFood_qty(count);
                    holder.itemQtyTxt.setText("" + count);

                } else {
                    pojo.setFood_qty(0);
                    holder.itemQtyTxt.setText("0");
                }
                setFoodDataQuery(pojo);

            }
        });*/

    }

    /*private void setFoodDataQuery(FoodItemDb foodItemDb) {

        CartDetailsDb cartDetailsDb = null;

        //check the foodItem Count to add it to localDb or remove it from LocalDb
        if (foodItemDb.getFood_qty() == 0) {
            App.getInstance().getmDaoSession().getFoodItemDbDao().delete(foodItemDb);
            if (App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll().size() <= 0) {
                App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
                context.finish();
            }
        } else {
            App.getInstance().getmDaoSession().getFoodItemDbDao().insertOrReplace(foodItemDb);
        }

        //check the foodItem Count to add it to localDb or remove it from LocalDb
        List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        if (cartDetailsDbList.size() > 0) {
            int totalAmount = 0;
            int totalCount = 0;
            List<FoodItemDb> foodItemDbList = App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll();
            for (FoodItemDb itemDb : foodItemDbList) {
                totalAmount = totalAmount + (itemDb.getFood_qty() * itemDb.getFood_cost());
                totalCount = totalCount + itemDb.getFood_qty();
            }

            //add data to local Db
            cartDetailsDb = new CartDetailsDb();
            cartDetailsDb.setRestaurant_id(CONST.restaren_id);
            cartDetailsDb.setTotalAmount("" + totalAmount);
            cartDetailsDb.setTotalCount("" + totalCount);
            cartDetailsDb.setRestaurant_name(listCartDetailsDb.get(0).getRestaurant_name());
            cartDetailsDb.setRestaurant_image(listCartDetailsDb.get(0).getRestaurant_image());
            cartDetailsDb.setRestaurant_address(listCartDetailsDb.get(0).getRestaurant_address());
            App.getInstance().getmDaoSession().getCartDetailsDbDao().insertOrReplace(cartDetailsDb);
            CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
            Log.e(TAG, "setFoodDataQuery: TotalAmount" + cartDetails.getTotalAmount());
            Log.e(TAG, "setFoodDataQuery: TotalCount" + cartDetails.getTotalCount());

        } else {
            if (foodItemDb.getFood_qty() != 0) {
                //add data to local Db
                cartDetailsDb = new CartDetailsDb();
                cartDetailsDb.setRestaurant_id(CONST.restaren_id);
                cartDetailsDb.setTotalAmount("" + foodItemDb.getFood_cost());
                cartDetailsDb.setTotalCount("" + foodItemDb.getFood_qty());
                cartDetailsDb.setRestaurant_name(listCartDetailsDb.get(0).getRestaurant_name());
                cartDetailsDb.setRestaurant_image(listCartDetailsDb.get(0).getRestaurant_image());
                cartDetailsDb.setRestaurant_address(listCartDetailsDb.get(0).getRestaurant_address());
                App.getInstance().getmDaoSession().getCartDetailsDbDao().insertOrReplace(cartDetailsDb);
                CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
                Log.e(TAG, "setFoodDataQuery: TotalAmount" + cartDetails.getTotalAmount());
                Log.e(TAG, "setFoodDataQuery: TotalCount" + cartDetails.getTotalCount());
            }
        }
        oncartUpdate.onUpdateCart();
    }*/

    @Override
    public int getItemCount() {
        return groupDbList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_qty_plus_img)
        ImageView itemQtyPlusImg;
        @BindView(R.id.item_qty_txt)
        TextView itemQtyTxt;
        @BindView(R.id.item_qty_minus_img)
        ImageView itemQtyMinusImg;
        @BindView(R.id.item_qty_linear)
        LinearLayout itemQtyLinear;
        @BindView(R.id.add_linear)
        LinearLayout addLinear;
        @BindView(R.id.item_name_txt)
        TextView itemNameTxt;
        @BindView(R.id.list_cart_item_amt)
        TextView listCartItemAmt;
        @BindView(R.id.item_addon_txt)
        TextView itemAddonTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

package com.speant.user.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.CONST;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.app.App;
import com.speant.user.Common.callBacks.OnItemClickCallBack;
import com.speant.user.Common.callBacks.OnTotalAmountUpdate;
import com.speant.user.Common.localDb.AddOnDbDao;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.localDb.FoodItemDb;
import com.speant.user.Common.localDb.FoodItemDbDao;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.AddOns;
import com.speant.user.Models.FinalFoodList;
import com.speant.user.Models.RestaurantData;
import com.speant.user.R;
import com.speant.user.ui.dialogs.AddOnBottomFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodCategoryAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FoodCategoryAdapter";

    private List<CartDetailsDb> cartDetailsDbList;
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private List<FinalFoodList> finalFoodList;
    private Activity context;
    private String forceInsertStr = "0";
    private String restaurant_id;
    private FoodItemDb foodItemDb;
    private RestaurantData restaurantData;
    private List<AddOns> addOnsList = new ArrayList<>();
    private AddOnBottomFragment addOnBottomFragment;
    private FragmentManager supportFragmentManager;
    private OnItemClickCallBack onItemClickCallBack;
    private OnTotalAmountUpdate onTotalAmountUpdate;
    private static final String SQL_SINGLE_DATA = "SELECT * FROM " + FoodItemDbDao.TABLENAME + " WHERE " + FoodItemDbDao.Properties.Food_id.columnName + "=";
    private static final String SQL_ADD_ON_DATA = "SELECT * FROM " + AddOnDbDao.TABLENAME + " WHERE " + AddOnDbDao.Properties.Food_id.columnName + "=";


    public FoodCategoryAdapter(Activity context, List<FinalFoodList> finalFoodList, FragmentManager supportFragmentManager, OnItemClickCallBack onItemClickCallBack, OnTotalAmountUpdate onTotalAmountUpdate) {
        this.finalFoodList = finalFoodList;
        this.context = context;
        this.onItemClickCallBack = onItemClickCallBack;
        this.supportFragmentManager = supportFragmentManager;
        this.onTotalAmountUpdate = onTotalAmountUpdate;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        intialiseRestIdDB();
    }

    public void setRestaurantDetails(RestaurantData restaurantData) {
        this.restaurantData = restaurantData;
    }

    private void intialiseRestIdDB() {
        cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        if (cartDetailsDbList.size() > 0) {
            restaurant_id = cartDetailsDbList.get(0).getRestaurant_id();
        } else {
            restaurant_id = "";
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CONST.CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
                return new CategoryViewHolder(view);
            case CONST.FOOD_ITEMS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recomended, parent, false);
                return new ItemViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (finalFoodList.get(position).getViewType()) {
            case CONST.CATEGORY:
                return CONST.CATEGORY;
            case CONST.FOOD_ITEMS:
                return CONST.FOOD_ITEMS;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final FinalFoodList pojo = finalFoodList.get(position);

        switch (pojo.getViewType()) {
            case CONST.CATEGORY:
                ((CategoryViewHolder) holder).txtCategoryName.setText(pojo.getCategoryName());
                System.out.println("the text is:"+pojo.getCategoryName());
                break;
            case CONST.FOOD_ITEMS:

                getFoodDataQuery(pojo);

                ((ItemViewHolder) holder).itemNameTxt.setText(foodItemDb.getFood_name());
                ((ItemViewHolder) holder).itemAmtTxt.setText(sessionManager.getCurrency() + foodItemDb.getFood_cost());
                ((ItemViewHolder) holder).itemDescTxt.setText(foodItemDb.getFood_desc());

                if (pojo.getOffer_amount() == null || pojo.getOffer_amount().isEmpty()) {
                    ((ItemViewHolder) holder).itemOfferTxt.setVisibility(View.GONE);
                } else {
                    ((ItemViewHolder) holder).itemOfferTxt.setText(pojo.getFood_offer());
                }


                Drawable non_veg = context.getResources().getDrawable(R.drawable.ic_red_agmark);
                Drawable veg = context.getResources().getDrawable(R.drawable.ic_green_agmark);

                if (foodItemDb.getFood_type() == 1) {
                    ((ItemViewHolder) holder).itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(veg, null, null, null);
                } else {
                    ((ItemViewHolder) holder).itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(non_veg, null, null, null);
                }

                if (foodItemDb.getFood_qty() > 0) {
                    ((ItemViewHolder) holder).itemAddTxt.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).itemQtyLinear.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).itemQtyTxtLinear.setText("" + foodItemDb.getFood_qty());
                } else {
                    ((ItemViewHolder) holder).itemAddTxt.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).itemQtyLinear.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).itemQtyTxtLinear.setText("" + foodItemDb.getFood_qty());
                }

                ((ItemViewHolder) holder).itemAddTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onItemClickCallBack.onAddClickCallBack(position);

                        /*if(isHavingAddOns(pojo)){
                            addOnsList = pojo.getAdd_ons();
                            addOnBottomFragment = new AddOnBottomFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList(CONST.ADDON_DATA, (ArrayList<? extends Parcelable>) addOnsList);
                            addOnBottomFragment.setArguments(bundle);
                            addOnBottomFragment.show(supportFragmentManager,"AddOnBottomFragment");
                        }


                        if (restaurant_id.isEmpty() || restaurant_id.equals(CONST.restaren_id)) {
                            getFoodDataQuery(pojo);
                            ((ItemViewHolder) holder).itemAddTxt.setVisibility(View.GONE);
                            ((ItemViewHolder) holder).itemQtyLinear.setVisibility(View.VISIBLE);
                            int count = foodItemDb.getFood_qty() + 1;
                            ((ItemViewHolder) holder).itemQtyTxtLinear.setText("" + count);
                            foodItemDb.setFood_qty(count);


                            setFoodDataQuery(foodItemDb);

                        } else {
                            alertDialogue(context.getString(R.string.clear_cart), position, holder);
                        }*/

                    }
                });

                ((ItemViewHolder) holder).itemQtyPlusImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickCallBack.onPlusClickCallBack(position);

                        /*if(isHavingAddOns(pojo)) {
                            addOnsList = pojo.getAdd_ons();
                            addOnBottomFragment = new AddOnBottomFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList(CONST.ADDON_DATA, (ArrayList<? extends Parcelable>) addOnsList);
                            addOnBottomFragment.setArguments(bundle);
                            addOnBottomFragment.show(supportFragmentManager, "AddOnBottomFragment");
                        }


                        getFoodDataQuery(pojo);
                        int count = foodItemDb.getFood_qty() + 1;
                        ((ItemViewHolder) holder).itemQtyTxtLinear.setText("" + count);

                        foodItemDb.setFood_qty(count);
                        setFoodDataQuery(foodItemDb);*/
                    }
                });

                ((ItemViewHolder) holder).itemQtyMinusImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickCallBack.onMinusClickCallBack(position);

                       /* getFoodDataQuery(pojo);
                        if (foodItemDb.getFood_qty() > 1) {
                            int count = foodItemDb.getFood_qty() - 1;
                            ((ItemViewHolder) holder).itemQtyTxtLinear.setText("" + count);
                            foodItemDb.setFood_qty(count);
                            setFoodDataQuery(foodItemDb);
                        } else {
                            ((ItemViewHolder) holder).itemAddTxt.setVisibility(View.VISIBLE);
                            ((ItemViewHolder) holder).itemQtyLinear.setVisibility(View.GONE);
                            ((ItemViewHolder) holder).itemQtyTxtLinear.setText("0");
                            foodItemDb.setFood_qty(0);
                            setFoodDataQuery(foodItemDb);
                        }*/
                    }
                });
                break;

        }

    }


    private void setFoodDataQuery(FoodItemDb foodItemDb) {
        CartDetailsDb cartDetailsDb = null;

        //check the foodItem Count to add it to localDb or remove it from LocalDb
        if (foodItemDb.getFood_qty() == 0) {
            App.getInstance().getmDaoSession().getFoodItemDbDao().delete(foodItemDb);
            if (App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll().size() <= 0) {
                App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
                intialiseRestIdDB();

            }
        } else {
            App.getInstance().getmDaoSession().getFoodItemDbDao().insertOrReplace(foodItemDb);
        }

        //check the foodItem Count to add it to localDb or remove it from LocalDb
        List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        if (cartDetailsDbList.size() > 0) {
            double totalAmount = 0;
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
            cartDetailsDb.setRestaurant_name(restaurantData.getRestaurant_name());
            cartDetailsDb.setRestaurant_image(restaurantData.getImage());
            cartDetailsDb.setRestaurant_address(restaurantData.getAddress());
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
                cartDetailsDb.setRestaurant_name(restaurantData.getRestaurant_name());
                cartDetailsDb.setRestaurant_image(restaurantData.getImage());
                cartDetailsDb.setRestaurant_address(restaurantData.getAddress());
                App.getInstance().getmDaoSession().getCartDetailsDbDao().insertOrReplace(cartDetailsDb);
                CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
                Log.e(TAG, "setFoodDataQuery: TotalAmount" + cartDetails.getTotalAmount());
                Log.e(TAG, "setFoodDataQuery: TotalCount" + cartDetails.getTotalCount());
            }
        }
        onTotalAmountUpdate.onAmountUpdate();
    }


    private void getFoodDataQuery(FinalFoodList pojo) {
        Cursor c = App.getInstance().getmDaoSession().getFoodItemDbDao().getDatabase().rawQuery(SQL_SINGLE_DATA + pojo.getFood_id(), null);

        //get Cart Count of preferred productId
        ArrayList<FoodItemDb> result = new ArrayList<>();

        final int food_id = c.getColumnIndex(FoodItemDbDao.Properties.Food_id.columnName);
        final int food_cost = c.getColumnIndex(FoodItemDbDao.Properties.Food_cost.columnName);
        final int food_name = c.getColumnIndex(FoodItemDbDao.Properties.Food_name.columnName);
        final int food_count = c.getColumnIndex(FoodItemDbDao.Properties.Food_qty.columnName);
        final int food_type = c.getColumnIndex(FoodItemDbDao.Properties.Food_type.columnName);
        final int food_desc = c.getColumnIndex(FoodItemDbDao.Properties.Food_desc.columnName);
        final int food_tax = c.getColumnIndex(FoodItemDbDao.Properties.Food_tax.columnName);
        final int rest_id = c.getColumnIndex(FoodItemDbDao.Properties.Restaurant_id.columnName);
        final int target_amount = c.getColumnIndex(FoodItemDbDao.Properties.Target_amount.columnName);
        final int offer_amount = c.getColumnIndex(FoodItemDbDao.Properties.Offer_amount.columnName);
        final int discount_type = c.getColumnIndex(FoodItemDbDao.Properties.Discount_type.columnName);
        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final String food_name_fin = c.getString(food_name);
                        final String food_desc_fin = c.getString(food_desc);
                        final double food_cost_fin = Double.parseDouble(c.getString(food_cost));
                        final int food_count_fin = Integer.parseInt(c.getString(food_count));
                        final int food_type_fin = Integer.parseInt(c.getString(food_type));
                        final double food_tax_fin = Double.parseDouble(c.getString(food_tax));
                        final String target_amount_fin = c.getString(target_amount);
                        final String offer_amount_fin = c.getString(offer_amount);
                        final String discount_type_fin = c.getString(discount_type);
                        result.add(new FoodItemDb(rest_id_fin, food_id_fin, food_count_fin, food_name_fin, food_type_fin, food_cost_fin, food_desc_fin, food_tax_fin, discount_type_fin, target_amount_fin, offer_amount_fin));
                    }
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }

        if (result.size() > 0) {
            for (FoodItemDb foodItemDbsingle : result) {
                foodItemDb = foodItemDbsingle;
                Log.e("TAG", "onProductDetailsFragmentView:result prod_name " + foodItemDb.getFood_id());
                Log.e("TAG", "onProductDetailsFragmentView:result prod_count" + foodItemDb.getFood_qty());
            }
        } else {
            foodItemDb = null;
        }

        if (foodItemDb == null) {
            foodItemDb = new FoodItemDb();
            foodItemDb.setFood_cost(Double.parseDouble(pojo.getPrice()));
            foodItemDb.setFood_id(pojo.getFood_id());
            foodItemDb.setFood_name(pojo.getName());
            foodItemDb.setFood_qty(pojo.getItem_count());
            foodItemDb.setFood_type(pojo.getIs_veg());
            foodItemDb.setFood_desc(pojo.getDescription());
            foodItemDb.setFood_tax(pojo.getItem_tax());
            foodItemDb.setTarget_amount(pojo.getTarget_amount());
            foodItemDb.setDiscount_type(pojo.getDiscount_type());
            foodItemDb.setOffer_amount(pojo.getOffer_amount());
            foodItemDb.setRestaurant_id(CONST.restaren_id);
        }

    }

    public void alertDialogue(String message, final int position, RecyclerView.ViewHolder holder) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // set title
        alertDialogBuilder.setTitle(context.getString(R.string.replace_cart_item));
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        App.getInstance().getmDaoSession().getFoodItemDbDao().deleteAll();
                        App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
                        intialiseRestIdDB();
                        FinalFoodList pojo = finalFoodList.get(position);
                        getFoodDataQuery(pojo);
                        ((ItemViewHolder) holder).itemAddTxt.setVisibility(View.GONE);
                        ((ItemViewHolder) holder).itemQtyLinear.setVisibility(View.VISIBLE);
                        int count = foodItemDb.getFood_qty() + 1;
                        ((ItemViewHolder) holder).itemQtyTxtLinear.setText("" + count);
                        foodItemDb.setFood_qty(count);
                        setFoodDataQuery(foodItemDb);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
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
        Log.e("Giri ", "getItemCount:finalFoodList " + finalFoodList.size());
        return finalFoodList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }


    static

    public class ItemViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.item_offer_txt)
        TextView itemOfferTxt;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_category_name)
        AppCompatTextView txtCategoryName;
        @BindView(R.id.lay_category)
        LinearLayout layCategory;

        public CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

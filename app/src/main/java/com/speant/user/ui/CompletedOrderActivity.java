package com.speant.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.app.App;
import com.speant.user.Common.callBacks.AdapterRefreshCallBack;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.localDb.DbStorage;
import com.speant.user.Models.FinalFoodList;
import com.speant.user.Models.FoodQuantity;
import com.speant.user.Models.FoodSize;
import com.speant.user.Models.ItemListHistory;
import com.speant.user.Models.PastOrders;
import com.speant.user.Models.Pivot;
import com.speant.user.Models.RestaurantData;
import com.speant.user.R;
import com.speant.user.ui.adapter.PastOrderItemAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.speant.user.Common.CONST.ADD_DATA;
import static com.speant.user.Common.CONST.ORDER_CANCELLED;

public class CompletedOrderActivity extends AppCompatActivity implements AdapterRefreshCallBack {
    private static final String TAG = "CompletedOrderActivity";
    PastOrders pastOrders;
    List<ItemListHistory> itemListList;
    @BindView(R.id.recycler_item_list)
    RecyclerView recyclerItemList;
    @BindView(R.id.item_total_amount_txt)
    TextView itemTotalAmountTxt;
    @BindView(R.id.offer_discount_amount_txt)
    TextView offerDiscountAmountTxt;
    @BindView(R.id.packaging_charge_amount_txt)
    TextView packagingChargeAmountTxt;
    @BindView(R.id.gst_amount_txt)
    TextView gstAmountTxt;
    @BindView(R.id.delivery_charge_amount_txt)
    TextView deliveryChargeAmountTxt;
    @BindView(R.id.total_to_pay_amount_txt)
    TextView totalToPayAmountTxt;
    @BindView(R.id.hotel_img)
    ImageView hotelImg;
    @BindView(R.id.hotel_name_txt)
    TextView hotelNameTxt;
    @BindView(R.id.hotel_place_txt)
    TextView hotelPlaceTxt;
    @BindView(R.id.hotel_delivery_txt)
    TextView hotelDeliveryTxt;
    @BindView(R.id.address_txt)
    TextView addressTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_repeat)
    AppCompatButton btnRepeat;
    @BindView(R.id.item_total_discount_txt)
    TextView itemTotalDiscountTxt;
    @BindView(R.id.btn_status)
    AppCompatButton btnStatus;
    private PastOrderItemAdapter pastOrderItemAdapter;
    private SessionManager sessionManager;
    private String currencyStr;
    private List<CartDetailsDb> cartDetailsDbList;
    private String restaurant_id;
    private DbStorage dbStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(CompletedOrderActivity.this);
        currencyStr = sessionManager.getCurrency();
        dbStorage = new DbStorage();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        pastOrders = bundle.getParcelable(CONST.PAST_ORDER_DETAIL);
        itemListList = bundle.getParcelableArrayList(CONST.PAST_ORDER_ITEMS);
        Log.e("Giri ", "onCreate:pastOrders " + pastOrders.getRequest_id());
        Log.e("Giri ", "onCreate:pastOrders " + itemListList.size());
        setAdapter();
        setDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setAdapter() {
        //set Toolbar
        String time = Global.getDateFromString(pastOrders.getOrdered_on(), "yyyy-MM-dd HH:mm", "hh:mm a");
        toolbar.setTitle(pastOrders.getOrder_id());
        toolbar.setSubtitle(time + " | " + itemListList.size() + " Item" + " | " + currencyStr + pastOrders.getBill_amount());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerItemList.setLayoutManager(linearLayoutManager);
        recyclerItemList.setNestedScrollingEnabled(false);
        pastOrderItemAdapter = new PastOrderItemAdapter(CompletedOrderActivity.this, itemListList, currencyStr);
        recyclerItemList.setAdapter(pastOrderItemAdapter);

    }

    private void setDetails() {
        String dateTime = Global.getDateFromString(pastOrders.getOrdered_on(), "yyyy-MM-dd HH:mm", "EEEE MMM, d hh:mm a");

        Log.e("Giri ", "setDetails:setTitle " + pastOrders.getOrder_id());
        Picasso.get().load(pastOrders.getRestaurant_image()).into(hotelImg);
        hotelNameTxt.setText(pastOrders.getRestaurant_name());
        hotelPlaceTxt.setText(pastOrders.getRestaurant_address());
        hotelDeliveryTxt.setText(dateTime);
        addressTxt.setText(pastOrders.getDelivery_address());

        itemTotalAmountTxt.setText(currencyStr + pastOrders.getItem_total());
        offerDiscountAmountTxt.setText(currencyStr + pastOrders.getOffer_discount());
        packagingChargeAmountTxt.setText(currencyStr + pastOrders.getRestaurant_packaging_charge());
        gstAmountTxt.setText(currencyStr + pastOrders.getTax());
        deliveryChargeAmountTxt.setText(currencyStr + pastOrders.getDelivery_charge());
        totalToPayAmountTxt.setText(currencyStr + pastOrders.getBill_amount());
        itemTotalDiscountTxt.setText(currencyStr + pastOrders.getRestaurant_discount());

        if(pastOrders.getStatus().equals(ORDER_CANCELLED)){
            btnStatus.setVisibility(View.VISIBLE);
        }else{
            btnStatus.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.total_to_pay_amount_txt, R.id.btn_repeat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.total_to_pay_amount_txt:
                break;
            case R.id.btn_repeat:
                try {
                    App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getQuantityDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getAddOnDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getGroupDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getFoodItemDbDao().deleteAll();

                    CONST.restaren_id = pastOrders.getRestaurant_id();
                    for (ItemListHistory itemList : itemListList) {
                        if (itemList.getAdd_ons().size() > 0 && itemList.getFood_size() != null) {
                            dbStorage.setAddOnData(itemList.getAdd_ons(), setFoodQuantity(itemList.getFood_size().get(0)), setRestaurantData(), setFinalFoodList(itemList), ADD_DATA, this);
                        } else if (itemList.getFood_size() != null) {
                            dbStorage.setAddOnData(null, setFoodQuantity(itemList.getFood_size().get(0)), setRestaurantData(), setFinalFoodList(itemList), ADD_DATA, this);
                        } else {
                            dbStorage.setAddOnData(null, null, setRestaurantData(), setFinalFoodList(itemList), ADD_DATA, this);
                        }
                    }
                    if (App.getInstance().getmDaoSession().getFoodItemDbDao().loadAll().size() > 0) {
                        CommonFunctions.shortToast(this, "Cart Updated Successfully");
                    }
                } catch (Exception e) {

                }

                /*for (ItemList itemList : itemListList) {
                    FoodItemDb foodItemDb = new FoodItemDb();
                    foodItemDb.setFood_id(itemList.getFood_id());
                    foodItemDb.setFood_qty(Integer.parseInt(itemList.getFood_quantity()));
                    foodItemDb.setFood_cost(Integer.parseInt(itemList.getItem_price()));
                    foodItemDb.setFood_name(itemList.getFood_name());
                    foodItemDb.setFood_tax(Double.parseDouble(itemList.getTax()));
                    foodItemDb.setFood_desc(itemList.getDescription());
                    foodItemDb.setFood_type(Integer.parseInt(itemList.getIs_veg()));
                    foodItemDb.setRestaurant_id(pastOrders.getRestaurant_id());
                    setFoodDataQuery(foodItemDb);
                }*/
                break;
        }
    }

    private FinalFoodList setFinalFoodList(ItemListHistory itemList) {
        FinalFoodList finalFoodList = new FinalFoodList();
        finalFoodList.setPrice(String.valueOf(itemList.getItem_price()));
        finalFoodList.setFood_id(itemList.getFood_id());
        finalFoodList.setName(itemList.getFood_name());
        finalFoodList.setItem_count(Integer.parseInt(itemList.getFood_quantity()));
        finalFoodList.setIs_veg(Integer.parseInt(itemList.getIs_veg()));
        finalFoodList.setDescription(itemList.getDescription());
        finalFoodList.setItem_tax(Double.parseDouble(itemList.getTax()));
        Log.e(TAG, "getFoodDataQuery:foodItemDbID " + itemList.getFood_id());
        return finalFoodList;
    }

    private RestaurantData setRestaurantData() {
        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setAddress(pastOrders.getRestaurant_address());
        restaurantData.setImage(pastOrders.getRestaurant_image());
        restaurantData.setRestaurant_name(pastOrders.getRestaurant_name());
        return restaurantData;
    }

    private FoodQuantity setFoodQuantity(FoodSize foodSize) {
        FoodQuantity foodQuatiy = new FoodQuantity();
        foodQuatiy.setId(foodSize.getId());
        foodQuatiy.setName(foodSize.getName());
        Pivot pivot = new Pivot();
        pivot.setPrice(foodSize.getPrice());
        foodQuatiy.setPivot(pivot);
        return foodQuatiy;
    }

    /*private void setFoodDataQuery(FoodItemDb foodItemDb) {
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
            cartDetailsDb.setRestaurant_name(pastOrders.getRestaurant_name());
            cartDetailsDb.setRestaurant_image(pastOrders.getRestaurant_image());
            cartDetailsDb.setRestaurant_address(pastOrders.getRestaurant_address());
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
                cartDetailsDb.setRestaurant_name(pastOrders.getRestaurant_name());
                cartDetailsDb.setRestaurant_image(pastOrders.getRestaurant_image());
                cartDetailsDb.setRestaurant_address(pastOrders.getRestaurant_address());
                App.getInstance().getmDaoSession().getCartDetailsDbDao().insertOrReplace(cartDetailsDb);
                CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
                Log.e(TAG, "setFoodDataQuery: TotalAmount" + cartDetails.getTotalAmount());
                Log.e(TAG, "setFoodDataQuery: TotalCount" + cartDetails.getTotalCount());
            }
        }
    }*/

    /* private void intialiseRestIdDB() {
         cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
         if (cartDetailsDbList.size() > 0) {
             restaurant_id = cartDetailsDbList.get(0).getRestaurant_id();
         } else {
             restaurant_id = "";
         }
     }
 */
    @Override
    public void onDbUpdateAdapterRefresh() {
        //not used in this screen
    }
}

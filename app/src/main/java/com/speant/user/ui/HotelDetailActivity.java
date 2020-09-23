package com.speant.user.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.app.App;
import com.speant.user.Common.callBacks.AdapterRefreshCallBack;
import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.Common.callBacks.OnAddOnSelectComplete;
import com.speant.user.Common.callBacks.OnItemClickCallBack;
import com.speant.user.Common.callBacks.OnTotalAmountUpdate;
import com.speant.user.Common.callBacks.onCartUpdate;
import com.speant.user.Common.localDb.CartDetailsDb;
import com.speant.user.Common.localDb.DbStorage;
import com.speant.user.Common.localDb.FoodItemDb;
import com.speant.user.Common.localDb.FoodItemDbDao;
import com.speant.user.Common.localDb.GroupDb;
import com.speant.user.Common.localDb.QuantityDb;
import com.speant.user.Common.networkListener.NetworkRefreshModel;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.AddOns;
import com.speant.user.Models.CategoryPojo;
import com.speant.user.Models.FinalFoodList;
import com.speant.user.Models.FoodListResponse;
import com.speant.user.Models.FoodQuantity;
import com.speant.user.Models.HotelDetailPojo;
import com.speant.user.Models.MenuPojo;
import com.speant.user.Models.RestaurantData;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.speant.user.ui.adapter.FoodCategoryAdapter;
import com.speant.user.ui.adapter.MenuAdapter;
import com.speant.user.ui.dialogs.AddOnBottomFragment;
import com.speant.user.ui.fragment.LoginBottomFragment;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.ADD_DATA;
import static com.speant.user.Common.CONST.DIFFERENT_ADDON_GROUP;
import static com.speant.user.Common.CONST.DIFFERENT_RESTAURANT;
import static com.speant.user.Common.CONST.REDUCE_DATA;

public class HotelDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,
        TabLayout.OnTabSelectedListener, OnTotalAmountUpdate, OnItemClickCallBack, OnAddOnSelectComplete, AdapterRefreshCallBack, onCartUpdate, LoginSuccessCallBack {

    private static final String TAG = "HotelDetailActivity";
    SessionManager sessionManager;
    APIInterface apiInterface;
    MenuAdapter menuAdapter;
    List<MenuPojo.Menus> menusList = new ArrayList<>();

    @BindView(R.id.main_backdrop)
    ImageView mainBackdrop;
    @BindView(R.id.hotel_detail_rating_txt)
    TextView hotelDetailRatingTxt;
    @BindView(R.id.hotel_detail_hotel_name_txt)
    TextView hotelDetailHotelNameTxt;
    @BindView(R.id.hotel_detail_hotel_type_txt)
    TextView hotelDetailHotelTypeTxt;
    @BindView(R.id.hotel_detail_hotel_discount_txt)
    TextView hotelDetailHotelDiscountTxt;
    @BindView(R.id.hotel_detail_time_txt)
    TextView hotelDetailTimeTxt;
    @BindView(R.id.hotel_detail_amt_txt)
    TextView hotelDetailAmtTxt;
    @BindView(R.id.switchButton)
    SwitchCompat switchButton;
    @BindView(R.id.hotel_detail_card)
    CardView hotelDetailCard;
    @BindView(R.id.menu_btm_rv)
    RecyclerView menuBtmRv;
    @BindView(R.id.menu_bottom_sheet)
    LinearLayout menuBottomSheet;
    @BindView(R.id.hotel_detail_menu_relative)
    LinearLayout hotelDetailMenuRelative;
    @BindView(R.id.hotel_detail_cart_amt_txt)
    TextView hotelDetailCartAmtTxt;
    @BindView(R.id.hotel_detail_cart_total_txt)
    TextView hotelDetailCartTotalTxt;
    @BindView(R.id.hotel_detail_card_relative)
    RelativeLayout hotelDetailCardRelative;
    @BindView(R.id.cart_card)
    RelativeLayout cartCard;
    @BindView(R.id.coordinator_lay)
    CoordinatorLayout coordinatorLay;
    @BindView(R.id.menu_card)
    CardView menuCard;
    @BindView(R.id.view_cart_txt)
    TextView viewCartTxt;
    @BindView(R.id.menu_txt)
    TextView menuTxt;
    AppCompatTextView txtTitle;
    AppCompatTextView txtSubTitle;
    LinearLayout layTitle;
    String hotelName, hotelReachTime;
    @BindView(R.id.time_txt)
    TextView timeTxt;
    @BindView(R.id.amt_txt)
    TextView amtTxt;
    @BindView(R.id.lay_selection)
    LinearLayout laySelection;
    @BindView(R.id.rating_txt)
    AppCompatTextView ratingTxt;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.lay_progress)
    FrameLayout layProgress;
    @BindView(R.id.collapse_toolbar)
    Toolbar collapseToolbar;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.lay_toolbar)
    FrameLayout layToolbar;
    @BindView(R.id.recycler_food_items)
    RecyclerView recyclerFoodItems;
    @BindView(R.id.btn_back)
    AppCompatImageView btnBack;
    @BindView(R.id.txt_restaurant_name)
    AppCompatTextView txtRestaurantName;
    @BindView(R.id.nested_lay)
    NestedScrollView nestedLay;
    @BindView(R.id.txt_credit_offer)
    AppCompatTextView txtCreditOffer;
    private LinearLayoutManager MyLayoutManager;
    private BottomSheetBehavior<LinearLayout> behavior;
    boolean likeBol = false;
    private FoodCategoryAdapter foodCategoryAdapter;
    private boolean isFirst = true;
    List<FoodListResponse.FoodList> foodLists = new ArrayList<>();
    List<FinalFoodList> finalFoodList = new ArrayList<>();
    HashMap<String, Integer> categoryPos = new HashMap<>();
    private String restaurant_id;
    private RestaurantData restaurantData;
    private List<CartDetailsDb> cartDetailsDbList;
    private List<AddOns> addOnsList = new ArrayList<>();
    private List<FoodQuantity> foodQuantityList = new ArrayList<>();
    private static final String SQL_SINGLE_DATA = "SELECT * FROM " + FoodItemDbDao.TABLENAME + " WHERE " + FoodItemDbDao.Properties.Food_id.columnName + "=";

    private AddOnBottomFragment addOnBottomFragment;
    private FoodItemDb foodItemDb;
    private int selectedPosition;
    private DbStorage dbStorage;
    private String ACTION_TYPE;
    private LoginBottomFragment loginBottomFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hotel_detail);
        ButterKnife.bind(this);
        dbStorage = new DbStorage();
        intialiseRestIdDB();

        setcheckedStatus();
        actionBar();
        if (isFirst) {
            layToolbar.setTranslationY(-500f);
            isFirst = false;
        }

        final Rect scrollBounds = new Rect();
        nestedLay.getHitRect(scrollBounds);

        nestedLay.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (mainBackdrop != null) {

                    if (mainBackdrop.getLocalVisibleRect(scrollBounds)) {
                        if (!mainBackdrop.getLocalVisibleRect(scrollBounds)
                                || scrollBounds.height() < mainBackdrop.getHeight()
                                || scrollBounds.height() == mainBackdrop.getHeight()) {
                            Log.i(TAG, "BTN APPEAR PARCIALY");

                            if (layToolbar.getTranslationY() == 0f) {
//                                layToolbar.setTranslationY(-500f).setDuration(400);
                                layToolbar.animate().translationYBy(-500f).setDuration(200);
                            }
                        } else {
                            Log.i(TAG, "BTN APPEAR FULLY!!!");
                        }
                    } else {
                        Log.i(TAG, "No");
                        if (layToolbar.getTranslationY() == -500f) {
                            layToolbar.animate().translationYBy(500f).setDuration(200);
                        }
                    }
                }

            }
        });


        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        switchButton.setOnCheckedChangeListener(this);
        tabs.addOnTabSelectedListener(this);

        MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerFoodItems.setLayoutManager(MyLayoutManager);
        recyclerFoodItems.setNestedScrollingEnabled(false);
        foodCategoryAdapter = new FoodCategoryAdapter(HotelDetailActivity.this, finalFoodList, getSupportFragmentManager(), this, this::onAmountUpdate);
        recyclerFoodItems.setAdapter(foodCategoryAdapter);

        HashMap<String, String> map = new HashMap<>();
        map.put(CONST.Params.veg_only, CONST.pureVegStr);
        map.put(CONST.Params.restaurant_id, CONST.restaren_id);
        jsonHotelDetail(map);
        jsonGetCategory();


    }


    @Override
    protected void onResume() {
        super.onResume();
        getfoodList();
        setTotalAmountView();
    }


    private void setTotalAmountView() {
        List<CartDetailsDb> cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        if (cartDetailsDbList.size() > 0) {
            CartDetailsDb cartDetails = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll().get(0);
            restaurant_id = cartDetails.getRestaurant_id();

            double totalAmount = Double.parseDouble(cartDetails.getTotalAmount());
            int totalCount = Integer.parseInt(cartDetails.getTotalCount());
            Log.e(TAG, "setTotalAmountView:onAdaptref " + totalAmount + " " + totalCount);
            cartValue(totalAmount, totalCount);
        } else {
            restaurant_id = "";
            cartValue(0, 0);
        }

    }

    public boolean isViewVisible(View view) {
        Rect scrollBounds = new Rect();
        nestedLay.getDrawingRect(scrollBounds);

        float top = view.getY();
        float bottom = top + view.getHeight();

        if (scrollBounds.top < top && scrollBounds.bottom > bottom) {
            return true;
        } else {
            return false;
        }
    }


    private void getfoodList() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CONST.Params.restaurant_id, CONST.restaren_id);
        map.put(CONST.Params.is_veg, CONST.pureVegStr);
        Call<FoodListResponse> call = apiInterface.getfoodlist(sessionManager.getHeader(), map);
        call.enqueue(new Callback<FoodListResponse>() {
            @Override
            public void onResponse(Call<FoodListResponse> call, Response<FoodListResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {

                        if (!finalFoodList.isEmpty()) {
                            finalFoodList.clear();
                        }

                        foodLists = response.body().getFood_list();

                        for (int i = 0; i < foodLists.size(); i++) {
                            FinalFoodList categoryItem = new FinalFoodList();
                            categoryItem.setCategoryName(foodLists.get(i).getCategory_name());
                            categoryItem.setCategory_id(foodLists.get(i).getCategory_id());
                            categoryItem.setViewType(CONST.CATEGORY);
                            finalFoodList.add(categoryItem);
                            categoryPos.put(foodLists.get(i).getCategory_id(), finalFoodList.size() - 1);

                            Log.e("Giri ", "onResponse:categoryPos on Add " + categoryPos.get(foodLists.get(i).getCategory_id()));

                            for (FoodListResponse.FoodList.Items item : foodLists.get(i).getItems()) {
                                Log.e(TAG, "onResponse:foodItem getAdd_ons " + item.getAdd_ons().size());

                                FinalFoodList foodItem = new FinalFoodList();
                                foodItem.setCategory_id(item.getCategory_id());
                                foodItem.setFood_id(item.getFood_id());
                                foodItem.setDescription(item.getDescription());
                                foodItem.setName(item.getName());
                                foodItem.setIs_veg(item.getIs_veg());
                                foodItem.setPrice(item.getPrice());
                                foodItem.setItem_count(item.getItem_count());
                                foodItem.setViewType(CONST.FOOD_ITEMS);
                                foodItem.setItem_tax(item.getItem_tax());
                                foodItem.setAdd_ons(item.getAdd_ons());
                                foodItem.setFood_quantity(item.getFood_quantity());
                                Log.e(TAG, "onResponse:foodItem Single " + foodItem.getItem_tax());
                                finalFoodList.add(foodItem);
                            }

                        }
                        restaurantData = response.body().getRestaurant_detail();
//                        restaurantData.setImage(IMAGE_BASE_URL+response.body().getRestaurant_detail().getImage());
                        foodCategoryAdapter.notifyDataChanged();
                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onFailure(Call<FoodListResponse> call, Throwable t) {

            }
        });

    }

    private void setcheckedStatus() {
        boolean checked = switchButton.isChecked();
        if (checked)
            CONST.pureVegStr = "1";
        else
            CONST.pureVegStr = "0";
    }

    public void jsonGetCategory() {

        Call<CategoryPojo> call = apiInterface.getCategory(sessionManager.getHeader(), CONST.restaren_id);
        call.enqueue(new Callback<CategoryPojo>() {
            @Override
            public void onResponse(Call<CategoryPojo> call, Response<CategoryPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        List<CategoryPojo.Category> categoryList = response.body().getCategory();
                        if (tabs.getTabCount() > 0) {
                            tabs.removeAllTabs();
                        }
                        for (int i = 0; i < categoryList.size(); i++) {
                            Log.e("Giri ", "onResponse:CategoryPojo ");

                            TabLayout.Tab tab = tabs.newTab();
                            tab.setText(categoryList.get(i).getName());
                            tab.setTag(categoryList.get(i).getCategoryId());
                            tabs.addTab(tab);
                        }

                    }

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<CategoryPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void getLike(Boolean fav) {
        likeBol = fav;
        invalidateOptionsMenu();
    }


    // slide the view from below itself to the current position
    public void slideUp() {
        hotelDetailMenuRelative.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                hotelDetailMenuRelative.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        hotelDetailMenuRelative.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown() {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                hotelDetailMenuRelative.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        hotelDetailMenuRelative.startAnimation(animate);
    }

    public void actionBar() {
        setSupportActionBar(collapseToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void jsonHotelDetail(HashMap<String, String> map) {

        Call<HotelDetailPojo> call = apiInterface.hotelDetail(sessionManager.getHeader(), map);
        call.enqueue(new Callback<HotelDetailPojo>() {
            @Override
            public void onResponse(Call<HotelDetailPojo> call, Response<HotelDetailPojo> response) {


                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        hotelDetailCard.setVisibility(View.VISIBLE);
                        layProgress.setVisibility(View.GONE);
                        HotelDetailPojo.Restaurants pojo = response.body().getRestaurants().get(0);

                        hotelName = pojo.getName();
                        hotelReachTime = pojo.getTravelTime();
                        txtRestaurantName.setText(hotelName);
                        hotelDetailHotelNameTxt.setText(pojo.getName());
                        hotelDetailRatingTxt.setText("" + pojo.getRating());
                        Picasso.get().load(pojo.getImage())
                                .placeholder(R.drawable.ic_placeholder)
                                .into(mainBackdrop);

                        if(pojo.getCredit_accept().equals("1")){
                            txtCreditOffer.setVisibility(View.VISIBLE);
                        }else {
                            txtCreditOffer.setVisibility(View.GONE);
                        }

                        String hotelTypeStr = "";
                        for (int i = 0; i < pojo.getCuisines().size(); i++) {
                            if (i != pojo.getCuisines().size() - 1) {
                                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName() + " • ";
                            } else
                                hotelTypeStr = hotelTypeStr + pojo.getCuisines().get(i).getName();
                        }


                        hotelDetailHotelTypeTxt.setText(hotelTypeStr);
                        if (!pojo.getDiscount().equals("0")) {
                            hotelDetailHotelDiscountTxt.setText(pojo.getDiscount() + "%" + getString(R.string.offer));
                        }
                        hotelDetailTimeTxt.setText(pojo.getTravelTime());
                        hotelDetailAmtTxt.setText(sessionManager.getCurrency() + pojo.getPrice());
                        hotelDetailRatingTxt.setText("" + pojo.getRating());

                        timeTxt.setText(pojo.getTravelTime());
                        amtTxt.setText(pojo.getPrice());
                        ratingTxt.setText("" + pojo.getRating());


                        if (pojo.getIsFavourite() == 1)
                            likeBol = true;
                        else
                            likeBol = false;

                        invalidateOptionsMenu();


                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());


                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<HotelDetailPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonLikeUpdate(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.homeLike(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

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

    @OnClick({R.id.hotel_detail_hotel_name_txt, R.id.hotel_detail_card, R.id.menu_card,
            R.id.cart_card, R.id.coordinator_lay, R.id.view_cart_txt, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hotel_detail_hotel_name_txt:
                break;
            case R.id.hotel_detail_card:
                break;
            case R.id.cart_card:
                break;
            case R.id.view_cart_txt:
                if (sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(HotelDetailActivity.this, ViewCartActivity.class);
                    startActivity(intent);
                } else {
                    loginBottomFragment = new LoginBottomFragment(this, this::onDismissFragment);
                    loginBottomFragment.showNow(getSupportFragmentManager(), "loginFragment");
                }
                break;
            case R.id.coordinator_lay:
//                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_back:
                /*Intent intentBack = new Intent(HotelDetailActivity.this, HomeActivity.class);
                startActivity(intentBack);
                finishAffinity();*/
                finish();
                break;
            case R.id.menu_card:
                Log.e(TAG, "onViewClicked: " + "Clicked");
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchButton:
                setcheckedStatus();
                jsonGetCategory();
                getfoodList();

                Log.e(TAG, "onCheckedChanged: " + CONST.pureVegStr);
                break;
        }

    }

    public void setCollapsing() {

        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public void cartValue(double amt, int qty) {
        Log.e("Giri ", "cartValue: " + qty);
        if (qty > 0) {
            cartCard.setVisibility(View.VISIBLE);
            hotelDetailCartAmtTxt.setText(sessionManager.getCurrency() + amt);
            hotelDetailCartTotalTxt.setText("" + qty);
        } else
            cartCard.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hotel_detail_scroll, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(false);
        menu.setGroupVisible();*/
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        /*MenuItem settingsItem = menu.findItem(R.id.action_like);
        // set your desired icon here based on a flag if you like
        if (likeBol)
            settingsItem.setIcon(getResources().getDrawable(R.drawable.ic_like));
        else
            settingsItem.setIcon(getResources().getDrawable(R.drawable.ic_white_heart));*/

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*Intent intent = new Intent(HotelDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();*/
                finish();
                return true;
            case R.id.action_like:
                likeBol = !likeBol;
                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.restaurant_id, CONST.restaren_id);
                jsonLikeUpdate(map);
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
       /* Intent intent = new Intent(HotelDetailActivity.this, HomeActivity.class);
        startActivity(intent);
        finishAffinity();*/
        finish();

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.e("Giri ", "onTabSelected: " + tab.getPosition());
        Log.e("Giri ", "onTabSelected: " + tab.getTag());

        if (tab.getPosition() == 0) {
            nestedLay.smoothScrollTo(0, 0);
        } else {
            try {
                String tagName = tab.getTag().toString();
                Log.e("Giri ", "onResponse:categoryPos on get " + categoryPos.get(tagName));
                float y = recyclerFoodItems.getY() + recyclerFoodItems.getChildAt(categoryPos.get(tagName) - 1).getY();
                nestedLay.smoothScrollTo(0, (int) y - 1);
            } catch (Exception e) {
                Log.e("Giri ", "onTabSelected:Exception " + e);
            }
        }


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NetworkRefresh(NetworkRefreshModel event) {
        //Refreshing the activity with new selected Video
        Intent intent = new Intent(HotelDetailActivity.this, HotelDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void selectTab(String category_id) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            if (tab.getTag().equals(category_id)) {
                tab.select();
            }
        }
    }

    // adapter connection section------------------------------------------------------------------------------------------------------------------

    private void intialiseRestIdDB() {
        cartDetailsDbList = App.getInstance().getmDaoSession().getCartDetailsDbDao().loadAll();
        if (cartDetailsDbList.size() > 0) {
            restaurant_id = cartDetailsDbList.get(0).getRestaurant_id();
        } else {
            restaurant_id = "";
        }
    }


    @Override
    public void onAmountUpdate() {
        setTotalAmountView();
    }


    @Override
    public void onAddClickCallBack(int position) {
        selectedPosition = position;
        ACTION_TYPE = ADD_DATA;

        if (restaurant_id.isEmpty() || restaurant_id.equals(CONST.restaren_id)) {
            if (finalFoodList.get(position).getAdd_ons().size() > 0) {
                openAddOnFragment(position);
            } else {
                dbStorage.setAddOnData(null, null, restaurantData, finalFoodList.get(selectedPosition), ACTION_TYPE, this);
            }
        } else {
            alertDialogue(this.getString(R.string.clear_cart), position, DIFFERENT_RESTAURANT);
        }

        /*if (finalFoodList.get(position).getAdd_ons().size() > 0) {
            openAddOnFragment(position);
        } else {
            if (restaurant_id.isEmpty() || restaurant_id.equals(CONST.restaren_id)) {
                dbStorage.setAddOnData(null, null, restaurantData, finalFoodList.get(selectedPosition), ACTION_TYPE, this);
            } else {
                alertDialogue(this.getString(R.string.clear_cart), position, DIFFERENT_RESTAURANT);
            }

        }*/

    }

    @Override
    public void onPlusClickCallBack(int position) {
        selectedPosition = position;
        ACTION_TYPE = ADD_DATA;

        if (finalFoodList.get(position).getAdd_ons().size() > 0) {
            int size = dbStorage.isHavingAddOnsInDbQuery(finalFoodList.get(position)).size();
            if (size > 0) {
           /* if(isHavingAddOnsInDbQuery(finalFoodList.get(position))){
               //open a fragment to repeat the last selected AddOn or to select a new Addon by opening the AddOnBottomFragment
            }*/
                //instead of this we need to display a alert to select last added combo or anna new combo
                openAddOnFragment(position);
            } else {
                openAddOnFragment(position);
            }

        } else {
            dbStorage.setAddOnData(null, null, restaurantData, finalFoodList.get(selectedPosition), ACTION_TYPE, this);
        }
    }

    @Override
    public void onMinusClickCallBack(int position) {
        selectedPosition = position;
        ACTION_TYPE = REDUCE_DATA;
        int size = dbStorage.isHavingAddOnsInDbQuery(finalFoodList.get(position)).size();
        if (size >= 2) {
            alertDialogue(getString(R.string.having_different_addons), position, DIFFERENT_ADDON_GROUP);
        } else if (size == 1) {
//            if (finalFoodList.get(position).getAdd_ons().size() > 0) { }
            Log.e(TAG, "onMinusClickCallBack:single Addon group remove ");
            List<GroupDb> groupDbList = dbStorage.getSingleGroupDbQuery(finalFoodList.get(position).getFood_id());
            List<FoodItemDb> foodItemDb = dbStorage.getAddedFoodDetailsQuery(groupDbList.get(0).getFood_id());
            List<QuantityDb> quantityDb = dbStorage.getAddedQuantityDetailsQuery(groupDbList.get(0).getQuantity_id(), groupDbList.get(0).getFood_id());
            dbStorage.setAddOnCartData(groupDbList.get(0), foodItemDb, quantityDb, ACTION_TYPE, this);
        } else {
            Log.e(TAG, "onMinusClickCallBack:direct ");
            dbStorage.setAddOnData(null, null, restaurantData, finalFoodList.get(selectedPosition), ACTION_TYPE, this);
        }

    }


    private void openAddOnFragment(int position) {
        addOnsList = finalFoodList.get(position).getAdd_ons();
        foodQuantityList = finalFoodList.get(position).getFood_quantity();
        Log.e(TAG, "openAddOnFragment:addOnsList " + addOnsList.size());
        addOnBottomFragment = new AddOnBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(CONST.ADDON_DATA, (ArrayList<? extends Parcelable>) addOnsList);
        bundle.putParcelableArrayList(CONST.QUANTITY_DATA, (ArrayList<? extends Parcelable>) foodQuantityList);
        addOnBottomFragment.setArguments(bundle);
        addOnBottomFragment.show(getSupportFragmentManager(), "AddOnBottomFragment");
    }

   /* private void AddFoodToDb(int position) {
        if (restaurant_id.isEmpty() || restaurant_id.equals(CONST.restaren_id)) {
            getFoodDataQuery(finalFoodList.get(position));
            int count = foodItemDb.getFood_qty() + 1;
            foodItemDb.setFood_qty(count);
            setFoodDataQuery(foodItemDb);
        } else {
            alertDialogue(this.getString(R.string.clear_cart), position, DIFFERENT_RESTAURANT);
        }
    }

    private void ReduceFoodInDb(int position) {
        getFoodDataQuery(finalFoodList.get(position));
        if (foodItemDb.getFood_qty() > 1) {
            int count = foodItemDb.getFood_qty() - 1;
            foodItemDb.setFood_qty(count);
            setFoodDataQuery(foodItemDb);
        } else {
            foodItemDb.setFood_qty(0);
            setFoodDataQuery(foodItemDb);
        }
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
        try {
            if (c.moveToFirst()) {
                do {
                    final String rest_id_fin = c.getString(rest_id);

                    if (rest_id_fin.equals(CONST.restaren_id)) {
                        // Read the values of a row in the table using the indexes acquired above
                        final String food_id_fin = c.getString(food_id);

                        final String food_name_fin = c.getString(food_name);
                        final String food_desc_fin = c.getString(food_desc);
                        final int food_cost_fin = Integer.parseInt(c.getString(food_cost));
                        final int food_count_fin = Integer.parseInt(c.getString(food_count));
                        final int food_type_fin = Integer.parseInt(c.getString(food_type));
                        final double food_tax_fin = Double.parseDouble(c.getString(food_tax));
                        result.add(new FoodItemDb(rest_id_fin, food_id_fin, food_count_fin, food_name_fin, food_type_fin, food_cost_fin, food_desc_fin, food_tax_fin));
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
            foodItemDb.setFood_cost(Integer.parseInt(pojo.getPrice()));
            foodItemDb.setFood_id(pojo.getFood_id());
            foodItemDb.setFood_name(pojo.getName());
            foodItemDb.setFood_qty(pojo.getItem_count());
            foodItemDb.setFood_type(pojo.getIs_veg());
            foodItemDb.setFood_desc(pojo.getDescription());
            foodItemDb.setFood_tax(pojo.getItem_tax());
            foodItemDb.setRestaurant_id(CONST.restaren_id);
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

        foodCategoryAdapter.notifyDataChanged();
        setTotalAmountView();
    }*/

    public void alertDialogue(String message, final int position, String alertType) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        if (alertType.equals(DIFFERENT_ADDON_GROUP)) {
            // set title
            alertDialogBuilder.setTitle(getString(R.string.unable_to_remove));
            // set dialog message
            alertDialogBuilder
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.view_cart), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(HotelDetailActivity.this, ViewCartActivity.class));
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        } else if (alertType.equals(DIFFERENT_RESTAURANT)) {
            // set title
            alertDialogBuilder.setTitle(getString(R.string.replace_cart_item));
            // set dialog message
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    App.getInstance().getmDaoSession().getCartDetailsDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getGroupLastAddedDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getQuantityDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getAddOnDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getGroupDbDao().deleteAll();
                    App.getInstance().getmDaoSession().getFoodItemDbDao().deleteAll();
                    setTotalAmountView();
                    intialiseRestIdDB();

                    HotelDetailActivity.this.onAddClickCallBack(position);
                            /*if (finalFoodList.get(position).getAdd_ons().size() > 0) {
                                openAddOnFragment(position);
                            } else {
                                AddFoodToDb(position);
                            }*/

                            /*FinalFoodList pojo = finalFoodList.get(position);
                            getFoodDataQuery(pojo);
                            int count = foodItemDb.getFood_qty() + 1;
                            foodItemDb.setFood_qty(count);
                            setFoodDataQuery(foodItemDb);*/
                    dialog.cancel();
                }
            });
            alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel();
                }
            });
        }

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    @Override
    public void onAddOnComplete(List<AddOns> selectedAddOnsList, FoodQuantity foodQuantity) {
        Log.e(TAG, "onAddOnComplete:selectedAddOnsList " + selectedAddOnsList);
        dbStorage.setAddOnData(selectedAddOnsList, foodQuantity, restaurantData, finalFoodList.get(selectedPosition), ACTION_TYPE, this);
        addOnBottomFragment.dismiss();
    }

    @Override
    public void onDbUpdateAdapterRefresh() {
        foodCategoryAdapter.notifyDataChanged();
        setTotalAmountView();
    }

    @Override
    public void onUpdateCart() {
        //only used when reducing the fooditem with single addon group
        foodCategoryAdapter.notifyDataChanged();
        setTotalAmountView();
    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
        Intent intent = new Intent(HotelDetailActivity.this, ViewCartActivity.class);
        startActivity(intent);
    }
}


package com.speant.user.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.ui.adapter.MenuAdapter;
import com.speant.user.ui.adapter.ViewPagerAdapter;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Models.CategoryPojo;
import com.speant.user.Models.CheckCartPojo;
import com.speant.user.Models.MenuPojo;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelItemsDetailActivity extends BaseActivity {

    private static final String TAG = "ActivityHotelItems";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.menu_card)
    CardView menuCard;
    @BindView(R.id.hotel_detail_cart_amt_txt)
    TextView hotelDetailCartAmtTxt;
    @BindView(R.id.hotel_detail_cart_total_txt)
    TextView hotelDetailCartTotalTxt;
    @BindView(R.id.hotel_detail_card_relative)
    RelativeLayout hotelDetailCardRelative;
    @BindView(R.id.cart_card)
    RelativeLayout cartCard;
    @BindView(R.id.hotel_detail_menu_relative)
    LinearLayout hotelDetailMenuRelative;
    @BindView(R.id.menu_btm_rv)
    RecyclerView menuBtmRv;
    @BindView(R.id.menu_bottom_sheet)
    LinearLayout menuBottomSheet;
    @BindView(R.id.view_cart_txt)
    TextView viewCartTxt;
    private BottomSheetBehavior<LinearLayout> behavior;

    private boolean likeBol = false;
    ViewPagerAdapter viewPagerAdapter;
    List<MenuPojo.Menus> menusList = new ArrayList<>();
    SessionManager sessionManager;
    APIInterface apiInterface;
    private LinearLayoutManager MyLayoutManager;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_items_detail);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        bottomView();

        actionBar();

        MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        menuBtmRv.setLayoutManager(MyLayoutManager);
        menuAdapter = new MenuAdapter(this, menusList, "ItemDetail");
        menuBtmRv.setAdapter(menuAdapter);

        jsonGetCategory();
        jsonCheckCart();
        HashMap<String, String> map = new HashMap<>();
        map.put(CONST.Params.restaurant_id, CONST.restaren_id);
        jsonMenu(map);
    }

    public void bottomView() {

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.MaterialDialogSheet);
        dialog.setContentView(R.layout.activity_hotel_detail);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        behavior = BottomSheetBehavior.from(menuBottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        //if you want the modal to be dismissed when user drags the bottomsheet down
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        menuCard.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        menuCard.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


    }

    public void jsonGetCategory() {

        Call<CategoryPojo> call = apiInterface.getCategory(sessionManager.getHeader(), CONST.restaren_id);
        call.enqueue(new Callback<CategoryPojo>() {
            @Override
            public void onResponse(Call<CategoryPojo> call, Response<CategoryPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                        List<CategoryPojo.Category> categoryList = response.body().getCategory();
                        for (int i = 0; i < categoryList.size(); i++) {
                            Log.e("Giri ", "onResponse: CategoryPojo ");
                            CategoryPojo.Category pojo = categoryList.get(i);
                            adapter.addFrag(pojo.getCategoryId(), pojo.getName());
                        }
                        viewPager.setAdapter(adapter);

                    }

                } else if(response.code() == 401){
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<CategoryPojo> call, Throwable t) {
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

    public void jsonMenu(HashMap<String, String> map) {

        Call<MenuPojo> call = apiInterface.menuList(sessionManager.getHeader(), map);
        call.enqueue(new Callback<MenuPojo>() {
            @Override
            public void onResponse(Call<MenuPojo> call, Response<MenuPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        if (response.body().getMenus().size() > 1)
                            menuCard.setVisibility(View.VISIBLE);
                        else
                            menuCard.setVisibility(View.GONE);

                        menusList.clear();
                        menusList.addAll(response.body().getMenus());
                        menuAdapter.notifyDataSetChanged();

                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.message());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<MenuPojo> call, Throwable t) {

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

                        cartValue(pojo.getAmount(), pojo.getQuantity());

                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }

            }

            @Override
            public void onFailure(Call<CheckCartPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void cartValue(double amt, int qty) {

        if (qty > 0) {
            cartCard.setVisibility(View.VISIBLE);
            hotelDetailCartAmtTxt.setText("र " + amt);
            hotelDetailCartTotalTxt.setText("" + qty);
        } else
            cartCard.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hotel_detail_scroll, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_like:
                likeBol = !likeBol;
                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.restaurant_id, CONST.restaren_id);
                jsonLikeUpdate(map);
                invalidateOptionsMenu();
                return true;
            case R.id.action_search:
                // Set the text color to red
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem likeItem = menu.findItem(R.id.action_like);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // set your desired icon here based on a flag if you like
        if (likeBol)
            likeItem.setIcon(getResources().getDrawable(R.drawable.ic_like));
        else
            likeItem.setIcon(getResources().getDrawable(R.drawable.ic_unlike));

        searchItem.setIcon(getResources().getDrawable(R.drawable.ic_search_black));

        return super.onPrepareOptionsMenu(menu);
    }

    public void actionBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.v_lit_grey)));
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.v_lit_grey));
        }

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

    public void setCollapsing() {

        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    @OnClick({R.id.menu_card, R.id.view_cart_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_card:
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.view_cart_txt:
                Intent intent = new Intent(HotelItemsDetailActivity.this, ViewCartActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else
            super.onBackPressed();
    }
}

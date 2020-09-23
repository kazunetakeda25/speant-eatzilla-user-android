package com.speant.user.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.speant.user.BuildConfig;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.Common.callBacks.RatingRefreshCallBack;
import com.speant.user.Common.callBacks.onFavouritesClick;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.BannerPojo;
import com.speant.user.Models.CurrentOrderPojo;
import com.speant.user.Models.FilterPojo;
import com.speant.user.Models.LatestVersionResponse;
import com.speant.user.Models.NearRestarentPojo;
import com.speant.user.Models.OnLocationFetch;
import com.speant.user.Models.OnRequestIdFetch;
import com.speant.user.Models.PopularBrandsPojo;
import com.speant.user.Models.RelevanceHotelPojo;
import com.speant.user.Models.TodayTrendPojo;
import com.speant.user.Models.TrendFoodList;
import com.speant.user.R;
import com.speant.user.ui.HomeActivity;
import com.speant.user.ui.adapter.HomeFilterAdapter;
import com.speant.user.ui.adapter.HomeOrderDetailAdapter;
import com.speant.user.ui.adapter.HomeRelevanceAdapter;
import com.speant.user.ui.adapter.HomeSlideAdapter;
import com.speant.user.ui.adapter.NearRestarentAdapter;
import com.speant.user.ui.adapter.PopularBrandsAdapter;
import com.speant.user.ui.adapter.RelevanceRestarentHotel;
import com.speant.user.ui.adapter.TodaySpecialAdapter;
import com.speant.user.ui.dialogs.AddressDialog;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.SessionManager.KEY_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements DiscreteScrollView.OnItemChangedListener, onFavouritesClick,
        LoginSuccessCallBack, RatingRefreshCallBack, ValueEventListener{

    private static final String TAG = "HomeFragment";
    Unbinder unbinder;

    @BindView(R.id.filter_img)
    ImageView filterImg;
    @BindView(R.id.image_count)
    LinearLayout imageCount;
    @BindView(R.id.home_undr_min_txt)
    TextView homeUndrMinTxt;
    @BindView(R.id.home_relevence_txt)
    TextView homeRelevenceTxt;
    @BindView(R.id.home_relevence_rv)
    ShimmerRecyclerView homeRelevenceRv;
    @BindView(R.id.home_more_restarent_txt)
    TextView homeMoreRestarentTxt;
    @BindView(R.id.home_restarent_rv)
    ShimmerRecyclerView homeRestarentRv;
    @BindView(R.id.home_delivery_txt)
    TextView homeDeliveryTxt;
    @BindView(R.id.home_delivery_address)
    TextView homeDeliveryAddressTxt;
    @BindView(R.id.lay_home)
    FrameLayout layHome;
    @BindView(R.id.ad_recycler)
    ShimmerRecyclerView adRecycler;
    @BindView(R.id.home_popular_brands_rv)
    ShimmerRecyclerView homePopularBrandsRv;
    @BindView(R.id.order_view_pager)
    ViewPager orderViewPager;
    @BindView(R.id.order_indicator)
    CircleIndicator orderIndicator;
    @BindView(R.id.lay_order_detail)
    LinearLayout layOrderDetail;
    @BindView(R.id.lay_no_service)
    LinearLayout layNoService;
    @BindView(R.id.lay_restaurant)
    LinearLayout layRestaurant;
    @BindView(R.id.nested_lay)
    NestedScrollView nestedLay;
    @BindView(R.id.lay_popular)
    RelativeLayout layPopular;
    @BindView(R.id.home_trend_rv)
    ShimmerRecyclerView homeTrendRv;
    @BindView(R.id.lay_special)
    RelativeLayout laySpecial;
    @BindView(R.id.lay_frag_home)
    LinearLayout layFragHome;
    private List<BannerPojo.Data> data = new ArrayList<>();
    String orderStatus = "-1";
    private InfiniteScrollAdapter infiniteAdapter;
    static TextView mDotsText[];
    private int mDotsCount;
    Activity activity;
    public AlertDialog alertDialog;
    HomeSlideAdapter homeSlideAdapter;
    APIInterface apiInterface, apiService;
    SessionManager sessionManager;
    List<FilterPojo.Data> filterList = new ArrayList<>();
    List<FilterPojo.Data> relevanceList = new ArrayList<>();
    List<NearRestarentPojo.Restaurants> nearRestarentList = new ArrayList<>();
    List<RelevanceHotelPojo.Restaurants> relevanceRestarentList = new ArrayList<>();
    List<PopularBrandsPojo.Data> popularBrandsList = new ArrayList<>();
    NearRestarentAdapter nearRestarentAdapter;
    RelevanceRestarentHotel relevanceRestarentAdapter;
    HomeFilterAdapter homeFilterAdapter;
    HomeRelevanceAdapter homeRelevanceAdapter;
    PopularBrandsAdapter popularBrandsAdapter;
    private LinearLayoutManager MyLayoutManager;
    AddressDialog addressDialog;
    String lat = "", lan = "";
    String imageBaseStr = "";
    String requestId;
    ArrayList<String> filterIdList = new ArrayList<>();
    ArrayList<String> relevanceIdList = new ArrayList<>();
    private LoginBottomFragment loginBottomFragment;
    private List<CurrentOrderPojo.OrderStatus> orderStatusList = new ArrayList<>();
    private HashMap<String, String> filterMap = new HashMap<>();
    private HashMap<String, String> userDetails;
    private DatabaseReference mAddressDatabaseReference;
    private LocationUpdate locationUpdate;
    private int totalPage = 1;
    private boolean isFirstScroll = true;
    TodaySpecialAdapter todaySpecialAdapter;
    List<TrendFoodList> trendFoodLists = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor

    }

    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();
        return fragment;
    }




    /*private void setStatusView(String status_value_str) {

        switch (status_value_str) {
            case CONST.NO_ORDER://No request found
                homeNotifyDeliveryRelative.setVisibility(View.GONE);
                break;
            case CONST.ORDER_CREATED://Delivery boy not get Assigned
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText("ORDER CREATED");
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.RESTAURANT_ACCEPTED:
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText("RESTAURANT ACCEPTED");
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.FOOD_PREPARED:
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText("FOOD BEING PREPARED");
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.DELIVERY_REQUEST_ACCEPTED:
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText("ORDER ASSIGNED");
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
            case CONST.ORDER_COMPLETE:
                homeNotifyDeliveryRelative.setVisibility(View.GONE);
                break;
            default:
                homeNotifyDeliveryRelative.setVisibility(View.VISIBLE);
                trackOrderTxt.setText("TRACK ORDER");
                trackOrderTxt.setVisibility(View.VISIBLE);
                break;
        }

    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
//        EventBus.getDefault().register(this);
        Log.e(TAG, "onCreateView:HomeFragment ");
        activity = getActivity();
        setNestedScrollListener();
        locationUpdate = new LocationUpdate(activity);
        addressDialog = new AddressDialog(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);
        userDetails = sessionManager.getUserDetails();

        MyLayoutManager = new LinearLayoutManager(getContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homeRelevenceRv.setLayoutManager(MyLayoutManager);
        relevanceRestarentAdapter = new RelevanceRestarentHotel(getContext(), relevanceRestarentList, this::favClickOnLogout);
        homeRelevenceRv.setAdapter(relevanceRestarentAdapter);
        homeRelevenceRv.showShimmerAdapter();

        MyLayoutManager = new LinearLayoutManager(getContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homePopularBrandsRv.setLayoutManager(MyLayoutManager);
        homePopularBrandsRv.showShimmerAdapter();

        LinearLayoutManager trendLinear = new LinearLayoutManager(getContext());
        trendLinear.setOrientation(LinearLayoutManager.HORIZONTAL);
        homeTrendRv.setLayoutManager(trendLinear);
        todaySpecialAdapter = new TodaySpecialAdapter(getActivity(), trendFoodLists);
        homeTrendRv.setAdapter(todaySpecialAdapter);
        homeTrendRv.showShimmerAdapter();

        adRecycler.setNestedScrollingEnabled(true);
        LinearLayoutManager adLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adRecycler.setLayoutManager(adLayoutManager);
        homeSlideAdapter = new HomeSlideAdapter(getActivity(), data, imageBaseStr);
        adRecycler.setAdapter(homeSlideAdapter);
        adRecycler.showShimmerAdapter();

        MyLayoutManager = new LinearLayoutManager(getContext());
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRestarentRv.setLayoutManager(MyLayoutManager);
        homeRestarentRv.setNestedScrollingEnabled(false);
        nearRestarentAdapter = new NearRestarentAdapter(activity, nearRestarentList, this::favClickOnLogout);
        nearRestarentAdapter.setHasStableIds(true);
        homeRestarentRv.setAdapter(nearRestarentAdapter);
        homeRestarentRv.showShimmerAdapter();
        return view;
    }

    private void checkLatestVersionApi() {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(CONST.Params.version, String.valueOf(BuildConfig.VERSION_CODE));
//        hashMap.put(CONST.Params.version, String.valueOf(5));
        Call<LatestVersionResponse> call = apiInterface.checkLatestVersion(sessionManager.getHeader() , hashMap);
        call.enqueue(new Callback<LatestVersionResponse>() {
            @Override
            public void onResponse(Call<LatestVersionResponse> call, Response<LatestVersionResponse> response) {
                if(response.code() == 200){
                    if(response.body().isStatus()){
                        setFirebaseListener();
                        jsonGetCurrentOrderStatus();
                    }else{
                        setVersionDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<LatestVersionResponse> call, Throwable t) {

            }
        });

    }

    private void setVersionDialog() {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("Version Update")
                .setMessage("New App  Version is Available.Kindly Update it")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        final String appPackageName = activity.getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finishAffinity();
                    }
                })
                .show();
    }


    @Override
    public void onResume() {
        super.onResume();
        checkLatestVersionApi();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void setFirebaseListener() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    if (mAddressDatabaseReference == null) {
                        userDetails = sessionManager.getUserDetails();
                        mAddressDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(userDetails.get(KEY_USER_ID));
                        mAddressDatabaseReference.addValueEventListener(HomeFragment.this);
                    }
                } else {
                    Log.e(TAG, "setFirebaseListener: locationPermissionCheck");
                    //dialog is set to open to Disable the locationPermissionCheck method call in HomeActivity's OnResume
                    ((HomeActivity) getActivity()).setIsDialogOpen(true);
                    locationUpdate.locationPermissionCheck(true);
                }
            }
        }, 3000);

    }

    //current address firebase listener
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
        if (map != null && map.get(CONST.Params.current_address) != null) {
            Log.e(TAG, "CONST.Params.current_address" + (String) map.get(CONST.Params.current_address));
//            passAddressListToFragment(map);
            onCurrentLocationFetch(new OnLocationFetch(map));
        } else {
            //getCurrentLocation
            ((HomeActivity) getActivity()).setIsDialogOpen(true);
            locationUpdate.locationPermissionCheck(true);
        }
    }

    private void setInvisibilityAnimation(View view) {
        Log.e(TAG, "setInvisibilityAnimation: " + view.getId());
        view.animate()
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.clearAnimation();
                        view.setVisibility(View.GONE);
                    }
                });
    }




    private void setVisibilityAnimation(View view) {
        Log.e(TAG, "setVisibilityAnimation: " + view.getId());
        view.animate()
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.clearAnimation();
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    //current address firebase listener
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
    }


   /* public void discreateSlider() {
        Log.e("Giri ", "discreateSlider: ");
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        Log.e("Nive ", "discreateSlider: " + imageBaseStr);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new HomeSlideAdapter(getActivity(), data, imageBaseStr));
        itemPicker.setAdapter(infiniteAdapter);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        //here we count the number of images we have to know how many dots we need
        mDotsCount = data.size();

        //here we create the dots
        //as you can see the dots are nothing but "."  of large size
        mDotsText = new TextView[mDotsCount];

        //here we set the dots
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i] = new TextView(activity);
            mDotsText[i].setText(".");
            mDotsText[i].setTextSize(45);
            mDotsText[i].setTypeface(null, Typeface.BOLD);
            mDotsText[i].setTextColor(Color.GRAY);
            imageCount.addView(mDotsText[i]);
        }

        if (data.size() > 0)
            onItemChanged(data.get(0));


    }*/

    private void onItemChanged(BannerPojo.Data bannerPojo) {

        for (int i = 0; i < mDotsCount; i++) {
            HomeFragment.mDotsText[i]
                    .setTextColor(Color.GRAY);
        }

        HomeFragment.mDotsText[bannerPojo.getPosition() - 1]
                .setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int position) {
        int positionInDataSet = infiniteAdapter.getRealPosition(position);
        onItemChanged(data.get(positionInDataSet));
    }

    public void alertFilter() {
        Log.e("Giri ", "alertFilter: ");
        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogSlideAnim_filter);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.alert_filter, null);
        // Set above view in alert dialog.
        builder.setView(view);

        ImageView filter_close_img = view.findViewById(R.id.filter_close_img);
        TextView filter_reset_txt = view.findViewById(R.id.filter_reset_txt);
        TextView apply_filter_txt = view.findViewById(R.id.apply_filter_txt);
        final CheckBox offers_filter_chk = view.findViewById(R.id.offers_filter_chk);
        final CheckBox veg_filter_chk = view.findViewById(R.id.veg_filter_chk);
        RecyclerView home_filter_rv = view.findViewById(R.id.home_filter_rv);

        if (CONST.offerStr.equalsIgnoreCase("1"))
            offers_filter_chk.setChecked(true);
        else
            offers_filter_chk.setChecked(false);

        if (CONST.pureVegStr.equalsIgnoreCase("1"))
            veg_filter_chk.setChecked(true);
        else
            veg_filter_chk.setChecked(false);

        MyLayoutManager = new LinearLayoutManager(getContext());
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homeFilterAdapter = new HomeFilterAdapter(getContext(), filterList);
        home_filter_rv.setAdapter(homeFilterAdapter);
        home_filter_rv.setLayoutManager(MyLayoutManager);

        if (CONST.filterPojo == null || CONST.filterPojo.size() < 1) {
            jsonFilter("1");
        } else {
            filterList.clear();
            List<FilterPojo.Data> list = CONST.filterPojo;
            filterList.addAll(list);
            homeFilterAdapter.notifyDataChanged();
        }

        filter_close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        filter_reset_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                offers_filter_chk.setChecked(false);
                veg_filter_chk.setChecked(false);

                for (int i = 0; i < filterList.size(); i++) {
                    filterList.get(i).setIsSelect(false);
                }
                CONST.filterPojo.clear();
                CONST.filterPojo.addAll(filterList);
                homeFilterAdapter.notifyDataChanged();

            }
        });

        apply_filter_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (veg_filter_chk.isChecked())
                    CONST.pureVegStr = "1";
                else
                    CONST.pureVegStr = "0";

                if (offers_filter_chk.isChecked())
                    CONST.offerStr = "1";
                else
                    CONST.offerStr = "0";

                totalPage = 1;
                nearRestarentList.clear();
                trendFoodLists.clear();
                popularBrandsList.clear();
                data.clear();

                setInvisibilityAnimation(layNoService);
                setVisibilityAnimation(layRestaurant);

                setData();
                alertDialog.cancel();
            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void alertRelevance() {
        Log.e("Giri ", "alertRelevance: ");
        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.alert_relevance, null);
        // Set above view in alert dialog.
        builder.setView(view);

        RecyclerView home_filter_rv = view.findViewById(R.id.home_relevance_rv);
        Button home_relevance_apply_btn = view.findViewById(R.id.home_relevance_apply_btn);

        MyLayoutManager = new LinearLayoutManager(getContext());
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_filter_rv.setLayoutManager(MyLayoutManager);
        homeRelevanceAdapter = new HomeRelevanceAdapter(getContext(), relevanceList);
        home_filter_rv.setAdapter(homeRelevanceAdapter);

        if (CONST.relevancePojo == null || CONST.relevancePojo.size() < 1) {
            jsonFilter("2");
        } else {
            relevanceList.clear();
            relevanceList.addAll(CONST.relevancePojo);
            homeRelevanceAdapter.notifyDataChanged();
        }

        home_relevance_apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeRelevanceAdapter.confirmFilter();
                totalPage = 1;
                nearRestarentList.clear();
                trendFoodLists.clear();
                popularBrandsList.clear();
                data.clear();
                setData();
                alertDialog.cancel();
            }
        });


        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        alertDialog.getWindow().setGravity(Gravity.TOP);
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        alertDialog.show();

    }


    @OnClick({R.id.filter_img, R.id.home_undr_min_txt, R.id.home_relevence_txt,
            R.id.home_more_restarent_txt, R.id.home_delivery_txt, R.id.home_delivery_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_delivery_txt:
                if (sessionManager.isLoggedIn()) {
                    addressDialog.showAddressDialog();
                } else {
                    openLoginFragment();
                }
                break;
            case R.id.home_delivery_address:
                if (sessionManager.isLoggedIn()) {
                    addressDialog.showAddressDialog();
                } else {
                    openLoginFragment();
                }
                break;
            case R.id.filter_img:
                alertFilter();
                break;
            case R.id.home_undr_min_txt:
                break;
            /*case R.id.track_order_txt:
                Intent intent = new Intent(activity, TrackingActivity.class);
                intent.putExtra(CONST.REQUEST_ID, requestId);
                activity.startActivity(intent);
                break;*/
            case R.id.home_relevence_txt:
                alertRelevance();
                break;
            case R.id.home_more_restarent_txt:
                break;
        }
    }

    private void openLoginFragment() {
        loginBottomFragment = new LoginBottomFragment(activity, this::onDismissFragment);
        loginBottomFragment.showNow(getFragmentManager(), "loginFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe(sticky = false,threadMode = ThreadMode.MAIN)
    public void onCurrentLocationFetch(OnLocationFetch onLocationFetch) {
//        EventBus.getDefault().unregister(this);
        HashMap<String, Object> addresses = onLocationFetch.getIntentHashMap();
        Log.e(TAG, "onCurrentLocationFetch: " + onLocationFetch.getIntentHashMap().get(CONST.CURRENT_LATITUDE));
        try {

            Log.e("Giri ", "passAddressListToFragment: " + addresses.get(CONST.CURRENT_ADDRESS).toString());
            Log.e("Giri ", "passAddressListToFragment: " + homeDeliveryAddressTxt.getText().toString());

            lat = String.valueOf((addresses.get(CONST.CURRENT_LATITUDE)));
            lan = String.valueOf((addresses.get(CONST.CURRENT_LONGITUDE)));

            //to stop calling service continuously
            Log.e(TAG, "onCurrentLocationFetch:homeDeliveryAddressTxt " + homeDeliveryAddressTxt.getHint().toString());

            //this total page check is done to check if the list with same page number not added multiple times
            if (!homeDeliveryAddressTxt.getText().toString().equals(addresses.get(CONST.CURRENT_ADDRESS))) {
                Log.e(TAG, "onCurrentLocationFetch:totalPage "+totalPage );
                nearRestarentList.clear();
                trendFoodLists.clear();
                popularBrandsList.clear();
                data.clear();

                if (nearRestarentList.size() == 0) {
                    homeDeliveryAddressTxt.setText(addresses.get(CONST.CURRENT_ADDRESS).toString());
                    totalPage = 1;
                    setData();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "passAddressToFragment: Error " + e.getMessage());
        }

    }

    @Subscribe(sticky = true , threadMode = ThreadMode.MAIN)
    public void onRequestIdFetch(OnRequestIdFetch onRequestIdFetch) {
        Log.e(TAG, "onRequestIdFetch: " + onRequestIdFetch.getRequestId());
        String requestIdStr = onRequestIdFetch.getRequestId();
        String statusStr = onRequestIdFetch.getStatus();
        try {
            Log.e(TAG, "passRequestIdToFragment: " + requestIdStr + "\t" + statusStr);

            if (statusStr != null) {

                if (!statusStr.equals(CONST.NO_ORDER) && !orderStatus.equals(statusStr)) {
                    orderStatus = statusStr;
                    Log.e(TAG, "passRequestIdToFragment: Order Status");
                    jsonGetCurrentOrderStatus();
                }
            }

            if (statusStr == null) {
                layOrderDetail.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Giri ", "passRequestIdToFragment:Exception " + e);
        }

    }
   /* @Override
    public void passRequestIdToFragment(String requestIdStr, String statusStr) {
        try {

            Log.e(TAG, "passRequestIdToFragment: " + requestIdStr + "\t" + statusStr);

            if (statusStr != null) {

                if (!statusStr.equals(CONST.NO_ORDER) && !orderStatus.equals(statusStr)) {
                    orderStatus = statusStr;
                    Log.e(TAG, "passRequestIdToFragment: Order Status");
                    jsonGetCurrentOrderStatus();
                }
            }

            if (statusStr == null) {
                layOrderDetail.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Giri ", "passRequestIdToFragment:Exception " + e);
        }
    }*/

    /*@Override
    public void passAddressListToFragment(HashMap<String, Object> addresses) {
        try {

            Log.e("Giri ", "passAddressListToFragment: " + addresses.get(CONST.CURRENT_ADDRESS).toString());
            homeDeliveryAddressTxt.setText(addresses.get(CONST.CURRENT_ADDRESS).toString());
            Log.e("Giri ", "passAddressListToFragment: " + homeDeliveryAddressTxt.getText().toString());

            lat = String.valueOf((addresses.get(CONST.CURRENT_LATITUDE)));
            lan = String.valueOf((addresses.get(CONST.CURRENT_LONGITUDE)));

            setData();

        } catch (Exception e) {
            Log.e(TAG, "passAddressToFragment: Error " + e.getMessage());
        }

    }*/


    private void setData() {
        Log.e(TAG, "setData:call ");
        Log.e(TAG, "setData:data "+data.size() );
        Log.e(TAG, "setData:nearRestarentList "+nearRestarentList.size());
        Log.e(TAG, "setData:trendFoodLists "+trendFoodLists.size());
        Log.e(TAG, "setData:popularBrandsList "+popularBrandsList.size());

        homeRestarentRv.showShimmerAdapter();
        adRecycler.showShimmerAdapter();
        homePopularBrandsRv.showShimmerAdapter();
        homeRelevenceRv.showShimmerAdapter();
        homeTrendRv.showShimmerAdapter();

        setFilterValues();
        getRelevanceRestaurants();
//        getTodayTrend();
        jsonPopularBrands();

        /*layNoService.setVisibility(View.GONE);
        layRestaurant.setVisibility(View.VISIBLE);*/

        /*setInvisibilityAnimation(layNoService);
        setVisibilityAnimation(layRestaurant);*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jsonHomeSlider();
                getNearbyRestaurants();
            }
        }, 2000);

    }

    private void getTodayTrend() {
        HashMap<String, String> locationMap = new HashMap<>();
        locationMap.put("lat", String.valueOf(lat));
        locationMap.put("lng", String.valueOf(lan));

        Log.e("Giri ", "getTodayTrend: ");
        Call<TodayTrendPojo> call = apiInterface.getTrendToday(sessionManager.getHeader(), locationMap);
        call.enqueue(new Callback<TodayTrendPojo>() {
            @Override
            public void onResponse(Call<TodayTrendPojo> call, Response<TodayTrendPojo> response) {

                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        trendFoodLists.addAll(response.body().getFood_list());
                        if(trendFoodLists.size() <= 0){
                            laySpecial.setVisibility(View.GONE);
                        }
                        todaySpecialAdapter.notifyDataSetChanged();
                    }else{
                        laySpecial.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(Call<TodayTrendPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    public void getRelevanceRestaurants() {
        HashMap<String, String> locationMap = new HashMap<>();
        locationMap.put("lat", String.valueOf(lat));
        locationMap.put("lng", String.valueOf(lan));
        jsonRelevanceRestarent(filterMap, filterIdList, relevanceIdList, locationMap);
    }

    private void getNearbyRestaurants() {
        HashMap<String, String> locationMap = new HashMap<>();
        locationMap.put("lat", String.valueOf(lat));
        locationMap.put("lng", String.valueOf(lan));
        jsonNearRestaurant(filterMap, filterIdList, relevanceIdList, locationMap);
    }

    private void setFilterValues() {

        filterIdList.clear();
        relevanceIdList.clear();

        if (CONST.filterPojo != null && CONST.filterPojo.size() > 0) {
            for (int m = 0; m < CONST.filterPojo.size(); m++) {
                if (CONST.filterPojo.get(m).getIsSelect()) {
                    filterIdList.add(String.valueOf(CONST.filterPojo.get(m).getId()));
                }
            }
        }

        if (CONST.relevancePojo != null && CONST.relevancePojo.size() > 0) {
            for (int n = 0; n < CONST.relevancePojo.size(); n++) {
                if (CONST.relevancePojo.get(n).getIsSelect()) {
                    relevanceIdList.add(String.valueOf(CONST.relevancePojo.get(n).getId()));
                }
            }
        }

        Log.e(TAG, "filterIdList: " + filterIdList);
        Log.e(TAG, "relevanceIdList: " + relevanceIdList);

        filterMap.put("is_pureveg", CONST.pureVegStr);
        filterMap.put("is_offer", CONST.offerStr);

        /*filterMap.put("pureveg", CONST.pureVegStr);
        filterMap.put("offer", CONST.offerStr);*/
    }


    public void jsonFilter(final String type) {
        Log.e("Giri ", "jsonFilter: ");

        Call<FilterPojo> call = apiInterface.getFilter(type, sessionManager.getHeader());
        call.enqueue(new Callback<FilterPojo>() {
            @Override
            public void onResponse(Call<FilterPojo> call, Response<FilterPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        if (type == "1") {
                            CONST.filterPojo.clear();
                            CONST.filterPojo = response.body().getData();
                            filterList.clear();
                            filterList.addAll(response.body().getData());
                            homeFilterAdapter.notifyDataChanged();
                        }
                        /*else if (type == "2") {
                            CONST.relevancePojo.clear();
                            CONST.relevancePojo = response.body().getData();
                            relevanceList.clear();
                            relevanceList.addAll(CONST.relevancePojo);
                            homeRelevanceAdapter.notifyDataChanged();
                        }*/

                    } else
                        CommonFunctions.shortToast(activity, response.body().getErrorMessage());

                }


            }

            @Override
            public void onFailure(Call<FilterPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonNearRestaurant(HashMap<String, String> mapString, ArrayList<String> filterList, ArrayList<String> relevanceIdList, HashMap<String, String> locationMap) {
        Log.e("Giri ", "jsonNearRestaurant: ");
        Call<NearRestarentPojo> call = apiInterface.getNearRestarent(sessionManager.getHeader(), mapString, filterList, relevanceIdList, locationMap, "" + totalPage);
        call.enqueue(new Callback<NearRestarentPojo>() {
            @Override
            public void onResponse(Call<NearRestarentPojo> call, Response<NearRestarentPojo> response) {
                if (response.code() == 200) {

                    Log.e(TAG, "onResponse: " + response.body().getStatus());
//                    nearRestarentList.clear();
                    if (response.body().getStatus()) {
                        /*layNoService.setVisibility(View.GONE);
                        layRestaurant.setVisibility(View.VISIBLE);*/
                        setInvisibilityAnimation(layNoService);
                        setVisibilityAnimation(layRestaurant);
                        nearRestarentList.addAll(response.body().getRestaurants());
                        Log.e(TAG, "onResponse: " + nearRestarentList.size());
                        nearRestarentAdapter.notifyDataChanged();
                        homeRestarentRv.hideShimmerAdapter();
                        adRecycler.hideShimmerAdapter();
                        homePopularBrandsRv.hideShimmerAdapter();
                        homeRelevenceRv.hideShimmerAdapter();
                        homeTrendRv.hideShimmerAdapter();
                    } else if (nearRestarentList.size() <= 0) {
                       /* layNoService.setVisibility(View.VISIBLE);
                        layRestaurant.setVisibility(View.GONE);*/
                        setInvisibilityAnimation(layRestaurant);
                        setVisibilityAnimation(layNoService);
                    }

                }

            }

            @Override
            public void onFailure(Call<NearRestarentPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonRelevanceRestarent(HashMap<String, String> mapString, ArrayList<String> filterList, ArrayList<String> relevanceIdList, HashMap<String, String> locationMap) {
        Log.e("Giri ", "jsonRelevanceRestarent: ");
        Call<RelevanceHotelPojo> call = apiInterface.getRelevance(sessionManager.getHeader(), mapString, filterList, relevanceIdList, locationMap);
        call.enqueue(new Callback<RelevanceHotelPojo>() {
            @Override
            public void onResponse(Call<RelevanceHotelPojo> call, Response<RelevanceHotelPojo> response) {
                if (response.code() == 200) {
                    relevanceRestarentList.clear();
                    if (response.body().getStatus()) {
                        relevanceRestarentList.addAll(response.body().getRestaurants());
                        Log.e(TAG, "onResponse: Relevance  " + relevanceRestarentList);
                        homeRelevenceRv.setVisibility(View.VISIBLE);
                        relevanceRestarentAdapter.notifyDataChanged();

                    }

                }

            }

            @Override
            public void onFailure(Call<RelevanceHotelPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonHomeSlider() {
        Log.e("Giri ", "jsonHomeSlider: ");
        Call<BannerPojo> call = apiInterface.getBanners(sessionManager.getHeader());
        call.enqueue(new Callback<BannerPojo>() {
            @Override
            public void onResponse(Call<BannerPojo> call, Response<BannerPojo> response) {

                if (response.code() == 200) {
                    try {
                        if (response.body().getStatus()) {

                            data.addAll(response.body().getData());
                            Log.e("Nive ", "onResponse:data " + data.size());
                            if (data.size() > 0) {
                                imageBaseStr = response.body().getBaseUrl();
                            }
                            homeSlideAdapter = new HomeSlideAdapter(getActivity(), data, imageBaseStr);
                            adRecycler.setAdapter(homeSlideAdapter);

                            if (adRecycler.getAdapter() != null) {
                                adRecycler.getAdapter().notifyDataSetChanged();
                            }


//                            discreateSlider();//Discreate Slider Scroll

                        }
                    } catch (Exception e) {
                        Log.e("Giri ", "onResponse:HomeSlider Exception " + e);

                    }

                }

            }

            @Override
            public void onFailure(Call<BannerPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonPopularBrands() {
        HashMap<String, String> locationMap = new HashMap<>();
        locationMap.put("lat", String.valueOf(lat));
        locationMap.put("lng", String.valueOf(lan));

        Log.e("Giri ", "jsonPopularBrands: ");
        Call<PopularBrandsPojo> call = apiInterface.getPopularBrands(sessionManager.getHeader(), locationMap);
        call.enqueue(new Callback<PopularBrandsPojo>() {
            @Override
            public void onResponse(Call<PopularBrandsPojo> call, Response<PopularBrandsPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        try {
                            if (response.body().getData() != null) {
                                layPopular.setVisibility(View.VISIBLE);
                                popularBrandsList.addAll(response.body().getData());
                                popularBrandsAdapter = new PopularBrandsAdapter(getContext(), popularBrandsList, response.body().getBaseUrl());
                                homePopularBrandsRv.setAdapter(popularBrandsAdapter);
                            }
                        } catch (Exception e) {
                            Log.e("Giri ", "onResponse:PopularBrands Exception " + e);
                        }

                    } else {
                        layPopular.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(Call<PopularBrandsPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void relevanceRecalculate() {
        Log.e("Giri ", "relevanceRecalculate: ");
        HashMap<String, String> mapStr = new HashMap<>();

        filterIdList.clear();
        relevanceIdList.clear();

        if (CONST.filterPojo != null && CONST.filterPojo.size() > 0) {
            for (int m = 0; m < CONST.filterPojo.size(); m++) {
                if (CONST.filterPojo.get(m).getIsSelect()) {
                    filterIdList.add(String.valueOf(CONST.filterPojo.get(m).getId()));
                }
            }
        }

        if (CONST.relevancePojo != null && CONST.relevancePojo.size() > 0) {
            for (int n = 0; n < CONST.relevancePojo.size(); n++) {
                if (CONST.relevancePojo.get(n).getIsSelect()) {
                    relevanceIdList.add(String.valueOf(CONST.relevancePojo.get(n).getId()));
                }
            }
        }

        Log.e(TAG, "filterIdList: " + filterIdList);
        Log.e(TAG, "relevanceIdList: " + relevanceIdList);

        mapStr.put("is_pureveg", CONST.pureVegStr);
        mapStr.put("is_offer", CONST.offerStr);

//       getRelevanceRestaurants();

    }


    public void jsonGetCurrentOrderStatus() {
        Log.e("Giri ", "jsonGetCurrentOrderStatus: ");
        Call<CurrentOrderPojo> call = apiInterface.getCurrentOrderStatus(sessionManager.getHeader());
        call.enqueue(new Callback<CurrentOrderPojo>() {
            @Override
            public void onResponse(Call<CurrentOrderPojo> call, Response<CurrentOrderPojo> response) {
                try {
                    if (response.code() == 200) {

                        if (response.body().getStatus()) {
                            orderStatusList.clear();
                            orderStatusList.addAll(response.body().getOrderStatus());
                            if (orderStatusList.size() > 0) {
                                Log.e(TAG, "onResponse:orderStatusList.size() " + orderStatusList.size());
                                layOrderDetail.setVisibility(View.VISIBLE);
                            }

                            orderViewPager.setAdapter(new HomeOrderDetailAdapter(activity, orderStatusList, HomeFragment.this::onRatingRefresh));
                            orderIndicator.setViewPager(orderViewPager);

                            CurrentOrderPojo.OrderStatus pojo = response.body().getOrderStatus().get(0);
                            requestId = "" + pojo.getRequestId();

                            /*Log.e("Giri ", "onResponse:status" + pojo.getStatus());
                            setStatusView("" + pojo.getStatus());

                            if (homeNotifyDeliveryRelative.getVisibility() == View.VISIBLE) {
                                homeNotifyHotelNameTxt.setText(pojo.getRestaurantName());
                                homeNotifyItemTxt.setText("" + pojo.getItemCount() + "\tItem");
                                homeNotifyAmountTxt.setText(sessionManager.getCurrency() + "\t" + pojo.getBillAmount());


                                DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                                Date date = null;

                                String inputText = pojo.getOrderedTime();
                                date = inputFormat.parse(inputText);
                                homeNotifyTimeTxt.setText(outputFormat.format(date));
                            }*/


                        } else {
//                        CommonFunctions.shortToast(activity, response.body().getMessage());
                            layOrderDetail.setVisibility(View.GONE);
                        }
                    } else if (response.code() == 401) {
                        sessionManager.logoutUser();
                    } else {
                        layOrderDetail.setVisibility(View.GONE);
                    }
                    layHome.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Log.e("Giri ", "onResponse: GetCurrentOrderStatus Exception " + e);
                }

            }

            @Override
            public void onFailure(Call<CurrentOrderPojo> call, Throwable t) {
                try {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    layOrderDetail.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Giri ", "onDestroy:HomeFragment ");

    }

    @Override
    public void favClickOnLogout() {
        openLoginFragment();
    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
        jsonGetCurrentOrderStatus();
        setFirebaseListener();
    }

    @Override
    public void onRatingRefresh() {
        jsonGetCurrentOrderStatus();
    }

    private void setNestedScrollListener() {
        nestedLay.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedLay.getChildAt(nestedLay.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedLay.getHeight() + nestedLay.getScrollY()));

                if (diff == 0) {
                    Log.e(TAG, "onScrollChanged: End");
                    if (nearRestarentAdapter.getFinalBindPosition() == nearRestarentList.size() - 1) {
                        Log.e(TAG, "onScrollChanged: FinalBind");
                        totalPage += 1;
                        getNearbyRestaurants();
                    }

                }

            }
        });
    }

    /*@Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        ((HomeActivity) getActivity()).homeFragmentCommunicator = this;
    }

    @Override
    public void passAddressToFragment(OnLocationFetch onLocationFetch) {
        HashMap<String, Object> addresses = onLocationFetch.getIntentHashMap();
        Log.e(TAG, "onCurrentLocationFetch: " + onLocationFetch.getIntentHashMap().get(CONST.CURRENT_LATITUDE));
        try {

            Log.e("Giri ", "passAddressListToFragment: " + addresses.get(CONST.CURRENT_ADDRESS).toString());
            Log.e("Giri ", "passAddressListToFragment: " + homeDeliveryAddressTxt.getText().toString());

            lat = String.valueOf((addresses.get(CONST.CURRENT_LATITUDE)));
            lan = String.valueOf((addresses.get(CONST.CURRENT_LONGITUDE)));

            //to stop calling service continuously
            Log.e(TAG, "onCurrentLocationFetch:homeDeliveryAddressTxt "+homeDeliveryAddressTxt.getHint().toString());
            nearRestarentList.clear();
            trendFoodLists.clear();
            popularBrandsList.clear();
            data.clear();

            if(nearRestarentList.size() == 0){
                homeDeliveryAddressTxt.setText(addresses.get(CONST.CURRENT_ADDRESS).toString());
                totalPage = 1;
                setData();
            }

        } catch (Exception e) {
            Log.e(TAG, "passAddressToFragment: Error " + e.getMessage());
        }
    }

    @Override
    public void passRequestIdToFragment(OnRequestIdFetch onRequestIdFetch) {
        Log.e(TAG, "onRequestIdFetch: " + onRequestIdFetch.getRequestId());
        String requestIdStr = onRequestIdFetch.getRequestId();
        String statusStr = onRequestIdFetch.getStatus();
        try {
            Log.e(TAG, "passRequestIdToFragment: " + requestIdStr + "\t" + statusStr);

            if (statusStr != null) {

                if (!statusStr.equals(CONST.NO_ORDER) && !orderStatus.equals(statusStr)) {
                    orderStatus = statusStr;
                    Log.e(TAG, "passRequestIdToFragment: Order Status");
                    jsonGetCurrentOrderStatus();
                }
            }

            if (statusStr == null) {
                layOrderDetail.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("Giri ", "passRequestIdToFragment:Exception " + e);
        }
    }*/
}

package com.speant.user.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.DeliveryTypeCallback;
import com.speant.user.Common.callBacks.DiningRestaurantClick;
import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.Common.callBacks.onFavouritesClick;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.NearRestarentPojo;
import com.speant.user.Models.OnLocationFetch;
import com.speant.user.Models.SuccessPojo;
import com.speant.user.R;
import com.speant.user.ui.HomeActivity;
import com.speant.user.ui.adapter.NearGORestarentAdapter;
import com.speant.user.ui.dialogs.DiningDialog;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.SessionManager.KEY_USER_ID;
import static io.fabric.sdk.android.Fabric.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoOutFragment extends Fragment implements onFavouritesClick, LoginSuccessCallBack, ValueEventListener, DiningRestaurantClick, DeliveryTypeCallback {


    @BindView(R.id.home_restarent_rv)
    ShimmerRecyclerView homeRestarentRv;
    @BindView(R.id.nested_lay)
    NestedScrollView nestedLay;
    @BindView(R.id.lay_restaurant)
    RelativeLayout layRestaurant;
    @BindView(R.id.lay_no_service)
    LinearLayout layNoService;
    private APIInterface apiInterface;
    private APIInterface apiService;
    private SessionManager sessionManager;
    private LinearLayoutManager MyLayoutManager;
    private NearGORestarentAdapter nearRestarentAdapter;
    List<NearRestarentPojo.Restaurants> nearRestarentList = new ArrayList<>();
    private LoginBottomFragment loginBottomFragment;
    private int totalPage = 1;
    HashMap<String, String> locationMap;
    private DatabaseReference mAddressDatabaseReference;
    private HashMap<String, String> userDetails;
    private String lat;
    private String lan;
    private Activity activity;
    private LocationUpdate locationUpdate;
    private List<String> deliveryTypeList = new ArrayList<>();
    NearRestarentPojo.Restaurants restaurants;
    private DiningDialog diningDialog;

    public GoOutFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        GoOutFragment goOutFragment = new GoOutFragment();
        return goOutFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_out, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        locationUpdate = new LocationUpdate(activity);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);
        setNestedScrollListener();

        MyLayoutManager = new LinearLayoutManager(getContext());
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRestarentRv.setLayoutManager(MyLayoutManager);
        homeRestarentRv.setNestedScrollingEnabled(false);
        nearRestarentAdapter = new NearGORestarentAdapter(activity, nearRestarentList, this::favClickOnLogout, this::onDiningRestaurantClick);
        homeRestarentRv.setAdapter(nearRestarentAdapter);
        homeRestarentRv.showShimmerAdapter();
        setFirebaseListener();

        return view;
    }

    private void setFirebaseListener() {
        if (sessionManager.isLoggedIn()) {
            if (mAddressDatabaseReference == null) {
                userDetails = sessionManager.getUserDetails();
                mAddressDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_address).child(userDetails.get(KEY_USER_ID));
                mAddressDatabaseReference.addValueEventListener(this);
            }
        } else {
            Log.e(TAG, "setFirebaseListener: locationPermissionCheck");
            //dialog is set to open to Disable the locationPermissionCheck method call in HomeActivity's OnResume
            ((HomeActivity) getActivity()).setIsDialogOpen(true);
            locationUpdate.locationPermissionCheck(true);
        }
    }

    //current address firebase listener
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
        if (map != null && map.get(CONST.Params.current_address) != null) {
            Log.e(TAG, "CONST.Params.current_address" + (String) map.get(CONST.Params.current_address));
            lat = String.valueOf((map.get(CONST.CURRENT_LATITUDE)));
            lan = String.valueOf((map.get(CONST.CURRENT_LONGITUDE)));
            jsonNearRestaurant();
        } else {
            //getCurrentLocation
            ((HomeActivity) getActivity()).setIsDialogOpen(true);
            locationUpdate.locationPermissionCheck(true);
        }
    }

    //current address firebase listener
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrentLocationFetch(OnLocationFetch onLocationFetch) {
        Log.e(TAG, "onCurrentLocationFetch: " + onLocationFetch);
        lat = String.valueOf((onLocationFetch.getIntentHashMap().get(CONST.CURRENT_LATITUDE)));
        lan = String.valueOf((onLocationFetch.getIntentHashMap().get(CONST.CURRENT_LONGITUDE)));
        jsonNearRestaurant();
    }


    public void jsonNearRestaurant() {
        locationMap = new HashMap<>();
        locationMap.put("lat", String.valueOf(lat));
        locationMap.put("lng", String.valueOf(lan));
        Log.e("Giri ", "jsonNearRestaurant: ");
        Call<NearRestarentPojo> call = apiInterface.getNearGoRestarent(sessionManager.getHeader(), locationMap, "" + totalPage);
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

    @Override
    public void favClickOnLogout() {
        openLoginFragment();
    }

    private void openLoginFragment() {
        loginBottomFragment = new LoginBottomFragment(activity, this::onDismissFragment);
        loginBottomFragment.showNow(getFragmentManager(), "loginFragment");
    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
        setFirebaseListener();
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
                        jsonNearRestaurant();
                    }

                }

            }
        });
    }

    @Override
    public void onDiningRestaurantClick(NearRestarentPojo.Restaurants restaurants) {
        this.restaurants = restaurants;
        deliveryTypeList.clear();
        deliveryTypeList.add("3");
        diningDialog = new DiningDialog(activity,restaurants,deliveryTypeList,DINING,this);
    }


    @Override
    public void OnDeliveryTypeConfirm(String deliveryType, String members, String time) {
        redirectToPayment(deliveryType,members,time);
        diningDialog.dismissDialog();
    }

    @Override
    public void OnDeliveryTypeCancel() {

    }

    private void redirectToPayment(String deliveryType, String members, String time) {

        Call<SuccessPojo> call = apiInterface.finalGoPayment(sessionManager.getHeader(), "" + restaurants.getId(), deliveryType, time, members);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        CommonFunctions.shortToast(activity, "Dining Request Sent to Restaurant");
                    } else
                        CommonFunctions.shortToast(activity, response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(activity, "Unauthorised");
                }
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
            }
        });
    }

}

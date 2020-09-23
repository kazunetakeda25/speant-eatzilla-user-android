package com.speant.user.ui.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Models.OnLocationFetch;
import com.speant.user.ui.HomeActivity;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.speant.user.Common.CONST;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.SearchRestaurantResponse;
import com.speant.user.R;
import com.speant.user.ui.adapter.SearchViewPagerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.SessionManager.KEY_USER_ID;
import static io.fabric.sdk.android.Fabric.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends androidx.fragment.app.Fragment implements ValueEventListener {

    Unbinder unbinder;
    @BindView(R.id.titleTabs)
    TabLayout titleTabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.edt_search)
    AppCompatEditText edtSearch;
    @BindView(R.id.lay_viewpager)
    RelativeLayout layViewpager;
    @BindView(R.id.lay_noresult)
    LinearLayout layNoresult;
    @BindView(R.id.img_search)
    AppCompatImageView imgSearch;
    @BindView(R.id.img_close)
    AppCompatImageView imgClose;
    private APIInterface apiInterface;
    SessionManager sessionManager;
    private SearchViewPagerAdapter viewPagerAdapter;
    private SearchedRestaurantFragment searchRestaurantFragment;
    List<SearchRestaurantResponse.SearchRestaurants> searchRestaurantsList = new ArrayList<>();
    private DatabaseReference mAddressDatabaseReference;
    private HashMap<String, String> userDetails;
    private String lat;
    private String lan;
    private Activity activity;
    private LocationUpdate locationUpdate;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(getActivity());
        userDetails = sessionManager.getUserDetails();
        locationUpdate = new LocationUpdate(activity);
        titleTabs.setupWithViewPager(viewPager);
        setTextChangeListener();
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
        }else {
            //getCurrentLocation
            ((HomeActivity) getActivity()).setIsDialogOpen(true);
            locationUpdate.locationPermissionCheck(true);
        }
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


    //current address firebase listener
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
    }

    private void initViewPager() {
        if (viewPager != null) {
            if (viewPager.getAdapter() == null) {
                Log.e("viewPager", "initViewPager:null ");
                viewPagerAdapter = new SearchViewPagerAdapter(getActivity().getSupportFragmentManager());

                searchRestaurantFragment = new SearchedRestaurantFragment();
                searchRestaurantFragment.setArguments(setBundle());

                viewPagerAdapter.addFragment(searchRestaurantFragment, "Restaurant");
                viewPager.setAdapter(viewPagerAdapter);
            } else {
                Log.e("viewPager", "initViewPager:notNull ");
                searchRestaurantFragment.setArguments(setBundle());
                searchRestaurantFragment.getBundle();
                viewPager.getAdapter().notifyDataSetChanged();
            }
        }

    }

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CONST.SEARCHED_RESTAURANT, (Serializable) searchRestaurantsList);
        return bundle;
    }

    private void setTextChangeListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("Giri ", "beforeTextChanged: " + s.length());
                if (s.length() > 0) {
                    Log.e("Giri ", "SetClose View: ");
                    setCloseView();
                } else {
                    Log.e("Giri ", "setSearch View: ");
                    setSearchView();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("Giri ", "onTextChanged: " + s.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() >= 3) {
                    Log.e("Giri ", "afterTextChanged:length " + editable.toString());
                    getSearchList(editable.toString());
                } else if (editable.toString().length() == 0) {

                } else {
                    layViewpager.setVisibility(View.INVISIBLE);
                    layNoresult.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void getSearchList(String searchName) {
        HashMap<String, String> locationMap = new HashMap<>();
        locationMap.put("lat", lat);
        locationMap.put("lng", lan);
        Log.e(TAG, "getSearchList:locationMap "+locationMap );

        Call<SearchRestaurantResponse> call = apiInterface.getSearchList(sessionManager.getHeader(), searchName, locationMap);
        call.enqueue(new Callback<SearchRestaurantResponse>() {
            @Override
            public void onResponse(Call<SearchRestaurantResponse> call, Response<SearchRestaurantResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        searchRestaurantsList = response.body().getRestaurants();
                        layNoresult.setVisibility(View.INVISIBLE);
                        layViewpager.setVisibility(View.VISIBLE);
                        initViewPager();
                    } else {
                        layViewpager.setVisibility(View.INVISIBLE);
                        layNoresult.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onFailure(Call<SearchRestaurantResponse> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.img_search, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_search:
                break;
            case R.id.img_close:
                setSearchView();
                break;
        }
    }

    private void setSearchView() {
        edtSearch.getText().clear();
        imgSearch.setVisibility(View.VISIBLE);
        imgClose.setVisibility(View.GONE);
    }

    private void setCloseView() {
        imgSearch.setVisibility(View.GONE);
        imgClose.setVisibility(View.VISIBLE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrentLocationFetch(OnLocationFetch onLocationFetch) {
        Log.e(TAG, "onCurrentLocationFetch: "+onLocationFetch );
        lat = String.valueOf((onLocationFetch.getIntentHashMap().get(CONST.CURRENT_LATITUDE)));
        lan = String.valueOf((onLocationFetch.getIntentHashMap().get(CONST.CURRENT_LONGITUDE)));
    }
}

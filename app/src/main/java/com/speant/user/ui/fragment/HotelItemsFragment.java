package com.speant.user.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.speant.user.ui.HotelDetailActivity;
import com.speant.user.ui.adapter.FoodCategoryAdapter;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Common.CONST;
import com.speant.user.Common.SessionManager;
import com.speant.user.Models.CategoryWiseFoodPojo;
import com.speant.user.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class HotelItemsFragment extends Fragment {

    private static final String TAG = "HotelItemsFragment";

    int categoryId;
    Context context;
    List<CategoryWiseFoodPojo.FoodList> categoryWiseFoodPojoList = new ArrayList<>();
    APIInterface apiInterface;
    SessionManager sessionManager;
    @BindView(R.id.category_rv)
    RecyclerView categoryRv;
    Unbinder unbinder;
    private LinearLayoutManager MyLayoutManager;
    private FoodCategoryAdapter foodCategoryAdapter;

    public static Fragment newInstance(int categoryId) {
        Log.e("Giri ", "onCreateView:categoryId position " + categoryId);
        HotelItemsFragment hotelItemsFragment = new HotelItemsFragment();
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        hotelItemsFragment.setArguments(args);
        return hotelItemsFragment;

    }

    public HotelItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("Giri ", "onCreateView:HotelItemsFragment ");
        View view = inflater.inflate(R.layout.fragment_hotel_items, container, false);
        unbinder = ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        context = getActivity();
        categoryId = getArguments().getInt("categoryId", -1);

        Log.e("Giri ", "onViewCreated: HotelItemsFragment ");
        MyLayoutManager = new LinearLayoutManager(getContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRv.setLayoutManager(MyLayoutManager);
        categoryRv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        categoryRv.setNestedScrollingEnabled(true);
        categoryRv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    ((HotelDetailActivity) context).slideDown();
                }
                if (dy < 0) {
                    // Scrolling up
                    ((HotelDetailActivity) context).slideDown();
                } else {
                    // Scrolling down
                    ((HotelDetailActivity) context).slideUp();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                    ((HotelDetailActivity) context).slideUp();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                    ((HotelDetailActivity) context).slideDown();
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // Do something
                    ((HotelDetailActivity) context).slideUp();
                } else {
                    // Do something
                }
            }
        });
        getCategory();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCategory();
    }

    public void jsonCategoryItems(HashMap<String, String> map) {

        Call<CategoryWiseFoodPojo> call = apiInterface.getCategoryWiseFoodList(sessionManager.getHeader(), map);
        call.enqueue(new Callback<CategoryWiseFoodPojo>() {
            @Override
            public void onResponse(Call<CategoryWiseFoodPojo> call, Response<CategoryWiseFoodPojo> response) {
                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        categoryWiseFoodPojoList.clear();
                        categoryWiseFoodPojoList.addAll(response.body().getFoodList());
                        Log.e("Giri ", "onResponse:categoryWiseFoodPojoList " + categoryWiseFoodPojoList.size());
//                        foodCategoryAdapter.notifyDataChanged();

//                        foodCategoryAdapter = new FoodCategoryAdapter(context, categoryWiseFoodPojoList);
                        if (categoryRv != null) {
                            if (categoryRv.getAdapter() == null) {
                                Log.e("Giri ", "onResponse null:categoryWiseFoodPojoList " + categoryWiseFoodPojoList.size());
                                categoryRv.setAdapter(foodCategoryAdapter);
                            } else {
                                Log.e("Giri ", "onResponse not null :categoryWiseFoodPojoList " + categoryWiseFoodPojoList.size());
                                categoryRv.getAdapter().notifyDataSetChanged();
                            }
                        }


//                        foodCategoryAdapter.updateList(categoryWiseFoodPojoList);

                        if (response.body().getIsFavourite() == 1)
                            ((HotelDetailActivity) context).getLike(true);
                        else
                            ((HotelDetailActivity) context).getLike(false);

                    }

                } else if(response.code() == 401){
                    sessionManager.logoutUser();
                }
            }

            @Override
            public void onFailure(Call<CategoryWiseFoodPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }


    //this method is overrided to call the api service on viewpager
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                Log.e("Giri ", "setUserVisibleHint:isVisibleToUser ");
                Log.e("Giri ", "setUserVisibleHint:SessionManager" + getActivity());
                Log.e("Giri ", "setUserVisibleHint:SessionManager" + getContext());
                getCategory();

            }
        }

    }

    private void getCategory() {

        HashMap<String, String> map = new HashMap<>();
        map.put(CONST.Params.restaurant_id, CONST.restaren_id);
        map.put(CONST.Params.category_id, String.valueOf(categoryId));
        map.put(CONST.Params.veg_only, CONST.pureVegStr);
        jsonCategoryItems(map);
    }
}

package com.speant.user.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.user.Common.callBacks.OnItemClickCallBack;
import com.speant.user.Common.callBacks.OnTotalAmountUpdate;
import com.speant.user.Models.FinalFoodList;
import com.speant.user.R;
import com.speant.user.ui.adapter.FoodCategoryAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment{


    @BindView(R.id.recycler_food_items)
    RecyclerView recyclerFoodItems;
    Activity activity;
    private FoodCategoryAdapter foodCategoryAdapter;
    List<FinalFoodList> finalFoodList;
    OnItemClickCallBack onItemClickCallBack;
    OnTotalAmountUpdate onTotalAmountUpdate;

    public RestaurantListFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        ButterKnife.bind(this, view);
        this.activity = getActivity();
        setRecycler();
        return view;
    }

    private void setRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerFoodItems.setLayoutManager(linearLayoutManager);
//        recyclerFoodItems.setNestedScrollingEnabled(false);
        Log.e("Tag", "setRecycler:finalFoodList "+finalFoodList.size() );
        foodCategoryAdapter = new FoodCategoryAdapter(activity, finalFoodList, getActivity().getSupportFragmentManager(), onItemClickCallBack, onTotalAmountUpdate);
        recyclerFoodItems.setAdapter(foodCategoryAdapter);
    }


    public void setValues(List<FinalFoodList> finalFoodList, OnItemClickCallBack onItemClickCallBack, OnTotalAmountUpdate onTotalAmountUpdate) {
        this.finalFoodList = finalFoodList;
        this.onItemClickCallBack = onItemClickCallBack;
        this.onTotalAmountUpdate = onTotalAmountUpdate;
        Log.e("Tag", "setValues:finalFoodList "+finalFoodList.size() );
        refreshAdapter();
    }

    public void refreshAdapter() {
        if(recyclerFoodItems != null && recyclerFoodItems.getAdapter() != null){
            Log.e("TAG", "refreshAdapter: NOT NULL ");
            recyclerFoodItems.getAdapter().notifyDataSetChanged();
        }
    }

    public float getTabSelectPositon(HashMap<String, Integer> categoryPos, String tagName) {
        return recyclerFoodItems.getY() + recyclerFoodItems.getChildAt(categoryPos.get(tagName) - 1).getY();
    }
}

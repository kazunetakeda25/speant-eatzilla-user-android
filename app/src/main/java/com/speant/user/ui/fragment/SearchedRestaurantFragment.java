package com.speant.user.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speant.user.Common.CONST;
import com.speant.user.Common.callBacks.LoginSuccessCallBack;
import com.speant.user.Common.callBacks.onFavouritesClick;
import com.speant.user.Models.SearchRestaurantResponse;
import com.speant.user.R;
import com.speant.user.ui.adapter.SearchedRestaurantAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchedRestaurantFragment extends Fragment implements onFavouritesClick, LoginSuccessCallBack {
    List<SearchRestaurantResponse.SearchRestaurants> searchRestaurantsList;
    @BindView(R.id.search_rest_recyler)
    RecyclerView searchRestRecyler;
    Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;
    SearchedRestaurantAdapter searchedRestaurantAdapter;
    private LoginBottomFragment loginBottomFragment;
    Activity activity;

    public SearchedRestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searched_restaurant, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = getActivity();
        getBundle();
        return view;
    }

    public void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {

            searchRestaurantsList = (List<SearchRestaurantResponse.SearchRestaurants>) bundle.getSerializable(CONST.SEARCHED_RESTAURANT);
            Log.e("SearchedRestaurantFrag", "onResponse: " + searchRestaurantsList.size());
            Log.e("SearchedRestaurantFrag", "onResponse: " + searchRestaurantsList.get(0).getName());

            //set Details
            linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            searchRestRecyler.setLayoutManager(linearLayoutManager);
            searchedRestaurantAdapter = new SearchedRestaurantAdapter(getActivity(), searchRestaurantsList, this::favClickOnLogout);
            setAdapter();

        }
    }

    private void setAdapter() {
        if (searchRestRecyler.getAdapter() == null) {
            searchRestRecyler.setAdapter(searchedRestaurantAdapter);
        } else {
            searchRestRecyler.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onDismissFragment() {
        loginBottomFragment.dismiss();
    }

    @Override
    public void favClickOnLogout() {
        openLoginFragment();
    }

    private void openLoginFragment() {
        loginBottomFragment = new LoginBottomFragment(activity, this::onDismissFragment);
        loginBottomFragment.showNow(getFragmentManager(), "loginFragment");
    }

}

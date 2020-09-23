package com.speant.user.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.speant.user.Models.HotelDetailPojo;
import com.speant.user.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantInfoFragment extends Fragment {


    @BindView(R.id.img_rest)
    AppCompatImageView imgRest;
    @BindView(R.id.txt_restName)
    AppCompatTextView txtRestName;
    @BindView(R.id.rest_address)
    AppCompatTextView restAddress;
    @BindView(R.id.txt_shop_desc)
    AppCompatTextView txtShopDesc;
    @BindView(R.id.txt_license)
    AppCompatTextView txtLicense;
    @BindView(R.id.rate_delivery)
    RatingBar rateDelivery;
    HotelDetailPojo.Restaurants restPojo;
    Activity activity;
    public RestaurantInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_info, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        setData();
        return view;

    }

    private void setData() {
        if(restPojo.getImage() != null && !restPojo.getImage().isEmpty()) {
            Glide.with(activity).load(restPojo.getImage()).into(imgRest);
        }
        txtRestName.setText(restPojo.getName());
        restAddress.setText(restPojo.getAddress());
        txtShopDesc.setText(restPojo.getShop_description());
        txtLicense.setText(restPojo.getFssai_license());
       rateDelivery.setRating((float) restPojo.getRating());
    }

    public void setValue(HotelDetailPojo.Restaurants restPojo) {
        this.restPojo = restPojo;
    }
}

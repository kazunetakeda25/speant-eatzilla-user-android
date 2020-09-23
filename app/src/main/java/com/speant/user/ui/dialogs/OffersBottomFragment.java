package com.speant.user.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.speant.user.Models.OfferResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speant.user.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OffersBottomFragment extends BottomSheetDialogFragment {
    View view;
    Unbinder unbinder;
    Activity activity;
    @BindView(R.id.txt_coup)
    AppCompatTextView txtCoup;
    @BindView(R.id.txt_coup_code)
    AppCompatTextView txtCoupCode;
    @BindView(R.id.txt_coup_title)
    AppCompatTextView txtCoupTitle;
    @BindView(R.id.txt_coup_desc)
    AppCompatTextView txtCoupDesc;
    @BindView(R.id.txt_rules)
    AppCompatTextView txtRules;
    @BindView(R.id.btnn_copy)
    AppCompatButton btnnCopy;
    List<String> rules = new ArrayList<>();
    OfferResponse.PromoList promoList;

    public OffersBottomFragment(OfferResponse.PromoList promoList) {
        this.promoList = promoList;
        activity = getActivity();
        rules.add("Valid "+promoList.getUse_per_customer()+" time per user");
        rules.add("Offer valid on android app");
        rules.add("Other T&Cs may apply");
        rules.add("Offer Valid on Order above "+promoList.getCoupon_value());
//        rules.add("Other valid till Aug 31, 2018 23:59 PM");
        rules.add("Other valid till "+promoList.getValid_till());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offer_bottom_sheet_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        setValues();
        return view;
    }

    private void setValues() {
        /*String rule = getString(R.string.bullets).concat(" ").concat(rules.get(0)).concat(getString(R.string.newline));
        for (int i = 1; i < rules.size(); i++) {
            Log.e("TAG", "setValues: " + rules.get(i));
            rule.concat(getString(R.string.bullets).concat(" ").concat(rules.get(i)).concat(getString(R.string.newline)));
        }*/
        String rule;
        StringBuilder builder = new StringBuilder();
        for(String s : rules) {
            builder.append(getString(R.string.bullets).concat(" ").concat(s).concat(getString(R.string.newline)));
        }
        rule = builder.toString();
        txtRules.setText(rule);
        txtCoupCode.setText(promoList.getCode());
        txtCoupTitle.setText(promoList.getTitle());
        txtCoupDesc.setText(promoList.getDescription());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnn_copy)
    public void onViewClicked() {
        dismiss();
    }
}

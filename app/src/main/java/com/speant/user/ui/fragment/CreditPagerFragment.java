package com.speant.user.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.UpdateCreditCallBack;
import com.speant.user.Common.customCarosuel.CarouselLinearLayout;
import com.speant.user.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditPagerFragment extends Fragment {


    private SessionManager sessionManager;

    public CreditPagerFragment() {
        // Required empty public constructor
    }

    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";

    private int[] imageArray = new int[]{R.drawable.ic_credit, R.drawable.ic_credit,R.drawable.ic_credit};
    private String[] creditArray = new String[]{"150", "300", "500"};

    private int screenWidth;
    private int screenHeight;
    static UpdateCreditCallBack onUpdateCreditCallBack;

    public static Fragment newInstance(Activity context, int pos, float scale, UpdateCreditCallBack updateCreditCallBack) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);
        onUpdateCreditCallBack = updateCreditCallBack;
        return Fragment.instantiate(context, CreditPagerFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);
        sessionManager = new SessionManager(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth , screenHeight / 4);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_credit_pager, container, false);

        TextView textAmount = (TextView) linearLayout.findViewById(R.id.text_amount);
        TextView textCredit= (TextView) linearLayout.findViewById(R.id.text_credit);
        CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pagerImg);

        textAmount.setText(sessionManager.getCurrency() +" "+creditArray[postion]);

        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(imageArray[postion]);
        Log.e("Tag", "onCreateView:postion "+postion );
        //handling click event
        textCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Tag", "onCreateView:onClick "+creditArray[postion]);
                onUpdateCreditCallBack.onCreditChange(creditArray[postion]);
            }
        });

        root.setScaleBoth(scale);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

}

package com.speant.user.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.speant.user.Models.RatingRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.callBacks.RatingRefreshCallBack;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.RatingResponse;
import com.speant.user.Models.TrackingDetailPojo;
import com.speant.user.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.DINING;
import static com.speant.user.Common.CONST.PICKUP_RESTAURANT;

public class RatingsBottomFragment extends BottomSheetDialogFragment {
    private static final String TAG = "RatingsBottomFragment";
    View view;
    Unbinder unbinder;
    Activity activity;
    @BindView(R.id.txt_rest_name)
    AppCompatTextView txtRestName;
    @BindView(R.id.rate_rest)
    RatingBar rateRest;
    @BindView(R.id.edt_rest_suggestion)
    AppCompatEditText edtRestSuggestion;
    @BindView(R.id.txt_delivery_name)
    AppCompatTextView txtDeliveryName;
    @BindView(R.id.rate_delivery)
    RatingBar rateDelivery;
    @BindView(R.id.txt_confirm)
    AppCompatTextView txtConfirm;
    int requestId;
    APIInterface apiInterface;
    SessionManager sessionManager;
    @BindView(R.id.txt_suggest_name)
    AppCompatTextView txtSuggestName;
    RatingRefreshCallBack ratingRefreshCallBack;

    public RatingsBottomFragment(int requestId, Activity activity, RatingRefreshCallBack ratingRefreshCallBack) {
        this.requestId = requestId;
        this.activity = activity;
        this.ratingRefreshCallBack = ratingRefreshCallBack;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /*@Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());

        });
    }*/


    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rating_bottom_sheet_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        HashMap<String, String> trackMap = new HashMap<>();
        trackMap.put(CONST.Params.request_id, String.valueOf(requestId));
        jsonGetTrackingStatus(trackMap);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.txt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_confirm:
                setRatingConfirmation();
                break;
        }
    }

    private void setRatingConfirmation() {
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRequest_id("" + requestId);
        ratingRequest.setRestaurant_feedback(edtRestSuggestion.getText().toString().isEmpty() ? "" : edtRestSuggestion.getText().toString());
        ratingRequest.setDelivery_boy_rating(String.valueOf(rateDelivery.getRating()).equals("0.0") ? "5.0" : ""+rateDelivery.getRating());
        ratingRequest.setRestaurant_rating(String.valueOf(rateRest.getRating()));
        Call<RatingResponse> call = apiInterface.setRating(sessionManager.getHeader(), ratingRequest);
        call.enqueue(new Callback<RatingResponse>() {
            @Override
            public void onResponse(Call<RatingResponse> call, Response<RatingResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        CommonFunctions.shortToast(activity, "Rated Successfully");
                        ratingRefreshCallBack.onRatingRefresh();
                        dismiss();
                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(activity, "Unauthorised");
                }
            }

            @Override
            public void onFailure(Call<RatingResponse> call, Throwable t) {

            }
        });
    }

    private void jsonGetTrackingStatus(HashMap<String, String> trackMap) {
        Call<TrackingDetailPojo> call = apiInterface.trackingDetail(sessionManager.getHeader(), trackMap);
        call.enqueue(new Callback<TrackingDetailPojo>() {
            @Override
            public void onResponse(Call<TrackingDetailPojo> call, Response<TrackingDetailPojo> response) {

                if (response.code() == 200) {

                    Log.e("Nive ", "onResponse:Track " + response.body());

                    if (response.body().getStatus()) {

                        com.speant.user.Models.TrackingDetailPojo.OrderStatus pojo = response.body().getOrderStatus().get(0);

                        if (pojo.getDelivery_type().equals(DINING) || pojo.getDelivery_type().equals(PICKUP_RESTAURANT)){
                            txtDeliveryName.setVisibility(View.GONE);
                            rateDelivery.setVisibility(View.GONE);
                        }else{
                            txtDeliveryName.setVisibility(View.VISIBLE);
                            rateDelivery.setVisibility(View.VISIBLE);
                        }

                        txtDeliveryName.setText("Rate Delivery by " + pojo.getDeliveryBoyName());
                        txtRestName.setText("Rate your Food - " + pojo.getRestaurantName());
                        txtSuggestName.setText("Your Suggestions for : "+pojo.getRestaurantName());
                    } else
                        CommonFunctions.shortToast(activity, response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(activity, "Unauthorised");
                }

            }

            @Override
            public void onFailure(Call<TrackingDetailPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}

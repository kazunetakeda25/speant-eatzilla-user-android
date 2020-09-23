package com.speant.user.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.speant.user.Common.CONST;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.Global;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.web.APIClient;
import com.speant.user.Common.web.APIInterface;
import com.speant.user.Models.TrackingDetailPojo;
import com.speant.user.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.user.Common.CONST.ORDER_CANCELLED;
import static com.speant.user.Common.SessionManager.KEY_USER_MOBILE;
import static com.speant.user.Common.SessionManager.KEY_USER_NAME;

public class DiningTrackActivity extends AppCompatActivity {

    private static final String TAG = "DiningTrackActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_restName)
    AppCompatTextView txtRestName;
    @BindView(R.id.rest_address)
    AppCompatTextView restAddress;
    @BindView(R.id.txt_book_stat)
    AppCompatTextView txtBookStat;
    @BindView(R.id.txt_book_desc)
    AppCompatTextView txtBookDesc;
    @BindView(R.id.txt_date)
    AppCompatTextView txtDate;
    @BindView(R.id.txt_guests)
    AppCompatTextView txtGuests;
    @BindView(R.id.txt_name)
    AppCompatTextView txtName;
    @BindView(R.id.txt_phonenumber)
    AppCompatTextView txtPhonenumber;
    @BindView(R.id.txt_address)
    AppCompatTextView txtAddress;
    @BindView(R.id.txt_call)
    AppCompatTextView txtCall;
    @BindView(R.id.img_rest)
    AppCompatImageView imgRest;
    @BindView(R.id.img_process)
    AppCompatImageView imgProcess;
    @BindView(R.id.btn_status)
    AppCompatTextView btnStatus;
    private SessionManager sessionManager;
    private APIInterface apiInterface;
    private APIInterface apiService;
    private String requestId;
    private String phoneNumber;
    private HashMap<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_track);
        ButterKnife.bind(this);
        getIntentValues();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        userDetails = sessionManager.getUserDetails();
        HashMap<String, String> trackMap = new HashMap<>();
        trackMap.put(CONST.Params.request_id, requestId);
        jsonGetTrackingStatus(trackMap);
    }

    public void jsonGetTrackingStatus(HashMap<String, String> trackMap) {

        Call<TrackingDetailPojo> call = apiInterface.trackingDetail(sessionManager.getHeader(), trackMap);
        call.enqueue(new Callback<TrackingDetailPojo>() {
            @Override
            public void onResponse(Call<TrackingDetailPojo> call, Response<TrackingDetailPojo> response) {

                if (response.code() == 200) {

                    Log.e("Nive ", "onResponse:Track " + response.body());

                    if (response.body().getStatus()) {

                        TrackingDetailPojo.OrderStatus pojo = response.body().getOrderStatus().get(0);
                        phoneNumber = pojo.getRestaurant_phone();
                        txtRestName.setText(pojo.getRestaurantName());
                        restAddress.setText(pojo.getRestaurant_address());
                        Picasso.get().load(pojo.getRestaurantImage()).error(R.drawable.ic_placeholder).into(imgRest);
                        if (pojo.getStatus() == 0) {
                            Glide.with(DiningTrackActivity.this).load(R.drawable.stopwatch).into(imgProcess);
                            txtBookStat.setText(getString(R.string.process_request));
                            txtBookDesc.setText(getString(R.string.process_desc));
                        } else if (pojo.getStatus() == 1) {
                            Glide.with(DiningTrackActivity.this).load(R.drawable.success).into(imgProcess);
                            txtBookStat.setText(getResources().getString(R.string.request_accept));
                            txtBookDesc.setText(getString(R.string.request_accept_desc));
                        } else if (pojo.getStatus() == 7) {
                            Glide.with(DiningTrackActivity.this).load(R.drawable.success).into(imgProcess);
                            txtBookStat.setText(getResources().getString(R.string.request_complete));
                            txtBookDesc.setText(getString(R.string.request_complete_desc));
                        }

                        txtDate.setText(Global.getDateFromString(pojo.getPickup_dining_time(), "yyyy-MM-dd HH:mm:ss", "EEEE MMM, d hh:mm a"));
                        txtGuests.setText(pojo.getTotal_members());
                        txtName.setText(userDetails.get(KEY_USER_NAME));
                        txtPhonenumber.setText(userDetails.get(KEY_USER_MOBILE));
                        txtAddress.setText(pojo.getRestaurant_address());

                        String status = String.valueOf(pojo.getStatus());

                        if(status.equals(ORDER_CANCELLED)){
                            btnStatus.setVisibility(View.VISIBLE);
                        }else{
                            btnStatus.setVisibility(View.GONE);
                        }

                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser();
                    CommonFunctions.shortToast(getApplicationContext(), "Unauthorised");
                }

            }

            @Override
            public void onFailure(Call<TrackingDetailPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getIntentValues() {
        if (getIntent() != null) {
            requestId = getIntent().getStringExtra(CONST.REQUEST_ID);
            Log.e("Giri ", "getIntentValues:requestId" + requestId);
        }
    }

    @OnClick({R.id.txt_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_call:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(dialIntent);
                break;
        }
    }
}

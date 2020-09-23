package com.speant.user.ui;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.speant.user.Common.FireBaseModels.ProviderLocation;
import com.speant.user.Common.Global;
import com.speant.user.Common.activities.CurrentLocation;
import com.speant.user.Common.locationCheck.GPSChangeNotifyEvent;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.R;
import com.google.android.gms.maps.model.LatLng;
import com.hbb20.CountryCodePicker;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginNewActivity extends CurrentLocation {

    private static final String TAG = "LoginNewActivity";
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.login_phone_num_edt)
    AppCompatEditText loginPhoneNumEdt;
    @BindView(R.id.txt_skip)
    AppCompatTextView txtSkip;
    private String countryCode;
    private LocationUpdate locationUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        locationUpdate = new LocationUpdate(this);
        locationUpdate.locationPermissionCheck(true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationReceived(ProviderLocation providerLocation) {
        if (!isEventStarted) {
            isEventStarted = true;
            Log.e("TAG", "onLocationReceived:mLastLocation " + providerLocation.getLng());
            location = providerLocation.getLat() + "/" + providerLocation.getLng();
            getCurrentCountryCode();
            isEventStarted = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGPSChangeNotify(GPSChangeNotifyEvent gpsChangeNotifyEvent) {
        Log.e(TAG, "onGPSNotify:EventCalled ");
        if (!isDialogOpen) {
            locationUpdate.locationPermissionCheck(true);
        }
    }

    @OnClick({R.id.ccp, R.id.login_phone_num_edt, R.id.txt_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ccp:
                Intent intent = new Intent(LoginNewActivity.this, LoginConfirmActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.login_phone_num_edt:
                Intent intent1 = new Intent(LoginNewActivity.this, LoginConfirmActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.txt_skip:
                Intent intent2 = new Intent(LoginNewActivity.this, HomeActivity.class);
                startActivity(intent2);
                finishAffinity();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    public void getCurrentCountryCode() {
        //GetCurrentLocation from AgentCurrentLocation Activity's String named as "location"

        if (location != null) {
            String[] currentLocation = location.split("/");
            double lat = Double.parseDouble(currentLocation[0]);
            double lng = Double.parseDouble(currentLocation[1]);
            LatLng latLng = new LatLng(lat, lng);
            Log.e("Giri ", "run:getCurrentLocation " + latLng);
            Log.e("ragu", "getCurrentCountryCode: "+latLng );
          countryCode = Global.getLocationCountryCode(LoginNewActivity.this, latLng);
//            countryCode = "880";
            Log.e("Giri ", "run:countryCode " + countryCode);
            if (countryCode != null) {
                ccp.setCountryForNameCode(countryCode);
            }
        }

    }
}

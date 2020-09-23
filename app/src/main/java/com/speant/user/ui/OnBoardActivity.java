package com.speant.user.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.speant.user.Common.CommonFunctions;
import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.Common.activities.CurrentLocation;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Common.locationCheck.PermissionSuccessEvent;
import com.speant.user.ui.adapter.SlideAdapter;
import com.speant.user.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class OnBoardActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final Integer[] XMEN = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4};
    private static final String[] titleStr = {"FIND RESTAURANT", "PICK THE BEST", "CHOOSE YOUR FOOD", "MEAL IS ON THE WAY"};
    private static final String[] descStr = {"Find the best restaurant in your neighbourhood",
            "Pick the right place using trusted ratings and reviews", "Easily find the type of food You're Craving",
            "Get ready and comfortable while our Delivery Partner brings Food to your door Step"};

    @BindView(R.id.login_skip_txt)
    TextView loginSkipTxt;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.login_pager_rel)
    RelativeLayout loginPagerRel;
    @BindView(R.id.pager_linear)
    LinearLayout pagerLinear;

    private ArrayList<Integer> XMENArray = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> descList = new ArrayList<>();
    private static int currentPage = 0;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    private static final int MY_REQUEST_CODE = 786;
    private static final int MY_SMS_CODE = 186;
    private boolean gps = false;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        sessionManager.firstTimeOpen();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_REQUEST_CODE);
            } else
                locationrequest();
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,},
                        MY_SMS_CODE);
            }
        }*/

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        init();//View Pagers
    }

    private void init() {
        for (int i = 0; i < XMEN.length; i++) {
            XMENArray.add(XMEN[i]);
            titleList.add(titleStr[i]);
            descList.add(descStr[i]);
        }

        pager.setAdapter(new SlideAdapter(OnBoardActivity.this, XMENArray, titleList, descList));
        indicator.setViewPager(pager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 6000);
    }

    @OnClick({/*R.id.login_txt,*/R.id.login_skip_txt})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.login_skip_txt:
                if (CommonFunctions.checkLocationPermission(getApplicationContext())) {
                    if (CommonFunctions.isGPSEnabled(OnBoardActivity.this)) {
                        intent = new Intent(OnBoardActivity.this, LoginNewActivity.class);
                        intent.putExtra("", "skip");
                        startActivity(intent);
                    } else
                        alertDialog(this, "Enable GPS", "Enable GPS to get your Accurate location", 1);

                } else {
                    alertDialog(this, "Permission", "Allow Location Permission", 0);
                }

                break;
        }
    }

    public void alertDialog(final Context context, String title, String message, final int gps) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (gps == 1) {
                            locationrequest();
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,},
                                            MY_REQUEST_CODE);
                                }
                            }
                        }
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();

    }

    private void locationrequest() {

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(OnBoardActivity.this)
                    .addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            builder.setNeedBle(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            gps = true;
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(OnBoardActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(getApplicationContext(), "Please Enable Location", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult()", Integer.toString(resultCode));
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case RESULT_OK: {
                        gps = true;
                        break;
                    }
                    case RESULT_CANCELED: {
                        gps = false;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, OnBoardActivity.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}

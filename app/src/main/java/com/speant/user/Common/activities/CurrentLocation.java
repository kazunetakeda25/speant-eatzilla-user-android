package com.speant.user.Common.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.speant.user.Common.locationCheck.GPSBroadCastReceiver;
import com.speant.user.Common.locationCheck.LocationEnableEvent;
import com.speant.user.Common.locationCheck.LocationUpdate;
import com.speant.user.Common.locationCheck.PermissionEnableEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CurrentLocation extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_REQUEST = 1188;
    GoogleApiClient mGoogleApiClient;
    LatLng currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private final String TAG = this.getClass().getSimpleName();
    public String location;
    LocationUpdate locationUpdate;
    public boolean isDialogOpen = false;
    final static int REQUEST_LOCATION = 299;
    boolean isLocationNeeded;
    public boolean isEventStarted = false;
    GPSBroadCastReceiver gpsBroadCastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationUpdate = new LocationUpdate(this);

        //Check Gps Change
        gpsBroadCastReceiver = new GPSBroadCastReceiver();
        registerReceiver(gpsBroadCastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(gpsBroadCastReceiver);
    }

    // if GPS and Permission is Enabled then the Location will be received through this EventBus Event
    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationReceived(ProviderLocation providerLocation) {
        isDialogOpen =false;
        Log.e("TAG", "onLocationReceived:mLastLocation " + providerLocation.getLng());
        location = providerLocation.getLat()+"/"+providerLocation.getLng();
    }*/

    // Receive the GPS Change
   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGPSNotify(GPSChangeNotifyEvent gpsChangeNotifyEvent) {
        Log.e(TAG, "onGPSNotify:EventCalled ");
        Log.e(TAG, "onGPSNotify:EventCalled "+gpsChangeNotifyEvent.isGPSEnabled());
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGPSCheck(LocationEnableEvent locationEnableEvent) {
        Log.e(TAG, "onGPSCheck:EventCalled ");
        isLocationNeeded = locationEnableEvent.isLocationNeeded();
        enableGoogleApi();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPermissionCheck(PermissionEnableEvent permissonEnableEvent) {
        Log.e(TAG, "onPermissionCheck:EventCalled ");
        isLocationNeeded = permissonEnableEvent.isLocationNeeded();
        isLocationPermissionEnabled();
    }


    public void isLocationPermissionEnabled() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ArrayList<String> permissionList = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (permissionList.size() > 0) {
                Log.e(TAG, "onComplete:Permission Open requested " );
                isDialogOpen = true;
                String[] permissionArray = new String[permissionList.size()];
                permissionList.toArray(permissionArray);
                requestPermissions(permissionArray, PERMISSION_REQUEST);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission) &&
                        grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.e(TAG, "onRequestPermissionsResult:PERMISSION_DENIED");
                    // user rejected the permission
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                    Log.e(TAG, "onRequestPermissionsResult:showRationale " + showRationale);
                    openLocationEnablePopup(showRationale, "Enable Location Permissions to access the app");
                    break;
                } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission) &&
                        grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "onRequestPermissionsResult:PERMISSION_GRANTED ");
                    locationUpdate.locationPermissionCheck(isLocationNeeded);
                    break;
                }
            }
        }
    }


    private void openLocationEnablePopup(boolean showRationale, String message) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        if (!showRationale) {
                            isDialogOpen = false;
                            /*if (!showRationale) { user also CHECKED "never ask again" }*/
                            // HERE USER CHECKED "never ask again"
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getPackageName()));
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            isLocationPermissionEnabled();
                        }
                    }
                });
        alert = builder.create();

        if (!alert.isShowing()) {
            alert.show();
        }
    }

    private void enableGoogleApi() {
        mGoogleApiClient = new
                GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("TAG", "onConnected:mGoogleApiClient ");
        gpsEnableRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(CurrentLocation.this, "GoogleApi Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(CurrentLocation.this, "GoogleApi Connection Failed", Toast.LENGTH_SHORT).show();
    }

    /*private void gpsEnableRequest() {
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
                Log.e("TAG", "onResult:gpsEnable status "+status );
                final LocationSettingsStates state = result.getLocationSettingsStates();
                Log.e("TAG", "onResult:gpsEnable state "+state );
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        locationUpdate.locationPermissionCheck(true);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(CurrentLocation.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        showGPSDisabledAlert();
                        break;
                }
            }
        });
    }*/

    private void gpsEnableRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Log.e(TAG, "onComplete:isGpsUsable " + response.getLocationSettingsStates().isGpsUsable());
                    locationUpdate.locationPermissionCheck(isLocationNeeded);

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            //-----Updated On May 30
                            if(!isDialogOpen) {
                                try {
                                    Log.e(TAG, "onComplete:GPS Open requested " );
                                    resolvable.startResolutionForResult(CurrentLocation.this, REQUEST_LOCATION);
                                    isDialogOpen = true;
                                } catch (IntentSender.SendIntentException e) {
                                    isDialogOpen = false;
                                    e.printStackTrace();
                                }
                            }
                            //--------------------------
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            showGPSDisabledAlert();
                            break;
                    }
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult()", Integer.toString(resultCode));
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case RESULT_OK: {
                        isDialogOpen = false;
                        locationUpdate.locationPermissionCheck(true);
                        break;
                    }
                    case RESULT_CANCELED: {
                        showGPSDisabledAlert();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device " + "\t" +
                "Need GPS to access the App" + "\t" +
                "Would you like to enable it?")
                .setCancelable(false)
                .setTitle("Enable GPS")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                isDialogOpen = false;
                                gpsEnableRequest();
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}

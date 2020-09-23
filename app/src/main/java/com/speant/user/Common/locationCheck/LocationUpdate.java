package com.speant.user.Common.locationCheck;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.speant.user.Common.FireBaseModels.ProviderLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.LOCATION_SERVICE;

public class LocationUpdate {

    private final FusedLocationProviderClient mFusedLocationClient;
    Activity activity;
    private LocationCallback mLocationCallback;
    final static int REQUEST_LOCATION = 1299;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public LocationUpdate(Activity activity) {
        this.activity = activity;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        setLocationRequestCallBack();
    }

    public void locationPermissionCheck(boolean isLocationNeeded) {
        Log.e("TAG", "LocationUpdate locationPermissionCheck:isLocationNeeded "+isLocationNeeded);
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("Giri ", "GPS Enabled");
            if (activity.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                Log.e("Giri ", "Permission Enabled");
                if(isLocationNeeded) {
                    requestLocationUpdates();
                }else{
                    EventBus.getDefault().post(new PermissionSuccessEvent());
                }
            } else {
                Log.e("Giri ", "Permission Disabled");
                PermissionEnableEvent permissionEnableEvent = new PermissionEnableEvent();
                permissionEnableEvent.setLocationNeeded(isLocationNeeded);
                EventBus.getDefault().post(permissionEnableEvent);
            }
        } else {
            Log.e("Giri ", "GPS Disabled ");
            LocationEnableEvent locationEnableEvent = new LocationEnableEvent();
            locationEnableEvent.setLocationNeeded(isLocationNeeded);
            EventBus.getDefault().post(locationEnableEvent);
        }
    }

    private void requestLocationUpdates() {
        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(8000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            getLastLocation(locationRequest);
            Log.e("Location", "Requesting location updates");
        } catch (Exception e) {
            Log.e("Location", "Lost location permission. Could not request updates. " + e);
        }
    }

    private void getLastLocation(LocationRequest locationRequest) {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();
                                updateLocation(mLastLocation);
                            } else {
                                mFusedLocationClient.requestLocationUpdates(locationRequest,
                                        mLocationCallback, Looper.myLooper());
                                Log.e("TAG", "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e("TAG", "Lost location permission." + unlikely);
        }
    }

    private void setLocationRequestCallBack() {
        if (mLocationCallback == null) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        super.onLocationResult(locationResult);
                        if(mLastLocation == null){
                            Location mLastLocation = locationResult.getLastLocation();
                            updateLocation(mLastLocation);
                        }
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

                    } else {
                        Log.e("Giri ", "onLocationResult: null");
                    }
                }
            };
        }
    }

    private void updateLocation(Location lastLocation) {
        Log.e("TAG", "onLocationResult:mLastLocation " + lastLocation);
        ProviderLocation providerLocation = new ProviderLocation();
        providerLocation.setLat("" + lastLocation.getLatitude());
        Log.e("ragu", "updateLocation: "+lastLocation.getLatitude() );
        providerLocation.setLng("" + lastLocation.getLongitude());
        //send LatLong to activities to display the scooter marker
        EventBus.getDefault().post(providerLocation);
    }



}

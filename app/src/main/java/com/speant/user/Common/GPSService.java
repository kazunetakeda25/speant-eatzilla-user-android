package com.speant.user.Common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import com.speant.user.Common.Interface.IGPSActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GPSService {

    private IGPSActivity main;

    // Helper for GPS-Position
    private LocationListener mlocListener;
    private LocationManager mlocManager;

    private boolean isRunning;

    public GPSService(IGPSActivity main) {
        this.main = main;

        // GPS Position
        mlocManager = (LocationManager) ((Activity) this.main).getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission((Activity) this.main,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        }
        // GPS Position END
        this.isRunning = true;
    }

    public void stopGPS() {
        if(isRunning) {
            mlocManager.removeUpdates( mlocListener);
            this.isRunning = false;
        }
    }

    public void resumeGPS() {
        if (ContextCompat.checkSelfPermission((Activity) this.main,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        }
        this.isRunning = true;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public class MyLocationListener implements LocationListener {

        private final String TAG = MyLocationListener.class.getSimpleName();

        @Override
        public void onLocationChanged(Location loc) {
            GPSService.this.main.locationChanged(loc);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }


    }

}

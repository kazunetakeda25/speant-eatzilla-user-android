package com.speant.user.Common.locationCheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.LOCATION_SERVICE;

public class GPSBroadCastReceiver extends BroadcastReceiver {
//    private boolean firstConnect = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            EventBus.getDefault().post(new GPSChangeNotifyEvent(true));
        }else{
            EventBus.getDefault().post(new GPSChangeNotifyEvent(false));
        }

        /*if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.e("TAG", "onReceive:firstConnect enable"+firstConnect);
            //enable
            if (firstConnect) {
                EventBus.getDefault().post(new GPSChangeNotifyEvent(true));
                firstConnect = false;
            }
        } else {
            Log.e("TAG", "onReceive:firstConnect disable"+firstConnect);
            //disable
            if (!firstConnect) {
                EventBus.getDefault().post(new GPSChangeNotifyEvent(false));
                firstConnect = true;
            }
        }*/
    }
}

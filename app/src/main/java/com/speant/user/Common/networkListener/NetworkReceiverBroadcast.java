package com.speant.user.Common.networkListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by giri on 9/5/18.
 */

public class NetworkReceiverBroadcast extends BroadcastReceiver {

    boolean status;
    private boolean slow;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("isOnline", "isOnline:before " + status);
        Log.e("isOnline", "isOnline:before " + intent.getAction());

        NetworkListenerModel networkListenerModel = new NetworkListenerModel();
        networkListenerModel.setOnline(isOnline(context));
        networkListenerModel.setType("OnlineChange");
        EventBus.getDefault().post(networkListenerModel);

    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        status = netInfo != null && netInfo.isConnected();
        Log.e("isOnline", "isOnline:after " + status);
        //should check null because in airplane mode it will be null
        return status;
    }

   /* public int getWifiLevel(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        int speed = WifiManager.calculateSignalLevel(linkSpeed, 5);
        Log.e("Giri ", "getWifiLevel: " + speed);
        Log.e("Giri ", "getWifiLevel: " + wifiManager.getWifiState());
        return speed;
    }*/

    /*public boolean getNetworkLevel(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        Log.e("Giri ", "getNetworkLevel: " + network.getSubtypeName());
        int netSubType = Integer.parseInt(network.getSubtypeName());
        if (netSubType == TelephonyManager.NETWORK_TYPE_GPRS ||
                netSubType == TelephonyManager.NETWORK_TYPE_EDGE ||
                netSubType == TelephonyManager.NETWORK_TYPE_1xRTT) {
            //user is in slow network
            return true;

        }
        return false;
    }

    public int getNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
        CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
        int signalLevel = cellSignalStrengthGsm.getDbm();
        Log.e("Giri ", "getNetwork:signalLevel "+signalLevel);
        return signalLevel;
    }*/


}

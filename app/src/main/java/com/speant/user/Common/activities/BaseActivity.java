package com.speant.user.Common.activities;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.speant.user.Common.CONST;
import com.speant.user.Common.networkListener.NetworkChangeBroadcast;
import com.speant.user.Common.networkListener.NetworkDialog;
import com.speant.user.Common.networkListener.NetworkListenerModel;
import com.speant.user.Common.networkListener.NetworkReceiverBroadcast;
import com.speant.user.Common.networkListener.NetworkRefreshModel;
import com.speant.user.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1120;
    AlertDialog alert = null;
    private NetworkReceiverBroadcast mNetworkReceiver;
    private NetworkChangeBroadcast networkChangeBroadcast;
    private TelephonyManager telephonyManager;
    Snackbar snackbar;
    NetworkDialog networkDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate:BaseActivity " );
        EventBus.getDefault().register(this);
        //Register network online listener
        mNetworkReceiver = new NetworkReceiverBroadcast();
        IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkReceiver,filters);

        //Register network Strength listener
        networkChangeBroadcast = new NetworkChangeBroadcast();
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(networkChangeBroadcast, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }


    /*@Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy:BaseActivity " );
        EventBus.getDefault().unregister(this);
        //UnRegister network listeners
        unregisterReceiver(mNetworkReceiver);
        telephonyManager.listen(networkChangeBroadcast, PhoneStateListener.LISTEN_NONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkListen(NetworkListenerModel networkListenerModel) {
        String type = networkListenerModel.getType();

        if (type.equals("OnlineChange")) {
            boolean isOnline = networkListenerModel.getisOnline();
            if (!isOnline) {
                if(networkDialog == null) {
                    Log.e("Giri ", "onNetworkListen: isOnline");
                    networkDialog = new NetworkDialog();
                    networkDialog.setCancelable(false);
                    networkDialog.show(getSupportFragmentManager(), "Network");
                }
            }else{
                Fragment dialog = getSupportFragmentManager().findFragmentByTag("Network");
                if (dialog != null) {
                    Log.e("Giri ", "onNetworkListen: isOnline not" );
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(dialog);
                    ft.commit();
                    networkDialog = null;
                    EventBus.getDefault().post(new NetworkRefreshModel());
                }
            }
        } else if (type.equals("SignalChange")) {
            //to check if it is Mobile Data Connected or  Wifi Connected
            checkConnectivityType(networkListenerModel);
        }
    }

    private void checkConnectivityType(NetworkListenerModel networkListenerModel) {

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //state 1 wifi Offline and state 3 wifi Online
        Log.e("isOnline", "isOnline:wifiManager " +  wifiManager.getWifiState());

        int wifiState = wifiManager.getWifiState();
        List<WifiConfiguration> networkInfo = wifiManager.getConfiguredNetworks();

        if(wifiState == CONST.WIFI_ON && networkInfo != null){
            if(getWifiLevel(this) <= 2){
                setSnackBar("Network Connection is slow!");
            }
        }else{
            if (networkListenerModel.isSlow()) {
                setSnackBar("Network Connection is slow!");
            }
        }
    }


    public void setSnackBar(String message) {
        //getDecorView() uses rootview to display the snackbar
        snackbar = Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        snackbar.show();
    }

    public int getWifiLevel(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        int speed = WifiManager.calculateSignalLevel(linkSpeed, 5);
        Log.e("Giri ", "getWifiLevel: " + speed);
        Log.e("Giri ", "getWifiLevel: " + wifiManager.getWifiState());
        return speed;
    }

    /*public boolean enablePermission() {
     *//* if (CommonFunctions.isGPSEnabled(this)) {
            checkpermission();
            return true;
        } else {
            showGPSDisabledAlertToUser();
            return false;
        }*//*
        checkpermission();
        return true;
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setTitle("Goto Settings Page To Enable GPS")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public boolean checkpermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ArrayList<String> permissionList = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED) {
                permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }

            if (permissionList.size() > 0) {
                String[] permissionArray = new String[permissionList.size()];
                permissionList.toArray(permissionArray);
                requestPermissions(permissionArray, PERMISSION_REQUEST);
                return false;
            }
        }
        return true;
    }

    private void openPermissionPopup(String message) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getPackageName()));
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                            enablePermission();
                        }
                    });
            alert = builder.create();

            if(!alert.isShowing()){
                alert.show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    openPermissionPopup("Enable Permissions to access the app");
                }
            }
        }
    }*/

    public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}

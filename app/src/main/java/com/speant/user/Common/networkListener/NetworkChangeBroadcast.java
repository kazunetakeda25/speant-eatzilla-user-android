package com.speant.user.Common.networkListener;

import android.os.Build;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by giri on 9/5/18.
 */

public class NetworkChangeBroadcast extends PhoneStateListener {
    boolean status;
    private int signalSupport;
    private boolean slow;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        Log.e("isOnline", "isOnline: " + status);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("Giri ", "onSignalStrengthsChanged:getLevel" + signalStrength.getLevel());
            signalSupport = signalStrength.getLevel();
            if (signalSupport >= 2) {
                slow = false;
            } else {
                slow = true;
            }
        } else {
            signalSupport = signalStrength.getGsmSignalStrength();
            if (signalSupport > 15) {
                slow = false;
            } else {
                slow = true;
            }
        }

        Log.e("Giri ", "onSignalStrengthsChanged:" + signalSupport);

        NetworkListenerModel networkListenerModel = new NetworkListenerModel();
        networkListenerModel.setType("SignalChange");
        networkListenerModel.setSlow(slow);
        EventBus.getDefault().post(networkListenerModel);


       /* if (signalSupport > 30) {
            Log.d(getClass().getCanonicalName(), "Signal GSM : Good");

        } else if (signalSupport > 20 && signalSupport < 30) {
            Log.d(getClass().getCanonicalName(), "Signal GSM : Avarage");

        } else if (signalSupport < 20 && signalSupport > 3) {
            Log.d(getClass().getCanonicalName(), "Signal GSM : Weak");

        } else if (signalSupport < 3) {
            Log.d(getClass().getCanonicalName(), "Signal GSM : Very weak");

        }*/
    }

}

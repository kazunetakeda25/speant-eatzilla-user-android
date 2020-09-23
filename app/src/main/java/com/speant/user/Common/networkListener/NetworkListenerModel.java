package com.speant.user.Common.networkListener;

/**
 * Created by giri on 9/5/18.
 */

public class NetworkListenerModel {
    private boolean isOnline;
    private boolean isSlow;
    private String type;

    public boolean isSlow() {
        return isSlow;
    }

    public void setSlow(boolean slow) {
        isSlow = slow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getisOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

}

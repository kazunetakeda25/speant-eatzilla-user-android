package com.speant.user.Common.locationCheck;

public class GPSChangeNotifyEvent {
    private boolean isGPSEnabled;

    public GPSChangeNotifyEvent(boolean isGPSEnabled) {
        this.isGPSEnabled = isGPSEnabled;
    }

    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }

    public void setGPSEnabled(boolean GPSEnabled) {
        isGPSEnabled = GPSEnabled;
    }
}

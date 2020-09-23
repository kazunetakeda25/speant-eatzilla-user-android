package com.speant.user.Common.locationCheck;

public class LocationEnableEvent {
    private boolean isLocationNeeded;


    public boolean isLocationNeeded() {
        return isLocationNeeded;
    }

    public void setLocationNeeded(boolean locationNeeded) {
        isLocationNeeded = locationNeeded;
    }
}

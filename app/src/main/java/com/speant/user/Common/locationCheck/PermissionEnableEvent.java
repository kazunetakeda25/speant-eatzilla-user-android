package com.speant.user.Common.locationCheck;

public class PermissionEnableEvent {
    private boolean isLocationNeeded;

    public boolean isLocationNeeded() {
        return isLocationNeeded;
    }

    public void setLocationNeeded(boolean locationNeeded) {
        isLocationNeeded = locationNeeded;
    }
}

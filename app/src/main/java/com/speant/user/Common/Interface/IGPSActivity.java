package com.speant.user.Common.Interface;

import android.location.Location;

public interface IGPSActivity {
    public void locationChanged(Location loc);
    public void displayGPSSettingsDialog();
}

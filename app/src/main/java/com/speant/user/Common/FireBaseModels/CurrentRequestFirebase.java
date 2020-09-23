package com.speant.user.Common.FireBaseModels;

public class CurrentRequestFirebase {

    private LatLngModel LatLng;

    private String request_id;

    private long status;

    private String provider_id;

    private long user_id;

    public LatLngModel getLatLng() {
        return LatLng;
    }

    public void setLatLng(LatLngModel latLng) {
        LatLng = latLng;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }


}
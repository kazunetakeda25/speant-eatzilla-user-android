package com.speant.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class HistoryResponse implements Parcelable {
    private List<UpcomingOrders> upcoming_orders;

    private String status;

    private List<PastOrders> past_orders;

    protected HistoryResponse(Parcel in) {
        status = in.readString();
    }

    public static final Creator<HistoryResponse> CREATOR = new Creator<HistoryResponse>() {
        @Override
        public HistoryResponse createFromParcel(Parcel in) {
            return new HistoryResponse(in);
        }

        @Override
        public HistoryResponse[] newArray(int size) {
            return new HistoryResponse[size];
        }
    };

    public List<UpcomingOrders> getUpcoming_orders() {
        return upcoming_orders;
    }

    public void setUpcoming_orders(List<UpcomingOrders> upcoming_orders) {
        this.upcoming_orders = upcoming_orders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PastOrders> getPast_orders() {
        return past_orders;
    }

    public void setPast_orders(List<PastOrders> past_orders) {
        this.past_orders = past_orders;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
    }
}

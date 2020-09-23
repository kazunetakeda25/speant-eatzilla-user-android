package com.speant.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddOns implements Parcelable {

    private String updated_at;

    private String restaurant_id;

    private String price;

    private String name;

    private String created_at;

    private String id;

    protected AddOns(Parcel in) {
        updated_at = in.readString();
        restaurant_id = in.readString();
        price = in.readString();
        name = in.readString();
        created_at = in.readString();
        id = in.readString();
    }

    public static final Creator<AddOns> CREATOR = new Creator<AddOns>() {
        @Override
        public AddOns createFromParcel(Parcel in) {
            return new AddOns(in);
        }

        @Override
        public AddOns[] newArray(int size) {
            return new AddOns[size];
        }
    };

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updated_at);
        dest.writeString(restaurant_id);
        dest.writeString(price);
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(id);
    }
}

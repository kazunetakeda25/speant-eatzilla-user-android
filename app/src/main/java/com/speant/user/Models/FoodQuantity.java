package com.speant.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodQuantity implements Parcelable {

    private String updated_at;

    private String name;

    private String created_at;

    private Pivot pivot;

    private String id;

    protected FoodQuantity(Parcel in) {
        updated_at = in.readString();
        name = in.readString();
        created_at = in.readString();
        id = in.readString();
    }

    public FoodQuantity() {
    }

    public static final Creator<FoodQuantity> CREATOR = new Creator<FoodQuantity>() {
        @Override
        public FoodQuantity createFromParcel(Parcel in) {
            return new FoodQuantity(in);
        }

        @Override
        public FoodQuantity[] newArray(int size) {
            return new FoodQuantity[size];
        }
    };

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
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
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(id);
    }
}

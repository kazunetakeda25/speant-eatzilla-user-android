package com.speant.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodSize implements Parcelable {

    private String updated_at;

    private String price;

    private String name;

    private String created_at;

    private String id;

    protected FoodSize(Parcel in) {
        updated_at = in.readString();
        price = in.readString();
        name = in.readString();
        created_at = in.readString();
        id = in.readString();
    }

    public static final Creator<FoodSize> CREATOR = new Creator<FoodSize>() {
        @Override
        public FoodSize createFromParcel(Parcel in) {
            return new FoodSize(in);
        }

        @Override
        public FoodSize[] newArray(int size) {
            return new FoodSize[size];
        }
    };

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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
        dest.writeString(price);
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(id);
    }
}

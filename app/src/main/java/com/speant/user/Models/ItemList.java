package com.speant.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ItemList implements Parcelable {

    private String food_name;

    private String item_price;

    private String is_veg;

    private String food_quantity;

    private String tax;

    private String food_id;

    private String description;

    private FoodSize food_size;

    private List<AddOns> add_ons;


    protected ItemList(Parcel in) {
        food_name = in.readString();
        item_price = in.readString();
        is_veg = in.readString();
        food_quantity = in.readString();
        tax = in.readString();
        food_id = in.readString();
        description = in.readString();
        food_size = in.readParcelable(FoodSize.class.getClassLoader());
        add_ons = in.createTypedArrayList(AddOns.CREATOR);
    }

    public static final Creator<ItemList> CREATOR = new Creator<ItemList>() {
        @Override
        public ItemList createFromParcel(Parcel in) {
            return new ItemList(in);
        }

        @Override
        public ItemList[] newArray(int size) {
            return new ItemList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(food_name);
        dest.writeString(item_price);
        dest.writeString(is_veg);
        dest.writeString(food_quantity);
        dest.writeString(tax);
        dest.writeString(food_id);
        dest.writeString(description);
        dest.writeParcelable(food_size, flags);
        dest.writeTypedList(add_ons);
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getIs_veg() {
        return is_veg;
    }

    public void setIs_veg(String is_veg) {
        this.is_veg = is_veg;
    }

    public String getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(String food_quantity) {
        this.food_quantity = food_quantity;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodSize getFood_size() {
        return food_size;
    }

    public void setFood_size(FoodSize food_size) {
        this.food_size = food_size;
    }

    public List<AddOns> getAdd_ons() {
        return add_ons;
    }

    public void setAdd_ons(List<AddOns> add_ons) {
        this.add_ons = add_ons;
    }
}

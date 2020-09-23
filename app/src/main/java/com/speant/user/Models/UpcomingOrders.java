package com.speant.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UpcomingOrders implements Parcelable {

    private List<ItemListHistory> item_list;

    private String delivery_address;

    private String bill_amount;

    private String restaurant_id;

    private String offer_discount;

    private String restaurant_address;

    private String item_total;

    private String tax;

    private String restaurant_name;

    private String restaurant_image;

    private String ordered_on;

    private String delivery_charge;

    private String restaurant_packaging_charge;

    private String request_id;

    private String order_id;

    private String pickup_dining_time;

    private String delivery_type;

    private String total_members;

    private String restaurant_discount;

    private String status;


    protected UpcomingOrders(Parcel in) {
        item_list = in.createTypedArrayList(ItemListHistory.CREATOR);
        delivery_address = in.readString();
        bill_amount = in.readString();
        restaurant_id = in.readString();
        offer_discount = in.readString();
        restaurant_address = in.readString();
        item_total = in.readString();
        tax = in.readString();
        restaurant_name = in.readString();
        restaurant_image = in.readString();
        ordered_on = in.readString();
        delivery_charge = in.readString();
        restaurant_packaging_charge = in.readString();
        request_id = in.readString();
        order_id = in.readString();
        pickup_dining_time = in.readString();
        delivery_type = in.readString();
        total_members = in.readString();
        restaurant_discount = in.readString();
        status = in.readString();
    }

    public static final Creator<UpcomingOrders> CREATOR = new Creator<UpcomingOrders>() {
        @Override
        public UpcomingOrders createFromParcel(Parcel in) {
            return new UpcomingOrders(in);
        }

        @Override
        public UpcomingOrders[] newArray(int size) {
            return new UpcomingOrders[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(item_list);
        parcel.writeString(delivery_address);
        parcel.writeString(bill_amount);
        parcel.writeString(restaurant_id);
        parcel.writeString(offer_discount);
        parcel.writeString(restaurant_address);
        parcel.writeString(item_total);
        parcel.writeString(tax);
        parcel.writeString(restaurant_name);
        parcel.writeString(restaurant_image);
        parcel.writeString(ordered_on);
        parcel.writeString(delivery_charge);
        parcel.writeString(restaurant_packaging_charge);
        parcel.writeString(request_id);
        parcel.writeString(order_id);
        parcel.writeString(pickup_dining_time);
        parcel.writeString(delivery_type);
        parcel.writeString(total_members);
        parcel.writeString(restaurant_discount);
        parcel.writeString(status);
    }

    public List<ItemListHistory> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemListHistory> item_list) {
        this.item_list = item_list;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getOffer_discount() {
        return offer_discount;
    }

    public void setOffer_discount(String offer_discount) {
        this.offer_discount = offer_discount;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public String getItem_total() {
        return item_total;
    }

    public void setItem_total(String item_total) {
        this.item_total = item_total;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_image() {
        return restaurant_image;
    }

    public void setRestaurant_image(String restaurant_image) {
        this.restaurant_image = restaurant_image;
    }

    public String getOrdered_on() {
        return ordered_on;
    }

    public void setOrdered_on(String ordered_on) {
        this.ordered_on = ordered_on;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getRestaurant_packaging_charge() {
        return restaurant_packaging_charge;
    }

    public void setRestaurant_packaging_charge(String restaurant_packaging_charge) {
        this.restaurant_packaging_charge = restaurant_packaging_charge;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPickup_dining_time() {
        return pickup_dining_time;
    }

    public void setPickup_dining_time(String pickup_dining_time) {
        this.pickup_dining_time = pickup_dining_time;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getTotal_members() {
        return total_members;
    }

    public void setTotal_members(String total_members) {
        this.total_members = total_members;
    }

    public String getRestaurant_discount() {
        return restaurant_discount;
    }

    public void setRestaurant_discount(String restaurant_discount) {
        this.restaurant_discount = restaurant_discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

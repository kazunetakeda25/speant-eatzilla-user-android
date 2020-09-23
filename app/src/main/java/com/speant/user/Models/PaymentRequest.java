package com.speant.user.Models;

import java.io.Serializable;

public class PaymentRequest implements Serializable {
    private String restaurant_id;
    private String coupon_code;
    private String item_total;
    private double offer_discount;
    private double restaurant_packaging_charge;
    private double gst;
    private double delivery_charge;
    private double bill_amount;
    private double restaurant_discount ;
    private String delivery_type;
    private String total_members;
    private String pickup_dining_time;
    private String is_wallet;
    private double wallet_amount;

    public double getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(double wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getIs_wallet() {
        return is_wallet;
    }

    public void setIs_wallet(String is_wallet) {
        this.is_wallet = is_wallet;
    }

    public double getRestaurant_discount() {
        return restaurant_discount;
    }

    public void setRestaurant_discount(double restaurant_discount) {
        this.restaurant_discount = restaurant_discount;
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

    public String getPickup_dining_time() {
        return pickup_dining_time;
    }

    public void setPickup_dining_time(String pickup_dining_time) {
        this.pickup_dining_time = pickup_dining_time;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getItem_total() {
        return item_total;
    }

    public void setItem_total(String item_total) {
        this.item_total = item_total;
    }

    public double getOffer_discount() {
        return offer_discount;
    }

    public void setOffer_discount(double offer_discount) {
        this.offer_discount = offer_discount;
    }

    public double getRestaurant_packaging_charge() {
        return restaurant_packaging_charge;
    }

    public void setRestaurant_packaging_charge(double restaurant_packaging_charge) {
        this.restaurant_packaging_charge = restaurant_packaging_charge;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(double delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public double getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(double bill_amount) {
        this.bill_amount = bill_amount;
    }
}

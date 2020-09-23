package com.speant.user.Models;

public class RestaurantData {
    private String restaurant_name;
    private String image;
    private String address;
    private String target_amount;
    private String offer_amount;
    private String discount_type;
    private String max_dining_count;

    public String getMax_dining_count() {
        return max_dining_count;
    }

    public void setMax_dining_count(String max_dining_count) {
        this.max_dining_count = max_dining_count;
    }

    public String getTarget_amount() {
        return target_amount;
    }

    public void setTarget_amount(String target_amount) {
        this.target_amount = target_amount;
    }

    public String getOffer_amount() {
        return offer_amount;
    }

    public void setOffer_amount(String offer_amount) {
        this.offer_amount = offer_amount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

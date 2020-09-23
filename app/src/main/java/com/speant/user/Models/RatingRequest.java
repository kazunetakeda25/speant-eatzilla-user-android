package com.speant.user.Models;

public class RatingRequest {
    private String request_id;
    private String restaurant_rating;
    private String restaurant_feedback;
    private String delivery_boy_rating;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getRestaurant_rating() {
        return restaurant_rating;
    }

    public void setRestaurant_rating(String restaurant_rating) {
        this.restaurant_rating = restaurant_rating;
    }

    public String getRestaurant_feedback() {
        return restaurant_feedback;
    }

    public void setRestaurant_feedback(String restaurant_feedback) {
        this.restaurant_feedback = restaurant_feedback;
    }

    public String getDelivery_boy_rating() {
        return delivery_boy_rating;
    }

    public void setDelivery_boy_rating(String delivery_boy_rating) {
        this.delivery_boy_rating = delivery_boy_rating;
    }
}

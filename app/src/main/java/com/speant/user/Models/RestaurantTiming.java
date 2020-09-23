package com.speant.user.Models;

public class RestaurantTiming {

    private String updated_at;

    private String restaurant_id;

    private String closing_time;

    private String opening_time;

    private String created_at;

    private String id;

    private String is_weekend;

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

    public String getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
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

    public String getIs_weekend() {
        return is_weekend;
    }

    public void setIs_weekend(String is_weekend) {
        this.is_weekend = is_weekend;
    }
}

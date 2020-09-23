package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearRestarentPojo {

    @Expose
    @SerializedName("restaurants")
    private List<Restaurants> restaurants;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public List<Restaurants> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurants> restaurants) {
        this.restaurants = restaurants;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Restaurants {
        @Expose
        @SerializedName("is_favourite")
        private int isFavourite;
        @Expose
        @SerializedName("price")
        private String price;
        @Expose
        @SerializedName("travel_time")
        private String travelTime;
        @Expose
        @SerializedName("cuisines")
        private List<Cuisines> cuisines;
        @Expose
        @SerializedName("is_open")
        private int isOpen;
        @Expose
        @SerializedName("rating")
        private Float rating;
        @Expose
        @SerializedName("discount")
        private String discount;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("weekday_opening_time")
        private String weekday_opening_time;
        @Expose
        @SerializedName("weekday_closing_time")
        private String weekday_closing_time;
        @Expose
        @SerializedName("weekend_opening_time")
        private String weekend_opening_time;
        @Expose
        @SerializedName("weekend_closing_time")
        private String weekend_closing_time;
        @Expose
        @SerializedName("credit_accept")
        private String credit_accept;
        @Expose
        @SerializedName("max_dining_count")
        private int max_dining_count;
        @Expose
        @SerializedName("restaurant_timing")
        private List<RestaurantTiming> restaurant_timing;

        public List<RestaurantTiming> getRestaurant_timing() {
            return restaurant_timing;
        }

        public void setRestaurant_timing(List<RestaurantTiming> restaurant_timing) {
            this.restaurant_timing = restaurant_timing;
        }

        public int getMax_dining_count() {
            return max_dining_count;
        }

        public void setMax_dining_count(int max_dining_count) {
            this.max_dining_count = max_dining_count;
        }

        public String getCredit_accept() {
            return credit_accept;
        }

        public void setCredit_accept(String credit_accept) {
            this.credit_accept = credit_accept;
        }

        public String getWeekday_opening_time() {
            return weekday_opening_time;
        }

        public void setWeekday_opening_time(String weekday_opening_time) {
            this.weekday_opening_time = weekday_opening_time;
        }

        public String getWeekday_closing_time() {
            return weekday_closing_time;
        }

        public void setWeekday_closing_time(String weekday_closing_time) {
            this.weekday_closing_time = weekday_closing_time;
        }

        public String getWeekend_opening_time() {
            return weekend_opening_time;
        }

        public void setWeekend_opening_time(String weekend_opening_time) {
            this.weekend_opening_time = weekend_opening_time;
        }

        public String getWeekend_closing_time() {
            return weekend_closing_time;
        }

        public void setWeekend_closing_time(String weekend_closing_time) {
            this.weekend_closing_time = weekend_closing_time;
        }

        public int getIsFavourite() {
            return isFavourite;
        }

        public void setIsFavourite(int isFavourite) {
            this.isFavourite = isFavourite;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(String travelTime) {
            this.travelTime = travelTime;
        }

        public List<Cuisines> getCuisines() {
            return cuisines;
        }

        public void setCuisines(List<Cuisines> cuisines) {
            this.cuisines = cuisines;
        }

        public int getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(int isOpen) {
            this.isOpen = isOpen;
        }

        public Float getRating() {
            return rating;
        }

        public void setRating(Float rating) {
            this.rating = rating;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class Cuisines {
            @Expose
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }


}

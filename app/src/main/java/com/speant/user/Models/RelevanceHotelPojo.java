package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RelevanceHotelPojo {


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

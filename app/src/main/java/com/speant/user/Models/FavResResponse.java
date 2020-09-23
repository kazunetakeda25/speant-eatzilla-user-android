package com.speant.user.Models;

import java.util.List;

public class FavResResponse {
    private boolean status;

    private List<FavouriteList> favourite_list;

    private String message;

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

    public List<FavouriteList> getFavourite_list() {
        return favourite_list;
    }

    public void setFavourite_list(List<FavouriteList> favourite_list) {
        this.favourite_list = favourite_list;
    }

    public class FavouriteList {
        private String price;

        private String address;

        private String name;

        private int is_open;

        private String restaurant_id;

        private List<NearRestarentPojo.Restaurants.Cuisines> cuisines;

        private String image;

        private String rating;

        private String travel_time;

        private String discount;

        private int is_favourite;

        public int getIsFavourite() {
            return is_favourite;
        }

        public void setIsFavourite(int is_favourite) {
            this.is_favourite = is_favourite;
        }

        public List<NearRestarentPojo.Restaurants.Cuisines> getCuisines() {
            return cuisines;
        }

        public void setCuisines(List<NearRestarentPojo.Restaurants.Cuisines> cuisines) {
            this.cuisines = cuisines;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_open() {
            return is_open;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public void setRestaurant_id(String restaurant_id) {
            this.restaurant_id = restaurant_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getTravel_time() {
            return travel_time;
        }

        public void setTravel_time(String travel_time) {
            this.travel_time = travel_time;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}

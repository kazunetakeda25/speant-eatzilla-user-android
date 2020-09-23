package com.speant.user.Models;

import java.io.Serializable;
import java.util.List;

public class SearchRestaurantResponse implements Serializable {
    private boolean status;

    private List<SearchRestaurants> restaurants;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<SearchRestaurants> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<SearchRestaurants> restaurants) {
        this.restaurants = restaurants;
    }

    public class SearchRestaurants implements Serializable {
        private List<Cuisines> cuisines;

        private String id;

        private String price;

        private String address;

        private String name;

        private String is_open;

        private String image;

        private String rating;

        private String travel_time;

        private String is_favourite;

        private String discount;

        public List<Cuisines> getCuisines() {
            return cuisines;
        }

        public void setCuisines(List<Cuisines> cuisines) {
            this.cuisines = cuisines;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getIs_open() {
            return is_open;
        }

        public void setIs_open(String is_open) {
            this.is_open = is_open;
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

        public String getIs_favourite() {
            return is_favourite;
        }

        public void setIs_favourite(String is_favourite) {
            this.is_favourite = is_favourite;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public class Cuisines implements Serializable{
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

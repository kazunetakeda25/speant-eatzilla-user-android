package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotelDetailPojo {

    @Expose
    @SerializedName("cart")
    private List<Cart> cart;
    @Expose
    @SerializedName("restaurants")
    private List<Restaurants> restaurants;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

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

    public static class Cart {
        @Expose
        @SerializedName("quantity")
        private double quantity;
        @Expose
        @SerializedName("amount")
        private double amount;

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    public static class Restaurants {
        @Expose
        @SerializedName("food_list")
        private List<FoodList> foodList;
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
        private double rating;
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
        @SerializedName("credit_accept")
        private String credit_accept;
        @Expose
        @SerializedName("address")
        private String address;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("shop_description")
        private String shop_description;
        @Expose
        @SerializedName("fssai_license")
        private String fssai_license;

        public String getCredit_accept() {
            return credit_accept;
        }

        public void setCredit_accept(String credit_accept) {
            this.credit_accept = credit_accept;
        }

        public String getShop_description() {
            return shop_description;
        }

        public void setShop_description(String shop_description) {
            this.shop_description = shop_description;
        }

        public String getFssai_license() {
            return fssai_license;
        }

        public void setFssai_license(String fssai_license) {
            this.fssai_license = fssai_license;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<FoodList> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodList> foodList) {
            this.foodList = foodList;
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

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
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


        public static class FoodList {
            @Expose
            @SerializedName("item_count")
            private int itemCount;
            @Expose
            @SerializedName("is_veg")
            private int isVeg;
            @Expose
            @SerializedName("description")
            private String description;
            @Expose
            @SerializedName("price")
            private double price;
            @Expose
            @SerializedName("name")
            private String name;
            @Expose
            @SerializedName("food_id")
            private int foodId;

            public int getItemCount() {
                return itemCount;
            }

            public void setItemCount(int itemCount) {
                this.itemCount = itemCount;
            }

            public int getIsVeg() {
                return isVeg;
            }

            public void setIsVeg(int isVeg) {
                this.isVeg = isVeg;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getFoodId() {
                return foodId;
            }

            public void setFoodId(int foodId) {
                this.foodId = foodId;
            }
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

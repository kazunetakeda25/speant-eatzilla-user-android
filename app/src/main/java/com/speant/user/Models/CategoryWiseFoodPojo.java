package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryWiseFoodPojo {

    @Expose
    @SerializedName("is_favourite")
    private int isFavourite;
    @Expose
    @SerializedName("food_list")
    private List<FoodList> foodList;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public List<FoodList> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<FoodList> foodList) {
        this.foodList = foodList;
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
        private int price;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
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
}

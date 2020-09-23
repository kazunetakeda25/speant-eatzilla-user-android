package com.speant.user.Models;

import java.util.List;

public class FinalFoodList {
    public int viewType;

    public String categoryName;

    private String food_id;

    private String price;

    private String description;

    private int is_veg;

    private String name;

    private String category_id;

    private int item_count;

    private double item_tax;

    private String discount_type;

    private String target_amount;

    private String offer_amount;

    private String food_offer;

    private List<AddOns> add_ons;

    private List<FoodQuantity> food_quantity;

    public String getFood_offer() {
        return food_offer;
    }

    public void setFood_offer(String food_offer) {
        this.food_offer = food_offer;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
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

    public List<FoodQuantity> getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(List<FoodQuantity> food_quantity) {
        this.food_quantity = food_quantity;
    }

    public List<AddOns> getAdd_ons() {
        return add_ons;
    }

    public void setAdd_ons(List<AddOns> add_ons) {
        this.add_ons = add_ons;
    }

    public double getItem_tax() {
        return item_tax;
    }

    public void setItem_tax(double item_tax) {
        this.item_tax = item_tax;
    }

    public int getIs_veg() {
        return is_veg;
    }

    public void setIs_veg(int is_veg) {
        this.is_veg = is_veg;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}

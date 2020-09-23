package com.speant.user.Models;

import java.util.List;

public class TrendFoodList {
    private String image;

    private String distance;

    private String restaurant_id;

    private String packaging_charge;

    private String description;

    private String created_at;

    private String tax;

    private TrendMenu menu;

    private String is_veg;

    private String is_special;

    private String category_id;

    private String updated_at;

    private String price;

    private String name;

    private List<AddOns> add_ons;

    private String id;

    private TrendCategory category;

    private String menu_id;

    private String status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getPackaging_charge() {
        return packaging_charge;
    }

    public void setPackaging_charge(String packaging_charge) {
        this.packaging_charge = packaging_charge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public TrendMenu getMenu() {
        return menu;
    }

    public void setMenu(TrendMenu menu) {
        this.menu = menu;
    }

    public String getIs_veg() {
        return is_veg;
    }

    public void setIs_veg(String is_veg) {
        this.is_veg = is_veg;
    }

    public String getIs_special() {
        return is_special;
    }

    public void setIs_special(String is_special) {
        this.is_special = is_special;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AddOns> getAdd_ons() {
        return add_ons;
    }

    public void setAdd_ons(List<AddOns> add_ons) {
        this.add_ons = add_ons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrendCategory getCategory() {
        return category;
    }

    public void setCategory(TrendCategory category) {
        this.category = category;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

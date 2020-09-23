package com.speant.user.Common.localDb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(nameInDb = "FoodItemDb")
public class FoodItemDb {

    private String restaurant_id;

    @Id
    private String food_id;

    private int food_qty;

    private String food_name;

    private int food_type;

    private double food_cost;

    private String food_desc;
    
    private double food_tax;

    private String discount_type;

    private String target_amount;

    private String offer_amount;

    @Generated(hash = 311331984)
    public FoodItemDb(String restaurant_id, String food_id, int food_qty,
            String food_name, int food_type, double food_cost, String food_desc,
            double food_tax, String discount_type, String target_amount,
            String offer_amount) {
        this.restaurant_id = restaurant_id;
        this.food_id = food_id;
        this.food_qty = food_qty;
        this.food_name = food_name;
        this.food_type = food_type;
        this.food_cost = food_cost;
        this.food_desc = food_desc;
        this.food_tax = food_tax;
        this.discount_type = discount_type;
        this.target_amount = target_amount;
        this.offer_amount = offer_amount;
    }

    @Generated(hash = 1367885374)
    public FoodItemDb() {
    }

    public String getRestaurant_id() {
        return this.restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getFood_id() {
        return this.food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public int getFood_qty() {
        return this.food_qty;
    }

    public void setFood_qty(int food_qty) {
        this.food_qty = food_qty;
    }

    public String getFood_name() {
        return this.food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_type() {
        return this.food_type;
    }

    public void setFood_type(int food_type) {
        this.food_type = food_type;
    }

    public double getFood_cost() {
        return this.food_cost;
    }

    public void setFood_cost(double food_cost) {
        this.food_cost = food_cost;
    }

    public String getFood_desc() {
        return this.food_desc;
    }

    public void setFood_desc(String food_desc) {
        this.food_desc = food_desc;
    }

    public double getFood_tax() {
        return this.food_tax;
    }

    public void setFood_tax(double food_tax) {
        this.food_tax = food_tax;
    }

    public String getDiscount_type() {
        return this.discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getTarget_amount() {
        return this.target_amount;
    }

    public void setTarget_amount(String target_amount) {
        this.target_amount = target_amount;
    }

    public String getOffer_amount() {
        return this.offer_amount;
    }

    public void setOffer_amount(String offer_amount) {
        this.offer_amount = offer_amount;
    }

    



}

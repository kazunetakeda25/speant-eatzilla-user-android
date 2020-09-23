package com.speant.user.Common.localDb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "AddOnDb")
public class AddOnDb {

    @Id(autoincrement = true)
    private Long id;

    private String food_id;

    private String addon_id;

    private String restaurant_id;

    private String name;

    private String price;

    @Generated(hash = 648423709)
    public AddOnDb(Long id, String food_id, String addon_id, String restaurant_id,
            String name, String price) {
        this.id = id;
        this.food_id = food_id;
        this.addon_id = addon_id;
        this.restaurant_id = restaurant_id;
        this.name = name;
        this.price = price;
    }

    @Generated(hash = 928425585)
    public AddOnDb() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFood_id() {
        return this.food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getAddon_id() {
        return this.addon_id;
    }

    public void setAddon_id(String addon_id) {
        this.addon_id = addon_id;
    }

    public String getRestaurant_id() {
        return this.restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    
}

package com.speant.user.Common.localDb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "QuantityDb")
public class QuantityDb {

    @Id(autoincrement = true)
    private Long id;

    private String food_id;

    private String quantity_id;

    private String restaurant_id;

    private String name;

    private String price;

    @Generated(hash = 575737498)
    public QuantityDb(Long id, String food_id, String quantity_id,
            String restaurant_id, String name, String price) {
        this.id = id;
        this.food_id = food_id;
        this.quantity_id = quantity_id;
        this.restaurant_id = restaurant_id;
        this.name = name;
        this.price = price;
    }

    @Generated(hash = 1266335648)
    public QuantityDb() {
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

    public String getQuantity_id() {
        return this.quantity_id;
    }

    public void setQuantity_id(String quantity_id) {
        this.quantity_id = quantity_id;
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

package com.speant.user.Common.localDb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "GroupDb")
public class GroupDb {

    @Id(autoincrement = true)
    private Long id;

    private String food_id;

    private String groupData;

    private String quantity_id;

    private int groupCount;

    private double groupamount;

    private String restaurant_id;

    private int is_veg;

    @Generated(hash = 1507600194)
    public GroupDb(Long id, String food_id, String groupData, String quantity_id,
            int groupCount, double groupamount, String restaurant_id, int is_veg) {
        this.id = id;
        this.food_id = food_id;
        this.groupData = groupData;
        this.quantity_id = quantity_id;
        this.groupCount = groupCount;
        this.groupamount = groupamount;
        this.restaurant_id = restaurant_id;
        this.is_veg = is_veg;
    }

    @Generated(hash = 986446889)
    public GroupDb() {
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

    public String getGroupData() {
        return this.groupData;
    }

    public void setGroupData(String groupData) {
        this.groupData = groupData;
    }

    public String getQuantity_id() {
        return this.quantity_id;
    }

    public void setQuantity_id(String quantity_id) {
        this.quantity_id = quantity_id;
    }

    public int getGroupCount() {
        return this.groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public double getGroupamount() {
        return this.groupamount;
    }

    public void setGroupamount(double groupamount) {
        this.groupamount = groupamount;
    }

    public String getRestaurant_id() {
        return this.restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getIs_veg() {
        return this.is_veg;
    }

    public void setIs_veg(int is_veg) {
        this.is_veg = is_veg;
    }




}

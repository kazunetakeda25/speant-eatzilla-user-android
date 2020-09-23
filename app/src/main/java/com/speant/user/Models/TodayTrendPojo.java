package com.speant.user.Models;

import java.util.List;

public class TodayTrendPojo {
    private List<TrendFoodList> food_list;

    private boolean status;

    public List<TrendFoodList> getFood_list() {
        return food_list;
    }

    public void setFood_list(List<TrendFoodList> food_list) {
        this.food_list = food_list;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

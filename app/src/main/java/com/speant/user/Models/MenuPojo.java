package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuPojo {


    @Expose
    @SerializedName("menus")
    private List<Menus> menus;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public List<Menus> getMenus() {
        return menus;
    }

    public void setMenus(List<Menus> menus) {
        this.menus = menus;
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

    public static class Menus {
        @Expose
        @SerializedName("food_count")
        private int foodCount;
        @Expose
        @SerializedName("menu_name")
        private String menuName;
        @Expose
        @SerializedName("menu_id")
        private int menuId;

        public int getFoodCount() {
            return foodCount;
        }

        public void setFoodCount(int foodCount) {
            this.foodCount = foodCount;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
            this.menuId = menuId;
        }
    }
}

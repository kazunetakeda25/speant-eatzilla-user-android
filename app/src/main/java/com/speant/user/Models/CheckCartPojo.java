package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckCartPojo {

    @Expose
    @SerializedName("cart")
    private List<Cart> cart;


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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CheckCartPojo{" +
                "cart=" + cart +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public static class ItemDetails {

        @Expose
        @SerializedName("item_name")
        private String item_name;

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return "ItemDetails{" +
                    "item_name='" + item_name + '\'' +
                    ", quantity=" + quantity +
                    ", price='" + price + '\'' +
                    '}';
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Expose
        @SerializedName("quantity")
        private int quantity;

        @Expose
        @SerializedName("price")
        private String price;


    }

    public static class OrderOn {
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Expose
        @SerializedName("date")

        private String date;
    }


    public static class Cart {
        @Expose
        @SerializedName("quantity")
        private int quantity;


        @Expose
        @SerializedName("order_on")
        private OrderOn order_on;

        @Expose
        @SerializedName("item_list")
        private List<ItemDetails> item_list;

        public OrderOn getOrder_on() {
            return order_on;
        }

        public void setOrder_on(OrderOn order_on) {
            this.order_on = order_on;
        }

        @Expose

        @SerializedName("restaurant_name")
        private String restaurant_name;

        @Expose
        @SerializedName("restaurant_image")
        private String restaurant_image;

        @Expose
        @SerializedName("restaurant_address")
        private String restaurant_address;

        public String getRestaurant_name() {
            return restaurant_name;
        }

        public void setRestaurant_name(String restaurant_name) {
            this.restaurant_name = restaurant_name;
        }

        public String getRestaurant_image() {
            return restaurant_image;
        }

        public void setRestaurant_image(String restaurant_image) {
            this.restaurant_image = restaurant_image;
        }

        public String getRestaurant_address() {
            return restaurant_address;
        }

        public void setRestaurant_address(String restaurant_address) {
            this.restaurant_address = restaurant_address;
        }

        public List<ItemDetails> getItem_list() {
            return item_list;
        }

        public void setItem_list(List<ItemDetails> item_list) {
            this.item_list = item_list;
        }


        @Override
        public String toString() {
            return "Cart{" +
                    "quantity=" + quantity +
                    ", amount=" + amount +
                    '}';
        }

        @Expose
        @SerializedName("amount")
        private double amount;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}

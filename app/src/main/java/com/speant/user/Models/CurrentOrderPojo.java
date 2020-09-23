package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentOrderPojo {
    @Expose
    @SerializedName("order_status")
    private List<OrderStatus> orderStatus;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public List<OrderStatus> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<OrderStatus> orderStatus) {
        this.orderStatus = orderStatus;
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

    public static class OrderStatus {
        @Expose
        @SerializedName("delivery_boy_phone")
        private String deliveryBoyPhone;
        @Expose
        @SerializedName("delvery_boy_image")
        private String delveryBoyImage;
        @Expose
        @SerializedName("delivery_boy_id")
        private int deliveryBoyId;
        @Expose
        @SerializedName("status")
        private int status;
        @Expose
        @SerializedName("bill_amount")
        private double billAmount;
        @Expose
        @SerializedName("item_count")
        private int itemCount;
        @Expose
        @SerializedName("restaurant_image")
        private String restaurantImage;
        @Expose
        @SerializedName("restaurant_name")
        private String restaurantName;
        @Expose
        @SerializedName("ordered_time")
        private String orderedTime;
        @Expose
        @SerializedName("order_id")
        private String orderId;
        @Expose
        @SerializedName("request_id")
        private int requestId;
        @Expose
        @SerializedName("delivery_type")
        private String delivery_type;
        @Expose
        @SerializedName("pickup_dining_time")
        private String pickup_dining_time;

        public String getPickup_dining_time() {
            return pickup_dining_time;
        }

        public void setPickup_dining_time(String pickup_dining_time) {
            this.pickup_dining_time = pickup_dining_time;
        }

        public String getDelivery_type() {
            return delivery_type;
        }

        public void setDelivery_type(String delivery_type) {
            this.delivery_type = delivery_type;
        }

        public String getDeliveryBoyPhone() {
            return deliveryBoyPhone;
        }

        public void setDeliveryBoyPhone(String deliveryBoyPhone) {
            this.deliveryBoyPhone = deliveryBoyPhone;
        }

        public String getDelveryBoyImage() {
            return delveryBoyImage;
        }

        public void setDelveryBoyImage(String delveryBoyImage) {
            this.delveryBoyImage = delveryBoyImage;
        }

        public int getDeliveryBoyId() {
            return deliveryBoyId;
        }

        public void setDeliveryBoyId(int deliveryBoyId) {
            this.deliveryBoyId = deliveryBoyId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(double billAmount) {
            this.billAmount = billAmount;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public String getRestaurantImage() {
            return restaurantImage;
        }

        public void setRestaurantImage(String restaurantImage) {
            this.restaurantImage = restaurantImage;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public String getOrderedTime() {
            return orderedTime;
        }

        public void setOrderedTime(String orderedTime) {
            this.orderedTime = orderedTime;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getRequestId() {
            return requestId;
        }

        public void setRequestId(int requestId) {
            this.requestId = requestId;
        }
    }
}

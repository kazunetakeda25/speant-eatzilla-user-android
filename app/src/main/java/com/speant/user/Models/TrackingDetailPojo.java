package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackingDetailPojo {

    @Expose
    @SerializedName("tracking_detail")
    private List<TrackingDetail> trackingDetail;
    @Expose
    @SerializedName("order_status")
    private List<OrderStatus> orderStatus;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("message")
    private String message;

    @Override
    public String toString() {
        return "TrackingDetailPojo{" +
                "trackingDetail=" + trackingDetail +
                ", orderStatus=" + orderStatus +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    public List<TrackingDetail> getTrackingDetail() {
        return trackingDetail;
    }

    public void setTrackingDetail(List<TrackingDetail> trackingDetail) {
        this.trackingDetail = trackingDetail;
    }

    public List<OrderStatus> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<OrderStatus> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class TrackingDetail {
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("detail")
        private String detail;
        @Expose
        @SerializedName("status")
        private int status;
        @Expose
        @SerializedName("request_id")
        private int requestId;

        @Override
        public String toString() {
            return "TrackingDetail{" +
                    "updatedAt='" + updatedAt + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", detail='" + detail + '\'' +
                    ", status=" + status +
                    ", requestId=" + requestId +
                    ", id=" + id +
                    '}';
        }

        @Expose
        @SerializedName("id")
        private int id;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getRequestId() {
            return requestId;
        }

        public void setRequestId(int requestId) {
            this.requestId = requestId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class OrderStatus {
        @Expose
        @SerializedName("user_lng")
        private double userLng;
        @Expose
        @SerializedName("user_lat")
        private double userLat;
        @Expose
        @SerializedName("restaurant_lng")
        private double restaurantLng;
        @Expose
        @SerializedName("restaurant_lat")
        private double restaurantLat;
        @Expose
        @SerializedName("delivery_boy_phone")
        private String deliveryBoyPhone;
        @Expose
        @SerializedName("delivery_boy_image")
        private String deliveryBoyImage;
        @Expose
        @SerializedName("delivery_boy_name")
        private String deliveryBoyName;
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
        @SerializedName("delivery_charge")
        private int deliveryCharge;
        @Expose
        @SerializedName("tax")
        private double tax;
        @Expose
        @SerializedName("restaurant_packaging_charge")
        private int restaurantPackagingCharge;
        @Expose
        @SerializedName("offer_discount")
        private double offerDiscount;
        @Expose
        @SerializedName("restaurant_discount")
        private double restaurant_discount ;
        @Expose
        @SerializedName("item_total")
        private int itemTotal;
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
        private String requestId;
        @Expose
        @SerializedName("restaurant_address")
        private String restaurant_address;
        @Expose
        @SerializedName("delivery_type")
        private String delivery_type;
        @Expose
        @SerializedName("total_members")
        private String total_members;
        @Expose
        @SerializedName("pickup_dining_time")
        private String pickup_dining_time;
        @Expose
        @SerializedName("restaurant_phone")
        private String restaurant_phone;

        @Override
        public String toString() {
            return "OrderStatus{" +
                    "userLng=" + userLng +
                    ", userLat=" + userLat +
                    ", restaurantLng=" + restaurantLng +
                    ", restaurantLat=" + restaurantLat +
                    ", deliveryBoyPhone='" + deliveryBoyPhone + '\'' +
                    ", deliveryBoyImage='" + deliveryBoyImage + '\'' +
                    ", deliveryBoyName='" + deliveryBoyName + '\'' +
                    ", deliveryBoyId=" + deliveryBoyId +
                    ", status=" + status +
                    ", billAmount=" + billAmount +
                    ", deliveryCharge=" + deliveryCharge +
                    ", tax=" + tax +
                    ", restaurantPackagingCharge=" + restaurantPackagingCharge +
                    ", offerDiscount=" + offerDiscount +
                    ", itemTotal=" + itemTotal +
                    ", itemCount=" + itemCount +
                    ", restaurantImage='" + restaurantImage + '\'' +
                    ", restaurantName='" + restaurantName + '\'' +
                    ", orderedTime='" + orderedTime + '\'' +
                    ", orderId='" + orderId + '\'' +
                    ", requestId='" + requestId + '\'' +
                    '}';
        }

        public double getRestaurant_discount() {
            return restaurant_discount;
        }

        public void setRestaurant_discount(double restaurant_discount) {
            this.restaurant_discount = restaurant_discount;
        }

        public String getRestaurant_address() {
            return restaurant_address;
        }

        public void setRestaurant_address(String restaurant_address) {
            this.restaurant_address = restaurant_address;
        }

        public String getDelivery_type() {
            return delivery_type;
        }

        public void setDelivery_type(String delivery_type) {
            this.delivery_type = delivery_type;
        }

        public String getTotal_members() {
            return total_members;
        }

        public void setTotal_members(String total_members) {
            this.total_members = total_members;
        }

        public String getPickup_dining_time() {
            return pickup_dining_time;
        }

        public void setPickup_dining_time(String pickup_dining_time) {
            this.pickup_dining_time = pickup_dining_time;
        }

        public String getRestaurant_phone() {
            return restaurant_phone;
        }

        public void setRestaurant_phone(String restaurant_phone) {
            this.restaurant_phone = restaurant_phone;
        }

        public double getUserLng() {
            return userLng;
        }

        public void setUserLng(double userLng) {
            this.userLng = userLng;
        }

        public double getUserLat() {
            return userLat;
        }

        public void setUserLat(double userLat) {
            this.userLat = userLat;
        }

        public double getRestaurantLng() {
            return restaurantLng;
        }

        public void setRestaurantLng(double restaurantLng) {
            this.restaurantLng = restaurantLng;
        }

        public double getRestaurantLat() {
            return restaurantLat;
        }

        public void setRestaurantLat(double restaurantLat) {
            this.restaurantLat = restaurantLat;
        }

        public String getDeliveryBoyPhone() {
            return deliveryBoyPhone;
        }

        public void setDeliveryBoyPhone(String deliveryBoyPhone) {
            this.deliveryBoyPhone = deliveryBoyPhone;
        }

        public String getDeliveryBoyImage() {
            return deliveryBoyImage;
        }

        public void setDeliveryBoyImage(String deliveryBoyImage) {
            this.deliveryBoyImage = deliveryBoyImage;
        }

        public String getDeliveryBoyName() {
            return deliveryBoyName;
        }

        public void setDeliveryBoyName(String deliveryBoyName) {
            this.deliveryBoyName = deliveryBoyName;
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

        public int getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(int deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public double getTax() {
            return tax;
        }

        public void setTax(double tax) {
            this.tax = tax;
        }

        public int getRestaurantPackagingCharge() {
            return restaurantPackagingCharge;
        }

        public void setRestaurantPackagingCharge(int restaurantPackagingCharge) {
            this.restaurantPackagingCharge = restaurantPackagingCharge;
        }

        public double getOfferDiscount() {
            return offerDiscount;
        }

        public void setOfferDiscount(double offerDiscount) {
            this.offerDiscount = offerDiscount;
        }

        public int getItemTotal() {
            return itemTotal;
        }

        public void setItemTotal(int itemTotal) {
            this.itemTotal = itemTotal;
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

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }
}

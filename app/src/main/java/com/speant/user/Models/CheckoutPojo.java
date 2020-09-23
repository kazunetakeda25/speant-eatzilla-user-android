package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CheckoutPojo {

    @Expose
    @SerializedName("invoice")
    private List<Invoice> invoice;

    @Expose
    @SerializedName("food_detail")
    private List<FoodDetail> foodDetail;

    @Expose
    @SerializedName("restaurant_detail")
    private List<RestaurantDetail> restaurantDetail;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("status")
    private boolean status;


    public List<Invoice> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<Invoice> invoice) {
        this.invoice = invoice;
    }

    public List<FoodDetail> getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(List<FoodDetail> foodDetail) {
        this.foodDetail = foodDetail;
    }

    public List<RestaurantDetail> getRestaurantDetail() {
        return restaurantDetail;
    }

    public void setRestaurantDetail(List<RestaurantDetail> restaurantDetail) {
        this.restaurantDetail = restaurantDetail;
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

    public static class Invoice {
        @Expose
        @SerializedName("coupon_code")
        private String couponCode;
        @Expose
        @SerializedName("bill_amount")
        private double billAmount;
        @Expose
        @SerializedName("delivery_charge")
        private double deliveryCharge;
        @Expose
        @SerializedName("gst")
        private double gst;
        @Expose
        @SerializedName("restaurant_packaging_charge")
        private double restaurantPackagingCharge;
        @Expose
        @SerializedName("offer_discount")
        private double offerDiscount;
        @Expose
        @SerializedName("item_total")
        private double itemTotal;

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }

        public double getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(double billAmount) {
            this.billAmount = billAmount;
        }

        public double getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(double deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public double getGst() {
            return gst;
        }

        public void setGst(double gst) {
            this.gst = gst;
        }

        public double getRestaurantPackagingCharge() {
            return restaurantPackagingCharge;
        }

        public void setRestaurantPackagingCharge(double restaurantPackagingCharge) {
            this.restaurantPackagingCharge = restaurantPackagingCharge;
        }

        public double getOfferDiscount() {
            return offerDiscount;
        }

        public void setOfferDiscount(double offerDiscount) {
            this.offerDiscount = offerDiscount;
        }

        public double getItemTotal() {
            return itemTotal;
        }

        public void setItemTotal(double itemTotal) {
            this.itemTotal = itemTotal;
        }
    }

    public static class FoodDetail {
        @Expose
        @SerializedName("quantity")
        private int quantity;
        @Expose
        @SerializedName("is_veg")
        private int isVeg;
        @Expose
        @SerializedName("price")
        private int price;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("food_id")
        private int foodId;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getIsVeg() {
            return isVeg;
        }

        public void setIsVeg(int isVeg) {
            this.isVeg = isVeg;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getFoodId() {
            return foodId;
        }

        public void setFoodId(int foodId) {
            this.foodId = foodId;
        }
    }

    public static class RestaurantDetail {
        @Expose
        @SerializedName("estimated_delivery_time")
        private String estimatedDeliveryTime;
        @Expose
        @SerializedName("address")
        private String address;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("restaurant_id")
        private int restaurantId;
        @Expose
        @SerializedName("delivery_type")
        private List<String> deliveryType;
        @Expose
        @SerializedName("weekday_opening_time")
        private String weekday_opening_time;
        @Expose
        @SerializedName("weekday_closing_time")
        private String weekday_closing_time;
        @Expose
        @SerializedName("weekend_opening_time")
        private String weekend_opening_time;
        @Expose
        @SerializedName("weekend_closing_time")
        private String weekend_closing_time;
        @Expose
        @SerializedName("max_dining_count")
        private String max_dining_count;
        @Expose
        @SerializedName("credit_accept")
        private String credit_accept;
        @Expose
        @SerializedName("restaurant_timing")
        private List<RestaurantTiming> restaurant_timing;

        public String getCredit_accept() {
            return credit_accept;
        }

        public void setCredit_accept(String credit_accept) {
            this.credit_accept = credit_accept;
        }

        public List<RestaurantTiming> getRestaurant_timing() {
            return restaurant_timing;
        }

        public void setRestaurant_timing(List<RestaurantTiming> restaurant_timing) {
            this.restaurant_timing = restaurant_timing;
        }

        public String getMax_dining_count() {
            return max_dining_count;
        }

        public void setMax_dining_count(String max_dining_count) {
            this.max_dining_count = max_dining_count;
        }

        public List<String> getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(List<String> deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getWeekday_opening_time() {
            return weekday_opening_time;
        }

        public void setWeekday_opening_time(String weekday_opening_time) {
            this.weekday_opening_time = weekday_opening_time;
        }

        public String getWeekday_closing_time() {
            return weekday_closing_time;
        }

        public void setWeekday_closing_time(String weekday_closing_time) {
            this.weekday_closing_time = weekday_closing_time;
        }

        public String getWeekend_opening_time() {
            return weekend_opening_time;
        }

        public void setWeekend_opening_time(String weekend_opening_time) {
            this.weekend_opening_time = weekend_opening_time;
        }

        public String getWeekend_closing_time() {
            return weekend_closing_time;
        }

        public void setWeekend_closing_time(String weekend_closing_time) {
            this.weekend_closing_time = weekend_closing_time;
        }

        public String getEstimatedDeliveryTime() {
            return estimatedDeliveryTime;
        }

        public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
            this.estimatedDeliveryTime = estimatedDeliveryTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
        }
    }
}

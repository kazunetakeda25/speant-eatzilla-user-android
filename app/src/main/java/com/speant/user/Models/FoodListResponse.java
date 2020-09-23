package com.speant.user.Models;

import java.util.List;

public class FoodListResponse {
    private boolean status;

    private List<FoodList> food_list;

    private RestaurantData restaurant_detail;

    public RestaurantData getRestaurant_detail() {
        return restaurant_detail;
    }

    public void setRestaurant_detail(RestaurantData restaurant_detail) {
        this.restaurant_detail = restaurant_detail;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<FoodList> getFood_list() {
        return food_list;
    }

    public void setFood_list(List<FoodList> food_list) {
        this.food_list = food_list;
    }

    public class FoodList {
        private String category_name;

        private List<Items> items;

        private String category_id;

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public List<Items> getItems() {
            return items;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public class Items {
            private String food_id;

            private String price;

            private String description;

            private String food_offer;

            private int is_veg;

            private String name;

            private String category_id;

            private String discount_type;

            private String target_amount;

            private String offer_amount;

            private List<AddOns> add_ons;

            private List<FoodQuantity> food_quantity;

            private int item_count;

            private double item_tax;

            public String getFood_offer() {
                return food_offer;
            }

            public void setFood_offer(String food_offer) {
                this.food_offer = food_offer;
            }

            public String getDiscount_type() {
                return discount_type;
            }

            public void setDiscount_type(String discount_type) {
                this.discount_type = discount_type;
            }

            public String getTarget_amount() {
                return target_amount;
            }

            public void setTarget_amount(String target_amount) {
                this.target_amount = target_amount;
            }

            public String getOffer_amount() {
                return offer_amount;
            }

            public void setOffer_amount(String offer_amount) {
                this.offer_amount = offer_amount;
            }

            public List<FoodQuantity> getFood_quantity() {
                return food_quantity;
            }

            public void setFood_quantity(List<FoodQuantity> food_quantity) {
                this.food_quantity = food_quantity;
            }

            public List<AddOns> getAdd_ons() {
                return add_ons;
            }

            public void setAdd_ons(List<AddOns> add_ons) {
                this.add_ons = add_ons;
            }

            public double getItem_tax() {
                return item_tax;
            }

            public void setItem_tax(double item_tax) {
                this.item_tax = item_tax;
            }

            public int getIs_veg() {
                return is_veg;
            }

            public void setIs_veg(int is_veg) {
                this.is_veg = is_veg;
            }

            public int getItem_count() {
                return item_count;
            }

            public void setItem_count(int item_count) {
                this.item_count = item_count;
            }

            public String getFood_id() {
                return food_id;
            }

            public void setFood_id(String food_id) {
                this.food_id = food_id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }
        }
    }
}

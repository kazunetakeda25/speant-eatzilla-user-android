package com.speant.user.Models;

public class Pivot {
        private String price;

        private String foodquantity_id;

        private String is_default;

        private String foodlist_id;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getFoodquantity_id() {
            return foodquantity_id;
        }

        public void setFoodquantity_id(String foodquantity_id) {
            this.foodquantity_id = foodquantity_id;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getFoodlist_id() {
            return foodlist_id;
        }

        public void setFoodlist_id(String foodlist_id) {
            this.foodlist_id = foodlist_id;
        }
    }
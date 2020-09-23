package com.speant.user.Models;

import java.util.List;

public class OfferResponse {
    private boolean status;
    private List<PromoList> promo_list;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<PromoList> getPromo_list() {
        return promo_list;
    }

    public void setPromo_list(List<PromoList> promo_list) {
        this.promo_list = promo_list;
    }

    public class PromoList {
        private String id;
        private String code;
        private String offer_type;
        private String value;
        private String available_from;
        private String valid_till;
        private String use_per_customer;
        private String total_use;
        private String status;
        private String title;
        private String description;
        private String created_at;
        private String coupon_value;
        private String updated_at;

        public String getCoupon_value() {
            return coupon_value;
        }

        public void setCoupon_value(String coupon_value) {
            this.coupon_value = coupon_value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOffer_type() {
            return offer_type;
        }

        public void setOffer_type(String offer_type) {
            this.offer_type = offer_type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getAvailable_from() {
            return available_from;
        }

        public void setAvailable_from(String available_from) {
            this.available_from = available_from;
        }

        public String getValid_till() {
            return valid_till;
        }

        public void setValid_till(String valid_till) {
            this.valid_till = valid_till;
        }

        public String getUse_per_customer() {
            return use_per_customer;
        }

        public void setUse_per_customer(String use_per_customer) {
            this.use_per_customer = use_per_customer;
        }

        public String getTotal_use() {
            return total_use;
        }

        public void setTotal_use(String total_use) {
            this.total_use = total_use;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}

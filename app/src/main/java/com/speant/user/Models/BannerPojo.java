package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class BannerPojo {

    @Expose
    @SerializedName("base_url")
    private String baseUrl;
    @Expose
    @SerializedName("data")
    private List<Data> data;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
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

    public static class Data {
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("status")
        private int status;
        @Expose
        @SerializedName("description")
        private String description;
        @Expose
        @SerializedName("title")
        private String title;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("restaurant_id")
        private int restaurantId;
        @Expose
        @SerializedName("position")
        private int position;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "updatedAt='" + updatedAt + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", status=" + status +
                    ", description='" + description + '\'' +
                    ", title='" + title + '\'' +
                    ", image='" + image + '\'' +
                    ", restaurantId=" + restaurantId +
                    ", position=" + position +
                    ", id=" + id +
                    '}';
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

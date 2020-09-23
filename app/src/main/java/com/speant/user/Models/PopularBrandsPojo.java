package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularBrandsPojo {

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
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("name")
        private String name;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

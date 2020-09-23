package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterPojo {

    @Expose
    @SerializedName("data")
    private List<Data> data;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("message")
    private String errorMessage;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public static class Data {
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("is_select")
        private boolean isSelect= false;
        @Expose
        @SerializedName("status")
        private int status;
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

        public boolean getIsSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPojo {

    @Expose
    @SerializedName("user_name")
    private String userName;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("profile_image")
    private String profileImage;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("authToken")
    private String authtoken;
    @Expose
    @SerializedName("authId")
    private int authid;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public int getAuthid() {
        return authid;
    }

    public void setAuthid(int authid) {
        this.authid = authid;
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
}

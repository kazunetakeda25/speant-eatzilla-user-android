package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessPojo {

    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("otp")
    private String otp;
    @Expose
    @SerializedName("error_code")
    private int errorCode;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("is_new_user")
    private String is_new_user;
    @Expose
    @SerializedName("hash_key")
    private String hash_key;

    public String getHash_key() {
        return hash_key;
    }

    public void setHash_key(String hash_key) {
        this.hash_key = hash_key;
    }

    public String getIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(String is_new_user) {
        this.is_new_user = is_new_user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}

package com.speant.user.Models;

public class CreditStatusResponse {
    public boolean status;
    public CreditDetails details;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CreditDetails getDetails() {
        return details;
    }

    public void setDetails(CreditDetails details) {
        this.details = details;
    }
}

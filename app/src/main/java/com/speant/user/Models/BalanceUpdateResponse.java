package com.speant.user.Models;

public class BalanceUpdateResponse {
    private boolean status;
    private String message;
    private String wallet_balance;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }
}

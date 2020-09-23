package com.speant.user.Models;

public class WalletBalanceResponse {
    private boolean status;
    private String wallet_balance;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }
}

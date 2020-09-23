package com.speant.user.Models;

public class AddCardRequest {

    private String last_four;
    private String card_token;
    private String customer_id;

    public String getLast_four() {
        return last_four;
    }

    public void setLast_four(String last_four) {
        this.last_four = last_four;
    }

    public String getCard_token() {
        return card_token;
    }

    public void setCard_token(String card_token) {
        this.card_token = card_token;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
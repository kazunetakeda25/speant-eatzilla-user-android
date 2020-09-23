package com.speant.user.Models;

import java.util.List;

public class CardListResponse {
    private List<CardData> data;

    private boolean status;

    public List<CardData> getData() {
        return data;
    }

    public void setData(List<CardData> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
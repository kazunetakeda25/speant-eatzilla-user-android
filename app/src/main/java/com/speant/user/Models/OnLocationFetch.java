package com.speant.user.Models;

import java.util.HashMap;

public class OnLocationFetch {
    private HashMap<String, Object> intentHashMap;

    public OnLocationFetch(HashMap<String, Object> intentHashMap) {
        this.intentHashMap = intentHashMap;
    }

    public HashMap<String, Object> getIntentHashMap() {
        return intentHashMap;
    }

    public void setIntentHashMap(HashMap<String, Object> intentHashMap) {
        this.intentHashMap = intentHashMap;
    }
}

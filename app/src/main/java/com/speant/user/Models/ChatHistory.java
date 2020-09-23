package com.speant.user.Models;

import java.util.ArrayList;

public class ChatHistory {

    ArrayList<ChatList> data;

    public String success;

    public ArrayList<ChatList> getData() {
        return data;
    }

    public void setData(ArrayList<ChatList> data) {
        this.data = data;
    }

    public String chat_status;

    public String getChat_status() {
        return chat_status;
    }

    public void setChat_status(String chat_status) {
        this.chat_status = chat_status;
    }


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}

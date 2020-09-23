package com.speant.user.Models;

public class ChatMessage {

    public String from;
    public String provider_id;
    public String user_id;
    public String request_id;
    public String type;
    public String to;


    public String message;


    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "from='" + from + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", request_id='" + request_id + '\'' +
                ", type='" + type + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}

package com.example.bingewatchers;


public class Message {
    public String message, senderEmail, name, type;
    String time;
    String url;

    public Message() {
    }


    public Message(String name, String message, String time, String senderEmail, String url, String type) {
        this.message = message;
        this.time = time;
        this.name = name;
        this.url = url;
        this.senderEmail = senderEmail;
        this.type = type;
    }

    public Message(String name, String message, String time, String email, String type) {
        this.type = type;
        this.message = message;
        this.name = name;
        this.time = time;
        this.senderEmail = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }
}


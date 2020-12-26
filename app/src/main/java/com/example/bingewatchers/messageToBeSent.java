package com.example.bingewatchers;

import java.net.URL;

public class messageToBeSent {
    URL url;
    private String message, time, senderEmail, name,type;

    public messageToBeSent(String message, String time, String senderEmail) {
        this.message = message;
        this.time = time;
        this.senderEmail = senderEmail;
    }

    public messageToBeSent(String name, String message, String time, String senderEmail,String type) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.senderEmail = senderEmail;
        this.type=type;
    }

    public messageToBeSent(String message, String time, String senderEmail, URL url) {
        this.message = message;
        this.time = time;
        this.url = url;
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }
}

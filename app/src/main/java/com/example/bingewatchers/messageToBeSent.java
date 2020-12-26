package com.example.bingewatchers;

import com.google.firebase.database.Exclude;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class messageToBeSent {
    public String message, time, senderEmail, name, type;
    URL url;
    Map obj;

    public messageToBeSent() {
    }

    public messageToBeSent(String message, String time, String senderEmail) {
        this.message = message;
        this.time = time;
        this.senderEmail = senderEmail;
    }

    public messageToBeSent(String name, String message, String time, String senderEmail, String type) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.senderEmail = senderEmail;
        this.type = type;
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

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
 public void setObj(Map obj){
        this.obj = obj;

 }
    @Exclude
    public Map<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("message", message);
        result.put("time", time);
        result.put("senderEmail", senderEmail);
        result.put("name", name);
        result.put("type", type);


        return result;
    }
}

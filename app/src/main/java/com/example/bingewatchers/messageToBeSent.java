package com.example.bingewatchers;

public class messageToBeSent {
private String message,time, senderEmail,url;

    public messageToBeSent(String message, String time, String senderEmail) {
        this.message= message;
        this.time= time;
        this.senderEmail=senderEmail;
    }
    public messageToBeSent(String message, String time, String senderEmail,String url) {
        this.message= message;
        this.time= time;
        this.url=url;
        this.senderEmail=senderEmail;
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

    public String getUrl() {
        return url;
    }
}

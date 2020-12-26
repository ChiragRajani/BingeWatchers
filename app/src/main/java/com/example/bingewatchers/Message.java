package com.example.bingewatchers;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.net.URL;
import java.util.Date;

public class Message implements IMessage {
    public String message, senderEmail, name, type;
    Date time;
    URL url;

    public Message() {
    }


    public Message(String message, Date time, String senderEmail) {
        this.message = message;
        this.time = time;
        this.senderEmail = senderEmail;
    }
//
//    public Message(String name, String message, Date time, String senderEmail, String type) {
//        this.name = name;
//        this.message = message;
//        this.time = time;
//        this.senderEmail = senderEmail;
//        this.type = type;
//    }
//
//    public Message(String message, String time, String senderEmail, URL url) {
//        this.message = message;
//        this.time = time;
//        this.url = url;
//        this.senderEmail = senderEmail;
//    }

    public Message(String name, String message, Date time, String email, String type) {
        this.type = type;
        this.message = message;
        this.name=name;
        this.time = time;
        this.senderEmail = email;
    }


    @Override
    public String getId() {
        return senderEmail;
    }

    @Override
    public String getText() {
        return message;
    }

    @Override
    public IUser getUser() {
        return null;
    }

    @Override
    public Date getCreatedAt() {
        return time;
    }
}


package com.example.bingewatchers;

public class Suggestion {
    String sender, date, grpname;
    Movie y;

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public String getGrpname() {
        return grpname;
    }

    public Movie getY() {
        return y;
    }

    public Suggestion(Movie y, String sender, String date, String grpname) {
        this.sender = sender;
        this.date = date;
        this.grpname = grpname;
        this.y = y;
    }
}

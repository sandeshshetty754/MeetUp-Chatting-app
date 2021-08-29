package com.example.meetup;

public class Conv {

    public boolean seen;
    public long timestamp;
    String date;

    public Conv(){

    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Conv(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
    public String getDate(){

        return date;
    }

    public void setDate(String date){
        this.date = date;
    }
}


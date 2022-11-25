package com.example.cse3310project;

public class Messages {
    private String text;
    private String date;
    private String time;
    private String name;

    public String getText(){ return text;}
    public void setText(){ this.text = text;}

    public String getDate(){ return date;}
    public void setDate(String date) { this.date = date;}

    public String getTime(){ return time;}
    public void setTime(String time) { this.time = time;}

    public String getName(){ return name;}
    public void setName(String name) { this.name = name;}

    public Messages(String text, String date, String time, String name){
        this.text = text;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public Messages() {

    }
}

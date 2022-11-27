package com.example.cse3310project;

public class tutorpost {
    private String name;
    private String email;
    private String tid;
    private String posterid;
    private Boolean request;
    private String field;
    private String schedule;
    private String price;

    public void setName(String name){this.name = name;}
    public String getName(){return name;}

    public void setTid(String lname){this.tid = tid;}
    public String getTid(){return tid;}

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

    public void setPosterid(String posterid){this.posterid = posterid;}
    public String getPosterid(){return posterid;}

    public void setField(String field){this.field = field;}
    public String getField(){return field;}

    public void setSchedule(String schedule){this.schedule = schedule;}
    public String getSchedule(){return schedule;}

    public void setRequest(Boolean request){this.request = request;}
    public Boolean getRequest(){return request;}

    public void setPrice(String price){this.price = price;}
    public String getPrice(){return price;}

    public tutorpost(String name, String posterid, String emailaddy, String field, String schedule, String price, Boolean request){
        this.name = name;
        this.posterid = posterid;
        this.email = emailaddy;
        this.request = request;
        this.field = field;
        this.schedule = schedule;
        this.price = price;
    }

    public tutorpost(){

    }
}

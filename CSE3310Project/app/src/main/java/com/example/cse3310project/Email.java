package com.example.cse3310project;

import java.util.ArrayList;

public class Email {
    private String senderid;
    private ArrayList<String> replyids;
    private ArrayList<String> recievers;
    private Messages email;
    private String eid;
    private String subject;

    public String getEid(){ return eid; }
    public void setEid(String chatid){ this.eid = eid;}

    public String getSubject(){ return subject; }
    public void setSubject(String subject){ this.subject = subject;}

    public ArrayList<String> getRecievers() { return recievers; }
    public void setRecievers(ArrayList<String> recievers){ this.recievers = recievers;}

    public Messages getEmail() { return email;}
    public void setEmail(Messages email) { this.email = email;}

    public String getSenderid(){ return senderid; }
    public void setSenderid(String senderid){ this.senderid = senderid;}

    public Email(ArrayList<String> recievers, Messages email, String senderid, String subject){
        this.recievers = recievers;
        this.replyids = new ArrayList<>();
        this.eid = eid;
        this.senderid = senderid;
        this.email = email;
        this.subject = subject;
    }

    public Email(){

    }
}

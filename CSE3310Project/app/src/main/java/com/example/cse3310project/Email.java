package com.example.cse3310project;

import java.util.ArrayList;

public class Email {
    private String senderid;
    private ArrayList<String> replyids;
    private String reciever;
    private Messages email;
    private String eid;
    private String subject;

    public ArrayList<String> getReplyids() { return replyids;}
    public void setReplyids(ArrayList<String> replyids){ this.replyids = replyids;}

    public String getEid(){ return eid; }
    public void setEid(String eid){ this.eid = eid;}

    public String getSubject(){ return subject; }
    public void setSubject(String subject){ this.subject = subject;}

    public String getReciever() { return reciever; }
    public void setReciever(String reciever){ this.reciever = reciever;}

    public Messages getEmail() { return email;}
    public void setEmail(Messages email) { this.email = email;}

    public String getSenderid(){ return senderid; }
    public void setSenderid(String senderid){ this.senderid = senderid;}

    public Email(String reciever, Messages email, String senderid, String subject){
        this.reciever = reciever;
        this.replyids = new ArrayList<>();
        this.eid = "";
        this.senderid = senderid;
        this.email = email;
        this.subject = subject;
    }

    public Email(){

    }
}

package com.example.cse3310project;

import java.util.ArrayList;

public class chatroom {
    private ArrayList<String> uids;
    private ArrayList<Messages> chat;
    private String chatid;

    public String getChatid(){ return chatid; }
    public void setChatid(String chatid){ this.chatid = chatid;}

    public ArrayList<String> getUids() { return uids; }
    public void setUids(ArrayList<String> uids){ this.uids = uids;}

    public ArrayList<Messages> getChat() { return chat;}
    public void setChat(ArrayList<Messages> chat) { this.chat = chat;}

    public chatroom(String chatid, ArrayList<String> uids){
        this.uids = uids;
        this.chat = null;
        this.chatid = chatid;
    }

    public chatroom(){

    }
}

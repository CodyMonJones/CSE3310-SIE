package com.example.cse3310project;

import java.util.ArrayList;

public class chatroom {
    private ArrayList<String> uids;
    private ArrayList<Messages> chat;
    private String chatid;
    private String name;

    public String getChatid(){ return chatid; }
    public void setChatid(String chatid){ this.chatid = chatid;}

    public ArrayList<String> getUids() { return uids; }
    public void setUids(ArrayList<String> uids){ this.uids = uids;}

    public ArrayList<Messages> getChat() { return chat;}
    public void setChat(ArrayList<Messages> chat) { this.chat = chat;}

    public String getName(){ return name; }
    public void setName(String name){ this.name = name;}

    public chatroom(ArrayList<String> uids, String name){
        this.uids = uids;
        this.chat = null;
        this.chatid = chatid;
        this.name = name;
    }

    public chatroom(){

    }
}

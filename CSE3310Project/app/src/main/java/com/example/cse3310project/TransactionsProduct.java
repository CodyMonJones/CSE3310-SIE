package com.example.cse3310project;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.firebase.Timestamp;

public class TransactionsProduct{
    private String title;
    private String desc;
    private String imageRef;
    private String price;
    private String lendTime;
    private String exchange;
    private String uniqueID;
    private Timestamp timestamp;

    public TransactionsProduct(String title, String desc, String imageRef, String price, String lendTime, String exchange, String uniqueID, Timestamp timestamp){
        this.title = title;
        this.desc = desc;
        this.imageRef = imageRef;
        this.price = price;
        this.lendTime = lendTime;
        this.exchange = exchange;
        this.uniqueID = uniqueID;
        this.timestamp = timestamp;
    }

    public TransactionsProduct()
    {

    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getDesc(){
        return desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getImageRef(){
        return imageRef;
    }
    public void setImageRef(String imageRef){
        this.imageRef = imageRef;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    public String getLendTime(){
        return lendTime;
    }
    public void setLendTime(String lendTime){
        this.lendTime = lendTime;
    }

    public String getExchange(){
        return exchange;
    }
    public void setExchange(String exchange){
        this.exchange = exchange;
    }

    public String getUniqueID() {
        return uniqueID;
    }
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


}

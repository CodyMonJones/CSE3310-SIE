package com.example.cse3310project;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionsProduct implements Parcelable {
    private String title;
    private String desc;
    private String imageRef;
    private Double price;
    private String lendTime;
    private String exchange;
    private String uniqueID;
    private String sellerID;
    private String datePosted;
    private Timestamp timestamp;

    public TransactionsProduct(String title, String desc, String imageRef, Double price, String lendTime, String exchange, String uniqueID, String sellerID, String datePosted, Timestamp timestamp){
        this.title = title;
        this.desc = desc;
        this.imageRef = imageRef;
        this.price = price;
        this.lendTime = lendTime;
        this.exchange = exchange;
        this.uniqueID = uniqueID;
        this.sellerID = sellerID;
        this.datePosted = datePosted;
        this.timestamp =timestamp;
    }

    public TransactionsProduct()
    {

    }

    protected TransactionsProduct(Parcel in) {
        title = in.readString();
        desc = in.readString();
        imageRef = in.readString();
        price = in.readDouble();
        lendTime = in.readString();
        exchange = in.readString();
        uniqueID = in.readString();
        sellerID = in.readString();
        datePosted = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<TransactionsProduct> CREATOR = new Creator<TransactionsProduct>() {
        @Override
        public TransactionsProduct createFromParcel(Parcel in) {
            return new TransactionsProduct(in);
        }

        @Override
        public TransactionsProduct[] newArray(int size) {
            return new TransactionsProduct[size];
        }
    };

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

    public Double getPrice(){
        return price;
    }
    public void setPrice(Double price){
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

    public String getSellerID() {
        return sellerID;
    }
    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getDatePosted() {
        return datePosted;
    }
    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(desc);
        parcel.writeString(imageRef);
        parcel.writeDouble(price);
        parcel.writeString(lendTime);
        parcel.writeString(exchange);
        parcel.writeString(uniqueID);
        parcel.writeString(sellerID);
        parcel.writeString(datePosted);
        parcel.writeParcelable(timestamp, i);
    }
}

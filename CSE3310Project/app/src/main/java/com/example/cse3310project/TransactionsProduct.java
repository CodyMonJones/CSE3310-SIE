package com.example.cse3310project;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class TransactionsProduct{
    private String title;
    private String desc;
    private ImageView image;
    private float price;
    private String uniqueID;

    public TransactionsProduct(String title, String desc, ImageView image, float price){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.price = price;
    }

    public TransactionsProduct()
    {

    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(){
        this.title = title;
    }
    public String getDesc(){
        return desc;
    }
    public void setDesc(){
        this.desc = desc;
    }
    public ImageView getImage(){
        return image;
    }
    public void setImage(){
        this.image = image;
    }
    public float getPrice(){
        return price;
    }
    public void setPrice(){
        this.price = price;
    }

}

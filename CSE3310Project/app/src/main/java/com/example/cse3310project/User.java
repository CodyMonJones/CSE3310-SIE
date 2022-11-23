package com.example.cse3310project;

import com.example.cse3310project.Discussion.DiscussionPost;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private String fname = "";
    private String lname = "";
    private String email = "";
    private int MavID;
    private String phoneNumber;
    private ArrayList<String> discussionPostsIDs;
    private ArrayList<String> transactionListedItemIDs;
    private ArrayList<String> transactionWishlistItemIDs;
    private ArrayList<String> transactionCartItemIDs;
    private ArrayList<contact> contactslist;
    private String profile_picture;
    private String creationDate;
    private String userID;

    public ArrayList<String> getDiscussionPostsIDs() {
        return discussionPostsIDs;
    }

    public void setDiscussionPostsIDs(ArrayList<String> discussionPostsIDs) {
        this.discussionPostsIDs = discussionPostsIDs;
    }

    public ArrayList<String> getTransactionListedItemIDs() {
        return transactionListedItemIDs;
    }

    public void setTransactionListedItemIDs(ArrayList<String> transactionListedItemIDs) {
        this.transactionListedItemIDs = transactionListedItemIDs;
    }

    public ArrayList<String> getTransactionWishlistItemIDs() {
        return transactionWishlistItemIDs;
    }

    public void setTransactionWishlistItemIDs(ArrayList<String> transactionWishlistItemIDs) {
        this.transactionWishlistItemIDs = transactionWishlistItemIDs;
    }

    public ArrayList<String> getTransactionCartItemIDs() {
        return transactionCartItemIDs;
    }

    public void setTransactionCartItemIDs(ArrayList<String> transactionCartItemIDs) {
        this.transactionCartItemIDs = transactionCartItemIDs;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMavID(int mavID) {
        MavID = mavID;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public int getMavID() {
        return MavID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<contact> getContatcs() {
        return contactslist;
    }

    public void setContactslist(ArrayList<contact> contactslist){
        this.contactslist = contactslist;
    }



    public User(String firstName, String lastName, String email, int Mav, String phone)
    {
        this.fname = firstName;
        this.lname = lastName;
        this.email = email;
        this.MavID = Mav;
        this.phoneNumber = phone;
        this.discussionPostsIDs = null;
        this.transactionListedItemIDs = null;
        this.transactionWishlistItemIDs = null;
        this.transactionCartItemIDs = null;
        this.profile_picture = null;
        this.contactslist = null;
    }
}

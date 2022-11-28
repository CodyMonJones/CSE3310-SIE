package com.example.cse3310project;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private String fname = "";
    private String lname = "";
    private String email = "";
    private int MavID;
    private String phoneNumber;
    private ArrayList<String> creditCards;
    private ArrayList<String> discussionPostsIDs;
    private ArrayList<String> tutorpostids;
    private ArrayList<String> ratings;
    private ArrayList<String> transactionListedItemIDs;
    private ArrayList<String> transactionWishlistItemIDs;
    private ArrayList<String> transactionCartItemIDs;
    private ArrayList<contact> contactslist;
    private ArrayList<String> chatids;
    private String profile_picture;
    private String creationDate;
    private String userID;
    private ArrayList<String> emailsent;
    private ArrayList<String> emailrecieved;


    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getMavID() {
        return MavID;
    }
    public void setMavID(int mavID) {
        MavID = mavID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<String> getCreditCards() {
        return creditCards;
    }
    public void setCreditCards(ArrayList<String> creditCards) {
        this.creditCards = creditCards;
    }

    public ArrayList<String> getTutorpostids() {
        return tutorpostids;
    }
    public void setTutorpostids(ArrayList<String> tutorpostids) {
        this.tutorpostids = tutorpostids;
    }

    public ArrayList<String> getRatings() {
        return ratings;
    }
    public void setRatings(ArrayList<String> ratings) {
        this.ratings = ratings;
    }

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

    public ArrayList<contact> getContactslist() {
        return contactslist;
    }
    public void setContactslist(ArrayList<contact> contactslist){
        this.contactslist = contactslist;
    }

    public ArrayList<String> getChatids() {
        return chatids;
    }
    public void setChatids(ArrayList<String> chatids){
        this.chatids = chatids;
    }

    public String getProfile_picture() {
        return profile_picture;
    }
    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
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

    public ArrayList<String> getEmailsent() {
        return emailsent;
    }
    public void setEmailsent(ArrayList<String> emailsent){
        this.emailsent = emailsent;
    }

    public ArrayList<String> getEmailrecieved() {
        return emailrecieved;
    }
    public void setEmailrecieved(ArrayList<String> emailrecieved){
        this.emailrecieved = emailrecieved;
    }

    public User(String firstName, String lastName, String email, int Mav, String phone)
    {
        this.fname = firstName;
        this.lname = lastName;
        this.email = email;
        this.MavID = Mav;
        this.phoneNumber = phone;
        this.creditCards = null;
        this.discussionPostsIDs = null;
        this.transactionListedItemIDs = null;
        this.transactionWishlistItemIDs = null;
        this.transactionCartItemIDs = null;
        this.profile_picture = null;
        this.contactslist = new ArrayList<contact>();
        this.chatids = new ArrayList<String>();
        this.emailsent = new ArrayList<>();
        this.emailrecieved = new ArrayList<>();
        this.tutorpostids = new ArrayList<String>();
<<<<<<< HEAD
        this.ratings = new ArrayList<String>();
=======
        this.ratings = new ArrayList<Integer>();
>>>>>>> 7108795 (15min)
    }

    public User(){

    }
}

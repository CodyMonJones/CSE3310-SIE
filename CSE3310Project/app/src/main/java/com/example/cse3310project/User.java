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
    private String password;
    private ArrayList<DiscussionPost> discussionPosts;
    private String profile_picture;


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

    public void setDiscussionPosts(ArrayList<DiscussionPost> posts) { this.discussionPosts = posts; }

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

    public ArrayList<DiscussionPost> getDiscussionPosts()
    {
        return discussionPosts;
    }

    public User(String firstName, String lastName, String email, int Mav, String phone, ArrayList<DiscussionPost> usersPosts)
    {
        this.fname = firstName;
        this.lname = lastName;
        this.email = email;
        this.MavID = Mav;
        this.phoneNumber = phone;
        this.discussionPosts = usersPosts;
    }
}

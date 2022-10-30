package com.example.cse3310project;

import java.io.Serializable;

public class User implements Serializable
{
    String fname = "";
    String lname = "";
    String email = "";
    int MavID;
    String phoneNumber;

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

    public User(String firstName, String lastName, String email, int Mav, String phone)
    {
        this.fname = firstName;
        this.lname = lastName;
        this.email = email;
        this.MavID = Mav;
        this.phoneNumber = phone;
    }
}

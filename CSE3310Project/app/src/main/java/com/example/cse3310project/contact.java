package com.example.cse3310project;

public class contact {
    private String fname;
    private String lname;
    private String email;
    private String phonenumber;

    public void setFname(String fname){this.fname = fname;}
    public String getFname(){return fname;}

    public void setLname(String lname){this.lname = lname;}
    public String getLname(){return lname;}

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

    public void setPhonenumber(String phonenumber){this.phonenumber = phonenumber;}
    public String getPhonenumber(){return phonenumber;}

    public contact(String firstname, String lastname, String emailaddy, String phonenum){
        this.fname = firstname;
        this.lname = lastname;
        this.email = emailaddy;
        this.phonenumber = phonenum;
    }

    public contact(){

    }

    @Override
    public String toString(){
        return fname + " " + lname;
    }
}

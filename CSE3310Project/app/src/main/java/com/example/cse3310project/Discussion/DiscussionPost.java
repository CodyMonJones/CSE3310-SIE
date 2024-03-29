package com.example.cse3310project.Discussion;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class DiscussionPost
{
    // Instance variables that define a Discussion Post
    private String postTitle;
    private String postBody;
    private String postCreationDate;
    private String postUsername;
    private String posterImage;
    private int dislikes;
    private int likes;
    private String uniqueID;
    private ArrayList<String> comments;
    private Timestamp timestamp;

    // Constructor used to create a Discussion Post when in the Activity "CreatePost"
    public DiscussionPost(String postTitle, String postBody, String postCreationDate, String postUsername)
    {
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postCreationDate = postCreationDate;
        this.postUsername = postUsername;
        this.comments = null;
        this.dislikes = 0;
        this.likes = 0;
        this.timestamp = null;
    }

    // Constructor used to create a Discussion Post when pulling from the database
    public DiscussionPost(String postTitle, String postBody, String postCreationDate, int dislikes, int likes)
    {
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postCreationDate = postCreationDate;
        this.dislikes = dislikes;
        this.likes = likes;
    }

    // Necessary constructor for FireBase to save the object to the database
    public DiscussionPost()
    {

    }

    // Getters and Setters
    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getPostUsername()
    {
        return postUsername;
    }

    public void setPostUsername(String postUsername)
    {
        this.postUsername = postUsername;
    }

    public ArrayList<String> getComments()
    {
        return comments;
    }

    public void setComments(ArrayList<String> comments)
    {
        this.comments = comments;
    }

    public String getPostTitle()
    {
        return postTitle;
    }

    public void setPostTitle(String postTitle)
    {
        this.postTitle = postTitle;
    }

    public String getPostBody()
    {
        return postBody;
    }

    public void setPostBody(String postBody)
    {
        this.postBody = postBody;
    }

    public String getPostCreationDate()
    {
        return postCreationDate;
    }

    public void setPostCreationDate(String postCreationDate)
    {
        this.postCreationDate = postCreationDate;
    }

    public int getDislikes()
    {
        return dislikes;
    }

    public void setDislikes(int dislikes)
    {
        this.dislikes = dislikes;
    }

    public int getLikes()
    {
        return likes;
    }

    public void setLikes(int likes)
    {
        this.likes = likes;
    }

    public String getUniqueID()
    {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID)
    {
        this.uniqueID = uniqueID;
    }

    public String getPosterImage()
    {
        return posterImage;
    }

    public void setPosterImage(String posterImage)
    {
        this.posterImage = posterImage;
    }
}

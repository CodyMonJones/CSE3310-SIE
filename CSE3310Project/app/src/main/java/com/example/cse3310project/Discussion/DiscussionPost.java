package com.example.cse3310project.Discussion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse3310project.R;

import java.util.ArrayList;

public class DiscussionPost
{
    private String postTitle;
    private String postBody;
    private String postCreationDate;
    private int dislikes;
    private int likes;
    private String uniqueID;
    private ArrayList<String> comments;

    public DiscussionPost(String postTitle, String postBody, String postCreationDate)
    {
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postCreationDate = postCreationDate;
        this.dislikes = 0;
        this.likes = 0;
    }

    public DiscussionPost(String postTitle, String postBody, String postCreationDate, int dislikes, int likes)
    {
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postCreationDate = postCreationDate;
        this.dislikes = dislikes;
        this.likes = likes;
    }

    public DiscussionPost()
    {

    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getPostCreationDate() {
        return postCreationDate;
    }

    public void setPostCreationDate(String postCreationDate) { this.postCreationDate = postCreationDate; }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUniqueID() { return uniqueID; }

    public void setUniqueID(String uniqueID) { this.uniqueID = uniqueID; }
}

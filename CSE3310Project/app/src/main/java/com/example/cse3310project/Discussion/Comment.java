package com.example.cse3310project.Discussion;

import com.google.firebase.Timestamp;

public class Comment
{
    private String commentBody;
    private String commentCreationDate;
    private String commentPoster;
    private String uniqueID;
    private Timestamp timestamp;

    // Empty constructor needed to saved the object in firestore
    public Comment()
    {

    }

    // Constructor for when we pull the comment data and create a comment object from that data
    public Comment(String commentBody, String commentPoster, String commentCreationDate)
    {
        this.commentBody = commentBody;
        this.commentPoster = commentPoster;
        this.commentCreationDate = commentCreationDate;
    }

    // Setters and Getters
    public String getUniqueID()
    {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID)
    {
        this.uniqueID = uniqueID;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody)
    {
        this.commentBody = commentBody;
    }

    public String getCommentCreationDate()
    {
        return commentCreationDate;
    }

    public void setCommentCreationDate(String commentCreationDate)
    {
        this.commentCreationDate = commentCreationDate;
    }

    public String getCommentPoster() {
        return commentPoster;
    }

    public void setCommentPoster(String commentPoster)
    {
        this.commentPoster = commentPoster;
    }
}

package com.example.cse3310project.Discussion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
{
    // Instance variables
    private ArrayList<Comment> comments;
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseFirestore postDatabase = FirebaseFirestore.getInstance();

    // Constructor for the class
    public CommentAdapter(ArrayList<Comment> comments, Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.comments = comments;
    }

    // Gets the item count of the recyclerview
    @Override
    public int getItemCount(){ return comments.size(); }

    // Sets the view of the recyclerview when created
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.comment, null);
        return new CommentAdapter.ViewHolder(view);
    }

    // Binds the data to the recyclerview
    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, final int position)
    {
        holder.bindData(comments.get(position));
    }

    // Inner class to handle the instructions on how to display the information
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView commentAuthor, commentBody, commentCreationDate;

        ViewHolder(View itemView)
        {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.Comment_Username);
            commentBody = itemView.findViewById(R.id.Comment_Body);
            commentCreationDate = itemView.findViewById(R.id.Comment_Creation_Date);
        }

        void bindData(final Comment comment)
        {
            commentAuthor.setText(comment.getCommentPoster());
            commentBody.setText(comment.getCommentBody());
            commentCreationDate.setText(comment.getCommentCreationDate());
        }
    }
}

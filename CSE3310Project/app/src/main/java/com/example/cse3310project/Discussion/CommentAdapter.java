package com.example.cse3310project.Discussion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private ArrayList<Comment> comments;
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseFirestore postDatabase = FirebaseFirestore.getInstance();

    public CommentAdapter(ArrayList<Comment> comments, Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getItemCount(){ return comments.size(); }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.comment, null);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, final int position)
    {
        holder.bindData(comments.get(position));
    }

    public void setItems(ArrayList<Comment> comments){ this.comments = comments; }

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

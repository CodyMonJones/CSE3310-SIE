package com.example.cse3310project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.Discussion.CustomAdapter;
import com.example.cse3310project.Discussion.DiscussionPost;

import java.util.ArrayList;

public class TutoringAdapter extends RecyclerView.Adapter<TutoringAdapter.ViewHolder>{
    Context context;
    ArrayList<tutorpost> tutors;
    LayoutInflater mInflater;

    public TutoringAdapter(Context context, ArrayList<tutorpost> tutors){
        this.context = context;
        this.tutors = tutors;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public TutoringAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.tutoringpostformat, null);
        return new TutoringAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TutoringAdapter.ViewHolder holder, final int position)
    {
        holder.bindData(tutors.get(position));
    }

    @Override
    public int getItemCount(){
        return tutors.size();
    }

    public void setItems(ArrayList<tutorpost> tutors)
    {
        this.tutors = tutors;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        void bindData(final tutorpost post)
        {
            //postTitle.setText(post.getPostTitle());
            //postBody.setText(post.getPostBody());
            //postCreationDate.setText(post.getPostCreationDate());
            //username.setText(post.getPostUsername());
        }
    }
}

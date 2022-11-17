package com.example.cse3310project.Discussion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
{
    private ArrayList<DiscussionPost> posts = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseFirestore postDatabase = FirebaseFirestore.getInstance();

    public CustomAdapter(ArrayList<DiscussionPost> posts, Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getItemCount() { return posts.size(); }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.discussion_post_layout, null);
        return new CustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.ViewHolder holder, final int position)
    {
        holder.bindData(posts.get(position));
    }

    public void setItems(ArrayList<DiscussionPost> discussionPosts)
    {
        posts = discussionPosts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView postTitle, postBody, postCreationDate, username;
        MaterialButton likeButton, dislikeButton, commentButton;
        CardView cv;

        ViewHolder(View itemView)
        {
            super(itemView);
            postTitle = itemView.findViewById(R.id.Post_Title);
            postBody = itemView.findViewById(R.id.Post_Body);
            postCreationDate = itemView.findViewById(R.id.Post_Creation_Date);
            username = itemView.findViewById(R.id.Username_Discussion_Forum);
            likeButton = itemView.findViewById(R.id.Like_Button);
            dislikeButton = itemView.findViewById(R.id.Dislike_Button);
            commentButton = itemView.findViewById(R.id.Comment_Button);
            cv = itemView.findViewById(R.id.cv);

            cv.setOnClickListener(this);
            likeButton.setOnClickListener(this);
            dislikeButton.setOnClickListener(this);
            commentButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view)
        {
            switch(view.getId())
            {
                case R.id.Like_Button:
                    int postPosition = getLayoutPosition();
                    int postCurrentLikes = posts.get(postPosition).getLikes();
                    posts.get(postPosition).setLikes(postCurrentLikes + 1);
                    posts.get(getLayoutPosition()).setPostBody("" + posts.get(postPosition).getLikes());

                    updateData(posts.get(postPosition));
                    notifyDataSetChanged();



                    Toast.makeText(context.getApplicationContext(), posts.get(getLayoutPosition()).getPostBody(), Toast.LENGTH_SHORT).show();
                    break;

                case R.id.Dislike_Button:
                    postPosition = getLayoutPosition();
                    int postCurrentDislikes = posts.get(postPosition).getDislikes();
                    posts.get(postPosition).setDislikes(postCurrentDislikes + 1);
                    posts.get(getLayoutPosition()).setPostBody("" + posts.get(postPosition).getDislikes());

                    updateData(posts.get(postPosition));
                    notifyDataSetChanged();

                    Toast.makeText(context.getApplicationContext(), "You disliked a post", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.cv:
                    goToCommentActivity();
                    break;

                case R.id.Comment_Button:
                    goToCommentActivity();
                    break;
            }
        }

        void bindData(final DiscussionPost post)
        {
            postTitle.setText(post.getPostTitle());
            postBody.setText(post.getPostBody());
            postCreationDate.setText(post.getPostCreationDate());
            username.setText(post.getPostUsername());
        }

        void updateData(DiscussionPost post)
        {
            DocumentReference ref = postDatabase.collection("Posts").document(post.getUniqueID());
            ref.update("likes", post.getLikes());

            ref.update("dislikes", post.getDislikes());
            notifyDataSetChanged();
        }

        void goToCommentActivity()
        {
            Intent commentActivity = new Intent(context.getApplicationContext(), CommentActivity.class);
            context.startActivity(commentActivity);
        }
    }
}

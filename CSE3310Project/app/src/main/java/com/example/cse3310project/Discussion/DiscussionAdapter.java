package com.example.cse3310project.Discussion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.R;
import com.example.cse3310project.TransactionsProduct;
import com.example.cse3310project.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> implements Filterable
{
    private ArrayList<DiscussionPost> posts;
    private ArrayList<DiscussionPost> postsFull;
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseFirestore postDatabase = FirebaseFirestore.getInstance();

    public DiscussionAdapter(ArrayList<DiscussionPost> posts, Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.posts = posts;
        postsFull = new ArrayList<>(posts);
    }

    @Override
    public int getItemCount() { return posts.size(); }

    @Override
    public Filter getFilter() {
        return transactionsFilter;
    }

    private Filter transactionsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DiscussionPost> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(postsFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DiscussionPost item : postsFull){
                    if(item.getPostTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            posts.clear();
            posts.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public DiscussionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.discussion_post_layout, null);
        return new DiscussionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DiscussionAdapter.ViewHolder holder, final int position)
    {
        holder.bindData(posts.get(position));
    }

    public void setItems(ArrayList<DiscussionPost> discussionPosts)
    {
        posts = discussionPosts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView postTitle, postBody, postCreationDate, username, postLikes, postDislikes;
        ShapeableImageView posterImage;
        MaterialButton likeButton, dislikeButton, commentButton;
        CardView cv;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        ViewHolder(View itemView)
        {
            super(itemView);
            postTitle = itemView.findViewById(R.id.Post_Title);
            postBody = itemView.findViewById(R.id.Post_Body);
            postCreationDate = itemView.findViewById(R.id.Post_Creation_Date);
            postLikes = itemView.findViewById(R.id.Post_Likes);
            postDislikes = itemView.findViewById(R.id.Post_Dislikes);
            posterImage = itemView.findViewById(R.id.Profile_Picture_Post_Header);
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

                    updateData(posts.get(postPosition));

                    postLikes.setText("Likes: " + posts.get(postPosition).getLikes());

                    notifyDataSetChanged();
                    break;

                case R.id.Dislike_Button:
                    postPosition = getLayoutPosition();
                    int postCurrentDislikes = posts.get(postPosition).getDislikes();
                    posts.get(postPosition).setDislikes(postCurrentDislikes + 1);

                    updateData(posts.get(postPosition));

                    postDislikes.setText("Dislikes: " + posts.get(postPosition).getDislikes());

                    notifyDataSetChanged();
                    break;

                case R.id.cv:
                    postPosition = getLayoutPosition();
                    goToCommentActivity(posts.get(postPosition), 0);
                    break;

                case R.id.Comment_Button:
                    postPosition = getLayoutPosition();
                    goToCommentActivity(posts.get(postPosition), 1);
                    break;
            }
        }

        void bindData(final DiscussionPost post)
        {
            postTitle.setText(post.getPostTitle());
            postBody.setText(post.getPostBody());
            postCreationDate.setText(post.getPostCreationDate());
            postLikes.setText("Likes: " + post.getLikes());
            postDislikes.setText("Dislikes: " + post.getDislikes());
            username.setText(post.getPostUsername());
            setProfileImage(post);
        }

        void updateData(DiscussionPost post)
        {
            DocumentReference ref = postDatabase.collection("Posts").document(post.getUniqueID());
            ref.update("likes", post.getLikes());

            ref.update("dislikes", post.getDislikes());
            notifyDataSetChanged();
        }

        void goToCommentActivity(DiscussionPost selectedPost, int requestFocus)
        {
            Intent commentActivity = new Intent(context.getApplicationContext(), CommentActivity.class);
            commentActivity.putExtra("DiscussionID", selectedPost.getUniqueID());
            commentActivity.putExtra("SelectedComment", requestFocus);
            context.startActivity(commentActivity);
        }

        void setProfileImage(DiscussionPost post)
        {
            if(post.getPosterImage().isEmpty())
                return;

            // download image from firebase storage
            StorageReference imageRef = storageReference.child(post.getPosterImage());

            imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // assigns the downloaded image, in bitmap form, to the imageView
                    posterImage.setImageBitmap(bitmap);
                }
            });
        }
    }
}

package com.example.cse3310project.Discussion;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse3310project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    // ArrayList for recyclerView
    private ArrayList<Comment> comments = new ArrayList<>();

    // ArrayList to hold current post comment IDs
    private ArrayList<String> currentPostIDs = new ArrayList<>();

    // Variables for the commentAdapter to handle comment updates and
    // the recyclerView
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;

    // Firebase Variables for the different data needed
    private DocumentReference postRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore postDatabase;
    private FirebaseFirestore commentDatabase;
    private FirebaseUser currentUser;

    // Activity variables that are given ids in the XML file
    private ShapeableImageView posterImage;
    private TextView postTitle, postUsername, postBody, postCreationDate;
    private MaterialButton addButton;
    private EditText commentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Intent to receive Discussion Post Unique ID from previous activity
        Intent intent = getIntent();

        // Initializes all Firebase variables
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        postDatabase = FirebaseFirestore.getInstance();
        commentDatabase = FirebaseFirestore.getInstance();

        // sets the postRef to specific data for the post selected in previous activity
        postRef = postDatabase.collection("Posts").document(intent.getStringExtra("DiscussionID"));

        // sets all XML variables to its proper attribute
        postTitle = findViewById(R.id.Post_Title);
        postBody = findViewById(R.id.Post_Body);
        postUsername = findViewById(R.id.Username_Discussion_Forum);
        postCreationDate = findViewById(R.id.Post_Creation_Date);
        posterImage = findViewById(R.id.Profile_Picture_Post_Header);
        addButton = findViewById(R.id.Add_Comment_Button);
        commentEditText = findViewById(R.id.Comment_EditText);

        // adds a button listener needed to create a new post
        addButton.setOnClickListener(this);

        // setting up the commentAdapter and the recyclerView to display and receive new comments
        commentAdapter = new CommentAdapter(comments, this);
        recyclerView = findViewById(R.id.Comment_Section);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));


        showPost();

        EventChangeListener();
    }

    // Shows the current selected post's title, body, creation date, username, and the profile
    // picture of the user who posted it
    public void showPost() {
        postRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                DiscussionPost selectedPost = documentSnapshot.toObject(DiscussionPost.class);

                postTitle.setText(selectedPost.getPostTitle());
                postBody.setText(selectedPost.getPostBody());
                postCreationDate.setText(selectedPost.getPostCreationDate());
                postUsername.setText(selectedPost.getPostUsername());
                setProfileImage(selectedPost);
            }
        });
    }

    // Checks to see if the post exists
    // If the post exists then it grabs the list of comment ids that
    // are used to find the specific comments in the Comment database.
    // Then the comments are downloaded and added into the recyclerView
    public void EventChangeListener()
    {
        postRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        currentPostIDs = (ArrayList<String>) document.get("comments");
                        if (currentPostIDs == null)
                        {
                            return;
                        }
                    }
                    else
                    {
                        Log.d(TAG, "No such document");
                    }
                }
                else
                {
                    Log.d(TAG, "get failed with ", task.getException());
                }

                commentDatabase.collection("Comments")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult())
                                    {
                                        for (String i : currentPostIDs)
                                        {
                                            if (i.equals(document.getString("uniqueID")))
                                            {
                                                Comment temp = document.toObject(Comment.class);
                                                comments.add(temp);
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    return;
                                }

                                commentAdapter = new CommentAdapter(comments, getApplicationContext());
                                recyclerView.setAdapter(commentAdapter);
                            }
                        });
            }
        });
    }

    // Creates the comment and adds it to the recyclerview as well as adds it the Comment database.
    // Lastly, it adds it to the list of comments for the specific Discussion Post selected
    @Override
    public void onClick(View view)
    {

        switch(view.getId())
        {
            case R.id.Add_Comment_Button:

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                Comment newComment = new Comment(commentEditText.getText().toString(), currentUser.getEmail(), currentDateandTime);
                comments.add(newComment);

                DocumentReference commentRef = postDatabase.collection("Comments").document();

                newComment.setUniqueID(commentRef.getId());
                newComment.setTimestamp(Timestamp.now());

                commentRef.set(newComment);

                DocumentReference postRef = postDatabase.collection("Posts").document(getIntent().getStringExtra("DiscussionID"));
                postRef.update("comments", FieldValue.arrayUnion(newComment.getUniqueID()));

                Toast.makeText(getApplicationContext(), "Added Comment", Toast.LENGTH_SHORT).show();

                commentEditText.setText("");
                commentAdapter.notifyDataSetChanged();
                break;
        }
    }

    // Sets the Discussion Post profile picture to the owners profile picture
    void setProfileImage(DiscussionPost post)
    {
        if(post.getPosterImage().isEmpty())
            return;

        // download image from firebase storage
        StorageReference imageRef = storageReference.child(post.getPosterImage());

        imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>()
        {
            @Override
            public void onSuccess(byte[] bytes)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Assigns the downloaded image, in bitmap form, to the imageView
                posterImage.setImageBitmap(bitmap);
            }
        });
    }


}
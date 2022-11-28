package com.example.cse3310project.Discussion;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse3310project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
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

    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private StorageReference storageReference;

    private DocumentReference postRef;
    private FirebaseFirestore postDatabase;
    private FirebaseFirestore commentDatabase;
    private FirebaseUser currentUser;

    private NestedScrollView scrollView;
    private TextView postTitle, postUsername, postBody, postCreationDate;
    private MaterialButton addButton;
    private EditText commentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        scrollView = findViewById(R.id.Display_Post_And_Comments);

        Intent intent = getIntent();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        postDatabase = FirebaseFirestore.getInstance();
        commentDatabase = FirebaseFirestore.getInstance();

        postRef = postDatabase.collection("Posts").document(intent.getStringExtra("DiscussionID"));

        postTitle = findViewById(R.id.Post_Title);
        postBody = findViewById(R.id.Post_Body);
        postUsername = findViewById(R.id.Username_Discussion_Forum);
        postCreationDate = findViewById(R.id.Post_Creation_Date);
        addButton = findViewById(R.id.Add_Comment_Button);
        commentEditText = findViewById(R.id.Comment_EditText);

        addButton.setOnClickListener(this);

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

    public void showPost() {
        postRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DiscussionPost selectedPost = documentSnapshot.toObject(DiscussionPost.class);

                postTitle.setText(selectedPost.getPostTitle());
                postBody.setText(selectedPost.getPostBody());
                postCreationDate.setText(selectedPost.getPostCreationDate());
                postUsername.setText(selectedPost.getPostUsername());
            }
        });
    }

    public void EventChangeListener()
    {

        postRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                // downloads data from FireStore into arraylist
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

                DocumentReference postRef = postDatabase.collection("Posts").document("TWGVSkX9tKJq5yhCUmPU");
                postRef.update("comments", FieldValue.arrayUnion(newComment.getUniqueID()));

                Toast.makeText(getApplicationContext(), "Added Comment", Toast.LENGTH_SHORT).show();

                commentEditText.setText("");
                commentAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void showKeyboard(final EditText ettext){
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }
}
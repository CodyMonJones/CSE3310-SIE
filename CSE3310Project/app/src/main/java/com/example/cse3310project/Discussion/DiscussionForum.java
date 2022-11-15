package com.example.cse3310project.Discussion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.cse3310project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DiscussionForum extends AppCompatActivity{

    private ArrayList<DiscussionPost> posts = new ArrayList<>();
    private FirebaseFirestore postDatabase;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addPostButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);

        postDatabase = FirebaseFirestore.getInstance();

        customAdapter = new CustomAdapter(posts, this);
        recyclerView = findViewById(R.id.Discussion_Forum);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);
        recyclerView.addItemDecoration(new ItemDecorator(50));
        addPostButton = findViewById(R.id.Add_Post_Button);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });

        EventChangeListener();
    }

    public void EventChangeListener()
    {
        postDatabase.collection("Posts").orderBy("postCreationDate", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null)
                {
                    Log.e("FIRESTORE ERROR", error.getMessage());
                }

                for(DocumentChange dc : value.getDocumentChanges())
                {
                    if(dc.getType() == DocumentChange.Type.ADDED)
                    {
                        DiscussionPost temp = dc.getDocument().toObject(DiscussionPost.class);

                        posts.add(temp);
                    }
                }

                customAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addPost()
    {
        Intent createPost = new Intent(getApplicationContext(), CreatePost.class);
        startActivity(createPost);
    }
}
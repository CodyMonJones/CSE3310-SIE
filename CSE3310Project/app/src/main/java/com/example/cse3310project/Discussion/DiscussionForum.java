package com.example.cse3310project.Discussion;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cse3310project.R;
import com.example.cse3310project.databinding.ActivityDiscussionForumBinding;
import com.example.cse3310project.drawerActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DiscussionForum extends drawerActivity{

    // Necessary variables needed to implement the Discussion Forum
    private ArrayList<DiscussionPost> posts = new ArrayList<>();
    private FirebaseFirestore postDatabase;
    private DiscussionAdapter customAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addPostButton;
    private StorageReference storageReference;
    private ActivityDiscussionForumBinding activityDiscussionForumBinding;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.transactions_search, menu);

        MenuItem searchItem = menu.findItem(R.id.transactions_searchbar);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binds this activity to the drawer layout used to navigate throughout
        // the app
        activityDiscussionForumBinding = ActivityDiscussionForumBinding.inflate(getLayoutInflater());
        setContentView(activityDiscussionForumBinding.getRoot());
        allocateActivityTitle("Discussions");

        // Sets the reference needed to access photos
        storageReference = FirebaseStorage.getInstance().getReference();

        // Sets the reference needed to access the posts that users
        // have created
        postDatabase = FirebaseFirestore.getInstance();

        // Initializes the adapter and sets the RecyclerView
        customAdapter = new DiscussionAdapter(posts, this);
        recyclerView = findViewById(R.id.Discussion_Forum);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);
        recyclerView.addItemDecoration(new ItemDecorator(50));

        // Sets the Floating Action Button and implements the listener
        addPostButton = findViewById(R.id.Add_Post_Button);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });

        EventChangeListener();
    }

    // Function that updates the RecyclerView with posts created when different users
    // create their own posts
    public void EventChangeListener()
    {
        postDatabase.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    // Sends the user to the CreatePost activity
    public void addPost()
    {
        Intent createPost = new Intent(getApplicationContext(), CreatePost.class);
        startActivity(createPost);
    }

    public void onClick(View view) {

    }
}
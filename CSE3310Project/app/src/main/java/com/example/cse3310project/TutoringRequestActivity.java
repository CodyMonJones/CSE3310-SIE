package com.example.cse3310project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cse3310project.databinding.ActivityComsBinding;
import com.example.cse3310project.databinding.ActivityTutoringRequestBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TutoringRequestActivity extends drawerActivity implements View.OnClickListener{

    ImageButton add, requests, offers;

    EditText search;

    RecyclerView rv;
    ContactAdapter.RecyclerViewClickListener listener;
    ContactAdapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;
    String userid;

    ActivityTutoringRequestBinding activityTutoringRequestBinding;

    ArrayList<String> list;
    ArrayList<tutorpost> tps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoring_request);
        activityTutoringRequestBinding = ActivityTutoringRequestBinding.inflate(getLayoutInflater());
        setContentView(activityTutoringRequestBinding.getRoot());
        allocateActivityTitle("Tutoring");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userid = currentUser.getUid();
        dbref = FirebaseDatabase.getInstance().getReference();
        ff = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        tps = new ArrayList<>();

        add = (ImageButton) findViewById(R.id.NewRequestButton);
        requests = (ImageButton) findViewById(R.id.RequestsButton);
        offers = (ImageButton) findViewById(R.id.OffersButton);
        rv = (RecyclerView) findViewById(R.id.TutorList);

        add.setOnClickListener(this);
        requests.setOnClickListener(this);
        offers.setOnClickListener(this);

        EventChangeListener();
    }

    public void EventChangeListener() {
        ff.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        User user = (dc.getDocument().toObject(User.class));
                        if(user.getUserID().equals(userid)) {
                            if (!user.getTutorpostids().isEmpty()){
                                for (String t : user.getTutorpostids()) {
                                    list.add(t);
                                }
                            }
                        }
                    }
                }
            }
        });

        ff.collection("tutorposts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        tutorpost post = (dc.getDocument().toObject(tutorpost.class));
                        if(!list.isEmpty()) {
                            for (String tut : list) {
                                if (post.getTid().equals(tut)) {
                                    if (post.getRequest()) {
                                        tps.add(post);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewRequestButton:
                break;
            case R.id.OffersButton:
                Intent x = new Intent(TutoringRequestActivity.this, TutoringOffersActivity.class);
                startActivity(x);
                finish();
                break;
        }
    }
}
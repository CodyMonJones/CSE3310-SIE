package com.example.cse3310project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cse3310project.databinding.ActivityComsBinding;
import com.example.cse3310project.databinding.ActivityTutoringRequestBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TutoringRequestActivity extends drawerActivity implements View.OnClickListener{

    ImageButton add, requests, offers, exit;

    EditText search, name, study, schedule, price;

    Button post;

    RecyclerView rv;
    ContactAdapter.RecyclerViewClickListener listener;
    TutoringAdapter adapter;

    AlertDialog.Builder pop;
    AlertDialog dialog;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;
    String userid;

    ActivityTutoringRequestBinding activityTutoringRequestBinding;

    ArrayList<String> list;
    ArrayList<tutorpost> tps;
    ArrayList<Integer> ratings;
    String email;

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
        ratings = new ArrayList<>();

        add = (ImageButton) findViewById(R.id.NewRequestButton);
        requests = (ImageButton) findViewById(R.id.RequestsButton);
        offers = (ImageButton) findViewById(R.id.OffersButton);
        rv = (RecyclerView) findViewById(R.id.TutorList);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TutoringAdapter(TutoringRequestActivity.this, tps);
        rv.setAdapter(adapter);

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
<<<<<<< HEAD
=======
                            if (!user.getRatings().isEmpty()){
                                for (int t : user.getRatings()) {
                                    ratings.add(t);
                                }
                            }
>>>>>>> 7108795 (15min)
                            email = user.getEmail();
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
                                if (post.getRequest()) {
                                    tps.add(post);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewRequestButton:
                addRequest();
                break;
            case R.id.OffersButton:
                Intent x = new Intent(TutoringRequestActivity.this, TutoringOffersActivity.class);
                startActivity(x);
                finish();
                break;
        }
    }

    public void addRequest() {
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.newtutoringpost, null);

        exit = (ImageButton) popupView.findViewById(R.id.exit);
        post = (Button) popupView.findViewById(R.id.post);

        name = (EditText) popupView.findViewById(R.id.EnterName);
        study = (EditText) popupView.findViewById(R.id.Enterstudy);
        schedule = (EditText) popupView.findViewById(R.id.Enterschedule);
        price = (EditText) popupView.findViewById(R.id.Enterprice);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                String stud = study.getText().toString();
                String sched = schedule.getText().toString();
                String money = price.getText().toString();

                if(TextUtils.isEmpty(n) || TextUtils.isEmpty(stud) || TextUtils.isEmpty(sched) || TextUtils.isEmpty(money)){
                    Toast.makeText(TutoringRequestActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                tutorpost tp = new tutorpost(n, userid, email, stud, sched, money, true);
                DocumentReference ref = ff.collection("tutorposts").document();
                tp.setTid(ref.getId());
                ref.set(tp);
                list.add(tp.getTid());
                ff.collection("Users").document(userid).update("tutorpostids", list);
                dialog.dismiss();
            }
        });

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();
    }
}
package com.example.cse3310project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.cse3310project.Discussion.DiscussionForum;
import com.example.cse3310project.databinding.ActivityHomeBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;



public class HomeActivity extends drawerActivity implements View.OnClickListener{

    ActivityHomeBinding activityHomeBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    private CardView messageCard, clubCard, marketCard, tutoringCard, discussionCard, settingsCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        messageCard = (CardView) findViewById(R.id.message_card);
        clubCard = (CardView) findViewById(R.id.club_card);
        marketCard = (CardView) findViewById(R.id.market_card);
        tutoringCard = (CardView) findViewById(R.id.tutoring_card);
        discussionCard = (CardView) findViewById(R.id.discussion_card);
        settingsCard = (CardView) findViewById(R.id.settings_card);

        messageCard.setOnClickListener((View.OnClickListener) this);
        clubCard.setOnClickListener((View.OnClickListener) this);
        marketCard.setOnClickListener((View.OnClickListener) this);
        tutoringCard.setOnClickListener((View.OnClickListener) this);
        discussionCard.setOnClickListener((View.OnClickListener) this);
        settingsCard.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            makeUserLogin();
        }
        else{
            displayUserProfile();
        }

    }

    private void displayUserProfile() {

        String currentUserUUID = mAuth.getCurrentUser().getUid();

        dbRef.child("User").child(currentUserUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("name").exists()){
                    Toast.makeText(HomeActivity.this, "Welcome " + snapshot.child("name").getValue(String.class), Toast.LENGTH_SHORT).show();
                }
                else{
                    makeUserCreateProf();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void makeUserCreateProf() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
    }

    private void makeUserLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.message_card:
                startActivity(new Intent(this, ComsMessagesActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.club_card:
                startActivity(new Intent(this, FormClubActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.market_card:
                startActivity(new Intent(this, TransactionsActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.tutoring_card:
                startActivity(new Intent(this, TutoringRequestActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.discussion_card:
                startActivity(new Intent(this, DiscussionForum.class));
                overridePendingTransition(0,0);
                break;

            case R.id.settings_card:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0,0);
                break;
        }
    }
}
package com.example.cse3310project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.databinding.ActivityMessagesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ComsMessagesActivity extends drawerActivity implements View.OnClickListener{

    ImageButton newmsg;
    ImageButton contacts;
    ImageButton email;

    ActivityMessagesBinding activityMessagesBinding;

    ArrayList<contact> list;

    RecyclerView rv;
    ContactAdapter.RecyclerViewClickListener listener;
    ContactAdapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        activityMessagesBinding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(activityMessagesBinding.getRoot());
        allocateActivityTitle("Communications");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();;
        dbref = FirebaseDatabase.getInstance().getReference();
        ff = FirebaseFirestore.getInstance();

        newmsg = (ImageButton) findViewById(R.id.NewMessageButton);
        contacts = (ImageButton) findViewById(R.id.ContactsMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);

        rv = findViewById(R.id.chats);
        list = new ArrayList<>();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        setOnClickListener();
        adapter = new ContactAdapter(ComsMessagesActivity.this, list, listener);
        rv.setAdapter(adapter);

        newmsg.setOnClickListener(this);
        contacts.setOnClickListener(this);
        email.setOnClickListener(this);

        EventChangeListener();

    }

    private void setOnClickListener() {
        listener = new ContactAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {
                //viewchat
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewMessageButton:
                break;
            case R.id.ContactsMenuButton:
                Intent x = new Intent(ComsMessagesActivity.this, ComsContactsActivity.class);
                startActivity(x);
                finish();
                break;
            case R.id.EmailMenuButton:
                Intent y = new Intent(ComsMessagesActivity.this, ComsEmailActivity.class);
                startActivity(y);
                finish();
                break;
        }
    }

    public void EventChangeListener(){
        ff.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        User user = (dc.getDocument().toObject(User.class));
                        if(user.getUserID().equals(currentUser.getUid())){
                            for(contact c : user.getContactslist()){
                                list.add(c);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
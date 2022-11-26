package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.cse3310project.databinding.ActivityComsChatroomBinding;
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

public class ComsChatroomActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityComsChatroomBinding activityComsChatroomBinding;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;

    ImageButton back, send;
    EditText type;
    ScrollView chat;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coms_chatroom);
        activityComsChatroomBinding = ActivityComsChatroomBinding.inflate(getLayoutInflater());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userid = currentUser.getUid();
        dbref = FirebaseDatabase.getInstance().getReference();
        ff = FirebaseFirestore.getInstance();

        back = (ImageButton)findViewById(R.id.back);
        send = (ImageButton)findViewById(R.id.sendMessage);
        chat = (ScrollView)findViewById(R.id.scroll);
        type = (EditText)findViewById(R.id.inputText);

        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent x = new Intent(ComsChatroomActivity.this, ComsMessagesActivity.class);
                startActivity(x);
                finish();
                break;
            case R.id.sendMessage:
                saveMessage();
                break;
        }
    }

    public void saveMessage() {
        String msg = type.getText().toString();
        if(TextUtils.isEmpty(msg)){
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT);
        } else {

        }
    }
}
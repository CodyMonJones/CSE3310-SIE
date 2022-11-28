package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse3310project.databinding.ActivityClubChatBinding;
import com.example.cse3310project.databinding.ActivityFormClubBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class clubChatActivity extends drawerActivity{
    private ImageButton sendMessageButton;
    private EditText chatMessageBox;
    private ScrollView chatScrollView;
    private TextView chatTextMessages;
    private String selectedGroupName, userUUID, currentUserName, messageDate, messageTime;

    private FirebaseAuth mAuth;
    private DatabaseReference refUser, clubNameRef, clubMessageListKeyRef;

    ActivityClubChatBinding clubChatBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clubChatBinding = ActivityClubChatBinding.inflate(getLayoutInflater());
        setContentView(clubChatBinding.getRoot());
        selectedGroupName = getIntent().getExtras().get("clubName").toString();
        allocateActivityTitle(selectedGroupName);
        Toast.makeText(this, "Welcome to the " + selectedGroupName + "Club!", Toast.LENGTH_SHORT).show();

        setFields();

        mAuth = FirebaseAuth.getInstance();
        userUUID = mAuth.getCurrentUser().getUid();
        refUser = FirebaseDatabase.getInstance().getReference().child("User");
        clubNameRef = FirebaseDatabase.getInstance().getReference().child("Clubs").child(selectedGroupName);

        grabUserProf();
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserMessageToDB();

                chatMessageBox.setText("");
            }
        });
    }

    private void sendUserMessageToDB() {
        String userMessage = chatMessageBox.getText().toString();
        String userMessageKey = clubNameRef.push().getKey();

        if(TextUtils.isEmpty(userMessage)){
            Toast.makeText(this, "Message must be written", Toast.LENGTH_SHORT).show();
        }
        else{
            Calendar grabMessageDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyyy");
            messageDate = currentDate.format(grabMessageDate.getTime());

            Calendar grabMessageTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
            messageTime = currentTime.format(grabMessageTime.getTime());

            HashMap<String, Object> clubMessageKVPair = new HashMap<>();
            clubNameRef.updateChildren(clubMessageKVPair);
            clubMessageListKeyRef = clubNameRef.child(userMessageKey);

            HashMap<String, Object> messageMap = new HashMap<>();
                messageMap.put("name", userUUID);
                messageMap.put("message", userMessage);
                messageMap.put("Date", messageDate);
                messageMap.put("Time", messageTime);
            clubMessageListKeyRef.updateChildren(messageMap);


        }
    }

    private void grabUserProf() {
        refUser.child(userUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userUUID = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setFields() {
        sendMessageButton = (ImageButton) findViewById(R.id.sendMessageButton);
        chatMessageBox = (EditText) findViewById(R.id.chatTypingBox);
        chatTextMessages = (TextView) findViewById(R.id.chat_box);
        chatScrollView = (ScrollView) findViewById(R.id.chatScrollView);
    }
}
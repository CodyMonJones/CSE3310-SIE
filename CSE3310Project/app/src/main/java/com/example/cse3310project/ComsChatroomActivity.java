package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.TextView;

import com.example.cse3310project.databinding.ActivityComsChatroomBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ComsChatroomActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityComsChatroomBinding activityComsChatroomBinding;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;

    ImageButton back, send;
    Button title;
    EditText type;
    TextView msg;
    ScrollView chat;
    String userid;
    String chatid;
    String chatText;
    String currDate;
    String currTime;
    String name;

    Messages newMsg;
    ArrayList<Messages> logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coms_chatroom);
        activityComsChatroomBinding = ActivityComsChatroomBinding.inflate(getLayoutInflater());
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            chatid = extras.getString("chatid");
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userid = currentUser.getUid();
        dbref = FirebaseDatabase.getInstance().getReference();
        ff = FirebaseFirestore.getInstance();

        back = (ImageButton)findViewById(R.id.back);
        send = (ImageButton)findViewById(R.id.sendMessage);
        chat = (ScrollView)findViewById(R.id.scroll);
        type = (EditText)findViewById(R.id.inputText);
        msg = (TextView)findViewById(R.id.textdisplay);
        title = (Button)findViewById(R.id.ChatHeader);

        back.setOnClickListener(this);
        send.setOnClickListener(this);

        ff.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        name = doc.getString("fname") + " " + doc.getString("lname");
                    }
                }
            }
        });


        ff.collection("chatrooms").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        chatroom cr = (dc.getDocument().toObject(chatroom.class));
                        if(cr.getChatid().equals(chatid)) {
                            title.setText(cr.getName());
                            logs = cr.getChat();
                            display();
                        }
                    }
                }
            }
        });
        chat.fullScroll(ScrollView.FOCUS_DOWN);
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
        chatText = type.getText().toString();
        if(TextUtils.isEmpty(chatText)){
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT);
        } else {
            Calendar finddate = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
            currDate = dateFormat.format(finddate.getTime());

            Calendar findtime = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            currTime = timeFormat.format(findtime.getTime());

            newMsg = new Messages(chatText, currDate, currTime, name);
            logs.add(newMsg);
            ff.collection("chatrooms").document(chatid).update("chat", logs);
            updateDisplay(newMsg);
            type.setText("");
            chat.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    public void display() {
         for(Messages ref : logs){
            String messageText = ref.getText();
            String messageDate = ref.getDate();
            String messageTime = ref.getTime();
            String messageName = ref.getName();

            msg.append(messageName + ":\n" + messageText + "\n" + messageTime + "      " + messageDate +"\n\n\n");
        }
        chat.fullScroll(View.FOCUS_DOWN);
    }
    public void updateDisplay(Messages mess){
        String messageText = mess.getText();
        String messageDate = mess.getDate();
        String messageTime = mess.getTime();
        String messageName = mess.getName();
        msg.append(messageName + ":\n" + messageText + "\n" + messageTime + "      " + messageDate +"\n\n\n");
        chat.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

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

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.databinding.ActivityMessagesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.DocumentReference;
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

    Button cancel;
    Button confirm;

    EditText member1;
    EditText member2;

    ActivityMessagesBinding activityMessagesBinding;

    RecyclerView rv;
    ChatListAdapter.RecyclerViewClickListener listener;
    ChatListAdapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;

    AlertDialog.Builder pop;
    AlertDialog dialog;

    ArrayList<chatroom> chatlist;
    ArrayList<String> chatidlist;

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

        chatlist = new ArrayList<chatroom>();
        chatidlist = new ArrayList<>();

        newmsg = (ImageButton) findViewById(R.id.NewMessageButton);
        contacts = (ImageButton) findViewById(R.id.ContactsMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);

        rv = findViewById(R.id.chats);
        chatidlist = new ArrayList<>();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        setOnClickListener();
        adapter = new ChatListAdapter(ComsMessagesActivity.this, chatlist, listener);
        rv.setAdapter(adapter);

        newmsg.setOnClickListener(this);
        contacts.setOnClickListener(this);
        email.setOnClickListener(this);

        EventChangeListener();

    }

    private void setOnClickListener() {
        listener = new ChatListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {viewChat(chatlist.get(pos));}
        };
    }

    public void viewChat(chatroom chatroom) {
        Intent x = new Intent(ComsMessagesActivity.this, ComsChatroomActivity.class);
        startActivity(x);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewMessageButton:
                createchatroom();
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

    private void createchatroom() {
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.newchatroompopup, null);

        cancel = (Button) popupView.findViewById(R.id.cancel);
        confirm = (Button) popupView.findViewById(R.id.confirm);
        member1 = (EditText) popupView.findViewById(R.id.recipient);
        member2 = (EditText) popupView.findViewById(R.id.recipient2);

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatter1 = member1.getText().toString();
                String chatter2 = member2.getText().toString();
                String userid = currentUser.getUid();

                if(TextUtils.isEmpty(chatter1)){
                    Toast.makeText(ComsMessagesActivity.this, "Please fill in the first field", Toast.LENGTH_SHORT).show();
                } else {
                    ff.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String rid = null;
                            String rid2 = null;
                            String chatname = null;
                            ArrayList<String> uids = new ArrayList<>();
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.exists()) {
                                    if (chatter1.equals(doc.getString("email"))) {
                                        rid = doc.getString("userID");
                                        uids.add(rid);
                                        chatname = doc.getString("fname") + " " + doc.getString("lname");
                                    }
                                    if (!chatter2.isEmpty()) {
                                        if (chatter2.equals(doc.getString("email"))) {
                                            rid2 = doc.getString("userID");
                                            chatname = chatname + ", " + doc.getString("fname") + " " + doc.getString("lname");
                                            uids.add(rid2);
                                        }
                                    }

                                }
                            }

                            chatroom cr = new chatroom(uids, chatname);
                            DocumentReference ref = ff.collection("chatrooms").document();
                            cr.setChatid(ref.getId());
                            chatlist.add(cr);
                            ref.set(cr);

                            chatidlist.add(chatname);

                            ff.collection("Users").document(userid).update("chatids", chatidlist);
                        }
                    });
                }
                dialog.dismiss();
            }
        });
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
                            if(!user.getChatids().isEmpty()) {
                                for (String cid : user.getChatids()) {
                                    chatidlist.add(cid);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
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
                        chatroom chat = (dc.getDocument().toObject(chatroom.class));
                        for(String cid : chatidlist){
                            if(chat.getChatid().equals(cid)){
                                chatlist.add(chat);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
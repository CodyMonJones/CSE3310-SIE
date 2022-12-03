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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
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
    Button all;

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

    String useremail;
    Boolean email1Exists, email2Exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        activityMessagesBinding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(activityMessagesBinding.getRoot());
        allocateActivityTitle("Communications");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        useremail = currentUser.getEmail();
        dbref = FirebaseDatabase.getInstance().getReference();
        ff = FirebaseFirestore.getInstance();

        chatlist = new ArrayList<chatroom>();
        chatidlist = new ArrayList<>();

        email1Exists = false;
        email2Exists = false;

        newmsg = (ImageButton) findViewById(R.id.NewMessageButton);
        contacts = (ImageButton) findViewById(R.id.ContactsMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);

        rv = findViewById(R.id.chats);
        chatidlist = new ArrayList<>();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        setOnClickListener();

        newmsg.setOnClickListener(this);
        contacts.setOnClickListener(this);
        email.setOnClickListener(this);

        EventChangeListener();

    }

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
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setOnClickListener() {
        listener = new ChatListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {viewChat(chatlist.get(pos));}
        };
    }

    public void viewChat(chatroom chatroom) {
        Intent x = new Intent(ComsMessagesActivity.this, ComsChatroomActivity.class);
        x.putExtra("chatid", chatroom.getChatid());
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
        all = (Button) popupView.findViewById(R.id.All);
        member1 = (EditText) popupView.findViewById(R.id.recipient);
        member2 = (EditText) popupView.findViewById(R.id.recipient2);

        if(currentUser.getUid().equals("GsOj1mpUeuW4yf7luJa65Tn9xik1")){
            all.setVisibility(View.VISIBLE);
            all.setClickable(true);
        } else {
            all.setVisibility(View.INVISIBLE);
            all.setClickable(false);
        }

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> allusers = new ArrayList<User>();
                ArrayList<String> alluids = new ArrayList<String>();
                ff.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.exists()) {
                                User user = doc.toObject(User.class);
                                allusers.add(user);
                                alluids.add(user.getUserID());
                            }
                        }
                        chatroom cr = new chatroom(alluids, "All Users!!!");
                        DocumentReference ref = ff.collection("chatrooms").document();
                        cr.setChatid(ref.getId());
                        ref.set(cr);

                        for(User curr : allusers){
                            ArrayList<String> id = new ArrayList<>(curr.getChatids());
                            id.add(cr.getChatid());
                            ff.collection("Users").document(curr.getUserID()).update("chatids", id);
                        }
                    }
                });

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
                } else if(chatter1.equals(useremail)) {
                    Toast.makeText(ComsMessagesActivity.this, "Invalid email, cannot be user email", Toast.LENGTH_SHORT).show();
                } else {
                    ff.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.exists()) {
                                    User user = doc.toObject(User.class);
                                    if(chatter1.equals(user.getEmail())){
                                        email1Exists = true;
                                    }
                                    if(!TextUtils.isEmpty(chatter2)) {
                                        if (chatter2.equals(user.getEmail())) {
                                            email2Exists = true;
                                        }
                                    }
                                }
                            }
                            if(email1Exists) {
                                String rid = null;
                                String rid2 = null;
                                String chatname = "";
                                ArrayList<String> uids = new ArrayList<>();
                                ArrayList<String> rcids = new ArrayList<>();
                                ArrayList<String> rcids2 = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.exists()) {
                                        if (userid.equals(doc.getString("userID"))) {
                                            chatname = chatname + doc.getString("firstName") + " " + doc.getString("lname").charAt(0) + ",";
                                        }
                                        if (chatter1.equals(doc.getString("email"))) {
                                            rid = doc.getString("userID");
                                            uids.add(rid);
                                            uids.add(currentUser.getUid());
                                            chatname = chatname + doc.getString("firstName") + " " + doc.getString("lname").charAt(0) + ",";

                                            rcids = doc.toObject(User.class).getChatids();
                                        }
                                        if (email2Exists) {
                                            if (chatter2.equals(doc.getString("email"))) {
                                                rid2 = doc.getString("userID");
                                                chatname = chatname + doc.getString("firstName") + " " + doc.getString("lname").charAt(0) + ",";
                                                uids.add(rid2);

                                                rcids2 = doc.toObject(User.class).getChatids();
                                            }
                                        }
                                    }
                                }
                                chatroom cr = new chatroom(uids, chatname);
                                DocumentReference ref = ff.collection("chatrooms").document();
                                cr.setChatid(ref.getId());
                                ref.set(cr);

                                chatidlist.add(cr.getChatid());
                                rcids.add(cr.getChatid());

                                ff.collection("Users").document(userid).update("chatids", chatidlist);
                                ff.collection("Users").document(rid).update("chatids", rcids);
                                if (rid2 != null) {
                                    rcids2.add(cr.getChatid());
                                    ff.collection("Users").document(rid2).update("chatids", rcids2);
                                }
                            } else {
                                Toast.makeText(ComsMessagesActivity.this, "Email address does not exist", Toast.LENGTH_SHORT).show();
                            }
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
                    adapter = new ChatListAdapter(ComsMessagesActivity.this, chatlist, listener);
                    rv.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
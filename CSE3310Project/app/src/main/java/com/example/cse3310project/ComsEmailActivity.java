package com.example.cse3310project;


import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse3310project.databinding.ActivityEmailBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.cse3310project.databinding.ActivityFormClubBinding;

public class ComsEmailActivity extends drawerActivity implements View.OnClickListener{

    ImageButton newemail, messages, contact, reply, forward, back;

    Button confirm, cancel, header;

    EditText recipient, subject, body;

    TextView content, emsub;

    ActivityEmailBinding activityEmailBinding;

    RecyclerView rv;
    EmailAdapter.RecyclerViewClickListener listener;
    EmailAdapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;
    String userid;

    AlertDialog.Builder pop;
    AlertDialog dialog;

    ArrayList<Email> allemails;
    ArrayList<String> sentids;
    ArrayList<String> recievedids;

    String replytoid;
    String resub;
    String readdress;
    String msgbody;
    Boolean response, forwardmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        activityEmailBinding = ActivityEmailBinding.inflate(getLayoutInflater());
        setContentView(activityEmailBinding.getRoot());
        allocateActivityTitle("Communications");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userid = currentUser.getUid();
        dbref = FirebaseDatabase.getInstance().getReference();
        ff = FirebaseFirestore.getInstance();

        newemail = (ImageButton) findViewById(R.id.NewEmailButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        contact = (ImageButton) findViewById(R.id.ContactsMenuButton);
        rv = findViewById(R.id.emails);

        newemail.setOnClickListener(this);
        messages.setOnClickListener(this);
        contact.setOnClickListener(this);

        allemails = new ArrayList<>();
        sentids = new ArrayList<>();
        recievedids = new ArrayList<>();

        rv = findViewById(R.id.emails);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        setOnClickListener();
        adapter = new EmailAdapter(ComsEmailActivity.this, allemails, listener);
        rv.setAdapter(adapter);

        response = false;
        forwardmsg = false;

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
                        if(user.getUserID().equals(userid)){
                            if(!user.getEmailsent().isEmpty()) {
                                for (String sid : user.getEmailsent()) {
                                    sentids.add(sid);
                                }
                            }
                            if(!user.getEmailrecieved().isEmpty()) {
                                for (String rid : user.getEmailrecieved()) {
                                    recievedids.add(rid);
                                }
                            }
                        }
                    }
                }
            }
        });
        ff.collection("emails").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                }
                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        Email email = (dc.getDocument().toObject(Email.class));
                        if(!sentids.isEmpty()) {
                            for (String sid : sentids) {
                                if (email.getEid().equals(sid)) {
                                    Messages em = email.getEmail();
                                    em.setName("To: " + email.getRecemail());
                                    email.setEmail(em);
                                    allemails.add(email);
                                }
                            }
                        }
                        if(!recievedids.isEmpty()) {
                            for (String rid : recievedids) {
                                if (email.getEid().equals(rid)) {
                                    Messages em = email.getEmail();
                                    em.setName("From: " + email.getSendemail());
                                    email.setEmail(em);
                                    allemails.add(email);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setOnClickListener() {
        listener = new EmailAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {viewEmail(allemails.get(pos));}
        };
    }

    public void viewEmail(Email email) {
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.activity_emailview, null);

        header = (Button) popupView.findViewById(R.id.EmailHeader);
        reply = (ImageButton) popupView.findViewById(R.id.sendMessage);
        forward = (ImageButton) popupView.findViewById(R.id.Forward);
        back = (ImageButton) popupView.findViewById(R.id.back);
        emsub = (TextView) popupView.findViewById(R.id.subject);
        content = (TextView) popupView.findViewById(R.id.content);

        header.setText(email.getEmail().getName());
        emsub.setText(email.getSubject() + " " + email.getEmail().getDate() + " " + email.getEmail().getTime());
        content.setText(email.getEmail().getText());

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                response = true;
                resub = email.getSubject();
                readdress = email.getEmail().getName();
                replytoid = email.getEid();
                createEmail();

                response = false;
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                forwardmsg = true;
                resub = email.getSubject();
                readdress = email.getEmail().getName();
                msgbody = email.getEmail().getText();
                createEmail();
                forwardmsg = false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewEmailButton:
                createEmail();
                break;
            case R.id.MessageMenuButton:
                Intent y = new Intent(ComsEmailActivity.this, ComsMessagesActivity.class);
                startActivity(y);
                finish();
                break;
            case R.id.ContactsMenuButton:
                Intent x = new Intent(ComsEmailActivity.this, ComsContactsActivity.class);
                startActivity(x);
                finish();
                break;
        }
    }

    public void createEmail() {
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.createemailpopup, null);

        cancel = (Button) popupView.findViewById(R.id.cancel);
        confirm = (Button) popupView.findViewById(R.id.confirm);

        recipient = (EditText) popupView.findViewById(R.id.recipient);
        subject = (EditText) popupView.findViewById(R.id.emailsubject);
        body = (EditText) popupView.findViewById(R.id.body);

        subject.setText("Subject: ");

        if (response) {
            recipient.setText(readdress);
            subject.setText("Subject:: re: " + resub);
        }
        if (forwardmsg) {
            subject.setText("Subject:: forward: " + readdress + " " + resub);
            body.setText(msgbody);
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
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whoto = recipient.getText().toString();
                String about = subject.getText().toString();
                String words = body.getText().toString();

                String currDate;
                String currTime;

                if (TextUtils.isEmpty(whoto) || TextUtils.isEmpty(about) || TextUtils.isEmpty(words)) {
                    Toast.makeText(ComsEmailActivity.this, "Please fill in every field", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar finddate = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
                    currDate = dateFormat.format(finddate.getTime());

                    Calendar findTime = Calendar.getInstance();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                    currTime = timeFormat.format(findTime.getTime());

                    Messages em = new Messages(words, currDate, currTime, whoto);


                    ff.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String whoid = "";
                            User user = new User();
                            Email emailing = new Email();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.exists()) {
                                    if (whoto.equals(doc.getString("email"))) {
                                        user = doc.toObject(User.class);
                                        whoid = user.getUserID();
                                        emailing = new Email(whoid, em, userid, about, currentUser.getEmail(), whoto);
                                        DocumentReference ref = ff.collection("emails").document();
                                        emailing.setEid(ref.getId());
                                        ref.set(emailing);
                                        ArrayList<String> recemid = new ArrayList<>();
                                        recemid.addAll(user.getEmailrecieved());
                                        recemid.add(emailing.getEid());
                                        sentids.add(emailing.getEid());
                                        ff.collection("Users").document(userid).update("emailsent", sentids);
                                        ff.collection("Users").document(whoid).update("emailrecieved", recemid);

                                        if (response) {
                                            ff.collection("emails").document(replytoid).update("replyids", emailing.getEid());
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });
    }

    public static class FormClubActivity extends drawerActivity {

        ActivityFormClubBinding activityFormClubBinding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            activityFormClubBinding = ActivityFormClubBinding.inflate(getLayoutInflater());
            setContentView(activityFormClubBinding.getRoot());
            allocateActivityTitle("Clubs");
        }

    }
}
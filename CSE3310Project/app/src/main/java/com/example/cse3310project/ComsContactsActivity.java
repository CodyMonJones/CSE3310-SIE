package com.example.cse3310project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.example.cse3310project.databinding.ActivityComsBinding;
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



public class ComsContactsActivity extends drawerActivity implements View.OnClickListener{
    ImageButton add, messages, email, exit;
    Button profile, cancel, confirm, emailbutton, messagebutton, delete, edit, yes, no;
    TextView noContact, cfname, csname, cemail, cphonenumber;
    EditText firstname, lastname, emailaddress, phone;

    AlertDialog.Builder pop;
    AlertDialog dialog;

    ArrayList<contact> list;

    RecyclerView rv;
    ContactAdapter.RecyclerViewClickListener listener;
    ContactAdapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore ff;
    DatabaseReference dbref;
    ActivityComsBinding activityComsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coms);
        activityComsBinding = ActivityComsBinding.inflate(getLayoutInflater());
        setContentView(activityComsBinding.getRoot());
        allocateActivityTitle("Communications");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();;
        dbref = FirebaseDatabase.getInstance().getReference();
        ff = FirebaseFirestore.getInstance();

        add = (ImageButton) findViewById(R.id.NewContactButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);
        profile = (Button) findViewById(R.id.UserContactButton);

        rv = findViewById(R.id.ContactList);
        list = new ArrayList<contact>();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        setOnClickListener();
        adapter = new ContactAdapter(ComsContactsActivity.this, list, listener);
        rv.setAdapter(adapter);

        add.setOnClickListener(this);
        messages.setOnClickListener(this);
        email.setOnClickListener(this);
        profile.setOnClickListener(this);

        String userid = currentUser.getUid();

        ff.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        profile.setText(doc.getString("fname") + " " + doc.getString("lname"));
                    }
                }
            }
        });

        EventChangeListener();
    }

    private void setOnClickListener() {
        listener = new ContactAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int pos) {
                viewContact(list.get(pos));
            }
        };
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
                        if(user.getUserID().equals(currentUser.getUid())) {
                            if (!user.getContactslist().isEmpty()){
                                for (contact c : user.getContactslist()) {
                                    list.add(c);
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
            case R.id.NewContactButton:
                addContact(list);
                break;
            case R.id.MessageMenuButton:
                Intent x = new Intent(ComsContactsActivity.this, ComsMessagesActivity.class);
                startActivity(x);
                finish();
                break;
            case R.id.EmailMenuButton:
                Intent y = new Intent(ComsContactsActivity.this, ComsEmailActivity.class);
                startActivity(y);
                finish();
                break;
        }
    }

    public void viewContact(contact friend){
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.contactpage, null);

        emailbutton = (Button) popupView.findViewById(R.id.emailcontact);
        messagebutton = (Button) popupView.findViewById(R.id.messagecontact);
        edit = (Button) popupView.findViewById(R.id.edit);
        delete = (Button) popupView.findViewById(R.id.delete);
        exit = (ImageButton) popupView.findViewById(R.id.exitbutton);

        cfname = (TextView) popupView.findViewById(R.id.confname);
        csname = (TextView) popupView.findViewById(R.id.conlname);
        cemail = (TextView) popupView.findViewById(R.id.conemail);
        cphonenumber = (TextView) popupView.findViewById(R.id.conphonenum);
        cfname.setText(friend.getFname());
        csname.setText(friend.getLname());
        cemail.setText(friend.getEmail());
        cphonenumber.setText(friend.getPhonenumber());

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        emailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        messagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                editContact(friend);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                deleteContact(friend);
            }
        });
    }

    public void addContact(ArrayList<contact> friend){
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.addcontactpage, null);

        cancel = (Button) popupView.findViewById(R.id.cancel);
        confirm = (Button) popupView.findViewById(R.id.confirm);
        firstname = (EditText) popupView.findViewById(R.id.fname);
        lastname = (EditText) popupView.findViewById(R.id.lname);
        emailaddress = (EditText) popupView.findViewById(R.id.enteremail);
        phone = (EditText) popupView.findViewById(R.id.enterphone);

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
                String userid = currentUser.getUid();
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String email = emailaddress.getText().toString();
                String num = phone.getText().toString();

                if(TextUtils.isEmpty(fname)){
                    Toast.makeText(ComsContactsActivity.this, "First Name is Required!", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(lname)){
                    Toast.makeText(ComsContactsActivity.this, "Last Name is Required!", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ComsContactsActivity.this, "Email is Required!", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(num)){
                    Toast.makeText(ComsContactsActivity.this, "Phone Number is Required!", Toast.LENGTH_SHORT).show();
                }
                else{
                    contact add = new contact(fname, lname, email, num);
                    friend.add(add);
                    ff.collection("Users").document(userid).update("contactslist", friend);
                    dialog.dismiss();
                }
            }
        });
    }

    public void editContact(contact friend){
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.editcontactpage, null);

        cancel = (Button) popupView.findViewById(R.id.cancel);
        confirm = (Button) popupView.findViewById(R.id.confirm);
        firstname = (EditText) popupView.findViewById(R.id.fname);
        lastname = (EditText) popupView.findViewById(R.id.lname);
        phone = (EditText) popupView.findViewById((R.id.enterphone));

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();

        firstname.setHint(friend.getFname());
        lastname.setHint(friend.getLname());
        phone.setHint(friend.getPhonenumber());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                viewContact(friend);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = currentUser.getUid();
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String num = phone.getText().toString();

                if(TextUtils.isEmpty(fname)){
                    Toast.makeText(ComsContactsActivity.this, "First Name is Required!", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(lname)){
                    Toast.makeText(ComsContactsActivity.this, "Last Name is Required!", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(num)){
                    Toast.makeText(ComsContactsActivity.this, "Phone Number is Required!", Toast.LENGTH_SHORT).show();
                }
                else{
                    list.remove(list.indexOf(friend));
                    friend.setFname(fname);
                    friend.setLname(lname);
                    friend.setPhonenumber(num);
                    list.add(friend);
                    ff.collection("Users").document(userid).update("contactslist", list);
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        });
    }

    public void deleteContact(contact friend){
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.deleteconfirm, null);

        no = (Button) popupView.findViewById(R.id.nodelete);
        yes = (Button) popupView.findViewById(R.id.yesdelete);

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                viewContact(friend);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(list.indexOf(friend));
                ff.collection("Users").document(currentUser.getUid()).update("contactslist", list);
                dialog.dismiss();
            }
        });
    }
}
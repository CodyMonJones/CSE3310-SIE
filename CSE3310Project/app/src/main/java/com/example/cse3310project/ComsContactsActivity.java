package com.example.cse3310project;

import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.core.view.GravityCompat;


import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.cse3310project.databinding.ActivityComsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ComsContactsActivity extends drawerActivity implements View.OnClickListener{
    ImageButton add, messages, email, exit;
    Button profile, cancel, confirm, emailbutton, messagebutton, delete, edit, yes, no;
    TextView noContact;
    EditText firstname, lastname, emailaddress;
    String name;

    AlertDialog.Builder pop;
    AlertDialog dialog;

    ListView contact;
    List list = new ArrayList();
    ArrayAdapter ad;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore userRef = FirebaseFirestore.getInstance();

    ActivityComsBinding activityComsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coms);

        add = (ImageButton) findViewById(R.id.NewContactButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);
        profile = (Button) findViewById(R.id.UserContactButton);
        noContact = (TextView) findViewById(R.id.NoContacts);

        contact = (ListView)findViewById(R.id.ContactList);
        contact.setOnItemClickListener(this::onItemClick);

        add.setOnClickListener(this);
        messages.setOnClickListener(this);
        email.setOnClickListener(this);
        profile.setOnClickListener(this);

        if(list.size() == 0){
            noContact.setVisibility(View.VISIBLE);
            contact.setVisibility(View.INVISIBLE);
        } else {
            noContact.setVisibility(View.INVISIBLE);
            contact.setVisibility(View.VISIBLE);
        }

        ad = new ArrayAdapter<>(ComsContactsActivity.this, R.layout.rowtextformat,list);
        contact.setAdapter(ad);

        profile.setText("Jason");

        //activityComsBinding = ActivityComsBinding.inflate(getLayoutInflater());
        //setContentView(activityComsBinding.getRoot());
    }

    public void onItemClick(AdapterView<?> l, View v, int pos, long id){
        viewContact();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewContactButton:
                addContact();
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
            case R.id.UserContactButton:
                break;
        }
    }

    public void viewContact(){
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.contactpage, null);

        emailbutton = (Button) popupView.findViewById(R.id.emailcontact);
        messagebutton = (Button) popupView.findViewById(R.id.messagecontact);
        edit = (Button) popupView.findViewById(R.id.edit);
        delete = (Button) popupView.findViewById(R.id.delete);
        exit = (ImageButton) popupView.findViewById(R.id.exitbutton);


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
                editContact();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                deleteContact();
            }
        });
    }

    public void addContact(){
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.addcontactpage, null);

        cancel = (Button) popupView.findViewById(R.id.cancel);
        confirm = (Button) popupView.findViewById(R.id.confirm);
        firstname = (EditText) popupView.findViewById(R.id.fname);
        lastname = (EditText) popupView.findViewById(R.id.lname);
        emailaddress = (EditText) popupView.findViewById(R.id.enteremail);

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

            }
        });
    }

    public void editContact(){
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.editcontactpage, null);

        cancel = (Button) popupView.findViewById(R.id.cancel);
        confirm = (Button) popupView.findViewById(R.id.confirm);
        firstname = (EditText) popupView.findViewById(R.id.fname);
        lastname = (EditText) popupView.findViewById(R.id.lname);

        pop.setView(popupView);
        dialog = pop.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                viewContact();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void deleteContact(){
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
                viewContact();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
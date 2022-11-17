package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.cse3310project.databinding.ActivityComsBinding;
import com.example.cse3310project.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ComsActivity extends drawerActivity implements View.OnClickListener{
    ImageButton tab, add, messages, email;
    Button profile, cancel, confirm;
    TextView noContact;
    ImageView defaultpic;
    EditText firstname, lastname, emailaddress;
    String username;

    AlertDialog.Builder pop;
    AlertDialog dialog;

    ListView contact;
    List list = new ArrayList();
    ArrayAdapter ad;

    ActivityComsBinding activityComsBinding;

    //DatabaseReference databaseReference = database.getReference("Users").child(firebaseAuth.getCurrentUser().getUid();)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coms);

        tab = (ImageButton) findViewById(R.id.TabButton);
        add = (ImageButton) findViewById(R.id.NewContactButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);
        profile = (Button) findViewById(R.id.UserContactButton);
        noContact = (TextView) findViewById(R.id.NoContacts);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        tab.setOnClickListener(this);
        add.setOnClickListener(this);
        messages.setOnClickListener(this);
        email.setOnClickListener(this);
        profile.setOnClickListener(this);

        findViewById(R.id.TabButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        contact = (ListView)findViewById(R.id.ContactList);
        for(int x = 1; x <=10; x++){
            list.add("Name " + x);
        }

        if(list.size() == 0){
            noContact.setVisibility(View.VISIBLE);
            contact.setVisibility(View.INVISIBLE);
        } else {
            noContact.setVisibility(View.INVISIBLE);
            contact.setVisibility(View.VISIBLE);
        }

        ad = new ArrayAdapter<>(ComsActivity.this, R.layout.rowtextformat,list);
        contact.setAdapter(ad);

        activityComsBinding = ActivityComsBinding.inflate(getLayoutInflater());
        setContentView(activityComsBinding.getRoot());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewContactButton:
                createNewContact();
                break;
            case R.id.MessageMenuButton:
                Intent x = new Intent(ComsActivity.this, MessagesActivity.class);
                startActivity(x);
                finish();
                break;
            case R.id.EmailMenuButton:
                Intent y = new Intent(ComsActivity.this, EmailActivity.class);
                startActivity(y);
                finish();
                break;
            case R.id.UserContactButton:
                break;
        }
    }

    public void createNewContact() {
        pop = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.addcontactpage, null);

        cancel = (Button) popupView.findViewById(R.id.cancelbutton);
        confirm = (Button) popupView.findViewById(R.id.confirmbutton);
        firstname = (EditText) popupView.findViewById(R.id.fname);
        lastname = (EditText) popupView.findViewById(R.id.lname);
        emailaddress = (EditText) popupView.findViewById(R.id.enteremail);
        defaultpic = (ImageView) popupView.findViewById(R.id.defaultprofile);

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
}
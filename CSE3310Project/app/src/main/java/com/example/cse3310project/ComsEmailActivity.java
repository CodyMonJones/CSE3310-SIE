package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;

public class ComsEmailActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton tab;
    ImageButton newemail;
    ImageButton messages;
    ImageButton contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        newemail = (ImageButton) findViewById(R.id.NewEmailButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        contact = (ImageButton) findViewById(R.id.ContactsMenuButton);

        tab.setOnClickListener(this);
        newemail.setOnClickListener(this);
        messages.setOnClickListener(this);
        contact.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewEmailButton:
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
}
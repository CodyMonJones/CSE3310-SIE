package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton tab;
    ImageButton newemail;
    ImageButton messages;
    ImageButton contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        tab = (ImageButton) findViewById(R.id.TabButton);
        newemail = (ImageButton) findViewById(R.id.NewEmailButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        contact = (ImageButton) findViewById(R.id.ContactsMenuButton);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        tab.setOnClickListener(this);
        newemail.setOnClickListener(this);
        messages.setOnClickListener(this);
        contact.setOnClickListener(this);

        findViewById(R.id.TabButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.TabButton:
                break;
            case R.id.NewEmailButton:
                break;
            case R.id.MessageMenuButton:
                Intent y = new Intent(EmailActivity.this, MessagesActivity.class);
                startActivity(y);
                finish();
                break;
            case R.id.ContactsMenuButton:
                Intent x = new Intent(EmailActivity.this, ComsActivity.class);
                startActivity(x);
                finish();
                break;
        }
    }
}
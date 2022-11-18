package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;

public class ComsMessagesActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton tab;
    ImageButton newmsg;
    ImageButton contacts;
    ImageButton email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        tab = (ImageButton) findViewById(R.id.TabButton);
        newmsg = (ImageButton) findViewById(R.id.NewMessageButton);
        contacts = (ImageButton) findViewById(R.id.ContactsMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        tab.setOnClickListener(this);
        newmsg.setOnClickListener(this);
        contacts.setOnClickListener(this);
        email.setOnClickListener(this);

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
            case R.id.NewMessageButton:
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
}
package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cse3310project.databinding.ActivityMessagesBinding;

public class ComsMessagesActivity extends drawerActivity implements View.OnClickListener{

    ImageButton newmsg;
    ImageButton contacts;
    ImageButton email;

    ActivityMessagesBinding activityMessagesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        activityMessagesBinding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(activityMessagesBinding.getRoot());
        allocateActivityTitle("Communications");

        newmsg = (ImageButton) findViewById(R.id.NewMessageButton);
        contacts = (ImageButton) findViewById(R.id.ContactsMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);

        newmsg.setOnClickListener(this);
        contacts.setOnClickListener(this);
        email.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
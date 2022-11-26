package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.cse3310project.databinding.ActivityEmailBinding;

public class ComsEmailActivity extends drawerActivity implements View.OnClickListener{

    ImageButton newemail;
    ImageButton messages;
    ImageButton contact;

    ActivityEmailBinding activityEmailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        activityEmailBinding = ActivityEmailBinding.inflate(getLayoutInflater());
        setContentView(activityEmailBinding.getRoot());
        allocateActivityTitle("Communications");

        newemail = (ImageButton) findViewById(R.id.NewEmailButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        contact = (ImageButton) findViewById(R.id.ContactsMenuButton);

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
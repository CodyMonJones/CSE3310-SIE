package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class ComsActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton tab;
    ImageButton add;
    ImageButton messages;
    ImageButton email;
    Button profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coms);

        tab = (ImageButton) findViewById(R.id.TabButton);
        add = (ImageButton) findViewById(R.id.NewContactButton);
        messages = (ImageButton) findViewById(R.id.MessageMenuButton);
        email = (ImageButton) findViewById(R.id.EmailMenuButton);
        profile = (Button) findViewById(R.id.UserContactButton);

        tab.setOnClickListener(this);
        add.setOnClickListener(this);
        messages.setOnClickListener(this);
        email.setOnClickListener(this);
        profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.TabButton:
                break;
            case R.id.NewContactButton:
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
}
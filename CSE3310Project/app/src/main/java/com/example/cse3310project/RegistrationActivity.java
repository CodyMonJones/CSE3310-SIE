package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    MaterialButton cancelButton;
    MaterialButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        cancelButton = (MaterialButton) findViewById(R.id.CancelButton);
        submitButton = (MaterialButton) findViewById(R.id.SubmitButton);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        submitButton.setEnabled(false);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.CancelButton:
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
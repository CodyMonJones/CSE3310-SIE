package com.example.cse3310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText password;
    MaterialButton loginButton;
    MaterialButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.Email);
        password = (EditText)  findViewById(R.id.Password);
        loginButton = (MaterialButton) findViewById(R.id.LoginButton);

        email.addTextChangedListener(new emailWatcher());
        password.addTextChangedListener(new emailWatcher());

        registerButton = (MaterialButton) findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(this);
    }

    private class emailWatcher implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();

            loginButton.setEnabled(!userEmail.isEmpty() && !userPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) 
        {

        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.RegisterButton:
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
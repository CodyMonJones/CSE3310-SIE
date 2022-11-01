package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText password;
    MaterialButton loginButton;
    MaterialButton registerButton;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.Email);
        password = (EditText)  findViewById(R.id.Password);
        loginButton = (MaterialButton) findViewById(R.id.LoginButton);

        email.addTextChangedListener(new emailWatcher());
        password.addTextChangedListener(new emailWatcher());

        registerButton = (MaterialButton) findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(this);

        loginButton.setOnClickListener(this);

        loginButton.setEnabled(false);
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

            case R.id.LoginButton:
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        Intent showHomepage = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(showHomepage);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
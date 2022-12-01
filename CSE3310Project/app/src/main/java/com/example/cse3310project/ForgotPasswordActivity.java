package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText userEmail;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEmail = findViewById(R.id.Email_Reset);
        submitButton = findViewById(R.id.Submit_Password_Reset);

        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText()).matches())
        {
            userEmail.setError("Invalid email");
            showKeyboard(userEmail);
            userEmail.requestFocus();
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Reset Email Sent!", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_LONG).show();
                userEmail.setError("User does not exist");
            }
        });
    }

    public void showKeyboard(final EditText ettext){
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }
}
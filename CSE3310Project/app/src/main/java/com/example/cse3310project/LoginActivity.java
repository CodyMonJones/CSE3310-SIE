package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse3310project.Discussion.DiscussionForum;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private Button forgotPasswordButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        loginButton = findViewById(R.id.LoginButton);
        forgotPasswordButton = findViewById(R.id.Forgot_Password_Button);
        registerButton = findViewById(R.id.RegisterButton);

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);

        loginButton.setEnabled(true);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.RegisterButton:
                Intent i = new Intent(LoginActivity.this, HomeActivity.RegistrationActivity.class);
                startActivity(i);
                break;

            case R.id.LoginButton:
                if(email.getText().toString().isEmpty())
                {
                    email.setError("Email Required");
                    showKeyboard(email);
                    email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())
                {
                    email.setError("Invalid email");
                    showKeyboard(email);
                    email.requestFocus();
                    return;
                }

                if(password.getText().toString().isEmpty())
                {
                    password.setError("Password Required");
                    showKeyboard(password);
                    password.requestFocus();
                    return;
                }

                if(password.getText().toString().length() < 6)
                {
                    password.setError("Password too short must be 6 characters long");
                    showKeyboard(password);
                    password.requestFocus();
                    return;
                }

                loginUser();
                break;

            case R.id.Forgot_Password_Button:
                Intent forgotPasswordActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordActivity);
                break;
        }
    }

    public void loginUser()
    {
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
package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    MaterialButton cancelButton;
    MaterialButton submitButton;

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phoneNumber;
    EditText MavID;
    EditText password;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        cancelButton = (MaterialButton) findViewById(R.id.CancelButton);
        submitButton = (MaterialButton) findViewById(R.id.SubmitButton);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        firstName = (EditText) findViewById(R.id.RegistrationFirstName);
        lastName = (EditText) findViewById(R.id.RegistrationLastName);
        email = (EditText) findViewById(R.id.RegistrationEmail);
        phoneNumber = (EditText) findViewById(R.id.RegistrationPhoneNumber);
        MavID = (EditText) findViewById(R.id.RegistrationMavID);
        password = (EditText) findViewById(R.id.RegistrationPassword);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        submitButton.setEnabled(true);
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

            case R.id.SubmitButton:
                if(firstName.getText().toString().isEmpty())
                {
                    firstName.setError("Must enter a first name");
                    showKeyboard(firstName);
                    firstName.requestFocus();
                    return;
                }

                if(lastName.getText().toString().isEmpty())
                {
                    lastName.setError("Must enter a last name");
                    showKeyboard(lastName);
                    lastName.requestFocus();
                    return;
                }

                if(email.getText().toString().isEmpty())
                {
                    email.setError("Email cannot be empty");
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

                if(phoneNumber.getText().toString().isEmpty())
                {
                    phoneNumber.setError("Phone Number cannot be empty");
                    showKeyboard(phoneNumber);
                    phoneNumber.requestFocus();
                    return;
                }

                if(phoneNumber.getText().length() != 10)
                {
                    phoneNumber.setError("Invalid Phone Number");
                    showKeyboard(phoneNumber);
                    phoneNumber.requestFocus();
                    return;
                }

                if(MavID.getText().length() != 10)
                {
                    MavID.setError("ID must be 10-digits long");
                    showKeyboard(MavID);
                    MavID.requestFocus();
                    return;
                }

                if(password.getText().length() <= 5)
                {
                    password.setError("Must be 6 characters or higher");
                    showKeyboard(password);
                    password.requestFocus();
                    return;
                }

                registerUser();

                break;
        }
    }

    public void registerUser()
    {
        User newUser= new User(firstName.getText().toString(), lastName.getText().toString(),
                email.getText().toString(), Integer.parseInt(MavID.getText().toString()),
                phoneNumber.getText().toString());

        HashMap<String, Object> user = new HashMap<>();
        user.put("fname", newUser.fname);
        user.put("lname", newUser.lname);
        user.put("email", newUser.email);
        user.put("phoneNumber", newUser.phoneNumber);
        user.put("MavID", newUser.MavID);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        user.put("creationDate", currentDateandTime);


        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegistrationActivity.this, "User Registered", Toast.LENGTH_LONG).show();
                Intent showHomePage = new Intent(RegistrationActivity.this, HomeActivity.class);
                startActivity(showHomePage);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
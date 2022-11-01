package com.example.cse3310project;

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

        firstName.addTextChangedListener(new editBoxListener());
        lastName.addTextChangedListener(new editBoxListener());
        email.addTextChangedListener(new editBoxListener());
        phoneNumber.addTextChangedListener(new editBoxListener());
        MavID.addTextChangedListener(new editBoxListener());
        password.addTextChangedListener(new editBoxListener());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private class editBoxListener implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            submitButton.setEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            String userFirstName = firstName.getText().toString();
            String userLastName = lastName.getText().toString();
            String userEmail = email.getText().toString();
            String userPhoneNumber = password.getText().toString();
            int userMavID = 0;
            String userPassword = password.getText().toString();

            try{
                if(!MavID.getText().toString().isEmpty())
                    userMavID = Integer.parseInt(MavID.getText().toString());
            }
            catch(NumberFormatException e)
            {

            }

            boolean boxesFilled = !userFirstName.isEmpty()
                                && !userLastName.isEmpty()
                                && !userEmail.isEmpty()
                                && !userPhoneNumber.isEmpty()
                                && userMavID != 0
                                && !userPassword.isEmpty();

            Log.d("TEST", "boxesFilled =" + boxesFilled);

            submitButton.setEnabled(boxesFilled);
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
            case R.id.CancelButton:
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.SubmitButton:
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

                break;
        }
    }
}
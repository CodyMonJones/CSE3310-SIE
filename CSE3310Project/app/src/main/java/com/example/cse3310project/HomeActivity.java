package com.example.cse3310project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.cse3310project.Discussion.DiscussionForum;
import com.example.cse3310project.databinding.ActivityHomeBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse3310project.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeActivity extends drawerActivity implements View.OnClickListener{

    ActivityHomeBinding activityHomeBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    private CardView messageCard, clubCard, marketCard, tutoringCard, discussionCard, settingsCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        messageCard = (CardView) findViewById(R.id.message_card);
        clubCard = (CardView) findViewById(R.id.club_card);
        marketCard = (CardView) findViewById(R.id.market_card);
        tutoringCard = (CardView) findViewById(R.id.tutoring_card);
        discussionCard = (CardView) findViewById(R.id.discussion_card);
        settingsCard = (CardView) findViewById(R.id.settings_card);

        messageCard.setOnClickListener((View.OnClickListener) this);
        clubCard.setOnClickListener((View.OnClickListener) this);
        marketCard.setOnClickListener((View.OnClickListener) this);
        tutoringCard.setOnClickListener((View.OnClickListener) this);
        discussionCard.setOnClickListener((View.OnClickListener) this);
        settingsCard.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            makeUserLogin();
        }
        else{
            displayUserProfile();
        }

    }

    private void displayUserProfile() {

        String currentUserUUID = mAuth.getCurrentUser().getUid();

        dbRef.child("User").child(currentUserUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("name").exists()){
                    Toast.makeText(HomeActivity.this, "Welcome " + snapshot.child("name").getValue(String.class), Toast.LENGTH_SHORT).show();
                }
                else{
                    makeUserCreateProf();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void makeUserCreateProf() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
    }

    private void makeUserLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.message_card:
                startActivity(new Intent(this, ComsMessagesActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.club_card:
                startActivity(new Intent(this, FormClubActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.market_card:
                startActivity(new Intent(this, TransactionsActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.tutoring_card:
                startActivity(new Intent(this, TutoringRequestActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.discussion_card:
                startActivity(new Intent(this, DiscussionForum.class));
                overridePendingTransition(0,0);
                break;

            case R.id.settings_card:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0,0);
                break;
        }
    }

    public static class RegistrationActivity extends AppCompatActivity implements View.OnClickListener
    {
        // XML variables
        private MaterialButton cancelButton, submitButton;
        private EditText firstName, lastName, email, phoneNumber, MavID, password;

        // Firebase variables
        private FirebaseFirestore db;
        private FirebaseStorage storage;
        private StorageReference storageRef;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            // Initializes the variables
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registration);

            cancelButton = findViewById(R.id.CancelButton);
            submitButton = findViewById(R.id.SubmitButton);

            submitButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

            firstName = findViewById(R.id.RegistrationFirstName);
            lastName = findViewById(R.id.RegistrationLastName);
            email = findViewById(R.id.RegistrationEmail);
            phoneNumber = findViewById(R.id.RegistrationPhoneNumber);
            MavID = findViewById(R.id.RegistrationMavID);
            password = findViewById(R.id.RegistrationPassword);

            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();

            submitButton.setEnabled(true);
        }

        // Implements the logic for the buttons
        @Override
        public void onClick(View view)
        {
            switch(view.getId())
            {
                case R.id.CancelButton:
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

        // Creates an User object and uploads it to the Users collection in firebase as well
        // as creates a new user authenticator
        public void registerUser()
        {
            User newUser= new User(firstName.getText().toString(), lastName.getText().toString(),
                    email.getText().toString(), Integer.parseInt(MavID.getText().toString()),
                    phoneNumber.getText().toString());


            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            newUser.setCreationDate(currentDateandTime);
            newUser.setProfile_picture("user-default/default-user.jpg");


            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>()
            {
                @Override
                public void onSuccess(AuthResult authResult)
                {
                    Toast.makeText(RegistrationActivity.this, "User Registered", Toast.LENGTH_LONG).show();

                    DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                    userRef.set(newUser);
                    userRef.update("userID", mAuth.getCurrentUser().getUid());

                    Intent showHomePage = new Intent(RegistrationActivity.this, HomeActivity.class);
                    startActivity(showHomePage);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        // Shows and sets an error on the entry field if the inputted information is invalid
        public void showKeyboard(final EditText ettext)
        {
            ettext.requestFocus();
            ettext.postDelayed(new Runnable()
                               {
                                   @Override public void run()
                                   {
                                       InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                       keyboard.showSoftInput(ettext,0);
                                   }
                               }
                    ,200);
        }
    }

}
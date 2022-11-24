package com.example.cse3310project;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cse3310project.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.initializedfields.qual.InitializedFields;

import java.util.HashMap;

public class SettingsActivity extends drawerActivity {

    ActivitySettingsBinding activitySettingsBinding;
    private Button updateAccount;
    private EditText userName, userStatus;
    private ImageView profileImage;

    private String currentUserUUID;
    private FirebaseAuth mAuth;
    private DatabaseReference currentDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        allocateActivityTitle("Settings");

        mAuth = FirebaseAuth.getInstance();
        currentUserUUID = mAuth.getCurrentUser().getUid();
        currentDBRef = FirebaseDatabase.getInstance().getReference();


        InitializedFields();

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccountSettings();
            }
        });
    }

    private void updateAccountSettings() {
        String getUserName = userName.getText().toString();
        String getUserStatus = userStatus.getText().toString();

        if(TextUtils.isEmpty(getUserName)){
            Toast.makeText(this, "User Name is Required!", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(getUserStatus)){
            Toast.makeText(this, "Status is Required!", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, String> userProfileMap = new HashMap<>();
                userProfileMap.put("UID", currentUserUUID);
                userProfileMap.put("name", getUserName);
                userProfileMap.put("Password", getUserStatus);

            currentDBRef.child("User").child(currentUserUUID).setValue(userProfileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String errMessage = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: " + errMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void InitializedFields(){
        updateAccount = (Button) findViewById(R.id.update_profileBtn);
        userName = (EditText) findViewById(R.id.set_username);
        userStatus = (EditText) findViewById(R.id.set_status);
        profileImage = (ImageView) findViewById(R.id.prof_image);
    }
}
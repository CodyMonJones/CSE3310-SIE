package com.example.cse3310project;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cse3310project.databinding.ActivitySettingsBinding;

import org.checkerframework.common.initializedfields.qual.InitializedFields;

public class SettingsActivity extends drawerActivity {

    ActivitySettingsBinding activitySettingsBinding;
    private Button updateAccount;
    private EditText userName, userPassword;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        allocateActivityTitle("Settings");

        InitializedFields();
    }

    private void InitializedFields(){
        updateAccount = (Button) findViewById(R.id.update_profileBtn);
        userName = (EditText) findViewById(R.id.set_username);
        userPassword = (EditText) findViewById(R.id.set_password);
        profileImage = (ImageView) findViewById(R.id.prof_image);
    }
}
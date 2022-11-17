package com.example.cse3310project;
import android.os.Bundle;
import com.example.cse3310project.databinding.ActivityFormClubBinding;


public class FormClubActivity extends drawerActivity{

    ActivityFormClubBinding activityFormClubBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFormClubBinding = ActivityFormClubBinding.inflate(getLayoutInflater());
        setContentView(activityFormClubBinding.getRoot());
        allocateActivityTitle("Clubs");
    }
}
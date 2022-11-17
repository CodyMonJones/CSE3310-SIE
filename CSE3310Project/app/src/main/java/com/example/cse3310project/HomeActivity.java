package com.example.cse3310project;
import android.os.Bundle;
import com.example.cse3310project.databinding.ActivityHomeBinding;



public class HomeActivity extends drawerActivity{

    ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");
    }
}
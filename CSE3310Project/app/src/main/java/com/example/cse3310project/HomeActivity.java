package com.example.cse3310project;
import android.os.Bundle;
import com.example.cse3310project.databinding.ActivityHomeBinding;


<<<<<<< HEAD
//<<<<<<< HEAD
//
//=======
//>>>>>>> 6c7efa8512aa1593d9efc53eb1cfda71dcce6e55
=======
public class HomeActivity extends drawerActivity{

    ActivityHomeBinding activityHomeBinding;

>>>>>>> 6746762 (fixed navigation and activity view)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");
    }
}
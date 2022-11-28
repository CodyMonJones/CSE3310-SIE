package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;

import android.text.TextUtils;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cse3310project.Discussion.CommentActivity;
import com.example.cse3310project.Discussion.DiscussionForum;
import com.example.cse3310project.Discussion.DiscussionPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.A;

import com.example.cse3310project.Discussion.DiscussionForum;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.tabs.TabLayout;


public class drawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Home:
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.Clubs:
                startActivity(new Intent(this, FormClubActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.Messages:
                startActivity(new Intent(this, ComsContactsActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.Marketplace:
                startActivity(new Intent(this, TransactionsActivity.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.Settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.Logout:
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0,0);
                Toast.makeText(drawerActivity.this, "Logout Successful ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.FormClub:
                newClubName();
                break;

            case R.id.Discussions:
                startActivity(new Intent(this, CommentActivity.class));
                overridePendingTransition(0,0);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void newClubName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(drawerActivity.this, R.style.Modal);
        builder.setTitle("Create new Club: ");

        final EditText clubName = new EditText(drawerActivity.this);
        clubName.setHint("Club Name");
        builder.setView(clubName);

        builder.setPositiveButton("Create Club", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userClubNameInput = clubName.getText().toString();

                if(TextUtils.isEmpty(userClubNameInput)){
                    Toast.makeText(drawerActivity.this, "Club Name is required...", Toast.LENGTH_SHORT).show();
                }
                else{
                    formNewClub(userClubNameInput);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void formNewClub(String clubName) {
        dbRef.child("Clubs").child(clubName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(drawerActivity.this, clubName + "Created successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}
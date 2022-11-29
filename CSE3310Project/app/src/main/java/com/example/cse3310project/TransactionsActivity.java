package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.cse3310project.Discussion.CommentActivity;
import com.example.cse3310project.Discussion.DiscussionForum;
import com.example.cse3310project.databinding.ActivityTransactionsBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.mediation.MediationBannerAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TransactionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    // ad stuff
    private AdView adView;

    //variables needed for drawer menu
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        // adapter used by viewpager to switch between tabs
        TransactionsVPAdapter transactionsVPAdapter = new TransactionsVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // adds tabs for viewpager to display
        transactionsVPAdapter.addFragment(new TransactionsShopFragment(),"Shop");
        transactionsVPAdapter.addFragment(new TransactionsSellFragment(),"Sell");
        transactionsVPAdapter.addFragment(new TransactionsWishlistFragment(),"Wishlist");
        transactionsVPAdapter.addFragment(new TransactionsCartFragment(),"Cart");

        viewPager = findViewById(R.id.transactionsViewPager);
        viewPager.setAdapter(transactionsVPAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        
        viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position ==0)
                    viewPager.getAdapter().notifyDataSetChanged();
                if (position == 1) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .detach(transactionsVPAdapter.getItem(1))
                            .attach(transactionsVPAdapter.getItem(1))
                            .commit();
                    viewPager.getAdapter().notifyDataSetChanged();
                }
                if (position == 2) {
                    viewPager.getAdapter().notifyDataSetChanged();
                }
                if (position == 3) {
                    viewPager.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.transactions_drawer_layout);
        toolbar = drawerLayout.findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // setting up navView needed to implement
        // activity switching functionality
        NavigationView navigationView = drawerLayout.findViewById(R.id.transactions_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.Home);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // initialize adView
        MobileAds.initialize(this);
        BannerAd();
    }

    void BannerAd(){
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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

            case R.id.Tutoring:
                startActivity(new Intent(this, TutoringRequestActivity.class));
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
                Toast.makeText(TransactionsActivity.this, "Logout Successful ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.FormClub:
                newClubName();
                break;

            case R.id.Discussions:
                startActivity(new Intent(this, DiscussionForum.class));
                overridePendingTransition(0,0);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void newClubName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionsActivity.this, R.style.Modal);
        builder.setTitle("Create new Club: ");

        final EditText clubName = new EditText(TransactionsActivity.this);
        clubName.setHint("Club Name");
        builder.setView(clubName);

        builder.setPositiveButton("Create Club", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userClubNameInput = clubName.getText().toString();

                if(TextUtils.isEmpty(userClubNameInput)){
                    Toast.makeText(TransactionsActivity.this, "Club Name is required...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(TransactionsActivity.this, clubName + "Created successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void goToTransactionsProductCreateActivity(View view) {
        Intent intent = new Intent(this, TransactionsProductCreateActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {

    }

}
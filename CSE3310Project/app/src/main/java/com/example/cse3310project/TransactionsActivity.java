package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.example.cse3310project.Discussion.DiscussionForum;
import com.example.cse3310project.databinding.ActivityTransactionsBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


public class TransactionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    //variables needed for drawer menu
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Home:
                startActivity(new Intent(this, DiscussionForum.class));
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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
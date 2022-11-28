package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.transactions_search, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.transactions_searchbar);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setIconifiedByDefault(false);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//
//                //transactionsShopAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
//
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        return super.onCreateOptionsMenu(menu);
//    }

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
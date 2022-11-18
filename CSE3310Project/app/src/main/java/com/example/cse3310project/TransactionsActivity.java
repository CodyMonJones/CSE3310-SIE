package com.example.cse3310project;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cse3310project.databinding.ActivityTransactionsBinding;
import com.google.android.material.tabs.TabLayout;


public class TransactionsActivity extends drawerActivity implements View.OnClickListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ActivityTransactionsBinding activityTransactionsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        /*
        <--- This code is to implement NavBar w/ Navigation currently not working with Transactions --->
        activityTransactionsBinding = ActivityTransactionsBinding.inflate(getLayoutInflater());
        setContentView(activityTransactionsBinding.getRoot());
        <--- Cody --->
         */

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.transactionsViewPager);

        tabLayout.setupWithViewPager(viewPager);

        TransactionsVPAdapter transactionsVPAdapter = new TransactionsVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        transactionsVPAdapter.addFragment(new TransactionsShopFragment(),"Shop");
        transactionsVPAdapter.addFragment(new TransactionsSellFragment(),"Sell");
        transactionsVPAdapter.addFragment(new TransactionsWishlistFragment(),"Wishlist");
        transactionsVPAdapter.addFragment(new TransactionsCartFragment(),"Cart");
        viewPager.setAdapter(transactionsVPAdapter);
    }


    public void goToTransactionsProductCreateActivity(View view) {
        Intent intent = new Intent(this, TransactionsProductCreateActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {

    }

}
package com.example.cse3310project;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.example.cse3310project.databinding.ActivityFormClubBinding;
import com.google.android.material.tabs.TabLayout;


public class FormClubActivity extends drawerActivity {

    ActivityFormClubBinding activityFormClubBinding;
    private TabLayout clubTabLayout;
    private ViewPager2 clubViewPager2;
    private tabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFormClubBinding = ActivityFormClubBinding.inflate(getLayoutInflater());
        setContentView(activityFormClubBinding.getRoot());
        allocateActivityTitle("Club Page");

        clubTabLayout = findViewById(R.id.clubTabLayout);
        clubViewPager2 = findViewById(R.id.clubViewPager);
        adapter = new tabAdapter(this);
        clubViewPager2.setAdapter(adapter);

        clubTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                clubViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        clubViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                clubTabLayout.getTabAt(position).select();
            }
        });
    }
}
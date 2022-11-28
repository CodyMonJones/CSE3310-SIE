package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class tabAdapter extends FragmentStateAdapter {

    public tabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                return new clubsFragment();

            case 2:
                return new clubContactFragment();

            default:
                return new clubMessageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

package com.example.cse3310project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class clubsFragment extends Fragment {

    private View clubFragmentView;
    private ListView clubsList;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfClubs = new ArrayList<>();

    private DatabaseReference clubsDBRef;

    public clubsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        clubFragmentView = inflater.inflate(R.layout.fragment_clubs, container, false);

        clubsDBRef = FirebaseDatabase.getInstance().getReference().child("Clubs");

        initializeClubListing();

        showClubList();

        clubsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int listPosition, long l) {
                String selectedClubName = adapterView.getItemAtPosition(listPosition).toString();

                Intent clubChatIntent = new Intent(getContext(), clubChatActivity.class);
                clubChatIntent.putExtra("clubName", selectedClubName);
                startActivity(clubChatIntent);
            }
        });

        return clubFragmentView;
    }

    private void showClubList() {
        clubsDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> clubSet = new HashSet<>();
                Iterator iterator = snapshot.getChildren().iterator();

                while(iterator.hasNext()){
                    clubSet.add(((DataSnapshot)iterator.next()).getKey());
                }
                listOfClubs.clear();
                listOfClubs.addAll(clubSet);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeClubListing() {
        clubsList = (ListView) clubFragmentView.findViewById(R.id.clubList);
        arrayAdapter = new ArrayAdapter<String> (getContext(), android.R.layout.simple_list_item_1, listOfClubs);
        clubsList.setAdapter(arrayAdapter);
    }
}
package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.cse3310project.Discussion.CustomAdapter;
import com.example.cse3310project.Discussion.DiscussionPost;
import com.example.cse3310project.databinding.ActivityDiscussionForumBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;


public class TransactionsShopFragment extends Fragment {

    ArrayList<TransactionsProduct> productArrayList = new ArrayList<>();
    private FirebaseFirestore marketplaceDb;
    private TransactionsAdapter transactionsAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transactions_shop, container, false);

        // setting up the recyclerView
        recyclerView = rootView.findViewById(R.id.transactions_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionsShopFragment.this.getContext()));

        // setting up firebase firestore
        marketplaceDb = FirebaseFirestore.getInstance();

        // downloads data from firestore into arraylist
        if(productArrayList.size()>0)
        {
            productArrayList.clear();
        }

        marketplaceDb.collection("Marketplace_Listings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                TransactionsProduct product = new TransactionsProduct(
                                        document.getString("title"),
                                        document.getString("desc"),
                                        document.getString("image"),
                                        document.getString("price"),
                                        document.getString("lendTime"),
                                        document.getString("exchange"),
                                        document.getString("uniqueID"));
                                productArrayList.add(product);
                            }
                        }
                        else {
                            Toast.makeText(TransactionsShopFragment.this.getContext(),
                                    "Error downloading from database",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        transactionsAdapter = new TransactionsAdapter(productArrayList,
                                TransactionsShopFragment.this.getContext());
                        recyclerView.setAdapter(transactionsAdapter);
                    }
                });

        // Inflate the layout for this fragment
        return rootView;
    }

}
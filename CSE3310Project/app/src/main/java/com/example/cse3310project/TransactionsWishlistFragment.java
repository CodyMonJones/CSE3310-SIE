package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class TransactionsWishlistFragment extends Fragment implements TransactionsRecyclerViewInterface{
    ArrayList<TransactionsProduct> productWishlistArrayList = new ArrayList<>();
    private FirebaseFirestore marketplaceDb;
    private TransactionsWishlistAdapter transactionsWishlistAdapter;
    private RecyclerView recyclerView;

    // used to get the current user
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transactions_wishlist, container, false);

        // setting up the recyclerView
        recyclerView = rootView.findViewById(R.id.transactions_wishlist_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionsWishlistFragment.this.getContext()));

        // setting up firebase firestore
        marketplaceDb = FirebaseFirestore.getInstance();

        // if the arraylist has data, clear it
        if(productWishlistArrayList.size()>0)
        {
            productWishlistArrayList.clear();
        }

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        // create an ArrayList to hold the wishlist data
        ArrayList<String> userWishlist = new ArrayList<>();
        // download the user's wishlist from firestore
        // and load it into the ArrayList
        DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> wishlistArray = (ArrayList<String>) document.get("transactionWishlistItemIDs");
                        if (wishlistArray != null){
                            for (String i : wishlistArray) {
                                userWishlist.add(i);
                            }
                        }

                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                // downloads data from firestore into arraylist
                marketplaceDb.collection("Marketplace_Listings")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        for(String i : userWishlist) {
                                            if(i.equals(document.getString("uniqueID"))) {
                                                TransactionsProduct product = new TransactionsProduct(
                                                        document.getString("title"),
                                                        document.getString("desc"),
                                                        document.getString("imageRef"),
                                                        document.getDouble("price"),
                                                        document.getString("lendTime"),
                                                        document.getString("exchange"),
                                                        document.getString("uniqueID"),
                                                        document.getString("sellerID"),
                                                        document.getString("datePosted"),
                                                        document.getTimestamp("timestamp"));
                                                productWishlistArrayList.add(product);
                                            }
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(TransactionsWishlistFragment.this.getContext(),
                                            "Error downloading from database",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                                transactionsWishlistAdapter = new TransactionsWishlistAdapter(productWishlistArrayList,
                                        TransactionsWishlistFragment.this.getContext(),
                                        TransactionsWishlistFragment.this);
                                recyclerView.setAdapter(transactionsWishlistAdapter);
                            }
                        });
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(getActivity(), TransactionsProductViewActivity.class);

        intent.putExtra("PRODUCT", productWishlistArrayList.get(position));

        startActivity(intent);
    }

    @Override
    public void onWishlistButtonChecked(int position, CompoundButton compoundButton) {
        TransactionsProduct localProduct = productWishlistArrayList.get(position);
        DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());
        if (compoundButton.isChecked()) {
            // Upload listing ID to user's account
            userRef.update("transactionWishlistItemIDs", FieldValue.arrayUnion(localProduct.getUniqueID()));
            Intent intent = new Intent();
            intent.setAction(TransactionsShopFragment.RADIO_DATASET_CHANGED);
            getActivity().getApplicationContext().sendBroadcast(intent);
            transactionsWishlistAdapter.notifyDataSetChanged();
        }
        else {
            userRef.update("transactionWishlistItemIDs", FieldValue.arrayRemove(localProduct.getUniqueID()));
            Intent intent = new Intent();
            intent.setAction(TransactionsShopFragment.RADIO_DATASET_CHANGED);
            getActivity().getApplicationContext().sendBroadcast(intent);
            transactionsWishlistAdapter.notifyDataSetChanged();

            // remove the deleted listing from the ArrayList
            productWishlistArrayList.remove(position);

            // reset the recyclerView to represent changes
            transactionsWishlistAdapter = new TransactionsWishlistAdapter(productWishlistArrayList,
                    TransactionsWishlistFragment.this.getContext(),
                    TransactionsWishlistFragment.this);
            recyclerView.setAdapter(transactionsWishlistAdapter);
        }
    }

    @Override
    public void onDeleteButtonClick(int position) {
    }

}
package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.NoCopySpan;
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

import java.util.ArrayList;


public class TransactionsShopFragment extends Fragment implements TransactionsRecyclerViewInterface{

    ArrayList<TransactionsProduct> productShopArrayList = new ArrayList<>();
    private FirebaseFirestore marketplaceDb;
    private TransactionsShopAdapter transactionsShopAdapter;
    private RecyclerView recyclerView;

    // used to get the current user
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

    // setup radio to recieve datasetchanged notice in wishlist fragment
    public static final String RADIO_DATASET_CHANGED = "com.example.cse3310.RADIO_DATASET_CHANGED";

    private Radio radio;

    private class Radio extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RADIO_DATASET_CHANGED)) {
                //Notify dataset changed here
                transactionsShopAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //using intent filter just in case you would like to listen for more transmissions
        IntentFilter filter = new IntentFilter();
        filter.addAction(RADIO_DATASET_CHANGED);
        getActivity().getApplicationContext().registerReceiver(radio, filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().getApplicationContext().unregisterReceiver(radio);
        }catch (Exception e){
            //Cannot unregister receiver
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radio = new Radio();
    }

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

        // if the arraylist has data, clear it
        if(productShopArrayList.size()>0)
        {
            productShopArrayList.clear();
        }

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        // create a string to hold the current userID
        DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());
        String user = userRef.getId();

        // downloads data from firestore into arraylist
        marketplaceDb.collection("Marketplace_Listings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!(user.equals(document.getString("sellerID")))) {
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
                                    productShopArrayList.add(product);
                                }
                            }
                        }
                        else {
                            Toast.makeText(TransactionsShopFragment.this.getContext(),
                                    "Error downloading from database",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        transactionsShopAdapter = new TransactionsShopAdapter(productShopArrayList,
                                TransactionsShopFragment.this.getContext(),
                                TransactionsShopFragment.this);
                        recyclerView.setAdapter(transactionsShopAdapter);
                    }
                });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(getActivity(), TransactionsProductViewActivity.class);

        intent.putExtra("PRODUCT", productShopArrayList.get(position));

        startActivity(intent);
    }

    @Override
    public void onWishlistButtonChecked(int position, CompoundButton compoundButton) {
        TransactionsProduct localProduct = productShopArrayList.get(position);
        DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());
        if (compoundButton.isChecked()) {
            // Upload listing ID to user's account
            userRef.update("transactionWishlistItemIDs", FieldValue.arrayUnion(localProduct.getUniqueID()));
            transactionsShopAdapter.notifyDataSetChanged();
        }
        else {
            userRef.update("transactionWishlistItemIDs", FieldValue.arrayRemove(localProduct.getUniqueID()));
            transactionsShopAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDeleteButtonClick(int position) {
    }
}
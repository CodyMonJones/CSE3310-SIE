package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TransactionsSellFragment extends Fragment implements TransactionsRecyclerViewInterface{

    ArrayList<TransactionsProduct> productSellArrayList = new ArrayList<>();
    private FirebaseFirestore marketplaceDb;
    private TransactionsSellAdapter transactionsSellAdapter;
    private RecyclerView recyclerView;

    // used to get the current user
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser) {
//            // create a string to hold the current userID
//            DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());
//            String user = userRef.getId();
//
//            // downloads data from firestore into arraylist
//            marketplaceDb.collection("Marketplace_Listings")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    if(user.equals(document.getString("sellerID"))) {
//                                        TransactionsProduct product = new TransactionsProduct(
//                                                document.getString("title"),
//                                                document.getString("desc"),
//                                                document.getString("imageRef"),
//                                                document.getDouble("price"),
//                                                document.getString("lendTime"),
//                                                document.getString("exchange"),
//                                                document.getString("uniqueID"),
//                                                document.getString("sellerID"),
//                                                document.getString("datePosted"),
//                                                document.getTimestamp("timestamp"));
//                                        productSellArrayList.add(product);
//                                    }
//                                }
//                            }
//                            else {
//                                Toast.makeText(TransactionsSellFragment.this.getContext(),
//                                        "Error downloading from database",
//                                        Toast.LENGTH_SHORT).show();
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//
//                            transactionsSellAdapter = new TransactionsSellAdapter(productSellArrayList,
//                                    TransactionsSellFragment.this.getContext(),
//                                    TransactionsSellFragment.this);
//                            recyclerView.setAdapter(transactionsSellAdapter);
//                        }
//                    });
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transactions_sell, container, false);

        // setting up the recyclerView
        recyclerView = rootView.findViewById(R.id.transactions_sell_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionsSellFragment.this.getContext()));

        // setting up firebase firestore
        marketplaceDb = FirebaseFirestore.getInstance();

        // if the arraylist has data, clear it
        if(productSellArrayList.size()>0)
        {
            productSellArrayList.clear();
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
                                if(user.equals(document.getString("sellerID"))) {
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
                                    productSellArrayList.add(product);
                                }
                            }
                        }
                        else {
                            Toast.makeText(TransactionsSellFragment.this.getContext(),
                                    "Error downloading from database",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        transactionsSellAdapter = new TransactionsSellAdapter(productSellArrayList,
                                TransactionsSellFragment.this.getContext(),
                                TransactionsSellFragment.this);
                        recyclerView.setAdapter(transactionsSellAdapter);
                    }
                });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(getActivity(), TransactionsProductEditActivity.class);

        intent.putExtra("PRODUCT", productSellArrayList.get(position));

        startActivity(intent);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        DocumentReference productRef = marketplaceDb.collection("Marketplace_Listings").document(productSellArrayList.get(position).getUniqueID());
        DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());

        userRef.update("transactionListedItemIDs", FieldValue.arrayRemove(productSellArrayList.get(position).getUniqueID()));
        productRef.delete();
        transactionsSellAdapter.notifyItemRemoved(position);

        // remove the deleted listing from the ArrayList
        productSellArrayList.remove(position);

        // reset the recyclerView to represent changes
        transactionsSellAdapter = new TransactionsSellAdapter(productSellArrayList,
                TransactionsSellFragment.this.getContext(),
                TransactionsSellFragment.this);
        recyclerView.setAdapter(transactionsSellAdapter);
    }

    @Override
    public void onWishlistButtonChecked(int position, CompoundButton compoundButton) {
    }
}
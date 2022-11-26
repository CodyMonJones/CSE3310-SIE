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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class TransactionsCartFragment extends Fragment implements TransactionsRecyclerViewInterface {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public Double runningSubtotal = 0.00;
    TextView subtotal;
    Button purchaseButton;

    ArrayList<TransactionsProduct> productCartArrayList = new ArrayList<>();
    private FirebaseFirestore marketplaceDb;
    private TransactionsCartAdapter transactionsCartAdapter;
    private RecyclerView recyclerView;

    // used to get the current user
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transactions_cart, container, false);

        subtotal = rootView.findViewById(R.id.transactions_cart_subtotal);
        purchaseButton = rootView.findViewById(R.id.transactions_cart_purchase_button);

        // setting up the recyclerView
        recyclerView = rootView.findViewById(R.id.transactions_cart_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionsCartFragment.this.getContext()));

        // setting up firebase firestore
        marketplaceDb = FirebaseFirestore.getInstance();

        // if the arraylist has data, clear it
        if (productCartArrayList.size() > 0) {
            productCartArrayList.clear();
        }

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        // create a string to hold the current userID
        DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());

        // create an ArrayList to hold the wishlist data
        ArrayList<String> userCart = new ArrayList<>();
        // download the user's wishlist from firestore
        // and load it into the ArrayList
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> cartArray = (ArrayList<String>) document.get("transactionCartItemIDs");
                        if (cartArray != null) {
                            for (String i : cartArray) {
                                userCart.add(i);
                            }
                        }

                    }
                    else{
                        Log.d(TAG, "No such document");
                    }
                }
                else{
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
                                        for (String i : userCart) {
                                            if (i.equals(document.getString("uniqueID"))) {
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
                                                productCartArrayList.add(product);
                                            }
                                            runningSubtotal = 0.0;
                                            for (TransactionsProduct j : productCartArrayList) {
                                                runningSubtotal = runningSubtotal + j.getPrice();
                                            }
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(TransactionsCartFragment.this.getContext(),
                                            "Error downloading from database",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                                transactionsCartAdapter = new TransactionsCartAdapter(productCartArrayList,
                                        TransactionsCartFragment.this.getContext(),
                                        TransactionsCartFragment.this);
                                recyclerView.setAdapter(transactionsCartAdapter);
                            }
                        });
            }
        });

        subtotal.setText("Subtotal: $" + df.format(runningSubtotal));

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productCartArrayList.size()>0) {
                    Intent intent = new Intent(getActivity(), PaymentsActivity.class);
                    intent.putExtra("SUBTOTAL", subtotal.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please add items to cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onProductClick(int position) {
        Intent intent = new Intent(getActivity(), TransactionsProductViewActivity.class);

        intent.putExtra("PRODUCT", productCartArrayList.get(position));

        startActivity(intent);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());

        userRef.update("transactionCartItemIDs", FieldValue.arrayRemove(productCartArrayList.get(position).getUniqueID()));
        transactionsCartAdapter.notifyItemRemoved(position);

        // remove the deleted listing from the ArrayList
        productCartArrayList.remove(position);

        // get updated running subtotal
        runningSubtotal = 0.0;
        for(TransactionsProduct i : productCartArrayList) {
            runningSubtotal = runningSubtotal + i.getPrice();
        }
        subtotal.setText("Subtotal: $" + df.format(runningSubtotal));

        // reset the recyclerView to represent changes
        transactionsCartAdapter = new TransactionsCartAdapter(productCartArrayList,
                TransactionsCartFragment.this.getContext(),
                TransactionsCartFragment.this);
        recyclerView.setAdapter(transactionsCartAdapter);


    }

    @Override
    public void onWishlistButtonChecked(int position, CompoundButton compoundButton) {
    }
}
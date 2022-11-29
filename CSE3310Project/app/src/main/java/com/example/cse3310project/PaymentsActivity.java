package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ArrayTable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PaymentsActivity extends AppCompatActivity{

    public ArrayList<String> creditCards = new ArrayList<>();
    ArrayAdapter<String> adapter;

    // Create ArrayList to hold all of the listing ID's in cart
    public ArrayList<String> listingsToDelete = new ArrayList<>();

    // create two ArrayLists to store each listing's seller ID and imageReference repsectively
    public ArrayList<String> sellerIDsToRemoveListingsFrom = new ArrayList<>();
    public ArrayList<String> listingImageReferences = new ArrayList<>();

    //Firestore used to upload data
    FirebaseFirestore paymentDB;
    // used to get the current user
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

    // Create a Cloud Storage reference from the app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    AutoCompleteTextView editTextFilledExposedDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        // initialize the firebase connection
        paymentDB = FirebaseFirestore.getInstance();

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        DocumentReference userRef = paymentDB.collection("Users").document(currentUser.getUid());

        // initialize the passed-in product object
        String importedSubtotal = getIntent().getStringExtra("SUBTOTAL");

        // connect each layout id to a variable
        TextView subtotal = findViewById(R.id.payments_subtotal);
        Button confirmPurchaseButton = findViewById(R.id.payments_confirm_purchase_button);
        Button addCardButton = findViewById(R.id.payments_add_card_button);

        // assign layout subtotal with passed-in subtotal
        subtotal.setText(importedSubtotal);

        // toolbar is defined in layout file
        Toolbar paymentsToolbar = (Toolbar) findViewById(R.id.payments_toolbar);
        setSupportActionBar(paymentsToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar paymentsActionBar = getSupportActionBar();

        // Enable the ActionBar Up button (go back to previous activity)
        paymentsActionBar.setDisplayHomeAsUpEnabled(true);

        // set up card selector dropdown menu
        // download any saved cards from current user's firestore document
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    creditCards.clear();
                    if (document.exists()) {
                        ArrayList<String> cardArray = (ArrayList<String>) document.get("creditCards");
                        if (cardArray != null){
                            creditCards.addAll(cardArray);
                        }
                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // place saved cards into ArrayAdapter if there are any existing cards
        if(creditCards != null){
            adapter =
                    new ArrayAdapter<>(
                            this,
                            R.layout.payments_dropdown_menu_popup_item,
                            creditCards);

            editTextFilledExposedDropdown = findViewById(R.id.payments_filled_exposed_dropdown);
            editTextFilledExposedDropdown.setAdapter(adapter);
        }


        // override the add-card button's onclick listener to run custom function
        addCardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goToAddCardActivity();
                adapter.notifyDataSetChanged();
            }
        });

        // override the add-to-cart button's onclick listener to run custom function
        confirmPurchaseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(editTextFilledExposedDropdown.getText().toString().length() != 16){
                    editTextFilledExposedDropdown.setError("Please select a credit card");
                    showKeyboard(editTextFilledExposedDropdown);
                    editTextFilledExposedDropdown.requestFocus();
                    return;
                }
                else{
                    confirmPurchase(sellerIDsToRemoveListingsFrom, listingImageReferences);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DocumentReference userRef = paymentDB.collection("Users").document(currentUser.getUid());

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    creditCards.clear();
                    if (document.exists()) {
                        ArrayList<String> cardArray = (ArrayList<String>) document.get("creditCards");
                        if (cardArray != null){
                            creditCards.addAll(cardArray);
                        }
                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        adapter.notifyDataSetChanged();
    }

    void goToAddCardActivity(){
        Intent intent = new Intent(this, PaymentsAddCardActivity.class);
        startActivity(intent);
    }

    // deletes all traces of purchased listing(s) from firebase
    void confirmPurchase(ArrayList<String> sellerIDsToRemoveListingsFrom, ArrayList<String> listingImageReferences) {

    DocumentReference userRef = paymentDB.collection("Users").document(currentUser.getUid());
        // download all listing ID's meant for deletion into the above ArrayList
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        ArrayList<String> cartArray = (ArrayList<String>) document.get("transactionCartItemIDs");
                        listingsToDelete.addAll(cartArray);
                    }
                    else{
                        Log.d(TAG, "No such document");
                    }
                }
                else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
                // download all relevant seller IDs and image references into the above ArrayLists
                for(String i : listingsToDelete){
                    paymentDB.collection("Marketplace_Listings").document(i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String sellerID = (String) document.get("sellerID");
                                    sellerIDsToRemoveListingsFrom.add(sellerID);
                                    String imageReference = (String) document.get("ImageRef");
                                    listingImageReferences.add(imageReference);
                                }
                                else {
                                    Log.d(TAG, "no such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                            // this goes through each seller's user document
                            for (String i : sellerIDsToRemoveListingsFrom){
                                paymentDB.collection("Users").document(i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()){
                                                ArrayList<String> userListedItems= (ArrayList<String>) document.get("transactionListedItemIDs");
                                                // loop through each listing in seller's user account
                                                for(String j : userListedItems){
                                                    // loop through each listing meant to be deleted
                                                    for(String k : listingsToDelete){
                                                        // if the ID of the seller's listing is equal
                                                        // to the ID of the listing to be deleted, then
                                                        // delete the seller's listing ID
                                                        if(j.equals(k)){
                                                            paymentDB.collection("Users").document(i).update("transactionListedItemIDs", FieldValue.arrayRemove(j));
                                                        }
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
                                        // this deletes the actual listings from the database
                                        for(String i : listingsToDelete){
                                            paymentDB.collection("Marketplace_Listings").document(i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            paymentDB.collection("Marketplace_Listings").document(i).delete();
                                                        }
                                                        else {
                                                            Log.d(TAG, "No such document");
                                                        }
                                                    }
                                                    else {
                                                        Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                    // this deletes the current user's cart
                                                    userRef.update("transactionCartItemIDs", FieldValue.delete());
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        this.finish();
    }

    public void showKeyboard(final EditText ettext){
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }
}
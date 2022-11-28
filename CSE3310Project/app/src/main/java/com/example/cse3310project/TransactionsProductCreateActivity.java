package com.example.cse3310project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.PipedReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.util.UUID;

public class TransactionsProductCreateActivity extends AppCompatActivity{

    // Button to select image
    // Button to submit listing
    Button ProductImage, submitButton, lendCheckBox, exchangeCheckBox;

    // One Preview Image
    ImageView PreviewImage;
    public Uri selectedImageUri;

    // User text-input fields
    EditText listingTitle, listingDescription, listingPrice, listingLend, listingExchange;

    //Firestore used to upload data
    FirebaseFirestore transactionDB;

    // Create a Cloud Storage reference from the app
    // used to upload the image
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    // used to get the current user
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_product_create);

        // Needed to transfer data with firebase
        transactionDB = FirebaseFirestore.getInstance();

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        // toolbar is defined in layout file
        Toolbar transactionsProductCreateToolbar = (Toolbar) findViewById(R.id.transactionProductCreateToolbar);
        setSupportActionBar(transactionsProductCreateToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar transactionsProductCreateActionBar = getSupportActionBar();

        // Enable the ActionBar Up button (go back to previous activity)
        transactionsProductCreateActionBar.setDisplayHomeAsUpEnabled(true);

        // register the UI widgets with their appropriate IDs
        ProductImage = findViewById(R.id.transactionProductImage);
        PreviewImage = findViewById(R.id.transactionProductImagePreview);
        // register the user input items with their appropriate IDs
        listingTitle = findViewById(R.id.transactionProductTitle);
        listingDescription = findViewById(R.id.transactionProductDescription);
        listingPrice = findViewById(R.id.transactionProductPrice);
        listingLend = findViewById(R.id.transactionProductLendTime);
        listingExchange = findViewById(R.id.transactionProductExchange);
        lendCheckBox = findViewById(R.id.transactionProductLendTimeCheckBox);
        exchangeCheckBox = findViewById(R.id.transactionProductExchangeCheckBox);
        submitButton = findViewById(R.id.transactionProductSubmitButton);

        // these 2 functions override the checkboxes to enable/disable
        // their respective editText entries
        lendCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox temp = (CheckBox)view;
                if (temp.isChecked()){
                    listingLend.setEnabled(true);
                    listingLend.setText(null);
                }else {
                    listingLend.setEnabled(false);
                    listingLend.setText("N/A");
                }
            }
        });
        exchangeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox temp = (CheckBox)view;
                if (temp.isChecked()){
                    listingExchange.setEnabled(true);
                    listingExchange.setText(null);
                }else {
                    listingExchange.setEnabled(false);
                    listingExchange.setText("N/A");
                }
            }
        });

        // handle the Choose Image button to trigger
        // the image chooser function
        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        imageChooser();
                    }
        });

        // override the submission button's onclick listener to run custom function
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTransactionProduct();
            }
        });
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    PreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    // this function is triggered when the submit
    // button is clicked. It ensures everything has
    // data and then runs the firebase upload functions
    public void createTransactionProduct()
    {
        if(PreviewImage.getDrawable() == null)
        {
            Toast.makeText(getApplicationContext(), "Please upload an image", Toast.LENGTH_LONG).show();
            return;
        }

        if(listingTitle.getText().toString().isEmpty())
        {
            listingTitle.setError("Please enter a title");
            showKeyboard(listingTitle);
            listingTitle.requestFocus();
            return;
        }
        else
        {
            listingTitle.setError(null);
        }
        if(listingDescription.getText().toString().isEmpty())
        {
            listingDescription.setError("Please enter a description");
            showKeyboard(listingDescription);
            listingDescription.requestFocus();
            return;
        }
        else
        {
            listingDescription.setError(null);
        }

        if(listingPrice.getText().toString().isEmpty())
        {
            listingPrice.setError("Please enter a price");
            showKeyboard(listingPrice);
            listingPrice.requestFocus();
            return;
        }
        else if((listingPrice.getText().toString().contains(".") == true) && (listingPrice.getText().toString().length()-(listingPrice.getText().toString().indexOf(".")+1) > 2))
        {
            listingPrice.setError("Invalid cent value");
            showKeyboard(listingPrice);
            listingPrice.requestFocus();
            return;
        }
        else
        {
            listingPrice.setError(null);
        }

        if(listingLend.getText().toString().isEmpty())
        {
            listingLend.setError("Please enter a lend period");
            showKeyboard(listingLend);
            listingLend.requestFocus();
            return;
        }
        else
        {
            listingLend.setError(null);
        }

        if(listingExchange.getText().toString().isEmpty())
        {
            listingExchange.setError("Please enter exchange details");
            showKeyboard(listingExchange);
            listingExchange.requestFocus();
            return;
        }
        else
        {
            listingExchange.setError(null);
        }

        TransactionProductSubmission(new TransactionsProduct(listingTitle.getText().toString(),
                listingDescription.getText().toString(),
                null,
                Double.parseDouble(listingPrice.getText().toString()),
                listingLend.getText().toString(),
                listingExchange.getText().toString(), null, null, null, null),
                selectedImageUri);

    }

    // this function uploads the passed-in class to firebase.
    // the image goes to firebase storage
    public void TransactionProductSubmission(TransactionsProduct product, Uri selectedImageUri)
    {

        // Create a reference to product image
        final String randomKey = UUID.randomUUID().toString();
        StorageReference listingImageRef = storageRef.child("images/"+randomKey);

        // Copy reference into object as a string
        product.setImageRef("images/"+randomKey);

        // Upload the image reference to firebase storage
        listingImageRef.putFile(selectedImageUri);

        // Assign unique ID to listing
        DocumentReference productReference = transactionDB.collection("Marketplace_Listings").document();
        product.setUniqueID(productReference.getId());

        // Upload listing ID to user's account
        DocumentReference userRef = transactionDB.collection("Users").document(currentUser.getUid());
        userRef.update("transactionListedItemIDs", FieldValue.arrayUnion(product.getUniqueID()));

        // Assign userID to listing
        product.setSellerID(userRef.getId());

        // Assign date/time to listing
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        product.setDatePosted(currentDate);

        // Assign timestamp to listing
        product.setTimestamp(Timestamp.now());

        // Upload listing to firestore
        productReference.set(product);
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
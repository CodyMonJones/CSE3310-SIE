package com.example.cse3310project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.PipedReader;
import java.util.UUID;

public class TransactionsProductCreateActivity extends AppCompatActivity{

    // Button to select image
    // Button to submit listing
    Button ProductImage, submitButton;

    // One Preview Image
    ImageView PreviewImage;
    public Uri selectedImageUri;

    EditText listingTitle, listingDescription, listingPrice;

    //Firestore used to upload data
    FirebaseFirestore transactionDB;

    // Create a Cloud Storage reference from the app
    // used to upload the image
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_product_create);

        transactionDB = FirebaseFirestore.getInstance();

        //toolbar is defined in layout file
        Toolbar transactionsProductCreateToolbar = (Toolbar) findViewById(R.id.transactionProductCreateToolbar);
        setSupportActionBar(transactionsProductCreateToolbar);

        //Get a support ActionBar corresponding to this toolbar
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
        submitButton = findViewById(R.id.transactionProductSubmitButton);

        //override the submission buttons onclick listener to run custom function
        submitButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                createTransactionProduct();
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
    // data and then runs the firebase upload function
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
        else
        {
            listingPrice.setError(null);
        }

        TransactionProductSubmission(new TransactionsProduct(listingTitle.getText().toString(), listingDescription.getText().toString(), null, Float.parseFloat(listingPrice.getText().toString())), selectedImageUri);

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

    // this function uploads the passed-in class to firebase.
    // the image goes to firebase storage
    public void TransactionProductSubmission(TransactionsProduct product, Uri selectedImageUri)
    {

        // Create a reference to product image
        final String randomKey = UUID.randomUUID().toString();
        StorageReference listingImageRef = storageRef.child("images/"+randomKey);

        //upload the reference to firebase
        listingImageRef.putFile(selectedImageUri);

        //Upload everything except for the image
        DocumentReference productReference = transactionDB.collection("Marketplace_Listings").document();
        product.setUniqueID(productReference.getId());

        productReference.set(product);
        this.finish();
    }
}

//ALTERNATIVE CODE: USE THIS AS LAST RESORT IF ABOVE CODE FOR UPLOADING IMAGES STOPS WORKING RIGHT
//    private void imageChooser()
//    {
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//
//        launchSomeActivity.launch(i);
//    }
//
//    ActivityResultLauncher<Intent> launchSomeActivity
//            = registerForActivityResult(
//            new ActivityResultContracts
//                    .StartActivityForResult(),
//            result -> {
//                if (result.getResultCode()
//                        == Activity.RESULT_OK) {
//                    Intent data = result.getData();
//                    // do your operation from here....
//                    if (data != null
//                            && data.getData() != null) {
//                        Uri selectedImageUri = data.getData();
//                        Bitmap selectedImageBitmap = null;
//                        try {
//                            selectedImageBitmap
//                                    = MediaStore.Images.Media.getBitmap(
//                                    this.getContentResolver(),
//                                    selectedImageUri);
//                        }
//                        catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        PreviewImage.setImageBitmap(
//                                selectedImageBitmap);
//                    }
//                }
//            });
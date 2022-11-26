package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TransactionsProductEditActivity extends AppCompatActivity {

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
    //not sure what this does lmao
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

    public TransactionsProduct product;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_product_edit);

        //insert below
        // Needed to transfer data with firebase
        transactionDB = FirebaseFirestore.getInstance();

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        // initialize the passed-in product object
        product = (TransactionsProduct) getIntent().getParcelableExtra("PRODUCT");

        // toolbar is defined in layout file
        Toolbar transactionsProductEditToolbar = (Toolbar) findViewById(R.id.transactionProductEditToolbar);
        setSupportActionBar(transactionsProductEditToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar transactionsProductEditActionBar = getSupportActionBar();

        // Enable the ActionBar Up button (go back to previous activity)
        transactionsProductEditActionBar.setDisplayHomeAsUpEnabled(true);

        // register the UI widgets with their appropriate IDs
        ProductImage = findViewById(R.id.transactionProductImageEdit);
        PreviewImage = findViewById(R.id.transactionProductImagePreviewEdit);
        // register the user input items with their appropriate IDs
        listingTitle = findViewById(R.id.transactionProductTitleEdit);
        listingDescription = findViewById(R.id.transactionProductDescriptionEdit);
        listingPrice = findViewById(R.id.transactionProductPriceEdit);
        listingLend = findViewById(R.id.transactionProductLendTimeEdit);
        listingExchange = findViewById(R.id.transactionProductExchangeEdit);
        lendCheckBox = findViewById(R.id.transactionProductLendTimeCheckBoxEdit);
        exchangeCheckBox = findViewById(R.id.transactionProductExchangeCheckBoxEdit);
        submitButton = findViewById(R.id.transactionProductSubmitButtonEdit);

        // assign each user input element with the corresponding product data
        StorageReference imageRef = storageRef.child(product.getImageRef());

        imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                PreviewImage.setImageBitmap(bitmap);
            }
        });
        listingTitle.setText(product.getTitle());
        listingDescription.setText(product.getDesc());
        listingPrice.setText(product.getPrice().toString());
        if(!(product.getLendTime().equals("N/A"))) {
            lendCheckBox.setActivated(true);
            listingLend.setText(product.getLendTime());
        }
        if(!(product.getExchange().equals("N/A"))) {
            exchangeCheckBox.setActivated(true);
            listingExchange.setText(product.getExchange());
        }

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
                editTransactionProduct();
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
    public void editTransactionProduct()
    {
        if(PreviewImage.getDrawable() == null)
        {
            Toast.makeText(getApplicationContext(), "Please upload an image", Toast.LENGTH_LONG).show();
            return;
        }

        if(listingTitle.getText().toString().isEmpty())
        {
            listingTitle.setError("Please enter a title");
            showKeyboardEdit(listingTitle);
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
            showKeyboardEdit(listingDescription);
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
            showKeyboardEdit(listingPrice);
            listingPrice.requestFocus();
            return;
        }
        else if((listingPrice.getText().toString().contains(".") == true) && (listingPrice.getText().toString().length()-(listingPrice.getText().toString().indexOf(".")+1) > 2))
        {
            listingPrice.setError("Invalid cent value");
            showKeyboardEdit(listingPrice);
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
            showKeyboardEdit(listingLend);
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
            showKeyboardEdit(listingExchange);
            listingExchange.requestFocus();
            return;
        }
        else
        {
            listingExchange.setError(null);
        }

        TransactionProductResubmission(new TransactionsProduct(listingTitle.getText().toString(),
                        listingDescription.getText().toString(),
                        product.getImageRef(),
                        Double.parseDouble(listingPrice.getText().toString()),
                        listingLend.getText().toString(),
                        listingExchange.getText().toString(), null, null, null, null),
                selectedImageUri);

    }

    // this function re-uploads the passed-in class to firebase.
    // the image goes to firebase storage
    public void TransactionProductResubmission(TransactionsProduct localProduct, Uri selectedImageUri)
    {
        // Create a reference to new product image
        if (selectedImageUri != null) {
            // delete the old image in firebase storage
            StorageReference deleteRef = storageRef.child(product.getImageRef());

            deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });

            final String randomKey = UUID.randomUUID().toString();
            StorageReference listingImageRef = storageRef.child("images/"+randomKey);

            // Upload the image reference to firebase storage
            listingImageRef.putFile(selectedImageUri);

            // Copy reference as a string into the product object
            localProduct.setImageRef("images/"+randomKey);
        }
        else {
            localProduct.setImageRef(product.getImageRef());
        }


        // Re-assign old unique ID to the updated listing
        DocumentReference productReference = transactionDB.collection("Marketplace_Listings").document(product.getUniqueID());
        localProduct.setUniqueID(product.getUniqueID());

        // Assign userID to listing
        localProduct.setSellerID(product.getSellerID());

        // Assign timestamp to listing
        localProduct.setDatePosted(product.getDatePosted());

        // Assign timestamp to listing
        localProduct.setTimestamp(product.getTimestamp());

        // Upload listing to firestore
        productReference.set(localProduct);
        this.finish();
    }

    public void showKeyboardEdit(final EditText ettext){
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
package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.common.aliasing.qual.Unique;

import java.util.Calendar;
import java.util.Locale;

public class TransactionsProductViewActivity extends AppCompatActivity {

    // create variables to hold screen content
    TextView productTitle, productTimestamp, productPrice, productLendTime, productExchange, productDesc, productSellerMavMail;
    ImageView productImage;
    Button addToCartButton;

    TransactionsProduct product;

    //Firestore used to upload data
    FirebaseFirestore transactionDB = FirebaseFirestore.getInstance();

    // Create a Cloud Storage reference from the app
    // used to upload the image
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_product_view);

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        // initialize the passed-in product object
        TransactionsProduct product = (TransactionsProduct) getIntent().getParcelableExtra("PRODUCT");

        // assign each layout id a variable
        productTitle = findViewById(R.id.transactions_product_view_title);
        productImage = findViewById(R.id.transactions_product_view_image);
        productTimestamp = findViewById(R.id.transactions_product_view_timestamp);
        productPrice = findViewById(R.id.transactions_product_view_price);
        productLendTime = findViewById(R.id.transactions_product_view_lendTime);
        productExchange = findViewById(R.id.transactions_product_view_exchange);
        productDesc = findViewById(R.id.transactions_product_view_description);
        productSellerMavMail = findViewById(R.id.transactions_product_view_seller);
        addToCartButton = findViewById(R.id.transactions_product_view_button);

        // assign each variable the values from the passed-in product
        productTitle.setText(product.getTitle());
        productTimestamp.setText("Posted on " + product.getDatePosted());
        productPrice.setText("Price: $" + product.getPrice().toString());
        productLendTime.setText("Lend Period: " + product.getLendTime());
        productExchange.setText("Exchange: " + product.getExchange());
        productDesc.setText(product.getDesc());

        // download image from firebase storage
        StorageReference imageRef = storageRef.child(product.getImageRef());

        imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // assigns the downloaded image, in bitmap form, to the imageView
                productImage.setImageBitmap(bitmap);
            }
        });

        // download seller email from firebase firestore
        // and add it to the page
        transactionDB.collection("Users")
                .whereEqualTo("userID", product.getSellerID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                productSellerMavMail.setText("Seller: " + document.getString("email"));
                            }
                        } else {
                            Toast.makeText(TransactionsProductViewActivity.this,
                                    "Error downloading from database",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // toolbar is defined in layout file
        Toolbar transactionsProductViewToolbar = (Toolbar) findViewById(R.id.transactions_product_view_toolbar);
        setSupportActionBar(transactionsProductViewToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar transactionsProductViewActionBar = getSupportActionBar();

        // Enable the ActionBar Up button (go back to previous activity)
        transactionsProductViewActionBar.setDisplayHomeAsUpEnabled(true);

        // override the add-to-cart button's onclick listener to run custom function
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToCart(product);
            }
        });
    }

    void addProductToCart(TransactionsProduct product) {
        // Upload listing ID to user's account
        DocumentReference userRef = transactionDB.collection("Users").document(currentUser.getUid());
        userRef.update("transactionCartItemIDs", FieldValue.arrayUnion(product.getUniqueID()));
        this.finish();
    }
}
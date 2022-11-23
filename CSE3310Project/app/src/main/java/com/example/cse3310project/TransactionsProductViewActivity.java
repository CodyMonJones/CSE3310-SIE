package com.example.cse3310project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;

public class TransactionsProductViewActivity extends AppCompatActivity {

    // create variables to hold screen content
    TextView productTitle, productTimestamp, productPrice, productLendTime, productExchange, productDesc, productSeller;
    ImageView productImage;
    Button addToCartButton;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_product_view);

        // initialize the passed-in listing UniqueID
        String UniqueID = getIntent().getStringExtra("UID");

        productTitle = findViewById(R.id.transactions_product_view_title);
        productImage = findViewById(R.id.transactions_product_view_image);
        productTimestamp = findViewById(R.id.transactions_product_view_timestamp);
        productPrice = findViewById(R.id.transactions_product_view_price);
        productLendTime = findViewById(R.id.transactions_product_view_lendTime);
        productExchange = findViewById(R.id.transactions_product_view_exchange);
        productDesc = findViewById(R.id.transactions_product_view_description);
        productSeller = findViewById(R.id.transactions_product_view_seller);
        addToCartButton = findViewById(R.id.transactions_product_view_button);

        // toolbar is defined in layout file
        Toolbar transactionsProductViewToolbar = (Toolbar) findViewById(R.id.transactions_product_view_toolbar);
        setSupportActionBar(transactionsProductViewToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar transactionsProductViewActionBar = getSupportActionBar();

        // Enable the ActionBar Up button (go back to previous activity)
        transactionsProductViewActionBar.setDisplayHomeAsUpEnabled(true);

        //replace all the elements with the firebase product info

    }
}
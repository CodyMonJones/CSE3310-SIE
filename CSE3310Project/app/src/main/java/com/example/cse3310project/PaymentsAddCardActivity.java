package com.example.cse3310project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;

public class PaymentsAddCardActivity extends AppCompatActivity {

    //Firestore used to upload data
    FirebaseFirestore paymentDB;
    // used to get the current user
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;

    EditText cardNumber;
    Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_add_card);

        // Needed to transfer data with firebase
        paymentDB = FirebaseFirestore.getInstance();

        // Find unique ID of current User
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();

        // assign variable to layout ids
        cardNumber = findViewById(R.id.payments_add_card_number);
        submitButton = findViewById(R.id.payments_add_card_submit_button);

        // toolbar is defined in layout file
        Toolbar paymentsAddCardToolbar = (Toolbar) findViewById(R.id.payments_add_card_toolbar);
        setSupportActionBar(paymentsAddCardToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar paymentsActionBar = getSupportActionBar();

        // Enable the ActionBar Up button (go back to previous activity)
        paymentsActionBar.setDisplayHomeAsUpEnabled(true);

        // override the submission button's onclick listener to run custom function
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCardObject();
            }
        });
    }

    public void createCardObject() {
        if((cardNumber.getText().toString().length() != 16)) {
            cardNumber.setError("Please enter a 16-digit card number");
            showKeyboard(cardNumber);
            cardNumber.requestFocus();
            return;
        }
        else
        {
            cardNumber.setError(null);
        }

        uploadCard(cardNumber.getText().toString());
    }

    public void uploadCard(String creditCard){
        DocumentReference userRef = paymentDB.collection("Users").document(currentUser.getUid());
        userRef.update("creditCards", FieldValue.arrayUnion(creditCard));
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
package com.example.cse3310project.Discussion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cse3310project.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePost extends AppCompatActivity implements View.OnClickListener{

    private TextView username, dateCreated;
    private EditText postBody, postTitle;
    private MaterialButton cancelButton, postButton;
    private FirebaseFirestore postDatabase;
    //private FirebaseUser mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postDatabase = FirebaseFirestore.getInstance();

        username = findViewById(R.id.Username_Create_Post);
        dateCreated = findViewById(R.id.Post_Creation_Date);

        postTitle = findViewById(R.id.Post_Title_Create_Post);
        postBody = findViewById(R.id.Post_Body_Create_Post);

        cancelButton = findViewById(R.id.Cancel_Button_Create_Post);
        postButton = findViewById(R.id.Post_Button_Create_Post);

        cancelButton.setOnClickListener(this);
        postButton.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        dateCreated.setText(currentDateandTime);

        //username.setText(mAuth.getEmail());
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId()) {
            case R.id.Cancel_Button_Create_Post:
                this.finish();
                break;

            case R.id.Post_Button_Create_Post:
                String pt, pb, creationDate;

                if(postTitle.getText().toString().isEmpty())
                {
                    postTitle.setError("Post Title cannot be empty");
                    showKeyboard(postTitle);
                    postTitle.requestFocus();
                    return;
                }
                else
                {
                    postTitle.setError(null);
                }

                if(postBody.getText().toString().isEmpty())
                {
                    postBody.setError("Post Body cannot be empty");
                    showKeyboard(postBody);
                    postBody.requestFocus();
                    return;
                }
                else
                {
                    postBody.setError(null);
                }

                pt = postTitle.getText().toString();
                pb = postBody.getText().toString();
                creationDate = dateCreated.getText().toString();

                uploadPost(new DiscussionPost(pt, pb, creationDate));

                this.finish();
                break;
        }
    }

    public void uploadPost(DiscussionPost post)
    {
        DocumentReference reference = postDatabase.collection("Posts").document();
        post.setUniqueID(reference.getId());

        reference.set(post);
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
package com.example.cse3310project.Discussion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse3310project.R;
import com.example.cse3310project.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePost extends AppCompatActivity implements View.OnClickListener{

    // Instance variables needed to create a post

    // XML variables
    private ShapeableImageView posterImage;
    private TextView username, dateCreated;
    private EditText postBody, postTitle;
    private MaterialButton cancelButton, postButton;

    // Firebase variables
    private FirebaseFirestore postDatabase;
    private FirebaseAuth currentUserAuthentication;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    // String needed to reference the profile picture of the poster
    private String posterImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Initialization of the variables used
        currentUserAuthentication = FirebaseAuth.getInstance();
        currentUser = currentUserAuthentication.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        postDatabase = FirebaseFirestore.getInstance();

        posterImage = findViewById(R.id.Profile_Picture_Post_Header);
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

        checkUser();
        setPosterImage();
    }

    // Handles the button clicks that can result in cancel the current process or
    // continuing forward with the creation of the post
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

                uploadPost(new DiscussionPost(pt, pb, creationDate, currentUser.getEmail()));

                this.finish();
                break;
        }
    }

    // Creates the post and uploads it to the "Posts" collection
    public void uploadPost(DiscussionPost post)
    {
        DocumentReference reference = postDatabase.collection("Posts").document();
        post.setUniqueID(reference.getId());
        post.setTimestamp(Timestamp.now());

        post.setPosterImage(posterImageUrl);

        DocumentReference userRef = postDatabase.collection("Users").document(currentUser.getUid());
        userRef.update("discussionPostsIDs", FieldValue.arrayUnion(post.getUniqueID()));

        reference.set(post);
    }

    // Shows the keyboard and sets the focus to the current entry field if it does not meet
    // conditions
    public void showKeyboard(final EditText ettext)
    {
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run()
                               {
                                   InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }

    // Safe Guard function that checks to see if the user is currently logged in
    public void checkUser()
    {
        if(currentUser != null)
        {
            username.setText(currentUser.getEmail());
        }
        else
        {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    // Sets the current poster's profile image to the post that they are creating
    public void setPosterImage()
    {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(currentUser.getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                User temp = documentSnapshot.toObject(User.class);

                posterImageUrl = temp.getProfile_picture();

                // download image from firebase storage
                StorageReference imageRef = storageReference.child(temp.getProfile_picture());

                imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>()
                {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        // assigns the downloaded image, in bitmap form, to the imageView
                        posterImage.setImageBitmap(bitmap);
                    }
                });

            }
        });
    }
}
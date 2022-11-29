package com.example.cse3310project;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cse3310project.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.initializedfields.qual.InitializedFields;

import java.util.HashMap;

public class SettingsActivity extends drawerActivity {

    ActivitySettingsBinding activitySettingsBinding;
    private Button updateAccount;
    private EditText userName, userStatus;
    private ImageView profileImage;
    private ShapeableImageView drawerImage;
//    private static int profImgSelected = 1;
//    private StorageReference profileImagesRef;

    private String currentUserUUID;
    private FirebaseAuth mAuth;
    private DatabaseReference currentDBRef;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        allocateActivityTitle("Settings");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUserUUID = mAuth.getCurrentUser().getUid();
        currentDBRef = FirebaseDatabase.getInstance().getReference();
//        profileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        InitializedFields();

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccountSettings();
            }
        });

        grabUserProfile();
//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent images = new Intent();
//                images.setAction(Intent.ACTION_GET_CONTENT);
//                images.setType("image/*");
//                startActivityForResult(images, profImgSelected);
//            }
//        });
        setProfileImage();
    }

    private void grabUserProfile() {
        currentDBRef.child("User").child(currentUserUUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if((snapshot.exists()) && (snapshot.hasChild("name")) && (snapshot.hasChild("image"))){
                            String grabUserName = snapshot.child("name").getValue().toString();
                            String grabUserStatus = snapshot.child("Status").getValue().toString();
                            String grabUserProfImg = snapshot.child("image").getValue().toString();

                            userName.setText(grabUserName);
                            userStatus.setText(grabUserStatus);
                        }
                        else if((snapshot.exists()) && (snapshot.hasChild("name"))){
                            String grabUserName = snapshot.child("name").getValue().toString();
                            String grabUserStatus = snapshot.child("Status").getValue().toString();

                            userName.setText(grabUserName);
                            userStatus.setText(grabUserStatus);

                        }
                        else{
                            Toast.makeText(SettingsActivity.this, "Please create profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void setProfileImage()
    {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User temp = documentSnapshot.toObject(User.class);

                // download image from firebase storage
                StorageReference imageRef = storageReference.child(temp.getProfile_picture());

                imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // assigns the downloaded image, in bitmap form, to the imageView
                        profileImage.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    private void updateAccountSettings() {
        String getUserName = userName.getText().toString();
        String getUserStatus = userStatus.getText().toString();

        if(TextUtils.isEmpty(getUserName)){
            Toast.makeText(this, "User Name is Required!", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(getUserStatus)){
            Toast.makeText(this, "Status is Required!", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, String> userProfileMap = new HashMap<>();
                userProfileMap.put("UID", currentUserUUID);
                userProfileMap.put("name", getUserName);
                userProfileMap.put("Status", getUserStatus);

            currentDBRef.child("User").child(currentUserUUID).setValue(userProfileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String errMessage = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: " + errMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void InitializedFields(){
        updateAccount = (Button) findViewById(R.id.update_profileBtn);
        userName = (EditText) findViewById(R.id.set_username);
        userStatus = (EditText) findViewById(R.id.set_status);
        profileImage = (ImageView) findViewById(R.id.prof_image);
    }
}
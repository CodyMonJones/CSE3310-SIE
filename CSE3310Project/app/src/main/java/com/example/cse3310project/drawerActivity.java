package com.example.cse3310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.cse3310project.Discussion.CommentActivity;
import com.example.cse3310project.Discussion.DiscussionForum;
import com.example.cse3310project.Discussion.DiscussionPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.A;

import com.example.cse3310project.Discussion.DiscussionForum;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class drawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private ShapeableImageView currentUserImage;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        currentUserImage = findViewById(R.id.prof_image);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        //setProfileImage();
    }

    public void setProfileImage()
    {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User temp = documentSnapshot.toObject(User.class);

                Log.d("CURRENT USER", FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.d("CURRENT USER", temp.getProfile_picture());

                // download image from firebase storage
                StorageReference imageRef = storageReference.child(temp.getProfile_picture());

                imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // assigns the downloaded image, in bitmap form, to the imageView
                        currentUserImage.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Home:
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.Clubs:
                startActivity(new Intent(this, ComsEmailActivity.FormClubActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.Messages:
                startActivity(new Intent(this, ComsContactsActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.Marketplace:
                startActivity(new Intent(this, TransactionsActivity.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.Settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.Logout:
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0,0);
                Toast.makeText(drawerActivity.this, "Logout Successful ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.FormClub:
                newClubName();
                break;

            case R.id.Discussions:
                startActivity(new Intent(this, DiscussionForum.class));
                overridePendingTransition(0,0);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void newClubName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(drawerActivity.this, R.style.Modal);
        builder.setTitle("Create new Club: ");

        final EditText clubName = new EditText(drawerActivity.this);
        clubName.setHint("Club Name");
        builder.setView(clubName);

        builder.setPositiveButton("Create Club", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userClubNameInput = clubName.getText().toString();

                if(TextUtils.isEmpty(userClubNameInput)){
                    Toast.makeText(drawerActivity.this, "Club Name is required...", Toast.LENGTH_SHORT).show();
                }
                else{
                    formNewClub(userClubNameInput);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void formNewClub(String clubName) {
        dbRef.child("Clubs").child(clubName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(drawerActivity.this, clubName + "Created successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }

    public static class TransactionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        private TabLayout tabLayout;
        private ViewPager viewPager;

        //variables needed for drawer menu
        public DrawerLayout drawerLayout;
        public Toolbar toolbar;
        public ActionBarDrawerToggle actionBarDrawerToggle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transactions);

            // adapter used by viewpager to switch between tabs
            TransactionsVPAdapter transactionsVPAdapter = new TransactionsVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            // adds tabs for viewpager to display
            transactionsVPAdapter.addFragment(new TransactionsShopFragment(),"Shop");
            transactionsVPAdapter.addFragment(new TransactionsSellFragment(),"Sell");
            transactionsVPAdapter.addFragment(new TransactionsWishlistFragment(),"Wishlist");
            transactionsVPAdapter.addFragment(new TransactionsCartFragment(),"Cart");

            viewPager = findViewById(R.id.transactionsViewPager);
            viewPager.setAdapter(transactionsVPAdapter);

            tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            // drawer layout instance to toggle the menu icon to open
            // drawer and back button to close drawer
            drawerLayout = findViewById(R.id.transactions_drawer_layout);
            toolbar = drawerLayout.findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);

            // setting up navView needed to implement
            // activity switching functionality
            NavigationView navigationView = drawerLayout.findViewById(R.id.transactions_nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.Home);

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

            // pass the Open and Close toggle for the drawer layout listener
            // to toggle the button
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.Home:
                    startActivity(new Intent(this, DiscussionForum.class));
                    overridePendingTransition(0, 0);
                    break;

                case R.id.Clubs:
                    startActivity(new Intent(this, ComsEmailActivity.FormClubActivity.class));
                    overridePendingTransition(0,0);
                    break;

                case R.id.Messages:
                    startActivity(new Intent(this, ComsContactsActivity.class));
                    overridePendingTransition(0,0);
                    break;

                case R.id.Marketplace:
                    startActivity(new Intent(this, TransactionsActivity.class));
                    overridePendingTransition(0, 0);
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


        public void goToTransactionsProductCreateActivity(View view) {
            Intent intent = new Intent(this, TransactionsProductCreateActivity.class);
            startActivity(intent);
        }

        public void onClick(View view) {

        }

    }
}
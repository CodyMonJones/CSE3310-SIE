package com.example.cse3310project;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.example.cse3310project.Discussion.DiscussionPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TransactionsShopAdapter extends RecyclerView.Adapter<TransactionsShopAdapter.ViewHolder> {
    private final TransactionsRecyclerViewInterface transactionsRecyclerViewInterface;

    ArrayList<TransactionsProduct> mData;
    LayoutInflater mInflater;
    Context context;

    // setting up firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public TransactionsShopAdapter(ArrayList<TransactionsProduct> itemList, Context context,
                                   TransactionsRecyclerViewInterface transactionsRecyclerViewInterface) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.transactionsRecyclerViewInterface = transactionsRecyclerViewInterface;
    }

    @Override
    public int getItemCount() { return mData.size(); }

    @Override
    public TransactionsShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.transactions_product_listing, null);
        return new TransactionsShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TransactionsShopAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView productListingCardview;
        ImageView productListingImage;
        TextView productListingTitle;
        CheckBox productListingWishlistButton;

        ViewHolder(View itemView) {
            super(itemView);

            productListingCardview = itemView.findViewById(R.id.product_listing_cardview);
            productListingImage = itemView.findViewById(R.id.product_listing_image);
            productListingTitle = itemView.findViewById(R.id.product_listing_title);
            productListingWishlistButton = itemView.findViewById(R.id.product_listing_wishlist_button);

            productListingCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (transactionsRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            transactionsRecyclerViewInterface.onProductClick(pos);
                        }
                    }
                }
            });

            productListingWishlistButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (transactionsRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            transactionsRecyclerViewInterface.onWishlistButtonChecked(pos, compoundButton);
                        }
                    }
                }
            });
        }


        void bindData(final TransactionsProduct item) {
            productListingTitle.setText(item.getTitle() + ": $" + item.getPrice().toString());

            // download image from firebase storage
            StorageReference imageRef = storageRef.child(item.getImageRef());

            imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    productListingImage.setImageBitmap(bitmap);
                }
            });

            // create an ArrayList to hold the wishlist data
            ArrayList<String> userWishlist = new ArrayList<String>();
            // used to get the current user
            FirebaseAuth currentUserAuthentication = FirebaseAuth.getInstance();
            FirebaseUser currentUser = currentUserAuthentication.getCurrentUser();
            // used to access firebase firestore
            FirebaseFirestore marketplaceDb = FirebaseFirestore.getInstance();
            // download the user's wishlist from firestore
            // and load it into the ArrayList
            DocumentReference userRef = marketplaceDb.collection("Users").document(currentUser.getUid());
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ArrayList<String> wishlistArray = (ArrayList<String>) document.get("transactionWishlistItemIDs");
                            if(wishlistArray != null){
                                for (String i : wishlistArray) {
                                    userWishlist.add(i);
                                }
                                for (String i : userWishlist) {
                                    if (i.equals(item.getUniqueID())) {
                                        productListingWishlistButton.setChecked(true);
                                    }
                                }
                            }
                        }
                        else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }
}
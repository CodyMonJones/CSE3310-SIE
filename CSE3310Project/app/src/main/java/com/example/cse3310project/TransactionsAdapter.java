package com.example.cse3310project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private final TransactionsRecyclerViewInterface transactionsRecyclerViewInterface;

    ArrayList<TransactionsProduct> mData;
    LayoutInflater mInflater;
    Context context;

    // setting up firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public TransactionsAdapter(ArrayList<TransactionsProduct> itemList, Context context,
                               TransactionsRecyclerViewInterface transactionsRecyclerViewInterface) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.transactionsRecyclerViewInterface = transactionsRecyclerViewInterface;
    }

    @Override
    public int getItemCount() { return mData.size(); }

    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.transactions_product_listing, null);
        return new TransactionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TransactionsAdapter.ViewHolder holder, final int position) {
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
        }


        void bindData(final TransactionsProduct item) {
            productListingTitle.setText(item.getTitle() + ": $" + item.getPrice());

            //download image from firebase storage
            StorageReference imageRef = storageRef.child(item.getImageRef());

            imageRef.getBytes(1024*1024*10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    productListingImage.setImageBitmap(bitmap);
                }
            });
        }
    }
}
package com.example.cse3310project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TransactionsSellAdapter extends RecyclerView.Adapter<TransactionsSellAdapter.ViewHolder> {
    private final TransactionsRecyclerViewInterface transactionsRecyclerViewInterface;

    ArrayList<TransactionsProduct> mData;
    LayoutInflater mInflater;
    Context context;

    // setting up firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public TransactionsSellAdapter(ArrayList<TransactionsProduct> itemList, Context context,
                                   TransactionsRecyclerViewInterface transactionsRecyclerViewInterface) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.transactionsRecyclerViewInterface = transactionsRecyclerViewInterface;
    }

    @Override
    public int getItemCount() { return mData.size(); }

    @Override
    public TransactionsSellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.transactions_product_user_listing, null);
        return new TransactionsSellAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TransactionsSellAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView productListingCardview;
        ImageView productListingImage;
        TextView productListingTitle;
        ImageButton productListingDeleteButton;

        ViewHolder(View itemView) {
            super(itemView);

            productListingCardview = itemView.findViewById(R.id.product_user_listing_cardview);
            productListingImage = itemView.findViewById(R.id.product_user_listing_image);
            productListingTitle = itemView.findViewById(R.id.product_user_listing_title);
            productListingDeleteButton = itemView.findViewById(R.id.product_user_listing_delete_button);

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

            productListingDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (transactionsRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            transactionsRecyclerViewInterface.onDeleteButtonClick(pos);
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
        }
    }
}

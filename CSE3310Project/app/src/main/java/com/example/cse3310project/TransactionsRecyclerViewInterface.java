package com.example.cse3310project;

import android.widget.CompoundButton;

public interface TransactionsRecyclerViewInterface {
    void onProductClick(int position);
    void onWishlistButtonChecked(int position, CompoundButton compoundButton);
    void onDeleteButtonClick(int position);
}

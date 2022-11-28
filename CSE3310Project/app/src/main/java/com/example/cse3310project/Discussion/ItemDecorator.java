package com.example.cse3310project.Discussion;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorator extends RecyclerView.ItemDecoration
{
    private final int VerticalSpaceHeight;

    public ItemDecorator(int verticalSpaceHeight)
    {
        this.VerticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        outRect.bottom = VerticalSpaceHeight;
    }
}

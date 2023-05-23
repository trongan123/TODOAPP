package com.example.todoapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewLayoutManager extends LinearLayoutManager {

    private int[] extraLayoutSpace;

    public RecyclerViewLayoutManager(Context context) {
        super(context);
    }

    public RecyclerViewLayoutManager(Context context, int[] extraLayoutSpace) {
        super(context);
        this.extraLayoutSpace = extraLayoutSpace;
    }

    public RecyclerViewLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setExtraLayoutSpace(int[] extraLayoutSpace) {
        this.extraLayoutSpace = extraLayoutSpace;
    }

    @Override
    protected void calculateExtraLayoutSpace(@NonNull RecyclerView.State state, @NonNull int[] extraLayoutSpace) {
        extraLayoutSpace[0] = this.extraLayoutSpace[0];
        extraLayoutSpace[1] = this.extraLayoutSpace[1];
    }
}

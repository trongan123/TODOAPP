package com.example.todoapp;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class RecyclerViewLayoutManager extends LinearLayoutManager {

    public RecyclerViewLayoutManager(Context context) {
        super(context);
    }

    public RecyclerViewLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public RecyclerViewLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    
}

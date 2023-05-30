package com.example.runder.CommonFiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView {
    private boolean scrollable = true;
    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }
    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void GoogleSignUpViewHolder(@NonNull View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }

    }

    @Override
    public void setLayoutManager(@Nullable LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
    }



}

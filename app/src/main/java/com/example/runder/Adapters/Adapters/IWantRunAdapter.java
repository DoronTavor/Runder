package com.example.runder.Adapters.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runder.Holders.IWantRunViewHolder;
import com.example.runder.R;

public class IWantRunAdapter extends RecyclerView.Adapter<IWantRunViewHolder> {

    @NonNull
    @Override
    public IWantRunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.runners_item,parent,false);
        return new IWantRunViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IWantRunViewHolder holder, int position) {

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return 1;
    }

}

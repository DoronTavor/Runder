package com.example.runder.Adapters.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runder.Holders.RegularSignUpViewHolder;
import com.example.runder.R;

public class RegularSignUpAdapter extends RecyclerView.Adapter<RegularSignUpViewHolder> {
    @NonNull
    @Override
    public RegularSignUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
         else if(viewType == 1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        else if(viewType == 2){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        else if(viewType == 3){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.re_enter_password_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        else if(viewType == 4){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.birthdate_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        else if(viewType == 5){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gender_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        else if(viewType == 6){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avg_distance_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        else if(viewType == 7){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avg_speed_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        else if(viewType == 8){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item,parent,false);
            return new RegularSignUpViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RegularSignUpViewHolder holder, int position) {


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 9;
    }
}

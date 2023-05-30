package com.example.runder.Adapters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.runder.Activities.GoogleSignUpActivity;
import com.example.runder.CommonFiles.MyRecyclerView;
import com.example.runder.Holders.GoogleSignUpViewHolder;
import com.example.runder.R;

public class GoogleSignUpAdapter extends MyRecyclerView.Adapter<GoogleSignUpViewHolder> {

    private Context context;

    public GoogleSignUpAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GoogleSignUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GoogleSignUpActivity.globalVars.setViewType(viewType);
        if(viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.birthdate_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gender_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
       else if(viewType == 2){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avg_distance_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 3){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avg_speed_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 4){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 5){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 6){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 7){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 8){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.height_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 9){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.id_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 10){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.max_distance_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 11){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.max_speed_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else if(viewType == 12){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_item,parent,false);
            return new GoogleSignUpViewHolder(view,context);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull GoogleSignUpViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 14;
    }



}

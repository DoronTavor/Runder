package com.example.runder.Adapters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runder.CommonFiles.ReadersAndNetworks.GeocodingMethods;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDetailsAdapter extends FirebaseRecyclerAdapter<RunnersModelFull, MyDetailsAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public MyDetailsAdapter(@NonNull FirebaseRecyclerOptions<RunnersModelFull> options,Context context) {
        super(options);
        this.context=context;

    }



    @NonNull
    @Override
    public MyDetailsAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community,parent,false);
        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RunnersModelFull model) {
        LatLng latLng= new LatLng(model.getCurrentPosition().get("Latitude"),model.getCurrentPosition().get("Longitude"));
        holder.address.setTextSize(10);
        holder.address.setText(GeocodingMethods.getAddress(latLng,context));
        holder.name.setText(model.getFirstSecondName());
        holder.gender.setText(model.getGender());
        holder.birthDate.setText(model.getBirthDate());
        Glide.with(holder.imageView.getContext()).load(model.getPicture()).placeholder
                        (com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(
                        holder.imageView);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView name,birthDate,gender, address;
        CircleImageView imageView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.PictureCommunity);
            name= (TextView)itemView.findViewById(R.id.textViewNameCardView);
            birthDate= (TextView)itemView.findViewById(R.id.textViewBirthDateCard);
            gender= (TextView)itemView.findViewById(R.id.GenderTextViewCard);
            address= (TextView)itemView.findViewById(R.id.TextViewAdressCard);

        }
    }
}

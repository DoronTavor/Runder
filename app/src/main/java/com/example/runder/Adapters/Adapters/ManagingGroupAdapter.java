package com.example.runder.Adapters.Adapters;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.ReadersAndNetworks.GeocodingMethods;
import com.example.runder.Models.ManagingModel;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagingGroupAdapter extends FirebaseRecyclerAdapter<ManagingModel,ManagingGroupAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String currentTeam;
    private Context context;
    public ManagingGroupAdapter(@NonNull FirebaseRecyclerOptions<ManagingModel> options, String currentTeam, Context context) {
        super(options);
        this.currentTeam=currentTeam;
        this.context= context;
    }





    @NonNull
    @Override
    public ManagingGroupAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_managing,parent,false);
        return new ManagingGroupAdapter.myViewHolder(view);
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ManagingModel model) {
        if(model.getActiveTeam() != null &&model.getActiveTeam().equals(this.currentTeam)){
            holder.constraintLayout.setVisibility(View.VISIBLE);
            LatLng latLng= new LatLng(model.getCurrentPosition().get("Latitude"),model.getCurrentPosition().get("Longitude"));
            holder.address.setText(GeocodingMethods.getAddress(latLng,context));
            holder.name.setText(model.getFirstSecondName());
            holder.gender.setText(model.getGender());
            holder.birthDate.setText(model.getBirthDate());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateOfBirth = LocalDate.parse(model.getBirthDate(), formatter);
            LocalDate now = LocalDate.now();
            Period age = Period.between(dateOfBirth, now);
            int years = age.getYears();
            String ageText= "גיל: ";
            ageText= ageText + years;
            holder.Age.setText(ageText);
            Glide.with(holder.imageView.getContext()).load(model.getPicture()).placeholder
                            (com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                    error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(
                            holder.imageView);

        }
        else {
            holder.constraintLayout.setVisibility(View.INVISIBLE);
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name,birthDate,gender,address;
        CircleImageView imageView;
        ConstraintLayout constraintLayout;
        TextView Email,Age;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.PictureManaging);
            name= (TextView)itemView.findViewById(R.id.textViewNameCardViewManaging);
            birthDate= (TextView)itemView.findViewById(R.id.textViewBirthDateCardManaging);
            gender= (TextView)itemView.findViewById(R.id.GenderTextViewCardManaging);
            address= (TextView)itemView.findViewById(R.id.TextViewAdressCardManaging);
            Age= (TextView) itemView.findViewById(R.id.AgeTextViewManaging);
            constraintLayout=(ConstraintLayout)itemView.findViewById(R.id.layoutManaging);


        }
    }
}

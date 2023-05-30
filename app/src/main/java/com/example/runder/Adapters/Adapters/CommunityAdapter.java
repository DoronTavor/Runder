package com.example.runder.Adapters.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runder.Models.CommunityModel;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityAdapter extends FirebaseRecyclerAdapter<CommunityModel,CommunityAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CommunityAdapter(@NonNull FirebaseRecyclerOptions<CommunityModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommunityAdapter.myViewHolder holder, int position, @NonNull CommunityModel model) {
        holder.address.setText(model.getAddress());
        holder.name.setText(model.getFirstSecondName());
        holder.gender.setText(model.getGender());
        holder.birthDate.setText(model.getBirthDate());
        Glide.with(holder.imageView.getContext()).load(model.getPicture()).placeholder
                (com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                   error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(
                holder.imageView);

    }

    @NonNull
    @Override
    public CommunityAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community,parent,false);
        return new myViewHolder(view);
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

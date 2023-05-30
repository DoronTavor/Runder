package com.example.runder.Adapters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.runder.Activities.ChoosingGroupActivity;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.CommonFiles.ReadersAndNetworks.GeocodingMethods;
import com.example.runder.CommonFiles.ReadersAndNetworks.HaveOrHaventGroupCB;
import com.example.runder.CommonFiles.SelectListener;
import com.example.runder.Models.ChoosingGroupModel;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChoosingGroupAdapter extends FirebaseRecyclerAdapter<ChoosingGroupModel,ChoosingGroupAdapter.myViewHolder>  {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private SelectListener listener;
    private String  max_D;
    private int max_S;
    private String  age;
    private HaveOrHaventGroupCB listenerGroup;
    private Context context;
    private HaveOrHaventGroupCB cb;

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public ChoosingGroupAdapter(@NonNull FirebaseRecyclerOptions<ChoosingGroupModel> options, SelectListener listener, String  max_D,
                                int max_S, String  age, Context context,HaveOrHaventGroupCB cb) {
        super(options);
        this.listener=listener;
        this.max_D=max_D;
        this.max_S=max_S;
        this.age=age;
        this.context= context;
        this.cb=cb;
        FireBaseReader reader= new FireBaseReader(context);

    }


    @Override
    protected void onBindViewHolder(@NonNull ChoosingGroupAdapter.myViewHolder holder, int position, @NonNull ChoosingGroupModel model) {
        String [] toCont = {"שם: ","סטטוס: ","טווח גילאים: ","קצב: ","מרחק: ","מיקום: "};
        boolean ageEqual= Objects.equals(this.age, model.getAgeRange());
        boolean distanceEqual= Objects.equals(this.max_D, model.getRunningDistance());
        boolean condToCheck=  ageEqual && Double.compare(model.getRunningSpeed(),Double.valueOf(this.max_S))== 0 && distanceEqual;
        if(condToCheck){
            if(ChoosingGroupActivity.chooseStat().equals("אין קבוצות כרגע")){
                cb.thereGroupsCallBack();
                GlobalVars.numOfActives++;
            }
            FirebaseDatabase.getInstance().getReference().child("Teams").child(model.getGroupID()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    GlobalVars.numOfActives--;
                    if(GlobalVars.numOfActives==0){
                        cb.noGroupsCallBack();
                    }

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            String ageStr= model.getAgeRange();
            String speedStr= model.getRunningDistance()+"KM";
            holder.cardView.setVisibility(View.VISIBLE);
            holder.name.setText(toCont[0].concat(model.getGroupName()));
            holder.status.setText(toCont[1].concat(model.getStatus()));
            holder.age.setText(toCont[2].concat(ageStr));
            holder.speed.setText(toCont[3].concat(String.valueOf(model.getRunningSpeed())));
            holder.distance.setText(toCont[4].concat(speedStr));
            holder.location.setText(toCont[5].concat(GeocodingMethods.getAddress(new LatLng(model.getCurrentPosition().get("Latitude"),model.getCurrentPosition().get("Longitude")), this.context)));
            Glide.with(holder.img.getContext()).load(model.getImage()).placeholder
                            (com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                    error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(
                            holder.img);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemCLicked(model);
                }
            });
        }
        else {
            holder.cardView.setVisibility(View.GONE);
        }




    }




    @NonNull
    @Override
    public ChoosingGroupAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,parent,false);
        return new ChoosingGroupAdapter.myViewHolder(view);

    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name,status,location,age,speed,distance;
        CircleImageView img;
        ConstraintLayout cardView;
        LinearLayoutCompat linearLayoutCompat;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.NameTextViewGroup);
            status = (TextView) itemView.findViewById(R.id.StatusTextViewGroup);
            location = (TextView) itemView.findViewById(R.id.LocationTextViewGroup);
            age = (TextView) itemView.findViewById(R.id.AgeTextViewGroup);
            speed = (TextView) itemView.findViewById(R.id.SpeedTextViewGroup);
            distance = (TextView) itemView.findViewById(R.id.DistanceTextViewGroup);
            img= (CircleImageView) itemView.findViewById(R.id.PictureGroupItem);
            cardView= (ConstraintLayout) itemView.findViewById(R.id.cardViewGroup);



        }
    }
}

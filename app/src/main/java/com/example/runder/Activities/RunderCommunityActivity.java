package com.example.runder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.runder.Adapters.Adapters.CommunityAdapter;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.Models.CommunityModel;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class RunderCommunityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    static TextView name;
    static CircleImageView picture;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runder_community);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // TODO P2 - make with scroll and enable changing of attributes- 06/03/2023
        // TODO P2- Activity to support user attributes updates
        Intent reciver= getIntent();
        name= findViewById(R.id.NameCommunity);
        picture=findViewById(R.id.PictureCommunity);
        FireBaseReader reader= new FireBaseReader(RunderCommunityActivity.this);
        reader.getCurrentUser(reciver.getStringExtra("EMAIL"));
        recyclerView=(RecyclerView) findViewById(R.id.RV_Runners);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        FirebaseRecyclerOptions<CommunityModel> options=
                new FirebaseRecyclerOptions.Builder<CommunityModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Runners"),
                                CommunityModel.class).build();
        adapter= new CommunityAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        back =findViewById(R.id.BackCommunity);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RunderCommunityActivity.this,MainActivity.class);
                in.putExtra("EMAIL",reciver.getStringExtra("EMAIL"));
                finish();
                startActivity(in);
            }
        });



    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!= null){
            adapter.stopListening();
        }

    }
    public static void setValueToCurrentViews(RunnersModelFull modelFull){
        Glide.with(picture.getContext()).load(modelFull.getPicture()).placeholder
                        (com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(
                        picture);
        name.setText(modelFull.getFirstSecondName());

    }

}

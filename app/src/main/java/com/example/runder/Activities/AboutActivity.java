package com.example.runder.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.runder.Adapters.Adapters.MyDetailsAdapter;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends AppCompatActivity {
    static TextView name;
    static CircleImageView pic;
    ImageButton back;
    private AlertDialog.Builder builder2;
    private AlertDialog dialog2;
    RecyclerView newRec;
    private LinearLayoutManager linearLayoutManager2;
    private FirebaseRecyclerAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        name= findViewById(R.id.NameDetails);
        pic= findViewById(R.id.PictureDetailsActivity);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog2();
            }
        });
        FireBaseReader reader= new FireBaseReader(this);
        reader.getCurrentUser(getIntent().getStringExtra("EMAIL"));
        back= findViewById(R.id.BackAbout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToSettings= new Intent(AboutActivity.this,MainActivity.class);
                backToSettings.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                finish();
                startActivity(backToSettings);
            }
        });
    }
    public static void setValueToView(RunnersModelFull newM) {
        Glide.with(pic.getContext()).load(newM.getPicture()).placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                error(R.drawable.oneperson).into(pic);
        name.setText(newM.getFirstSecondName());
    }
    public void createDialog2(){
        builder2= new AlertDialog.Builder(this);
        final View popUpView= getLayoutInflater().inflate(R.layout.my_details_popup,null);
        Button dis= popUpView.findViewById(R.id.BtnDisableDetails);
        newRec= popUpView.findViewById(R.id.RVMYDETAILS);
        linearLayoutManager2 = new LinearLayoutManager(this);
        newRec.setLayoutManager(linearLayoutManager2);
        newRec.setHasFixedSize(true);
        FirebaseRecyclerOptions<RunnersModelFull> options=
                new FirebaseRecyclerOptions.Builder<RunnersModelFull>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Runners").orderByChild("Email").equalTo(GlobalVars.currentRunner.getEmail()),
                                RunnersModelFull.class).build();
        adapter2= new MyDetailsAdapter(options,this);
        newRec.setAdapter(adapter2);
        adapter2.startListening();
        adapter2.notifyDataSetChanged();
        builder2.setView(popUpView);
        dialog2=builder2.create();
        dialog2.show();
        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter2.stopListening();
                dialog2.dismiss();
            }
        });

    }

}
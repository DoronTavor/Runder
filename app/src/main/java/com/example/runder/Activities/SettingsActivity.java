package com.example.runder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.runder.Adapters.Adapters.MyDetailsAdapter;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    Button details,community,logOut,about;
    static CircleImageView imageView;
    static TextView name;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageButton btnBack;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button logOutSure,cancel;
    private AlertDialog.Builder builder2;
    private AlertDialog dialog2;
    RecyclerView newRec;
    private LinearLayoutManager linearLayoutManager2;
    private FirebaseRecyclerAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent i = getIntent();
        setIdToViews();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        FireBaseReader reader= new FireBaseReader(SettingsActivity.this);
        reader.getCurrentUser(i.getStringExtra("EMAIL"));

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);
        logOut.setOnClickListener(view -> createDialog());
        btnBack.setOnClickListener(view -> {
            Intent backToMain= new Intent(SettingsActivity.this,MainActivity.class);
            backToMain.putExtra("EMAIL",i.getStringExtra("EMAIL"));
            finish();
            startActivity(backToMain);
        });
        details.setOnClickListener(view -> {
            Intent toMyDetails= new Intent(SettingsActivity.this,MyDetailsActivity.class);
            toMyDetails.putExtra("EMAIL",i.getStringExtra("EMAIL"));
            finish();
            startActivity(toMyDetails);
        });
        community.setOnClickListener(view -> {
            Intent toCommunity= new Intent(SettingsActivity.this,RunderCommunityActivity.class);
            toCommunity.putExtra("EMAIL",i.getStringExtra("EMAIL"));
            finish();
            startActivity(toCommunity);
        });
        imageView.setOnClickListener(view -> createDialog2());
        about.setOnClickListener(view -> {
            Intent toAbout= new Intent(SettingsActivity.this,AboutActivity.class);
            toAbout.putExtra("EMAIL",i.getStringExtra("EMAIL"));
            finish();
            startActivity(toAbout);

        });


    }

    public void setIdToViews(){
        details= (Button) findViewById(R.id.Details_Btn);
        community= (Button) findViewById(R.id.Community_Btn);
        logOut= (Button) findViewById(R.id.LogOutBtn);
        about=(Button) findViewById(R.id.About_Btn);
        imageView= (CircleImageView) findViewById(R.id.PictureWantRunActivity);
        name= (TextView) findViewById(R.id.NameWantRun);
        btnBack=(ImageButton) findViewById(R.id.BtnBackWantRun);
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

    public static void setValueToViews(String nameStr,String url){
        Glide.with(imageView.getContext()).load(url).placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
              error(R.drawable.oneperson).into(imageView);
        name.setText(nameStr);
    }


    public void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }


    public void createDialog(){
        builder= new AlertDialog.Builder(this);
        final View popUpView= getLayoutInflater().inflate(R.layout.popupcancellogout,null);
        logOutSure=(Button) popUpView.findViewById(R.id.SignOutSureBtn);
        cancel=(Button) popUpView.findViewById(R.id.CancelSignOutBtn);
        builder.setView(popUpView);
        dialog=builder.create();
        dialog.show();
        logOutSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent sentHome= new Intent(SettingsActivity.this,LoginActivity.class);
                dialog.dismiss();
                finish();
                startActivity(sentHome);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "Sign Out canceled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }
}
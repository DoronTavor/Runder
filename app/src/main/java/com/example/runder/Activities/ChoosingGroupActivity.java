package com.example.runder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.runder.Adapters.Adapters.ChoosingGroupAdapter;
import com.example.runder.Adapters.Adapters.MyDetailsAdapter;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.ReadersAndNetworks.CoordinatesReader;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.CommonFiles.ReadersAndNetworks.HasCallBack;
import com.example.runder.CommonFiles.ReadersAndNetworks.HaveOrHaventGroupCB;
import com.example.runder.CommonFiles.ReadersAndNetworks.ReaderCallBack;
import com.example.runder.CommonFiles.SelectListener;
import com.example.runder.Models.ChoosingGroupModel;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChoosingGroupActivity  extends AppCompatActivity implements OnMapReadyCallback, SelectListener, HasCallBack {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    static TextView name;
    static CircleImageView picture;
    FloatingActionButton button;
    ImageButton back,mapBtn;
    MapView mapView;
    CoordinatesReader coReader;
    private AlertDialog.Builder builder,builder1;
    private AlertDialog dialog,dialog1;
    private Button sure,unsure;
    ViewGroup view;
    public Toast toast;
    static TextView choose;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    private AlertDialog.Builder builder2;
    private AlertDialog dialog2;
    private AlertDialog.Builder builder3;
    private AlertDialog dialog3;
    RecyclerView newRec;
    private LinearLayoutManager linearLayoutManager3;
    private FirebaseRecyclerAdapter adapter3;


    //TODO P1- add nickname to the group- the nickname should be presented on the group icon on the map
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad="iw_IL";
        Locale locale= new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_choosing_group);
        mapView= findViewById(R.id.MapViewChoosing);
        GlobalVars.numOfActive=0;
        Intent reciver= getIntent();
        name= findViewById(R.id.NameChoosingGroup);
        toast = new Toast(this);
        picture=findViewById(R.id.PictureChoosingGroup);
        mapBtn= (ImageButton) findViewById(R.id.SetMapChoosing);
        choose= findViewById(R.id.TextViewGroupsChoose);
        builder1= new AlertDialog.Builder(this);
        View newView= this.getLayoutInflater().inflate(R.layout.nogroups,null);
        builder1.setView(newView);
        dialog1=builder1.create();
        FireBaseReader reader= new FireBaseReader(ChoosingGroupActivity.this);
        reader.setRunningStatus(GlobalVars.currentRunner.getRunningStatus(),reciver.getStringExtra("EMAIL"));
        reader.getCurrentUser(reciver.getStringExtra("EMAIL"));
        //sharedPreferences= this.getPreferences(Context.MODE_PRIVATE);
        recyclerView=(RecyclerView) findViewById(R.id.RV_Groups);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        choose.setText("אין קבוצות כרגע");
        FirebaseRecyclerOptions<ChoosingGroupModel> options=
                new FirebaseRecyclerOptions.Builder<ChoosingGroupModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Teams"),
                                ChoosingGroupModel.class).build();
        String  maxD=  reciver.getStringExtra("AVGDISTANCE");
        int maxS= reciver.getIntExtra("AVGSPEED",2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        String  ageRange= reciver.getStringExtra("AGE");
        adapter= new ChoosingGroupAdapter(options, this, maxD,
                reciver.getIntExtra("AVGSPEED", 0), ageRange, ChoosingGroupActivity.this, new HaveOrHaventGroupCB() {
            @Override
            public void noGroupsCallBack() {
                choose.setText("אין קבוצות כרגע");
            }

            @Override
            public void thereGroupsCallBack() {
                choose.setText("הקבוצות הן: ");
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        reader.hasTeam(reciver.getStringExtra("EMAIL"),this);

        button= findViewById(R.id.CreateNewGroupBtn);
        initGoogleMap(savedInstanceState);
        reader.choosingGroupActivityEventListener(maxD,
                reciver.getIntExtra("AVGSPEED",0),ageRange);
        coReader= new CoordinatesReader(ChoosingGroupActivity.this);
        button.setOnClickListener(view -> {
            FireBaseReader reader2= new FireBaseReader(ChoosingGroupActivity.this);
            String email = reciver.getStringExtra("EMAIL");
            reader2.updateGroupToFireBase(maxD, ageRange, maxS, new ReaderCallBack() {
                @Override
                public void callBackAfterRead(String currentTeam) {
                    GlobalVars.currentRunner.setRunningStatus(GlobalVars.StatusForManager);
                    FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.choosingGroupActivityValueEvent);
                    FirebaseDatabase.getInstance().getReference().child("Teams").removeEventListener(GlobalVars.isGroupEvent);
                    moveToManaging(currentTeam);
                }


            });


        });
        back= findViewById(R.id.BackChoosing);
        back.setOnClickListener(view -> {
            Intent backIntent= new Intent(ChoosingGroupActivity.this,IWantRunActivity.class);
            backIntent.putExtra("EMAIL",reciver.getStringExtra("EMAIL"));
            FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.choosingGroupActivityValueEvent);
            FirebaseDatabase.getInstance().getReference().child("Teams").removeEventListener(GlobalVars.isGroupEvent);
            finish();
            startActivity(backIntent);
        });
        mapBtn.setOnClickListener(view -> {
            if(recyclerView.getVisibility()==View.VISIBLE){
                recyclerView.setVisibility(View.INVISIBLE);
                mapView.setVisibility(View.VISIBLE);

                //coReader.returnMarkersForChoosing(reciver.getStringExtra("AVGDISTANCE"),reciver.getStringExtra("AVGSPEED"),
                  //      reciver.getIntExtra("AGE",0)  );
                mapBtn.setImageResource(R.drawable.ic_baseline_format_list_bulleted_24);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.INVISIBLE);
                mapBtn.setImageResource(R.drawable.maps);
            }
        });
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog2();
            }
        });


    }
    public static String chooseStat(){
        return choose.getText().toString();
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        adapter.startListening();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!= null){
            adapter.stopListening();
        }
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    public static void setValueToView(RunnersModelFull model1){
        Glide.with(picture.getContext()).load(model1.getPicture()).placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                error(R.drawable.oneperson).into(picture);
        name.setText(model1.getFirstSecondName());
    }
    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(GlobalVars.MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync((OnMapReadyCallback) this);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(GlobalVars.MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(GlobalVars.MAPVIEW_BUNDLE_KEY, mapViewBundle);


        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GlobalVars.mapForChoosing=googleMap;
        GlobalVars.mapForChoosing.setMyLocationEnabled(true);

    }
    public static void setPointsForMap(MarkerOptions markerOptions){
        GlobalVars.mapForChoosing.addMarker(markerOptions);
    }
    public void createDialog2(){
        builder3= new AlertDialog.Builder(this);
        final View popUpView= getLayoutInflater().inflate(R.layout.my_details_popup,null);
        Button dis= popUpView.findViewById(R.id.BtnDisableDetails);
        newRec= popUpView.findViewById(R.id.RVMYDETAILS);
        linearLayoutManager3 = new LinearLayoutManager(this);
        newRec.setLayoutManager(linearLayoutManager3);
        newRec.setHasFixedSize(true);
        FirebaseRecyclerOptions<RunnersModelFull> options=
                new FirebaseRecyclerOptions.Builder<RunnersModelFull>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Runners").orderByChild("Email").equalTo(GlobalVars.currentRunner.getEmail()),
                                RunnersModelFull.class).build();
        adapter3= new MyDetailsAdapter(options,this);
        newRec.setAdapter(adapter3);
        adapter3.startListening();
        adapter3.notifyDataSetChanged();
        builder3.setView(popUpView);
        dialog3=builder3.create();
        dialog3.show();
        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter3.stopListening();
                dialog3.dismiss();
            }
        });




    }



    public void createDialog(ChoosingGroupModel model){
        builder= new AlertDialog.Builder(this);
        final View popUpView= getLayoutInflater().inflate(R.layout.double_check_join,null);
        sure=(Button) popUpView.findViewById(R.id.JoinSureBtn);
        unsure=(Button) popUpView.findViewById(R.id.CancelJoinBtn);
        builder.setView(popUpView);
        dialog=builder.create();
        dialog.show();
        FireBaseReader reader= new FireBaseReader(ChoosingGroupActivity.this);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reader.matchUserAndGroup(GlobalVars.currentRunner, model, new ReaderCallBack() {
                    @Override
                    public void callBackAfterRead(String currentTeam) {
                        if(currentTeam.contains(GlobalVars.currentRunner.getEmail().replace('@',' ').replace('.',' '))){
                            GlobalVars.currentRunner.setRunningStatus(GlobalVars.StatusForManager);
                        }
                        else {
                            GlobalVars.currentRunner.setRunningStatus(GlobalVars.StatusForMember);
                        }

                        FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.choosingGroupActivityValueEvent);
                        FirebaseDatabase.getInstance().getReference().child("Teams").removeEventListener(GlobalVars.isGroupEvent);
                        moveToManaging(currentTeam);
                    }


                });
            }
        });

        unsure.setOnClickListener(view -> {
            Toast.makeText(ChoosingGroupActivity.this, "The User hasn't join this group", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });



    }
    public void moveToManagingForExisting(String team){
        Intent moveToManagingActivity= new Intent(ChoosingGroupActivity.this,ManagingGroupActivity.class);

        moveToManagingActivity.putExtra("Current Team",team);
        moveToManagingActivity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
        startActivity(moveToManagingActivity);
        finish();


    }


    public void moveToManaging(String teamID){
        Intent moveToManagingActivity= new Intent(ChoosingGroupActivity.this,ManagingGroupActivity.class);
        SharedPreferences sharedRef= getSharedPreferences("ProjectPref",Context.MODE_PRIVATE);
        Log.d("PREF2",sharedRef.getAll().toString());

        //sharedRef.getString("IDGROUP",null)
        if(sharedRef.getString("IDGROUP","RE").equals("RE")){
            Log.d("SharedPrefs34", "RERERE");
        }
        moveToManagingActivity.putExtra("Current Team",teamID);
        moveToManagingActivity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
        startActivity(moveToManagingActivity);
        finish();


    }

    @Override
    public void onItemCLicked(ChoosingGroupModel model) {
        createDialog(model);
    }

    @Override
    public void moveAfterSuccessCallBack(String team) {
        moveToManagingForExisting(team);


    }




}
package com.example.runder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.runder.Adapters.Adapters.ManagingGroupAdapter;
import com.example.runder.Adapters.Adapters.MyDetailsAdapter;
import com.example.runder.CommonFiles.CBS.CBForTemp;
import com.example.runder.CommonFiles.CBS.FinishSending;
import com.example.runder.CommonFiles.CBS.InterfaceForName;
import com.example.runder.CommonFiles.CBS.PresentWhenManagerEnds;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.ReadersAndNetworks.CoordinatesReader;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.CommonFiles.ReadersAndNetworks.GoogleMapFunctions;
import com.example.runder.CommonFiles.ReadersAndNetworks.MessageSender;
import com.example.runder.CommonFiles.ReadersAndNetworks.ShowTimeCB;
import com.example.runder.CommonFiles.ReadersAndNetworks.TempReader;
import com.example.runder.Models.ManagingModel;
import com.example.runder.Models.RunStatisticsModel;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagingGroupActivity extends AppCompatActivity implements OnMapReadyCallback {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter,adapter2;
    static TextView name;
    static CircleImageView picture;
    static Button status;
    ImageButton mapBtn;
    MapView mapView;
    CoordinatesReader coReader;
    GoogleMapFunctions googleMapFunctions;
    Button stop,pause,start;
    static TextView temp;
    static ImageView tempPic;
    TextView textViewGroup;
    int seconds,minutes;
    private AlertDialog.Builder builder2,builder3;
    private AlertDialog dialog2,dialog3;
    RecyclerView newRec;
    private LinearLayoutManager linearLayoutManager2,linearLayoutManager3;
    ConstraintLayout layoutStats,detailsLayout;
    ImageView tempPicStats;
    ImageButton ButtonForExitWhenManager;
    ImageButton imageButtonStats;
    TextView textViewGroupName,statusTV;
    TextView tempTextViewManagingStats, GroupNameTextView,StatusStatsTV,TextViewGroupTime,
            textViewMyTime,TextViewMySpeed,TextViewGroupDistance,TextViewMyDistance,TextViewGroupSpeed;
    ImageButton exitForRunner,messageButton;
    boolean exited=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad="iw_IL";
        Locale locale= new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_managing_group);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        mapView= findViewById(R.id.MapViewManaging);
        coReader= new CoordinatesReader(this);
        GlobalVars.polylineOptions= new PolylineOptions();
        Intent reciver= getIntent();
        Log.d("EMAIL",reciver.getStringExtra("EMAIL"));
        FireBaseReader reader= new FireBaseReader(ManagingGroupActivity.this);
        reader.getCurrentUser(reciver.getStringExtra("EMAIL"));
        Log.d("CHECKRECIVED",reciver.getStringExtra("Current Team"));
        temp=findViewById(R.id.TempTextViewManaging);
        tempPic=findViewById(R.id.TempPicManaging);
        name= findViewById(R.id.NameManagingGroup);
        picture=findViewById(R.id.PictureManagingGroup);
        mapBtn= findViewById(R.id.SetMapManaging);
        stop= findViewById(R.id.StopRunBtn);
        stop.setVisibility(View.INVISIBLE);
        pause= findViewById(R.id.PauseRunBtn);
        start= findViewById(R.id.startRunBtn);
        exitForRunner= findViewById(R.id.ExitForRunner);
        textViewGroupName= findViewById(R.id.TextViewGroupName);
        textViewGroup= findViewById(R.id.TextViewGroupsChoose);
        detailsLayout= findViewById(R.id.layout_for_details);
        detailsLayout.setVisibility(View.VISIBLE);
        imageButtonStats= findViewById(R.id.ImageButtonStats);
        imageButtonStats.setVisibility(View.INVISIBLE);
        statusTV= findViewById(R.id.TextViewStatus);
        messageButton= findViewById(R.id.MessageRunBtn);
        messageButton.setVisibility(View.VISIBLE);
        ButtonForExitWhenManager= findViewById(R.id.ButtonForExitWhenManager);
        setIdsToStatsLayout();
        GlobalVars.runDistance=findViewById(R.id.Distance_Text_View);
        GlobalVars.runTime=findViewById(R.id.Time_Text_View);
        reader.updateRunnerStatus("בבנייה");
        if(reciver.getStringExtra("Current Team").contains(reciver.getStringExtra("EMAIL").replace('@',' ').replace('.',' '))){
            stop.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
            start.setVisibility(View.VISIBLE);
            exitForRunner.setVisibility(View.INVISIBLE);
        }
        else {
            stop.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.INVISIBLE);
            start.setVisibility(View.INVISIBLE);
            stop.setClickable(false);
            pause.setClickable(false);
            start.setClickable(false);
            exitForRunner.setVisibility(View.VISIBLE);
        }
        //reader.setTextForButton(reciver.getStringExtra("EMAIL"));
        reader.setRunningStatus(GlobalVars.currentRunner.getRunningStatus(),reciver.getStringExtra("EMAIL"));
        textViewGroup.setText("הקבוצה של: "+GlobalVars.currentGroup.getManagerName());
        reader.presentName(reciver.getStringExtra("Current Team"), new InterfaceForName() {
                    @Override
                    public void presentGroupName(String name, String status) {
                        textViewGroupName.setText(name);
                        statusTV.setText(GlobalVars.returnHebrewStatus(status));
                    }
                });

                initGoogleMap(savedInstanceState);
        if(reciver.getStringExtra("Current Team").contains(reciver.getStringExtra("EMAIL").replace('@',' ').replace('.',' '))){
            reader.managingGroupActivityEventListenerForManager();
        }
        else {
            reader.managingGroupActivityEventListenerForMember(new PresentWhenManagerEnds() {
                @Override
                public void present(RunStatisticsModel statisticsModel) {
                    GlobalVars.currentRunner.setStatus("סוגרים");
                    setTextToViewsOfStats(statisticsModel);
                    layoutStats.setVisibility(View.VISIBLE);
                    imageButtonStats.setVisibility(View.VISIBLE);
                    imageButtonStats.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(layoutStats.getVisibility()== View.VISIBLE){
                                imageButtonStats.setImageResource(R.drawable.ic_baseline_format_list_bulleted_24);
                                layoutStats.setVisibility(View.INVISIBLE);
                            }
                            else {
                                imageButtonStats.setImageResource(R.drawable.maps);
                                layoutStats.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    detailsLayout.setVisibility(View.GONE);
                }
            });
            reader.waitForStartTime(reciver.getStringExtra("Current Team"),new ShowTimeCB() {
                @Override
                public void onFinishedUpload() {

                }

                @Override
                public void onRunStarted() {
                    calculateAndPresentTime();

                }
            });
        }
        TempReader tempReader = new TempReader(ManagingGroupActivity.this);
        LatLng position= new LatLng(GlobalVars.currentRunner.getCurrentPosition().get("Latitude"),GlobalVars.currentRunner.getCurrentPosition().get("Longitude"));
        tempReader.getTemp(position);
        recyclerView= findViewById(R.id.RV_Groups);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        FirebaseRecyclerOptions<ManagingModel> options=
             new FirebaseRecyclerOptions.Builder<ManagingModel>()
                  .setQuery(FirebaseDatabase.getInstance().getReference().child("Runners").orderByChild("ActiveTeam").equalTo(reciver.getStringExtra("Current Team")),
                             ManagingModel.class).build();
        //.orderByChild("Active Team").equalTo(reciver.getStringExtra("Current Team")
        adapter= new ManagingGroupAdapter(options,reciver.getStringExtra("Current Team"),this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        googleMapFunctions= new GoogleMapFunctions(ManagingGroupActivity.this);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        mapBtn.setOnClickListener(view -> {
            if(recyclerView.getVisibility()==View.VISIBLE){
                recyclerView.setVisibility(View.INVISIBLE);
                mapView.setVisibility(View.VISIBLE);
                textViewGroup.setText("הקבוצה של "+GlobalVars.currentGroup.getManagerName());
                //coReader.updateGroupMarkers(reciver.getStringExtra("Current Team"));
                mapBtn.setImageResource(R.drawable.ic_baseline_format_list_bulleted_24);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.INVISIBLE);
                mapBtn.setImageResource(R.drawable.maps);
            }
        });
        if(!GlobalVars.currentGroup.getManagerName().equals(GlobalVars.currentRunner.getFirstSecondName())){
            exitForRunner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder3;
                    AlertDialog dialog3;
                    builder3= new AlertDialog.Builder(ManagingGroupActivity.this);
                    final View popUpView= getLayoutInflater().inflate(R.layout.double_check_leave,null);
                    Button sure= popUpView.findViewById(R.id.FinishSureBtn);
                    Button cancel= popUpView.findViewById(R.id.CancelFinishBtn);
                    builder3.setView(popUpView);
                    dialog3=builder3.create();
                    dialog3.show();
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog3.dismiss();
                            FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).child("RunnersIds").child(GlobalVars.currentRunner.getFirstSecondName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot snapshot) {
                                            RunStatisticsModel statsModel= snapshot.getValue(RunStatisticsModel.class);
                                            DecimalFormat df = new DecimalFormat("#.###");
                                            GlobalVars.currentRunner.setStatus("סוגרים");
                                            statsModel.setMyDistance(df.format(GlobalVars.TotalDistance));
                                            statsModel.setMyTime(GlobalVars.passedTime);
                                            statsModel.setNewElapsedTIme();
                                            statsModel.setSpeeds();
                                            statsModel.roundNumbers(3);
                                            setTextToViewsOfStats(statsModel);
                                            layoutStats.setVisibility(View.VISIBLE);
                                            messageButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    sendMessage(statsModel);
                                                }
                                            });
                                            imageButtonStats.setVisibility(View.VISIBLE);
                                            detailsLayout.setVisibility(View.GONE);
                                            imageButtonStats.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if(layoutStats.getVisibility()== View.VISIBLE){
                                                        imageButtonStats.setImageResource(R.drawable.ic_baseline_format_list_bulleted_24);
                                                        layoutStats.setVisibility(View.INVISIBLE);
                                                    }
                                                    else {
                                                        imageButtonStats.setImageResource(R.drawable.maps);
                                                        layoutStats.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                            ButtonForExitWhenManager.setVisibility(View.VISIBLE);
                                            detailsLayout.setVisibility(View.GONE);
                                            ButtonForExitWhenManager.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(GlobalVars.currentRunner.getEmail())).child("ActiveTeam").setValue("No").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(ManagingGroupActivity.this, "Thank you for choosing our app, " +
                                                                    "you are sent back to main activity", Toast.LENGTH_SHORT).show();
                                                            Intent toMainActivity= new Intent(ManagingGroupActivity.this,MainActivity.class);
                                                            toMainActivity.putExtra("EMAIL",GlobalVars.currentRunner.getEmail());
                                                            finish();
                                                            startActivity(toMainActivity);
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });

                                }
                            });
                           //

                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog3.dismiss();
                            Toast.makeText(ManagingGroupActivity.this, "Exit is canceled", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

      /*  status.setOnClickListener(view -> {
            String statusS= "";
            if(status.getText().toString().equals("רצים")){
                statusS=status.getText().toString();
                //googleMapFunctions.addPointsToPolyLine(reciver.getStringExtra("EMAIL"), latLng -> addPointsToPolyLine(latLng));
                status.setText("סיימנו לרוץ");
                reader.setTeamStatus(statusS,reciver.getStringExtra("Current Team"),
                        reciver.getStringExtra("EMAIL") );
                //reader.setTimeForRun(reciver.getStringExtra("Current Team"));

            }
            else if(status.getText().toString().equals("סיימנו לרוץ")){
                statusS=status.getText().toString();
                status.setText("סוגרים");
                reader.setTeamStatus(statusS,reciver.getStringExtra("Current Team"),
                        reciver.getStringExtra("EMAIL") );

            }



        });*/
        start.setOnClickListener(view -> {
            reader.setTeamStatus("רצים", reciver.getStringExtra("Current Team"),
                    reciver.getStringExtra("EMAIL"), new ShowTimeCB() {
                        @Override
                        public void onFinishedUpload() {
                            calculateAndPresentTime();
                            stop.setVisibility(View.VISIBLE);
                            GlobalVars.statusGroup= "רצים";
                            reader.updateRunnerStatus("רצים");
                            statusTV.setText(GlobalVars.statusGroup);
                            reader.updateTeamStatus(GlobalVars.statusGroup,reciver.getStringExtra("Current Team"));
                            //GlobalVars.cBforLOc.setNewDis();


                        }

                        @Override
                        public void onRunStarted() {

                        }
                    });
            Toast.makeText(ManagingGroupActivity.this, "Run started", Toast.LENGTH_SHORT).show();
        });
        pause.setOnClickListener(view -> {
            reader.setTeamStatus("pause",reciver.getStringExtra("Current Team"),
                    reciver.getStringExtra("EMAIL"),null );
            GlobalVars.statusGroup= "pause";
            Toast.makeText(ManagingGroupActivity.this, "Run paused", Toast.LENGTH_SHORT).show();
        });
        stop.setOnClickListener(view -> {
            stopClickListenerMethods();





        });





    }

    private void setIdsToStatsLayout() {
        layoutStats=findViewById(R.id.LayoutStats);
        tempTextViewManagingStats= findViewById(R.id.tempTextViewManagingStats);
        GroupNameTextView=findViewById(R.id.GroupNameTextView);
        StatusStatsTV=findViewById(R.id.StatusStatsTV);
        tempPicStats=findViewById(R.id.tempPicStats);
        TextViewGroupTime=findViewById(R.id.TextViewGroupTime);
        textViewMyTime=findViewById(R.id.textViewMyTime);
        TextViewMySpeed=findViewById(R.id.TextViewMySpeed);
        TextViewGroupDistance=findViewById(R.id.TextViewGroupDistance);
        TextViewMyDistance=findViewById(R.id.TextViewMyDistance);
        TextViewGroupSpeed=findViewById(R.id.TextViewGroupSpeed);
    }

    private void stopClickListenerMethods() {
         AlertDialog.Builder builder3;
         AlertDialog dialog3;
         builder3= new AlertDialog.Builder(this);
        final View popUpView= getLayoutInflater().inflate(R.layout.double_check_leave,null);
        Button sure= popUpView.findViewById(R.id.FinishSureBtn);
        Button cancel= popUpView.findViewById(R.id.CancelFinishBtn);
        builder3.setView(popUpView);
        dialog3=builder3.create();
        dialog3.show();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).child("Status").setValue("סוגרים").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalVars.statusGroup= "סוגרים";
                        FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(GlobalVars.currentRunner.getEmail())).child("Status").setValue("סוגרים").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                layoutStats.setVisibility(View.VISIBLE);
                                FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()){
                                            if(task.getResult() != null){
                                                GlobalVars.currentRunner.setStatus("סוגרים");
                                                RunStatisticsModel statsModel= task.getResult().getValue(RunStatisticsModel.class);
                                                statsModel.setMyDistance(String.valueOf(GlobalVars.TotalDistance));
                                                statsModel.setMyTime(GlobalVars.passedTime);
                                                statsModel.setNewElapsedTIme();
                                                statsModel.setSpeeds();
                                                statsModel.roundNumbers(3);
                                                setTextToViewsOfStats(statsModel);
                                                messageButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        sendMessage(statsModel);
                                                    }
                                                });
                                                imageButtonStats.setVisibility(View.VISIBLE);
                                                imageButtonStats.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if(layoutStats.getVisibility()== View.VISIBLE){
                                                            imageButtonStats.setImageResource(R.drawable.ic_baseline_format_list_bulleted_24);
                                                            layoutStats.setVisibility(View.INVISIBLE);
                                                        }
                                                        else {
                                                            imageButtonStats.setImageResource(R.drawable.maps);
                                                            layoutStats.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                });
                                                ButtonForExitWhenManager.setVisibility(View.VISIBLE);
                                                detailsLayout.setVisibility(View.GONE);
                                            }

                                            ButtonForExitWhenManager.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(GlobalVars.currentRunner.getEmail())).child("ActiveTeam").setValue("No").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(ManagingGroupActivity.this, "Thank you for choosing our app, " +
                                                                            "you are sent back to main activity", Toast.LENGTH_SHORT).show();
                                                                    Intent toMainActivity= new Intent(ManagingGroupActivity.this,MainActivity.class);
                                                                    toMainActivity.putExtra("EMAIL",GlobalVars.currentRunner.getEmail());
                                                                    finish();
                                                                    startActivity(toMainActivity);

                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        });



                    }
                });

            };
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManagingGroupActivity.this, "סיום הריצה בוטל", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void sendMessage(RunStatisticsModel statsModel) {
        builder3= new AlertDialog.Builder(this);
        final View popUpView= getLayoutInflater().inflate(R.layout.popupmesssage,null);
        Button send= popUpView.findViewById(R.id.SureSendBtn);
        EditText etNumber= popUpView.findViewById(R.id.editTextPhone);
        builder3.setView(popUpView);
        dialog3=builder3.create();
        dialog3.show();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageSender.sendSMS(etNumber.getText().toString(), statsModel, new FinishSending() {
                    @Override
                    public void finished() {
                        Toast.makeText(ManagingGroupActivity.this, "Finish sending", Toast.LENGTH_SHORT).show();
                        dialog3.dismiss();
                    }
                });

            }
        });

    }

    private void setTextToViewsOfStats(RunStatisticsModel statsModel) {
        //  tempPicStats;
        //     tempTextViewManagingStats, ,,,
        //            ,,,,
        GroupNameTextView.setText(GlobalVars.currentGroup.getGroupName());
        StatusStatsTV.setText(GlobalVars.currentRunner.getStatus());
        TextViewGroupTime.setText(String.valueOf(statsModel.getGroupElapsedTime())+" שניות");
        textViewMyTime.setText(String.valueOf(statsModel.getMyElapsedTime())+" שניות");
        TextViewMySpeed.setText(String.valueOf(statsModel.getMySpeed())+" מטר/שניה");
        TextViewGroupDistance.setText(String.valueOf(statsModel.getDistance())+" מטר");
        TextViewMyDistance.setText(statsModel.getMyDistance()+" מטר");
        TextViewGroupSpeed.setText(String.valueOf(statsModel.getGroupSpeed())+" מטר/שניה");
        TempReader reader= new TempReader(ManagingGroupActivity.this);
        LatLng latLng= new LatLng(GlobalVars.currentRunner.getCurrentPosition().get("Latitude"),GlobalVars.currentRunner.getCurrentPosition().get("Longitude"));
        reader.weatherStats(latLng, new CBForTemp() {
            @Override
            public void presentTemp(String temp) {
                tempTextViewManagingStats.setText(temp);
                if (Double.parseDouble(temp.replace("°C", "")) >= 20) {
                    tempPicStats.setImageResource(R.drawable.high_temperature_icon);
                } else {
                    tempPicStats.setImageResource(R.drawable.low_temperature_icon);
                }
            }
        });



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
        //FireBaseReader.removeTeam(GlobalVars.currentRunner.getID());
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
        GlobalVars.mapForManaging= googleMap;
        GlobalVars.mapForManaging.setMyLocationEnabled(true);
    }


    /*public static void setPointsForMap(MarkerOptions markerOptions){
           GlobalVars.mapForManaging.addMarker(markerOptions) ;
           GlobalVars.MARKERSFORMANAGING.add(markerOptions);
        }*/
    public static void setButtonText(Boolean isManager){
        if(isManager){
            status.setText("רצים");

        }
        else {
            status.setText("יציאה");
        }
    }
    public static void addPointsToPolyLine(LatLng latLng){
        GlobalVars.polylineOptions.add(latLng);
        if(GlobalVars.polylineOptions.getPoints().size()>=2){
            GlobalVars.mapForManaging.clear();
            GlobalVars.mapForManaging.addPolyline(GlobalVars.polylineOptions);
            //add the deleted markers
            for(MarkerOptions markerOptions:GlobalVars.MARKERSFORMANAGING){
                GlobalVars.mapForManaging.addMarker(markerOptions);

            }

        }

    }

    public static void setWeather(String weather) {

        temp.setText(weather);
        if (Double.parseDouble(weather.replace("°C", "")) >= 20) {
            tempPic.setImageResource(R.drawable.hotlogo);
        } else {
            tempPic.setImageResource(R.drawable.coldlogo);
        }
    }

    public void calculateAndPresentTime(){
        Timer timer= new Timer();
         Handler handler = new Handler();
        TimerTask timerTask= new TimerTask() {
            int times=0;
            @Override
            public void run() {
                if(GlobalVars.statusGroup.equals("רצים")){
                    times++;
                    if(times>1){
                        GlobalVars.GlobalSeconds+=10;
                    }


                    if(GlobalVars.GlobalSeconds==60){
                        GlobalVars.GlobalSeconds=0;
                        GlobalVars.GlobalMinutes++;
                    }
                    if(GlobalVars.GlobalMinutes==60){
                        GlobalVars.GlobalHours++;
                        GlobalVars.GlobalSeconds=0;
                        GlobalVars.GlobalMinutes=0;

                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String time = String.format("%02d:%02d:%02d",GlobalVars.GlobalHours ,GlobalVars.GlobalMinutes, GlobalVars.GlobalSeconds);
                        GlobalVars.runTime.setText("זמן: "+time +" שעות ");
                        GlobalVars.passedTime= time;
                        if(GlobalVars.GlobalSeconds%10 ==0){
                            FireBaseReader reader= new FireBaseReader(ManagingGroupActivity.this);
                            reader.updateGroupTime(GlobalVars.currentGroup.getGroupID(),time);
                        }




                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10000);
    }

    public void createDialog(){
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
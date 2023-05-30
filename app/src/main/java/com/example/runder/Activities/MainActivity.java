package com.example.runder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.runder.Adapters.Adapters.MyDetailsAdapter;
import com.example.runder.CommonFiles.DebugModeService;
import com.example.runder.CommonFiles.MarkerAndMail;
import com.example.runder.CommonFiles.ReadersAndNetworks.CoordinatesReader;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.CommonFiles.ReadersAndNetworks.GeocodingMethods;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.ReadersAndNetworks.GetCurrentUserInterface;
import com.example.runder.CommonFiles.ReadersAndNetworks.TempReader;
import com.example.runder.Models.ChoosingGroupModel;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //TODO P2- Check why all time transactions with the firebase occurs
    //TODO P2- check if i can do the floating textviews like in runkeeper
    //TODO P2- make the Main checking rectangle with rounded corners
    CircleImageView image;
    TextView name;
    TextView date,statusTV;
    static TextView temp;
    ImageButton menuBtn, iWantRunBtn,music;
    MapView mapView;
    static ImageView tempPic;
    public static RunnersModelFull model, modelGlobal;
    SimpleDateFormat sdf;
    String currentDateandTime;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button sureWantRun, cancel;
    String email;
    Marker userMarker;
    boolean currentUserIsReady= false;
    static LocationRequest locationRequest;
    private AlertDialog.Builder builder2;
    private AlertDialog dialog2;
    RecyclerView newRec;
    private LinearLayoutManager linearLayoutManager2;
    private FirebaseRecyclerAdapter adapter2;


    //Google API for location services, the majority of the app functions using this class
   static FusedLocationProviderClient fusedLocationClient;
    FireBaseReader reader;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DateTime format
        sdf = new SimpleDateFormat(" dd/MM/yyyy", Locale.getDefault());
        // set app language to Hebrew
        String languageToLoad = "iw_IL";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        reader = new FireBaseReader(this);
        Intent i = getIntent();
        email = i.getStringExtra("EMAIL");
        GlobalVars.currentRunner.setRunningStatus(GlobalVars.StatusForNotRunning);
        reader.setRunningStatus(GlobalVars.currentRunner.getRunningStatus(),email);
        setContentView(R.layout.activity_main);
     //   Intent serviceIntent = new Intent(this, MyForegroundService.class);
       // startService(serviceIntent);
        // get current user identity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = new Intent(this, DebugModeService.class);
        startService(intent);



        connectViewsToId();
        GlobalVars.TotalDistance =0;

        // Map initialization
        initGoogleMap(savedInstanceState);
        GlobalVars.mainActivityMarkers= new HashMap<>();

        // Current user initialization

        Log.d("MainActivity","Current user is initiated with  " + GlobalVars.currentRunner);
        getCurrentUser(email, new GetCurrentUserInterface() {
            @Override
            public void onFinished() {
                // Need to wait until getCurrentUser completes reading the currentUser from FB
                Log.d("MainActivity","Current user is initiated with" + GlobalVars.currentRunner + GlobalVars.currentRunner.getEmail());
                // Shared preferences initialization
                sharedpreferences = getSharedPreferences("ProjectPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Log.d("PREF", sharedpreferences.getAll().toString());


                // Get my location
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                // All location permissions are statically granted
                setupLocationUpdates(5);
                reader.mainActivityOnDataChanged();
                image.setClickable(true);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createDialog2();
                    }
                });
                // buttons handlers
                iWantRunBtn.setOnClickListener(view -> createDialog());

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);
                popupMenu.getMenuInflater().inflate(R.menu.system_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.menuMyDetails:
                                Intent toMyDetails= new Intent(MainActivity.this,MyDetailsActivity.class);
                                toMyDetails.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                                finish();
                                popupMenu.dismiss();
                                startActivity(toMyDetails);
                                return true;
                            case R.id.menuRunderCommunity:
                                Intent toCommunity= new Intent(MainActivity.this,RunderCommunityActivity.class);
                                toCommunity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                                finish();
                                popupMenu.dismiss();
                                startActivity(toCommunity);
                                return true;
                            case R.id.menuLogOut:
                                createDialogForLeaving(popupMenu);
                                return true;
                            case R.id.menuAbout:
                                Intent toAbout= new Intent(MainActivity.this,AboutActivity.class);
                                toAbout.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                                finish();
                                popupMenu.dismiss();
                                startActivity(toAbout);
                                return true;
                        }
                        return true;

                    }
                    }

                );
                menuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupMenu.show();
                    }
                });



            }
        });


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

    @SuppressLint("MissingPermission")
    private void setupLocationUpdates(int minDistance) {
         locationRequest = new LocationRequest.Builder(700)
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateDistanceMeters(minDistance)
                 .setMinUpdateIntervalMillis(1500)
                .build();

        LocationSettingsRequest locationSettingsRequest= new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();

        SettingsClient settingsClient= LocationServices.getSettingsClient(MainActivity.this);
        settingsClient.checkLocationSettings(locationSettingsRequest).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               fusedLocationClient.requestLocationUpdates(locationRequest,locationCallbackUpdate, Looper.getMainLooper());

           }
           else {
               if(task.getException() instanceof ResolvableApiException){

               }
               else {

               }
           }
        });

    }

    // get my location
     LocationCallback locationCallbackUpdate = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("CheckLocation","onComplete  "+locationResult);
            Location location= locationResult.getLocations().get(0);

            String address= GeocodingMethods.getAddress(new LatLng(location.getLatitude(),location.getLongitude()),MainActivity.this);
            if(address==null){
                Log.d("ErrorMainActivity","In update Location, the converted address is null");
            }
            if(GlobalVars.isInDebug){
                Toast.makeText(MainActivity.this, "Address Update"+address.substring(0,12)+"  "+getActivityName(), Toast.LENGTH_SHORT).show();
            }

            updateUserPosition(new LatLng(location.getLatitude(),location.getLongitude()),GlobalVars.currentRunner);


        }
    };

    private boolean isTimeBigger() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date time = format.parse(GlobalVars.currentRunner.getLocationLastUpdateHour());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Calendar calendarCurrent = Calendar.getInstance();
        return (((calendarCurrent.getTimeInMillis()-calendar.getTimeInMillis())/ 1000)/60) >=1;




    }
//TODO P1- Show the groups on the map. one mock group to be presented on the map- 13/03/2023

    public void updateUserPosition(LatLng currentLatLng,RunnersModelFull currentRunner){
        Map<String,Object> locationToUpdate = new HashMap<>();
        DatabaseReference mRunderDB =
                FirebaseDatabase.getInstance().getReference();
        if(GlobalVars.statusGroup.equals("רצים")){
            presentRunDistance(currentLatLng,currentRunner);
        }
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateCheckUpdate = new SimpleDateFormat("dd/MM/yyyy");
        String dateCheck = dateCheckUpdate.format(currentTime);
        DateFormat hourCheckUpdate= new SimpleDateFormat("HH:mm");
        String hourCheck= hourCheckUpdate.format(currentTime);
        String emailToEqual= currentRunner.getEmail().replace('@',' ').replace('.',' ');
        locationToUpdate.put("/Runners/"+emailToEqual+"/CurrentPosition/Latitude",currentLatLng.latitude);
        locationToUpdate.put("/Runners/"+emailToEqual+"/CurrentPosition/Longitude",currentLatLng.longitude);
        locationToUpdate.put("/Runners/"+emailToEqual+"/LocationLastUpdateDate",dateCheck);
        locationToUpdate.put("/Runners/"+emailToEqual+"/LocationLastUpdateHour",hourCheck);

        try {
            mRunderDB.updateChildren(locationToUpdate).addOnSuccessListener(unused -> Log.d("UPosition","succeeded"+getActivityName())).addOnFailureListener(e -> {
                Log.d("UPosition","UNsucceeded"+e.getMessage());
                Toast.makeText(MainActivity.this, "Problem in updating  "+e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        catch (Exception e){
            Log.d("UPosition","Error in updating position if FB "+e.getMessage());
            Toast.makeText(MainActivity.this, "Problem in updating  "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // update the global current user object with current location and date of updates
        HashMap<String,Double> position= new HashMap<>();
        position.put("Latitude",currentLatLng.latitude);
        position.put("Longitude",currentLatLng.longitude);
        GlobalVars.currentRunner.setCurrentPosition(position);
        GlobalVars.currentRunner.setLocationLastUpdateDate(dateCheck);
        GlobalVars.currentRunner.setLocationLastUpdateHour(hourCheck);

    }

    private void presentRunDistance(LatLng currentLatLng, RunnersModelFull currentRunner) {
        Location newLoc= new Location("");
        newLoc.setLatitude(currentLatLng.latitude);
        newLoc.setLongitude(currentLatLng.longitude);
        Location prevLoc= new Location("");
        prevLoc.setLatitude(currentRunner.getCurrentPosition().get("Latitude"));
        prevLoc.setLongitude(currentRunner.getCurrentPosition().get("Longitude"));
        double distanceNew= (newLoc.distanceTo(prevLoc))/1000.0;
        GlobalVars.TotalDistance +=distanceNew;
        if(GlobalVars.TotalDistance == 0){
            GlobalVars.runDistance.setText("מרחק: "+ 0+" ק'מ ");
        }
        else {
            DecimalFormat df = new DecimalFormat("#.###");
            GlobalVars.runDistance.setText("מרחק: "+ df.format(GlobalVars.TotalDistance)+" ק'מ ");
        }




    }

    private String getActivityName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (!runningTasks.isEmpty()) {
            String activityName = runningTasks.get(0).topActivity.getShortClassName();
            return activityName;
        }
        else {
            return this.getClass().getName();
        }
    }

    public void manageRunnersOnMap(){

               GlobalVars.MainActivityCallBack =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //TODO P3- Read only the children that actually changed their position 08/03/2023
                String emails= "";
                for(DataSnapshot runnersSnapShot: snapshot.getChildren()){
                    Log.d("RunnersFB",runnersSnapShot.toString());
                 //   RunnersModelFull runner= addToModel(runnersSnapShot,new RunnersModelFull());
                    RunnersModelFull runner= runnersSnapShot.getValue(RunnersModelFull.class);
                    runner.setID(runnersSnapShot.getKey());
                    Log.d("RunnersFB",runner.toString());
                    emails= emails + runner.getEmail()+" ";
                    Log.d("TypeCheck",runner.getCurrentPosition().toString());
                    LatLng position= new LatLng((Double) runner.getCurrentPosition().get("Latitude"),(Double)runner.getCurrentPosition().get("Longitude"));
                    String address= GeocodingMethods.getAddress(position,MainActivity.this);
                    if(!runner.getEmail().equals(GlobalVars.currentRunner.getEmail())){
                        if(!GlobalVars.mainActivityMarkers.containsKey(runner.getEmail())){
                            Bitmap markerIcon= BitmapFactory.decodeResource(getResources(), R.drawable.person_yellow);
                            if(runner.getGender().equals("נקבה")){
                                 markerIcon= BitmapFactory.decodeResource(getResources(), R.drawable.woman_yellow);
                            }
                            int width = 120;
                            int height = 120;
                            Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                            GlobalVars.mainActivityMarkers.put(runner.getEmail(), GlobalVars.map.addMarker(new MarkerOptions().position(position).snippet(address).title(runner.getFirstSecondName())
                                    .icon(bitmapDescriptor)));
                            if(!runner.getActiveTeam().equals("No")&&runner.getActiveTeam().contains(runner.getEmail().replace('@',' ').replace('.',' '))){
                                //Check if the manager's team is in the map
                                if(GlobalVars.mainActivityGroupsMarkers.containsKey(runner.getActiveTeam())){
                                    GlobalVars.mainActivityGroupsMarkers.get(runner.getActiveTeam()).setPosition(position);
                                    GlobalVars.mainActivityGroupsMarkers.get(runner.getActiveTeam()).setSnippet(address);
                                }
                                else {
                                    FirebaseDatabase.getInstance().getReference().child("Teams").child(runner.getActiveTeam()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if(task.isSuccessful()&& task.getResult().getValue(ChoosingGroupModel.class) != null){
                                                ChoosingGroupModel groupModel= task.getResult().getValue(ChoosingGroupModel.class);
                                                groupModel.setGroupID(task.getResult().getKey());
                                                Bitmap markerIcon2= BitmapFactory.decodeResource(getResources(), R.drawable.group_purple);
                                                Bitmap resizedIcon2 = Bitmap.createScaledBitmap(markerIcon2, width, height, false);
                                                BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromBitmap(resizedIcon2);
                                                GlobalVars.mainActivityGroupsMarkers.put(groupModel.getGroupID(),GlobalVars.map.addMarker(new MarkerOptions().position(position).snippet(address)
                                                        .title(groupModel.getGroupName()).icon(bitmapDescriptor2)));
                                                //Setting the markers of the runners invisible
                                                for(Map.Entry<String, String> entry : groupModel.getRunnersIds().entrySet()){
                                                    int partOf= entry.getValue().indexOf("gmail");
                                                    String toMatch= entry.getValue().substring(0,partOf-1);
                                                    toMatch=toMatch+"@";
                                                    if(toMatch.contains(" ")){
                                                        toMatch.replace(" ",".");
                                                    }
                                                    String plus= entry.getValue().substring(partOf).replace(" ",".");
                                                    String emailC= toMatch+plus;
                                                    GlobalVars.mainActivityMarkers.get(emailC).setVisible(false);



                                                }
                                            }
                                        }
                                    });
                                }

                            }
                            else {
                                if(runner.getActiveTeam().equals("No")&& isHashMapContains(GlobalVars.mainActivityGroupsMarkers,runner.getEmail().replace('@',' ').replace('.',' ')) && !GlobalVars.mainActivityMarkers.get(runner.getEmail())
                                        .isVisible()){
                                    GlobalVars.mainActivityMarkers.get(runner.getEmail()).setVisible(true);
                                    if(isHashMapContains(GlobalVars.mainActivityGroupsMarkers,runner.getEmail().replace('@',' ').replace('.',' '))){
                                        GlobalVars.mainActivityGroupsMarkers.get(isHashMapContainsKey(GlobalVars.mainActivityGroupsMarkers,runner.getEmail().replace('@',' ').replace('.',' '))).setVisible(false);
                                    }
                                   /* for(Map.Entry<String,Marker> entry: GlobalVars.mainActivityGroupsMarkers.entrySet()){
                                        if(entry.getKey().contains(runner.getEmail().replace('@',' ').replace('.',' '))){
                                            entry.getValue().setVisible(false);
                                        }
                                    }*/

                                }
                            }

                            //GlobalVars.map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17.0f));

                        }
                        else {
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setPosition(position);
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setSnippet(address);
                            if(GlobalVars.mainActivityGroupsMarkers.containsKey(runner.getActiveTeam())){
                                GlobalVars.mainActivityGroupsMarkers.get(runner.getActiveTeam()).setPosition(position);
                                GlobalVars.mainActivityGroupsMarkers.get(runner.getActiveTeam()).setSnippet(address);
                            }
                            else {
                                Query query;
                                if(runner.getActiveTeam().equals("No")){
                                    query= FirebaseDatabase.getInstance().getReference().child("Teams").orderByChild("GroupID").startAt(runner.getID());
                                }
                                else {
                                    query =FirebaseDatabase.getInstance().getReference().child("Teams").child(runner.getActiveTeam());
                                }

                              query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()&& task.getResult().getValue(ChoosingGroupModel.class) != null){
                                            ChoosingGroupModel groupModel= task.getResult().getValue(ChoosingGroupModel.class);
                                            groupModel.setGroupID(task.getResult().getKey());
                                            Bitmap markerIcon2= BitmapFactory.decodeResource(getResources(), R.drawable.group_purple);
                                            int width = 120;
                                            int height = 120;
                                            Bitmap resizedIcon2 = Bitmap.createScaledBitmap(markerIcon2, width, height, false);
                                            BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromBitmap(resizedIcon2);
                                            GlobalVars.mainActivityGroupsMarkers.put(groupModel.getGroupID(),GlobalVars.map.addMarker(new MarkerOptions().position(position).snippet(address)
                                                    .title(groupModel.getGroupName()).icon(bitmapDescriptor2)));
                                            //Setting the markers of the runners invisible or visible if the team deleted
                                            for(Map.Entry<String, String> entry : groupModel.getRunnersIds().entrySet()){
                                                int partOf= entry.getValue().indexOf("gmail");
                                                String toMatch= entry.getValue().substring(0,partOf-1);
                                                toMatch=toMatch+"@";
                                                if(toMatch.contains(" ")){
                                                    toMatch.replace(" ",".");
                                                }
                                                String plus= entry.getValue().substring(partOf).replace(" ",".");
                                                String emailC= toMatch+plus;
                                                if(runner.getActiveTeam().equals("No")){
                                                    GlobalVars.mainActivityMarkers.get(emailC).setVisible(true);
                                                }
                                                else {
                                                    GlobalVars.mainActivityMarkers.get(emailC).setVisible(false);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                            //GlobalVars.map.animateCamera(CameraUpdateFactory.newLatLngZoom((position), 17.0f));
                        }
                    }




                }
                Toast.makeText(MainActivity.this, emails, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Runners").addValueEventListener(GlobalVars.MainActivityCallBack);


    }

    // I wanna run dialog
    public void createDialog() {
        builder = new AlertDialog.Builder(this);
        final View popUpView = getLayoutInflater().inflate(R.layout.popupcancelrun, null);
        sureWantRun = (Button) popUpView.findViewById(R.id.SureSendBtn);
        cancel = (Button) popUpView.findViewById(R.id.CancelRunBtn);
        builder.setView(popUpView);
        dialog = builder.create();
        dialog.show();
        sureWantRun.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, IWantRunActivity.class);
            intent.putExtra("EMAIL", email);
            finish();
            FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.newMainActivityValueEvent);
            FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.MainActivityChildDeleted);
            GlobalVars.currentRunner.setRunningStatus(GlobalVars.StatusForChoosing);
            startActivity(intent);
            dialog.dismiss();

        });
        cancel.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Run canceled", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });


    }


    public void getCurrentUser(String email, GetCurrentUserInterface getCurrentUserInterface) {
        DatabaseReference currentUserRef;
        currentUserRef = FirebaseDatabase.getInstance().getReference();
        //
        currentUserRef.child("Runners").child(email.replace('@',' ').replace('.',' ')).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("MainActivityBug", "Error getting current user data ", task.getException());
                }
                else {
                    Log.d("MainActivityBug",task.getResult().toString());
                    DataSnapshot snapshot= task.getResult();
                    RunnersModelFull currentUserFromFB = new RunnersModelFull();
                    String id = "";

                    currentUserFromFB= snapshot.getValue(RunnersModelFull.class);
                    Log.d("GetCurrentUser", "before update   "+ GlobalVars.currentRunner);

                    //Update current user in global var, to be used by all activities
                    GlobalVars.currentRunner= currentUserFromFB;
                    //Check if he is a mannager of a team, and if, change the team's position
                    if(!GlobalVars.currentRunner.getActiveTeam().equals("No") && GlobalVars.currentRunner.getActiveTeam().contains(email.replace('@',' ').replace('.',' '))){
                        HashMap<String,Object> posUp= new HashMap<>();
                        posUp.put("/Teams/"+GlobalVars.currentRunner.getActiveTeam()+"/CurrentPosition",GlobalVars.currentRunner.getCurrentPosition());
                        FirebaseDatabase.getInstance().getReference().updateChildren(posUp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    }

                    Log.d("GetCurrentUser", "after update   "+ GlobalVars.currentRunner);

                    //TODO P2- The current user logo should be removed from this method. To be done once the system initiated the new activity
                    // Present current user logo on activity
                    setValueToViews(currentUserFromFB);
                    currentUserIsReady=true;
                    getCurrentUserInterface.onFinished();
                }

            }
        });


    }


    public void connectViewsToId() {
        image = (CircleImageView) findViewById(R.id.PictureWantRunActivity);
        name = (TextView) findViewById(R.id.NameWantRun);
        menuBtn = (ImageButton) findViewById(R.id.MenuBtnMain);
        iWantRunBtn = (ImageButton) findViewById(R.id.IWantRunBtnIwantRun);

        mapView = (MapView) findViewById(R.id.mapView);
        date = (TextView) findViewById(R.id.DateTextView);
        temp = (TextView) findViewById(R.id.TempTextView);
        tempPic = (ImageView) findViewById(R.id.TempPic);
        statusTV= findViewById(R.id.StatusTextView);
        music=findViewById(R.id.MusicImgBtn);

    }



    public void createDialogForLeaving(PopupMenu popupMenu){
        GoogleSignInClient gsc;
         AlertDialog.Builder builder;
         AlertDialog dialog;
         Button logOutSure,cancel;
        GoogleSignInOptions gso;
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);
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
                signOut(gsc);
                Intent sentHome= new Intent(MainActivity.this,LoginActivity.class);
                dialog.dismiss();
                popupMenu.dismiss();
                finish();
                startActivity(sentHome);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Sign Out canceled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    public void signOut(GoogleSignInClient gsc){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
    public void setValueToViews(RunnersModelFull model1) {
        //Set image

        Glide.with(getApplicationContext()).load(model1.getPicture()).placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                error(R.drawable.oneperson).into(image);
        name.setText(model1.getFirstSecondName());
        statusTV.setText(GlobalVars.returnHebrewStatus(model1.getRunningStatus()));

        //setMyPosition(model1.getCurrentPosition(), model1);
        currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        date.setText(currentDateandTime);
        TempReader tempReader = new TempReader(MainActivity.this);
        LatLng position= new LatLng(model1.getCurrentPosition().get("Latitude"),model1.getCurrentPosition().get("Longitude"));
        tempReader.getTemp(position);

        //TODO P2- in ALL relevant activities, the lower part of the screen to upper corners should be rounded, take from get taxi


    }


   /* public RunnersModelFull addToModel(DataSnapshot child, RunnersModelFull theModel) {

        theModel.setAvgDistance(child.child("AvgDistance").getValue(String.class));
        theModel.setAvgSpeed(child.child("AvgSpeed").getValue(Double.class));
        theModel.setBirthDate(child.child("BirthDate").getValue(String.class));
        float latitude = child.child("CurrentPosition").child(
                "Latitude").getValue(Float.class);

        float longitude = child.child("CurrentPosition").child("Longitude").getValue(Float.class);

        theModel.setCurrentPosition(new LatLng(latitude, longitude));
        theModel.setFirstSecondName(child.child("FirstSecondName").getValue(String.class));
        theModel.setPicture(child.child("Picture").getValue(String.class));

        return theModel;


    }*/

    private void initGoogleMap(Bundle savedInstanceState) {
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
    public void onMapReady(GoogleMap map) {
        //map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("marker"));
        GlobalVars.map = map;
        GlobalVars.map.setMyLocationEnabled(true);

   }

    public void setMyPosition(LatLng latLng, RunnersModelFull runnersModelFull) {
        GlobalVars.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

        if (GeocodingMethods.getAddress(latLng, this).equals("")) {

            if(GlobalVars.userAndMarker.contains(runnersModelFull.getID())&&!GlobalVars.userAndMarker.get(runnersModelFull.getID()).getPosition().equals(latLng)){
                GlobalVars.userAndMarker.get(runnersModelFull.getID()).setPosition(latLng);
                GlobalVars.userAndMarker.get(runnersModelFull.getID()).setTitle(runnersModelFull.getFirstSecondName() + " " + GeocodingMethods.getAddress(latLng, this));

                GlobalVars.userAndMarker.get(runnersModelFull.getID()).setIcon(bitmapDescriptorFromVector(MainActivity.this,R.drawable.map_pin_one));

            }
            else  if (!GlobalVars.userAndMarker.contains(runnersModelFull.getID())) {
                GlobalVars.userAndMarker.put(runnersModelFull.getID(),GlobalVars.map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude))
                        .title(runnersModelFull.getFirstSecondName() + " " + GeocodingMethods.getAddress(latLng, this)).icon(bitmapDescriptorFromVector(MainActivity.this,R.drawable.map_pin_one))));
            }

            GlobalVars.markerGlobal.remove();

        }
        else {
            if (runnersModelFull.getID() != null) {
                if (GlobalVars.userAndMarker.contains(runnersModelFull.getID())&&!GlobalVars.userAndMarker.get(runnersModelFull.getID()).getPosition().equals(latLng)) {

                    GlobalVars.userAndMarker.get(runnersModelFull.getID()).setPosition(latLng);
                    GlobalVars.userAndMarker.get(runnersModelFull.getID()).setTitle(runnersModelFull.getFirstSecondName() + " " + GeocodingMethods.getAddress(latLng, this));
                    //TAVOR-23/03/2023

                    GlobalVars.userAndMarker.get(runnersModelFull.getID()).setIcon(bitmapDescriptorFromVector(MainActivity.this,R.drawable.map_pin_one));
                }
                else if (!GlobalVars.userAndMarker.contains(runnersModelFull.getID())) {
                    GlobalVars.userAndMarker.put(runnersModelFull.getID(), GlobalVars.map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude))
                            .title(runnersModelFull.getFirstSecondName() + " " + GeocodingMethods.getAddress(latLng, this)).icon(bitmapDescriptorFromVector(MainActivity.this,R.drawable.map_pin_one))));
                }
                }

        }

        CoordinatesReader coordinatesReader = new CoordinatesReader(runnersModelFull, MainActivity.this);

        //coordinatesReader.returnMarkers(email);

    }


    public static void setPoints(MarkerOptions markerOptions,RunnersModelFull runnersModelFull) {

        //GlobalVars.markerAndMails.removeAll(toDel);
        Log.d("MARKEROPTIONS444",markerOptions.getTitle()+markerOptions.getPosition().toString());

        if(GlobalVars.userAndMarker.contains(runnersModelFull.getID())){
            GlobalVars.userAndMarker.get(runnersModelFull.getID()).setPosition(markerOptions.getPosition());
            GlobalVars.userAndMarker.get(runnersModelFull.getID()).setTitle(markerOptions.getTitle());
        }
        else {
            GlobalVars.userAndMarker.put(runnersModelFull.getID(),GlobalVars.map.addMarker(markerOptions));
        }


       // GlobalVars.markerGlobal.remove();
        MarkerAndMail m = new MarkerAndMail(markerOptions,runnersModelFull.getEmail());
        if(!GlobalVars.markerAndMails.contains(m)){
            GlobalVars.markerAndMails.add(m);
        }
      Log.d("MARKERSTACK",m.toString()+GlobalVars.markerAndMails.size());

    }


    public static void setWeather(String weather) {

        temp.setText(weather);
        if (Double.parseDouble(weather.replace("°C", "")) >= 20) {
            tempPic.setImageResource(R.drawable.high_temperature_icon);
        } else {
            tempPic.setImageResource(R.drawable.low_temperature_icon);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        //Ehud location 02/03/2023
       //startLocationUpdates();

    }
    @Override
    protected void onStop(){
        super.onStop();
        mapView.onStop();
        Intent intent = new Intent(this, DebugModeService.class);
        stopService(intent);
        //Ehud location 02/03/2023
        //stopLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        GlobalVars.map.clear();
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public boolean isHashMapContains(HashMap<String, Marker> hashMap, String key){
        for(String insideKey: hashMap.keySet()){
            if(insideKey.contains(key)){
                return true;
            }
        }
        return false;
    }
    public String  isHashMapContainsKey(HashMap<String, Marker> hashMap, String key){
        for(String insideKey: hashMap.keySet()){
            if(insideKey.contains(key)){
                return insideKey;
            }
        }
        return "";
    }
}

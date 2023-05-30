package com.example.runder.CommonFiles.ReadersAndNetworks;

import static android.content.Context.ACTIVITY_SERVICE;

import static com.example.runder.CommonFiles.GlobalVars.currentRunner;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.runder.Activities.AboutActivity;
import com.example.runder.Activities.ChoosingGroupActivity;
import com.example.runder.Activities.IWantRunActivity;
import com.example.runder.Activities.ManagingGroupActivity;
import com.example.runder.Activities.MyDetailsActivity;
import com.example.runder.Activities.RunderCommunityActivity;
import com.example.runder.Activities.SettingsActivity;
import com.example.runder.CommonFiles.CBS.InterfaceForName;
import com.example.runder.CommonFiles.CBS.PresentWhenManagerEnds;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.Models.ChoosingGroupModel;
import com.example.runder.Models.RunStatisticsModel;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.Objects;

import javax.microedition.khronos.opengles.GL;

public class FireBaseReader {

    Context context;

    public FireBaseReader(Context context) {
        this.context = context;
    }
    public void getCurrentUser(String email) {

        DatabaseReference postRef =
                FirebaseDatabase.getInstance().getReference().child("Runners");
        GlobalVars.myEvent= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                RunnersModelFull newM = new RunnersModelFull();
                DataSnapshot runnerSnap= null;
                String id = "";
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    id = newSnap.getKey();
                    runnerSnap=newSnap;
                }
                ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

                newM= runnerSnap.getValue(RunnersModelFull.class);
                Log.d("LATITUDE",newM.getCurrentPosition().toString());
                if(context.getClass()==SettingsActivity.class &&
                        taskInfo.get(0).topActivity.getClassName().equals(context.getClass().getName())){
                    SettingsActivity.setValueToViews(newM.getFirstSecondName(),newM.getPicture());
                }
                else if(context.getClass()== IWantRunActivity.class &&
                        taskInfo.get(0).topActivity.getClassName().equals(context.getClass().getName()) ){
                    IWantRunActivity.setValueToView(newM,context);
                }
                else if(context.getClass()== MyDetailsActivity.class &&
                        taskInfo.get(0).topActivity.getClassName().equals(context.getClass().getName()) ){
                    MyDetailsActivity.setValueToTheViews(newM);
                }
                else if(context.getClass()== RunderCommunityActivity.class  &&
                        taskInfo.get(0).topActivity.getClassName().equals(context.getClass().getName())){
                    RunderCommunityActivity.setValueToCurrentViews(newM);
                }
                else if(context.getClass()== ChoosingGroupActivity.class  &&
                        taskInfo.get(0).topActivity.getClassName().equals(context.getClass().getName())){
                    ChoosingGroupActivity.setValueToView(newM);
                }
                else if(context.getClass()== ManagingGroupActivity.class  &&
                        taskInfo.get(0).topActivity.getClassName().equals(context.getClass().getName())){
                    ManagingGroupActivity.setValueToView(newM);
                }
                else if(context.getClass()== AboutActivity.class  &&
                        taskInfo.get(0).topActivity.getClassName().equals(context.getClass().getName())){
                    AboutActivity.setValueToView(newM);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        postRef.orderByChild("Email").equalTo(email)

                .addValueEventListener(GlobalVars.myEvent);
        //TODO 1 Changing the way that the model is returning - because i'm receiving nulls and nulls

    }
    public void addGroupToUser(String email, String name, ReaderCallBack readerCallBack){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Runners");
        ref.orderByChild("Email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull newM = new RunnersModelFull();
                DataSnapshot runnerSnap=null;
                String id = "";
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    id = newSnap.getKey();
                    runnerSnap=newSnap;

                }

                newM=runnerSnap.getValue(RunnersModelFull.class);
                //Checking if he has a team
                if(!newM.getActiveTeam().equals("No")){
                    Toast.makeText(context, "The user is in a team, he can't join another",
                            Toast.LENGTH_SHORT).show();
                    //Not supposed to be there because he will be moved to the managing group activity, it's there for to be check
                }
                else {
                    //addUserAndGroup(newM.getID(),name, readerCallBack);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateGroupPosition(String managerId){

    }
    public void updateGroupToFireBase(String  dis, String age,double speed,ReaderCallBack readerCallBack){
        DatabaseReference postRef =
                FirebaseDatabase.getInstance().getReference().child("Runners");
        postRef.orderByChild("Email").equalTo(currentRunner.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull newM = new RunnersModelFull();
                DataSnapshot runnerSnap=null;
                String id = "";
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    id = newSnap.getKey();
                    runnerSnap=newSnap;

                }

                newM= runnerSnap.getValue(RunnersModelFull.class);
                //Setting the values to the Firebase
                Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy+HH:mm");
                String currentDateTimeString = dateFormat.format(currentTime);
                DateFormat hour= new SimpleDateFormat("HH:mm:ss");
                // saving the id to sharedPrefferences
                // TODO change the way that we pass the ID instead of SharedPreferences
                SharedPreferences sharedpreferences = context.getSharedPreferences("ProjectPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedpreferences.edit();
                String teamID = newM.getEmail().replace('@',' ').replace('.',' ').concat(" -").concat(currentDateTimeString);
                HashMap<String ,String> runners= new HashMap<>();
                runners.put("Manager",newM.getEmail().replace('@',' ').replace('.',' '));
                editor.putString("IDGROUP",teamID);
                editor.apply();
                //Building an object
                ChoosingGroupModel uploadedModel= new ChoosingGroupModel(teamID, currentRunner.getFirstSecondName(), currentRunner.getCurrentPosition(),
                        runners,dis,speed,"בבנייה","",age,currentRunner.getPicture(),hour.format(currentTime),"0");
                HashMap<String,Object> toUp= new HashMap<>();
                toUp.put("/Teams/"+teamID,uploadedModel.toMap());
                toUp.put("/Runners/"+newM.getEmail().replace('@',' ').replace('.',' ')+"/ActiveTeam",teamID);
                FirebaseDatabase.getInstance().getReference().updateChildren(toUp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GlobalVars.currentGroup= uploadedModel;
                        readerCallBack.callBackAfterRead(uploadedModel.getGroupID());
                    }
                });
                /*FirebaseDatabase.getInstance().getReference().child("Teams").child(teamID).setValue(uploadedModel.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {



                    }
                });*/




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void matchUserAndGroup(RunnersModelFull user, ChoosingGroupModel groupModel, ReaderCallBack readerCallBack){
        //Build new HashMap
        HashMap<String ,Object> uploader= new HashMap<>();
        HashMap<String ,String > current= new HashMap<>();
        String emailToEqual= user.getEmail().replace('@',' ').replace('.',' ');
        current.put(user.getFirstSecondName(),emailToEqual);
        uploader.put("/Runners/"+emailToEqual+"/ActiveTeam",groupModel.getGroupID());
        uploader.put("/Teams/"+groupModel.getGroupID()+"/RunnersIds/"+user.getFirstSecondName(),emailToEqual);
        FirebaseDatabase.getInstance().getReference().updateChildren(uploader).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                GlobalVars.currentGroup= groupModel;
                readerCallBack.callBackAfterRead(groupModel.getGroupID());


            }
        });

    }





/*
    public void addGroupToFireBase(String email,String maxDistance,String maxSpeed,int age,ReaderCallBack readerCallBack){
        DatabaseReference postRef =
                FirebaseDatabase.getInstance().getReference().child("Runners");
        postRef.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull newM = new RunnersModelFull();
                DataSnapshot runnerSnap=null;
                String id = "";
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    id = newSnap.getKey();
                    runnerSnap=newSnap;

                }

                newM= runnerSnap.getValue(RunnersModelFull.class);
                //Setting the values to the Firebase
                Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy+HH:mm");
                String currentDateTimeString = dateFormat.format(currentTime);
                Map<String,Object> addGroup= new HashMap<>();

                // saving the id to sharedPrefferences
                // TODO change the way that we pass the ID instead of SharedPreferences
                SharedPreferences sharedpreferences = context.getSharedPreferences("ProjectPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedpreferences.edit();
                String teamID = newM.getID().concat(" -").concat(currentDateTimeString);
                long startTime = System.currentTimeMillis();
                editor.putString("IDGROUP",teamID);
                if(editor.commit()){
                    Log.d("SharedPref","Succedded");
                    readerCallBack.callBackAfterRead();
                }
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                Log.d("SharedPrefs34", "Method took: " + elapsedTime + " milliseconds");

                //Building new Model
                ChoosingGroupModel groupModel= new ChoosingGroupModel();






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    public RunnersModelFull addToModel(DataSnapshot child, RunnersModelFull theModel) {

        theModel.setAvgDistance(child.child("AvgDistance").getValue(String.class));
        theModel.setAvgSpeed(child.child("AvgSpeed").getValue(Double.class));
        theModel.setBirthDate(child.child("Birthdate").getValue(String.class));
        float latitude = child.child("CurrentPosition").child(
                "Latitude").getValue(Float.class);

        float longitude = child.child("CurrentPosition").child("Longitude").getValue(Float.class);

        //theModel.setCurrentPosition(new LatLng(latitude, longitude));
        theModel.setFirstSecondName(child.child("FirstAndSecondName").getValue(String.class));
        theModel.setPicture(child.child("Picture").getValue(String.class));
        theModel.setGender(child.child("Gender").getValue(String.class));
        theModel.setID(child.child("ID").getValue(String.class));
        theModel.setActiveTeam(child.child("ActiveTeam").getValue(String.class));

        return theModel;


    }
    public  void setTextForButton(String email){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Runners");
        ref.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull newM = new RunnersModelFull();
                DataSnapshot runnerSnap=null;
                String key = "";
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    key = newSnap.getKey();
                    runnerSnap=newSnap;
                }
                newM= runnerSnap.getValue(RunnersModelFull.class);
                String teamID= newM.getActiveTeam();
                String userID= key;
                if(teamID.contains(userID)){
                    ManagingGroupActivity.setButtonText(true);

                }
                else {
                    ManagingGroupActivity.setButtonText(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /* private void addUserAndGroup(String userID, String groupName, ReaderCallBack readerCallBack){
         DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Teams");
         ref.orderByChild("Name").equalTo(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 String key = "";
                 for (DataSnapshot newSnap : snapshot.getChildren()) {
                     key = newSnap.getKey();

                 }
                 Map<String,Object> map1= new HashMap<>();
                 map1.put(String.valueOf(userID.charAt(0)+userID.charAt(1)),userID);
                 String finalKey = key;
                 FirebaseDatabase.getInstance().getReference().child("Teams").child(key).child("Runners").push().setValue(map1).addOnCompleteListener(task -> {
                     Toast.makeText(context, "the user joined to "+groupName, Toast.LENGTH_SHORT).show();
                     FirebaseDatabase.getInstance().getReference().child("Teams").child(finalKey).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot1) {
                             String id= snapshot1.getValue(String.class);
                             SharedPreferences sharedpreferences =( (Activity) context ).getSharedPreferences("ProjectPref",Context.MODE_MULTI_PROCESS);
                             SharedPreferences.Editor editor= sharedpreferences.edit();
                             editor.putString("IDGROUP",id);
                             boolean success = editor.commit();
                             if (success) {
                                 Log.d("SharedPrefs", "Value saved successfully");
                                 readerCallBack.callBackAfterRead();
                             } else {
                                 Log.d("SharedPrefs", "Failed to save value");
                             }
                             FirebaseDatabase.getInstance().getReference().child("Runners").orderByChild("ID").equalTo(userID).addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                     String key1 = "";
                                     for (DataSnapshot newSnap : snapshot1.getChildren()) {
                                         key1 = newSnap.getKey();

                                     }
                                     Log.d("NULLEX",id);

                                     FirebaseDatabase.getInstance().getReference().child("Runners").child(key1).child("ActiveTeam").setValue(id).addOnCompleteListener(task1 -> {

                                     }).addOnFailureListener(e -> Toast.makeText(context, "MovingError", Toast.LENGTH_SHORT).show());

                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError error) {

                                 }
                             });

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
                 });

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
     }
 */
    // TODO recheck this- because we want to rewrite the code that uploads the position into the firebase
    public void updatePosition(LatLng latLng, String email,PresentAfterRecivingCB CB) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Runners");
        ref.orderByChild("Email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull newM = new RunnersModelFull();
                String key = "";
                DataSnapshot runnerSnap=null;
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    key = newSnap.getKey();
                    runnerSnap=newSnap;
                }
                newM= runnerSnap.getValue(RunnersModelFull.class);
                Location location = new Location("A");
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                Location loc2 = new Location("B");
                loc2.setLatitude(newM.getCurrentPosition().get("Latitude"));
                loc2.setLongitude(newM.getCurrentPosition().get("Longitude"));
                if(location.distanceTo(loc2)>50){
                    CB.PresentAfterReciving(newM,latLng);
                    FirebaseDatabase.getInstance().getReference().child("Runners").child(key).
                            child("CurrentPosition").child("Latitude").setValue(latLng.latitude).addOnCompleteListener(task -> {

                            });
                    FirebaseDatabase.getInstance().getReference().child("Runners").child(key).
                            child("CurrentPosition").child("Longitude").setValue(latLng.longitude).addOnCompleteListener(task -> {

                            });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setTeamStatus(String teamStatus,String teamID,String email, ShowTimeCB cb){
        if( !teamStatus.equals("יציאה")){
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Teams");
            reference.orderByChild("ID").equalTo(teamID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String key = "";
                    for (DataSnapshot newSnap : snapshot.getChildren()) {
                        key = newSnap.getKey();
                    }
                    FirebaseDatabase.getInstance().getReference().child("Teams").child(teamID).
                            child("Status").setValue(teamStatus).addOnCompleteListener(task -> {
                                FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(email)).child("Status").setValue(teamStatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if(teamStatus.equals("רצים")){
                                            if(cb !=null){
                                                cb.onFinishedUpload();
                                            }


                                        }
                                    }
                                });

                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Runners");
            ref.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String key = "";
                    for (DataSnapshot newSnap : snapshot.getChildren()) {
                        key = newSnap.getKey();
                    }
                    FirebaseDatabase.getInstance().getReference().child("Runners").child(key).
                            child("ActiveTeam").setValue("No").addOnCompleteListener(task -> {

                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
    // sent by ChoosingGroupActivity
    public void hasTeam(String email, HasCallBack callBack ){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Runners");
        reference.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull newM = new RunnersModelFull();
                String key = "";
                DataSnapshot runnerSnap=null;
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    key = newSnap.getKey();
                    runnerSnap= newSnap;
                }
                newM = runnerSnap.getValue(RunnersModelFull.class);
                if(!newM.getActiveTeam().equals("No")){
                    callBack.moveAfterSuccessCallBack(newM.getActiveTeam());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public String  returnName(DataSnapshot child) {
        return(child.child("Name").getValue(String.class));

    }

    // TODO CHECK THIS- DIDN'T WORK WHEN I WORKED ON IT
    public void setTimeForRun(String teamID){
        GlobalVars.calendar= Calendar.getInstance();
        Date ct= new Date();
        GlobalVars.dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String fD= GlobalVars.dateFormat.format(ct);
        try {
            GlobalVars.date= GlobalVars.dateFormat.parse(fD);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Teams");
        reference.orderByChild("ID").equalTo(teamID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "";
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    key = newSnap.getKey();
                }
                Calendar calendarN= Calendar.getInstance();
                String fDn= GlobalVars.dateFormat.format(new Date());
                Date date = new Date();
                try {
                    date= GlobalVars.dateFormat.parse(fDn);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long runTime=(date.getTime()-GlobalVars.date.getTime())/1000;

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Teams");
                if(ref.child(key).child("Status").equals("רצים") && ((runTime-GlobalVars.prevTime)/1000)==10){
                    ref.child(key).child("RunningTime").setValue(runTime).
                            addOnSuccessListener(unused -> GlobalVars.prevTime= runTime).
                            addOnFailureListener(e -> Log.d("FAILURE535",e.toString()));
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void setRunningStatus(String runningStatus,String email){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String ,Object> toUpdate = new HashMap<>();
        String emailToEqual= email.replace('@',' ').replace('.',' ');
        toUpdate.put("/Runners/"+emailToEqual+"/RunningStatus",runningStatus);
        try{
            reference.updateChildren(toUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if(GlobalVars.isInDebug){
                                Toast.makeText(context, "comp "+runningStatus, Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

        }
        catch (Exception exception){
            exception.printStackTrace();

        }
    }

    public void mainActivityOnDataChanged(){
        GlobalVars.newMainActivityValueEvent= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot runnersSnapShot: snapshot.getChildren()) {
                    Log.d("RunnersFB", runnersSnapShot.toString());
                    //   RunnersModelFull runner= addToModel(runnersSnapShot,new RunnersModelFull());
                    RunnersModelFull runner = runnersSnapShot.getValue(RunnersModelFull.class);
                    if(runner.getRunningStatus().equals(GlobalVars.StatusForNotRunning)) {
                        //TODO TODAY 28/03 Managing the Main Activity MAP

                        if (runner.getEmail().equals(currentRunner.getEmail())) {
                            // TODO TODAY 28/03 Manage the map
                            if (!GlobalVars.mainActivityMarkers.containsKey(currentRunner.getEmail())) {
                                LatLng position = new LatLng((Double) runner.getCurrentPosition().get("Latitude"), (Double) runner.getCurrentPosition().get("Longitude"));
                                String address = GeocodingMethods.getAddress(position, context);
                                Bitmap markerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.runnermenlogoonmap);
                                if(GlobalVars.currentRunner.getGender().equals("נקבה")){
                                    markerIcon= BitmapFactory.decodeResource(context.getResources(),R.drawable.woman_current_runner);
                                }
                                int width = 300;
                                int height = 300;
                                Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                                GlobalVars.mainActivityMarkers.put(runner.getEmail(), GlobalVars.map.addMarker(new MarkerOptions().position(position).snippet(address).title(runner.getFirstSecondName())
                                        .icon(bitmapDescriptor)));
                                GlobalVars.map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17.0f));
                            } else {
                                LatLng position = new LatLng((Double) runner.getCurrentPosition().get("Latitude"), (Double) runner.getCurrentPosition().get("Longitude"));
                                String address = GeocodingMethods.getAddress(position, context);
                                GlobalVars.mainActivityMarkers.get(runner.getEmail()).setPosition(position);
                                GlobalVars.mainActivityMarkers.get(runner.getEmail()).setSnippet(address);
                                GlobalVars.map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17.0f));
                            }

                        }
                        else {

                            if (!GlobalVars.mainActivityMarkers.containsKey(runner.getEmail())) {
                                LatLng position = new LatLng((Double) runner.getCurrentPosition().get("Latitude"), (Double) runner.getCurrentPosition().get("Longitude"));
                                String address = GeocodingMethods.getAddress(position, context);
                                Bitmap markerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.person_yellow);
                                if (runner.getGender().equals("נקבה")) {
                                    markerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.woman_yellow);
                                }
                                int width = 120;
                                int height = 120;
                                Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                                GlobalVars.mainActivityMarkers.put(runner.getEmail(), GlobalVars.map.addMarker(new MarkerOptions().position(position).snippet(address).title(runner.getFirstSecondName())
                                        .icon(bitmapDescriptor)));
                            } else {
                                LatLng position = new LatLng((Double) runner.getCurrentPosition().get("Latitude"), (Double) runner.getCurrentPosition().get("Longitude"));
                                String address = GeocodingMethods.getAddress(position, context);
                                if(!GlobalVars.mainActivityMarkers.get(runner.getEmail()).isVisible()){
                                    GlobalVars.mainActivityMarkers.get(runner.getEmail()).setVisible(true);

                                }
                                Bitmap markerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.person_yellow);
                                if (runner.getGender().equals("נקבה")) {
                                    markerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.woman_yellow);
                                }
                                int width = 120;
                                int height = 120;
                                Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                                GlobalVars.mainActivityMarkers.get(runner.getEmail()).setIcon(bitmapDescriptor);
                                GlobalVars.mainActivityMarkers.get(runner.getEmail()).setPosition(position);
                                GlobalVars.mainActivityMarkers.get(runner.getEmail()).setSnippet(address);
                            }
                          /*  Calendar CalenderNow = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            Date time = null;
                            try {
                                time = format.parse(runner.getLocationLastUpdateHour());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Calendar calenderFromFB = Calendar.getInstance();
                            calenderFromFB.setTime(time);
                            if (((CalenderNow.getTimeInMillis() - calenderFromFB.getTimeInMillis()) / 1000) / 60 >= 15) {
                                if (GlobalVars.mainActivityMarkers.containsKey(runner.getEmail())) {
                                    // TODO TODAY/TOMORROW 28-29/03 Manage the HashMap and the map
                                    Marker marker = GlobalVars.mainActivityMarkers.get(runner.getEmail());
                                    GlobalVars.mainActivityMarkers.get(runner.getEmail()).remove();
                                    GlobalVars.mainActivityMarkers.remove(marker);
                                    //STOP THIS RUNNING OF THE FOR LOOP

                                }




                            }
                            else {

                            }*/
                        }
                    }
                    if(runner.getRunningStatus().equals(GlobalVars.StatusForChoosing) || runner.getRunningStatus().equals(GlobalVars.StatusForChoosing)){
                        //TODO TODAY 28/03 Manage the map
                        if(!GlobalVars.mainActivityMarkers.containsKey(runner.getEmail())){
                            LatLng position= new LatLng((Double) runner.getCurrentPosition().get("Latitude"),(Double)runner.getCurrentPosition().get("Longitude"));
                            String address= GeocodingMethods.getAddress(position,context);
                            Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.person_yellow);
                            if(runner.getGender().equals("נקבה")){
                                markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.woman_yellow);
                            }
                            int width = 120;
                            int height = 120;
                            Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                            GlobalVars.mainActivityMarkers.put(runner.getEmail(), GlobalVars.map.addMarker(new MarkerOptions().position(position).snippet(address).title(runner.getFirstSecondName())
                                    .icon(bitmapDescriptor)));
                            // TODO TODAY 28/03 Add the runner to the map
                            //TODO TODAY ALSO UPDATE MARKER POSITION

                        }
                        else {
                            LatLng position= new LatLng((Double) runner.getCurrentPosition().get("Latitude"),(Double)runner.getCurrentPosition().get("Longitude"));
                            String address= GeocodingMethods.getAddress(position,context);
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setPosition(position);
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setSnippet(address);
                        }
                    }
                    else if(runner.getRunningStatus().equals(GlobalVars.StatusForManager)){
                        if(!GlobalVars.mainActivityMarkers.containsKey(runner.getEmail())){
                            // TODO TODAY add the marker to the map
                            LatLng position= new LatLng((Double) runner.getCurrentPosition().get("Latitude"),(Double)runner.getCurrentPosition().get("Longitude"));
                            String address= GeocodingMethods.getAddress(position,context);
                            Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.group_purple);

                            int width = 120;
                            int height = 120;
                            Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                            GlobalVars.mainActivityMarkers.put(runner.getEmail(), GlobalVars.map.addMarker(new MarkerOptions().position(position).snippet(address).title(runner.getFirstSecondName()+"'s Team")
                                    .icon(bitmapDescriptor)));
                            // TODO TODAY 28/03 Add the runner to the map
                            //TODO TODAY ALSO UPDATE MARKER POSITION

                        }
                        else {
                            LatLng position= new LatLng((Double) runner.getCurrentPosition().get("Latitude"),(Double)runner.getCurrentPosition().get("Longitude"));
                            String address= GeocodingMethods.getAddress(position,context);
                            Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.group_purple);

                            int width = 120;
                            int height = 120;
                            Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setIcon(bitmapDescriptor);
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setPosition(position);
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setSnippet(address);
                        }
                        // TODO TODAY Update the marker and set set the marker icon
                    }
                    else if(runner.getRunningStatus().equals(GlobalVars.StatusForMember)){
                        // TODO TODAY Delete it from the map
                        if(GlobalVars.mainActivityMarkers.get(runner.getEmail()) !=null){
                            Marker marker= GlobalVars.mainActivityMarkers.get(runner.getEmail());
                            GlobalVars.mainActivityMarkers.get(runner.getEmail()).setVisible(false);
                        }



                    }
                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Runners").addValueEventListener(GlobalVars.newMainActivityValueEvent);
        GlobalVars.MainActivityChildDeleted= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                RunnersModelFull delChild= snapshot.getValue(RunnersModelFull.class);
                if(GlobalVars.mainActivityMarkers.containsKey(delChild.getEmail())){
                    GlobalVars.mainActivityMarkers.get(delChild.getEmail()).setVisible(false);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Runners").addChildEventListener(GlobalVars.MainActivityChildDeleted);



    }
    public void choosingGroupActivityEventListener(String MaxD,int AvgSpeed,String AgeRange){
        GlobalVars.choosingGroupActivityValueEvent= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot runnersSnapShot: snapshot.getChildren()) {
                    Log.d("RunnersFB", runnersSnapShot.toString());
                    //   RunnersModelFull runner= addToModel(runnersSnapShot,new RunnersModelFull());
                    RunnersModelFull runner = runnersSnapShot.getValue(RunnersModelFull.class);
                    if(runner.getRunningStatus().equals(GlobalVars.StatusForManager)){
                        FirebaseDatabase.getInstance().getReference().child("Teams").child(runner.getActiveTeam()).get().addOnCompleteListener(task -> {
                            if(task.isSuccessful() && task.getResult().getValue(ChoosingGroupModel.class)!=null){
                                ChoosingGroupModel modelGroup= task.getResult().getValue(ChoosingGroupModel.class);
                                boolean checker= modelGroup.getAgeRange().equals(AgeRange)&& modelGroup.getRunningDistance().equals(MaxD) && AvgSpeed== modelGroup.getRunningSpeed();
                                if(checker){
                                    if(!GlobalVars.choosingGroupActivityMarkers.containsKey(runner.getEmail())){

                                        LatLng position= new LatLng((Double) runner.getCurrentPosition().get("Latitude"),(Double)runner.getCurrentPosition().get("Longitude"));
                                        String address= GeocodingMethods.getAddress(position,context);
                                        Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.group_purple);

                                        int width = 120;
                                        int height = 120;
                                        Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                                        GlobalVars.choosingGroupActivityMarkers.put(runner.getEmail(), GlobalVars.mapForChoosing.addMarker(new MarkerOptions().position(position).snippet(address).title(runner.getFirstSecondName()+"'s Team")
                                                .icon(bitmapDescriptor)));
                                        LatLng positionNew= new LatLng((Double) currentRunner.getCurrentPosition().get("Latitude"),(Double)currentRunner.getCurrentPosition().get("Longitude"));
                                        GlobalVars.mapForChoosing.animateCamera(CameraUpdateFactory.newLatLngZoom(positionNew, 17.0f));
                                        GlobalVars.choosingGroupActivityMarkers.get(runner.getEmail()).setVisible(true);
                                    }
                                    else {
                                        LatLng position= new LatLng((Double) runner.getCurrentPosition().get("Latitude"),(Double)runner.getCurrentPosition().get("Longitude"));
                                        String address= GeocodingMethods.getAddress(position,context);
                                        GlobalVars.choosingGroupActivityMarkers.get(runner.getEmail()).setPosition(position);
                                        GlobalVars.choosingGroupActivityMarkers.get(runner.getEmail()).setSnippet(address);
                                        GlobalVars.choosingGroupActivityMarkers.get(runner.getEmail()).setVisible(true);
                                    }
                                }
                            }
                        });


                    }
                    else if(!runner.getRunningStatus().equals(GlobalVars.StatusForManager) && GlobalVars.choosingGroupActivityMarkers.containsKey(runner.getEmail())){
                        GlobalVars.choosingGroupActivityMarkers.get(runner.getEmail()).setVisible(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Runners").addValueEventListener(GlobalVars.choosingGroupActivityValueEvent);

    }
    public void managingGroupActivityEventListenerForManager(){
        GlobalVars.ManagingGroupActivityEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull modelFull= snapshot.getValue(RunnersModelFull.class);
                if(modelFull.getRunningStatus().equals(GlobalVars.StatusForManager)){
                    // Update the group status in firebase
                    LatLng position= new LatLng((Double) modelFull.getCurrentPosition().get("Latitude"),(Double)modelFull.getCurrentPosition().get("Longitude"));
                    if(GlobalVars.groupMarker==null){
                        LatLng position2= new LatLng((Double) modelFull.getCurrentPosition().get("Latitude"),(Double)modelFull.getCurrentPosition().get("Longitude"));
                        String address= GeocodingMethods.getAddress(position,context);
                        Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.group_purple);
                        int width = 120;
                        int height = 120;
                        Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);

                        GlobalVars.groupMarker= GlobalVars.mapForManaging.addMarker(new MarkerOptions().position(position).snippet(address).title(modelFull.getFirstSecondName()+"'s Team")
                                .icon(bitmapDescriptor));
                        GlobalVars.oldMarker=GlobalVars.groupMarker;
                        LatLng positionNew= new LatLng((Double) currentRunner.getCurrentPosition().get("Latitude"),(Double)currentRunner.getCurrentPosition().get("Longitude"));
                        GlobalVars.mapForManaging.animateCamera(CameraUpdateFactory.newLatLngZoom(positionNew, 17.0f));
                    }
                    else {
                        LatLng position2= new LatLng((Double) modelFull.getCurrentPosition().get("Latitude"),(Double)modelFull.getCurrentPosition().get("Longitude"));
                        String address= GeocodingMethods.getAddress(position,context);
                        GlobalVars.oldMarker=GlobalVars.groupMarker;
                        GlobalVars.groupMarker.setPosition(position);
                        GlobalVars.groupMarker.setSnippet(address);


                    }
                    if(GlobalVars.oldMarker.getPosition()!=position){
                        FirebaseDatabase.getInstance().getReference().child("Teams").child(modelFull.getActiveTeam()).get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                ChoosingGroupModel groupModel= task.getResult().getValue(ChoosingGroupModel.class);
                                if(groupModel!=null){
                                    if(groupModel.getStatus().equals("רצים")){
                                        HashMap<String ,Object> uploader= new HashMap<>();
                                        uploader.put("/Teams/"+modelFull.getActiveTeam()+"/CurrentPosition",groupModel.getCurrentPosition());
                                        Date currentTime = Calendar.getInstance().getTime();
                                        DateFormat hour= new SimpleDateFormat("HH:mm:ss");
                                        double newDistance;
                                        LatLng position2= new LatLng((Double) modelFull.getCurrentPosition().get("Latitude"),(Double)modelFull.getCurrentPosition().get("Longitude"));
                                        LatLng oldPos= GlobalVars.oldMarker.getPosition();

                                        uploader.put("/Teams/"+modelFull.getActiveTeam()+"/Distance",GlobalVars.TotalDistance);
                                        uploader.put("/Teams/"+modelFull.getActiveTeam()+"/Hour",hour.format(currentTime));
                                        Date fromFB= null;
                                        try {
                                            fromFB= hour.parse(groupModel.getHour());
                                            fromFB.setDate(currentTime.getDate());
                                            fromFB.setMonth(currentTime.getMonth());
                                            fromFB.setYear(currentTime.getYear());


                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        float durationInMillis = (currentTime.getTime() - fromFB.getTime())/(1000f*60f);
                                        //uploader.put("/Teams/"+modelFull.getActiveTeam()+"/ElapsedTime",GlobalVars.passedTime);
                                        DecimalFormat df = new DecimalFormat("#.###");
                                        // GlobalVars.runTime.setText("זמן: "+df.format(newElapsed) +" דקות ");
                                        //GlobalVars.runDistance.setText("מרחק: "+ df.format(newDistance)+" ק'מ ");
                                        FirebaseDatabase.getInstance().getReference().updateChildren(uploader).addOnCompleteListener(task1 -> {
                                            if(GlobalVars.polylineForManaging!= null){
                                                GlobalVars.polyLineOptionsGlobal.color(R.color.yellow);
                                                GlobalVars.polyLineOptionsGlobal.add(position2);
                                                GlobalVars.polyLineOptionsGlobal.width(20);
                                                GlobalVars.polyLineOptionsGlobal.jointType(JointType.ROUND);
                                                GlobalVars.polylineForManaging.remove();
                                                GlobalVars.polylineForManaging= GlobalVars.mapForManaging.addPolyline(GlobalVars.polyLineOptionsGlobal);
                                            }
                                            else {
                                                if(GlobalVars.statusGroup.equals("רצים")){
                                                    GlobalVars.polyLineOptionsGlobal.add(position2);
                                                    GlobalVars.polylineForManaging= GlobalVars.mapForManaging.addPolyline(GlobalVars.polyLineOptionsGlobal);
                                                }

                                            }



                                        });
                                        //Update the route

                                    }
                                }






                            }
                        });





                    }



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Runners").child(currentRunner.getEmail().replace('@',' ').replace('.',' ')).addValueEventListener(GlobalVars.ManagingGroupActivityEventListener);

    }
    public void waitForStartTime(String group,ShowTimeCB cb){
        FirebaseDatabase.getInstance().getReference().child("Teams").child(group).child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);
                if(status.equals("רצים")){
                    GlobalVars.statusGroup= status;
                    cb.onRunStarted();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void memberActionsWhenManagerLeaves(PresentWhenManagerEnds presentWhenManagerEnds){
        FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).child("Status").equalTo("סוגרים").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class)== null){
                    FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).get().addOnSuccessListener(snapshot1 -> {
                        RunStatisticsModel statsModel= snapshot1.getValue(RunStatisticsModel.class);
                        statsModel.setMyDistance(String.valueOf(GlobalVars.TotalDistance));
                        statsModel.setMyTime(GlobalVars.passedTime);
                        statsModel.setNewElapsedTIme();
                        statsModel.setSpeeds();
                        statsModel.roundNumbers(3);
                        presentWhenManagerEnds.present(statsModel);
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void managingGroupActivityEventListenerForMember(PresentWhenManagerEnds presentWhenManagerEnds){
        String ManagerId= GlobalVars.currentGroup.getRunnersIds().get("Manager");
        GlobalVars.ManagingGroupActivityEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RunnersModelFull managerModel= snapshot.getValue(RunnersModelFull.class);
                FirebaseDatabase.getInstance().getReference().child("Teams").child(managerModel.getActiveTeam()).child("Status").get().addOnSuccessListener(snapshot12 -> GlobalVars.statusGroup= snapshot12.getValue(String.class));

                if(managerModel.getStatus().equals("סוגרים")){
                    FirebaseDatabase.getInstance().getReference().child("Teams").child(GlobalVars.currentGroup.getGroupID()).get().addOnSuccessListener(snapshot1 -> {
                        RunStatisticsModel statsModel= snapshot1.getValue(RunStatisticsModel.class);
                        statsModel.setMyDistance(String.valueOf(GlobalVars.TotalDistance));
                        statsModel.setMyTime(GlobalVars.passedTime);
                        statsModel.setNewElapsedTIme();
                        statsModel.setSpeeds();
                        statsModel.roundNumbers(3);
                        presentWhenManagerEnds.present(statsModel);
                    });
                }
                else
                if(GlobalVars.groupMarker==null){
                    LatLng position= new LatLng((Double) managerModel.getCurrentPosition().get("Latitude"),(Double)managerModel.getCurrentPosition().get("Longitude"));
                    String address= GeocodingMethods.getAddress(position,context);
                    Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.group_purple);
                    int width = 120;
                    int height = 120;
                    Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                    GlobalVars.groupMarker= GlobalVars.mapForManaging.addMarker(new MarkerOptions().position(position).snippet(address).title(managerModel.getFirstSecondName()+"'s Team")
                            .icon(bitmapDescriptor));
                    LatLng positionNew= new LatLng((Double) currentRunner.getCurrentPosition().get("Latitude"),(Double)currentRunner.getCurrentPosition().get("Longitude"));
                    GlobalVars.mapForManaging.animateCamera(CameraUpdateFactory.newLatLngZoom(positionNew, 17.0f));
                    if(GlobalVars.statusGroup.equals("רצים")){
                        GlobalVars.polyLineOptionsGlobal.add(position);
                        if(GlobalVars.polylineForManaging !=null){
                            GlobalVars.polylineForManaging.remove();
                        }

                        GlobalVars.polylineForManaging= GlobalVars.mapForManaging.addPolyline(GlobalVars.polyLineOptionsGlobal);
                    }

                }
                else {
                    LatLng position= new LatLng((Double) managerModel.getCurrentPosition().get("Latitude"),(Double)managerModel.getCurrentPosition().get("Longitude"));
                    String address= GeocodingMethods.getAddress(position,context);
                    GlobalVars.groupMarker.setPosition(position);
                    GlobalVars.groupMarker.setSnippet(address);
                    if(GlobalVars.statusGroup.equals("רצים")){
                        GlobalVars.polyLineOptionsGlobal.add(position);
                        if(GlobalVars.polylineForManaging !=null){
                            GlobalVars.polylineForManaging.remove();
                        }
                            GlobalVars.polylineForManaging= GlobalVars.mapForManaging.addPolyline(GlobalVars.polyLineOptionsGlobal);


                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Runners").child(ManagerId).addValueEventListener(GlobalVars.ManagingGroupActivityEventListener);
    }
    /*public void isGroups(String  max_D,
                         int max_S, String  age){
        GlobalVars.isHave= false;
        GlobalVars.numOfActive=0;
        GlobalVars.numTotal=0;
        GlobalVars.isGroupEvent= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot newSnap: snapshot.getChildren()){
                    ChoosingGroupModel model= newSnap.getValue(ChoosingGroupModel.class);
                    boolean ageEqual= Objects.equals(age, model.getAgeRange());
                    boolean distanceEqual= Objects.equals(max_D, model.getRunningDistance());
                    boolean condToCheck=  ageEqual && Double.compare(model.getRunningSpeed(),Double.valueOf(max_S))== 0 && distanceEqual;
                    GlobalVars.isHave= GlobalVars.isHave|| condToCheck;
                    if(condToCheck){
                        GlobalVars.numOfActive++;
                    }
                    GlobalVars.numTotal++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("Teams").addValueEventListener(GlobalVars.isGroupEvent);

    }*/

    public void checkAndRemoveUserFromTeam(String email){
        FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(email)).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                RunnersModelFull modelRunner= task.getResult().getValue(RunnersModelFull.class);
                if(!modelRunner.getActiveTeam().equals("No")){
                    //Check if he is a member
                    if(!modelRunner.getActiveTeam().contains(GlobalVars.returnId(email))){
                        FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(email)).child("ActiveTeam").setValue("No").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Remove from team JSON
                                FirebaseDatabase.getInstance().getReference().child("Teams").child(modelRunner.getActiveTeam()).child("RunnersIds").child(modelRunner.getFirstSecondName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                    }
                                });
                            }
                        });
                    }
                    else {
                        FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(email)).child("ActiveTeam").setValue("No").addOnSuccessListener(unused -> {
                            //Delete the team
                            FirebaseDatabase.getInstance().getReference().child("Teams").child(modelRunner.getActiveTeam()).removeValue().addOnSuccessListener(unused1 -> {


                            });
                        });
                    }
                }
            }
        });
    }
    public void presentName(String groupName, InterfaceForName interfaceForName){
        FirebaseDatabase.getInstance().getReference().child("Teams").child(groupName)
                .get().addOnSuccessListener(snapshot -> {
                    ChoosingGroupModel model = snapshot.getValue(ChoosingGroupModel.class);
                    String name= model.getGroupName();
                    interfaceForName.presentGroupName(name,model.getStatus());

                });
    }
    public void updateTeamStatus(String status,String groupN){
        FirebaseDatabase.getInstance().getReference().child("Teams").child(groupN).child("Status").setValue(status).addOnSuccessListener(unused -> {

        });
    }


    public void updateGroupTime(String groupID, String time) {
        if(groupID.contains(GlobalVars.returnId(GlobalVars.currentRunner.getEmail()))){
            FirebaseDatabase.getInstance().getReference().child("Teams").child(groupID).child("ElapsedTime").setValue(time).addOnSuccessListener(unused -> {

            });
        }
    }
    public static void removeTeam(String ID){
        FirebaseDatabase.getInstance().getReference().child("Runners").child(ID).child("ActiveTeam").setValue("No");
    }

    public void updateRunnerStatus(String status) {
        FirebaseDatabase.getInstance().getReference().child("Runners").child(GlobalVars.returnId(currentRunner.getEmail())).child("Status").setValue(status).addOnSuccessListener(unused -> {

        });
    }
}

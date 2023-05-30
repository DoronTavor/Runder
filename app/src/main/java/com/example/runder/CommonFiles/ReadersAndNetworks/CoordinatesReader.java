package com.example.runder.CommonFiles.ReadersAndNetworks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.runder.Activities.ChoosingGroupActivity;
import com.example.runder.Activities.MainActivity;
import com.example.runder.Activities.ManagingGroupActivity;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.Models.ChoosingGroupModel;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;

public class CoordinatesReader {
    RunnersModelFull myRunner;
    Context context;

    public CoordinatesReader(RunnersModelFull myRunner, Context context) {
        this.myRunner = myRunner;
        this.context = context;
    }

    public CoordinatesReader(Context context) {
        this.context = context;
    }

    public void returnMarkers(String email){

        DatabaseReference postRef =
                FirebaseDatabase.getInstance().getReference().child("Runners");
        postRef.orderByChild("Email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    if(!newSnap.child("Email").getValue(String.class).equals(email)){
                        DataSnapshot newSnap2= snapshot.child(newSnap.getKey());
                        LatLng newL= new LatLng(newSnap2.child("CurrentPosition").child("Latitude").getValue(Double.class),
                                newSnap2.child("CurrentPosition").child("Longitude").getValue(Double.class));
                        String mail= newSnap.child("Email").getValue(String.class);
                        String title = newSnap.child("FirstAndSecondName").getValue(String.class) + " "+ GeocodingMethods.getAddress(newL,context);
                        Log.e("Before","111");
                        FireBaseReader reader = new FireBaseReader(context);
                        RunnersModelFull modelFull= new RunnersModelFull();
                        modelFull= reader.addToModel(newSnap2,modelFull);
                        modelFull.setEmail(mail);
                        MainActivity.setPoints(new MarkerOptions().position(newL).title(title),modelFull);
                        Log.e("Before","222");
                    }
                    Log.e("Before", "333: ");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void returnMarkersForChoosing(String max_D,String max_S, int age) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Teams");
        ref.orderByChild("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot newSnap: snapshot.getChildren()){
                    DataSnapshot snapshot1= snapshot.child(newSnap.getKey());


                    LatLng latLng= new LatLng(snapshot1.child("Latitude").getValue(Double.class),
                            snapshot1.child("Longtitude").getValue(Double.class));
                    boolean showingCond= max_D.equals(snapshot1.child("Max_Distance").getValue(String.class))&&
                            max_S.equals(snapshot1.child("Max_Speed").getValue(String.class)) &&
                            (age == snapshot1.child("Age").getValue(Integer.class));
                    if(showingCond){
                        String title= newSnap.child("Name").getValue(String.class)+GeocodingMethods.getAddress(latLng,context);
                        Random rnd = new Random();
                        int [] countOfColors= {R.drawable.map_pin_one,R.drawable.map_pin_two,R.drawable.map_pin_three};
                        int num = rnd.nextInt(countOfColors.length);
                        if(num == 0 ){
                            ChoosingGroupActivity.setPointsForMap(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context,R.drawable.map_pin_one)));
                        }
                        else if(num == 1 ){
                            ChoosingGroupActivity.setPointsForMap(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context,R.drawable.map_pin_two)));
                        }
                        else {
                            ChoosingGroupActivity.setPointsForMap(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context,R.drawable.map_pin_three)));
                        }

                        GlobalVars.mapForChoosing.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
   public void updateGroupMarkers(String currentTeam){
        GlobalVars.managingActivityMarkers.clear();
       FirebaseDatabase.getInstance().getReference().child("Teams").child(currentTeam).child("CurrentPosition").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot teamSnap=null;
                String id = "";
                HashMap<String,Double> posn= new HashMap<>();
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    id = newSnap.getKey();
                    teamSnap=newSnap;
                    posn.put(id,newSnap.getValue(Double.class));

                }
                if(posn!=null){
                    LatLng currentPos= new LatLng(posn.get("Latitude"),posn.get("Longitude"));
                    int width = 120;
                    int height = 120;

                    Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.group_purple);
                    Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                    if(!GlobalVars.managingActivityMarkers.containsKey(currentTeam)){
                        GlobalVars.managingActivityMarkers.put(currentTeam,GlobalVars.mapForManaging.addMarker(new MarkerOptions().position(currentPos).icon(bitmapDescriptor).
                                title(GlobalVars.currentGroup.getGroupName()).snippet(GeocodingMethods.getAddress(currentPos,context))));
                        GlobalVars.mapForManaging.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 17.0f));
                    }
                    else {
                        GlobalVars.managingActivityMarkers.get(currentTeam).setPosition(currentPos);
                        GlobalVars.managingActivityMarkers.get(currentTeam).setSnippet(GeocodingMethods.getAddress(currentPos,context));
                        GlobalVars.mapForManaging.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 17.0f));
                    }
                }

                //Adding the rest of the markers

                FirebaseDatabase.getInstance().getReference().child("Runners").orderByChild("ActiveTeam").equalTo(currentTeam).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot newSnap: snapshot.getChildren()){
                            RunnersModelFull runnersModelFull= newSnap.getValue(RunnersModelFull.class);
                            LatLng newPos= new LatLng(runnersModelFull.getCurrentPosition().get("Latitude"),runnersModelFull.getCurrentPosition().get("Longitude"));
                            Location loc2= new Location("");
                            loc2.setLatitude(newPos.latitude);
                            loc2.setLongitude(newPos.longitude);
                            int width = 120;
                            int height = 120;
                            Bitmap markerIcon= BitmapFactory.decodeResource(context.getResources(), R.drawable.person_yellow);
                            Bitmap resizedIcon = Bitmap.createScaledBitmap(markerIcon, width, height, false);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedIcon);
                            if(loc2.distanceTo(GlobalVars.teamLoc)>=250){
                                if(!GlobalVars.managingActivityMarkers.containsKey(runnersModelFull.getID())){
                                    GlobalVars.managingActivityMarkers.put(runnersModelFull.getID(),GlobalVars.mapForManaging.addMarker(new MarkerOptions().position(newPos).icon(bitmapDescriptor).title(runnersModelFull.getFirstSecondName())
                                            .snippet(GeocodingMethods.getAddress(newPos,context))));
                                }
                                else {
                                    GlobalVars.managingActivityMarkers.get(runnersModelFull.getID()).setPosition(newPos);
                                    GlobalVars.managingActivityMarkers.get(runnersModelFull.getID()).setSnippet(GeocodingMethods.getAddress(newPos,context));
                                }
                            }
                            else {
                                if(GlobalVars.managingActivityMarkers.containsKey(runnersModelFull.getID())){
                                    GlobalVars.managingActivityMarkers.get(runnersModelFull.getID()).remove();
                                    GlobalVars.managingActivityMarkers.remove(runnersModelFull.getID());

                                }
                            }



                        }
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
   }

    /*public void returnMarkersForShowingGroup(String teamId) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Runners");
        ref.orderByChild("ActiveTeam").equalTo(teamId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot newSnap: snapshot.getChildren()){
                    DataSnapshot snapshot1= snapshot.child(newSnap.getKey());


                    LatLng latLng= new LatLng(snapshot1.child("CurrentPosition").child("Latitude").getValue(Double.class),
                            snapshot1.child("CurrentPosition").child("Longitude").getValue(Double.class));
                        String title= newSnap.child("FirstAndSecondName").getValue(String.class)+GeocodingMethods.getAddress(latLng,context);
                        Random rnd = new Random();
                        int [] countOfColors= {R.drawable.map_pin_one,R.drawable.map_pin_two,R.drawable.map_pin_three};
                        int num = rnd.nextInt(countOfColors.length);
                        if(num == 0 ){
                            ManagingGroupActivity.setPointsForMap(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context,R.drawable.map_pin_one)));
                        }
                        else if(num == 1 ){
                            ManagingGroupActivity.setPointsForMap(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context,R.drawable.map_pin_two)));
                        }
                        else {
                            ManagingGroupActivity.setPointsForMap(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context,R.drawable.map_pin_three)));
                        }

                        GlobalVars.mapForManaging.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}

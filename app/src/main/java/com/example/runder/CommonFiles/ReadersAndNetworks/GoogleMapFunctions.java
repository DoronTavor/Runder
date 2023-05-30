package com.example.runder.CommonFiles.ReadersAndNetworks;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.Models.RunnersModelFull;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoogleMapFunctions extends FireBaseReader{


    public GoogleMapFunctions(Context context) {
        super(context);
    }

    public void addPointsToPolyLine(String Email,PointForMapCB pointForMapCB){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Runners");
        ref.orderByChild("Email").equalTo(Email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "";
                for (DataSnapshot newSnap : snapshot.getChildren()) {
                    key = newSnap.getKey();
                }
                RunnersModelFull modelFull= new RunnersModelFull();
                modelFull= addToModel(snapshot.child(key),modelFull);
               // LatLng team= modelFull.getCurrentPosition();
              /*  if(GlobalVars.currentTeamLatlng==null){
                    GlobalVars.currentTeamLatlng=team;
                    pointForMapCB.callBackForChange(GlobalVars.currentTeamLatlng);

                }
                else {
                    if(!GlobalVars.currentTeamLatlng.equals(team)){
                        GlobalVars.currentTeamLatlng=team;
                        pointForMapCB.callBackForChange(GlobalVars.currentTeamLatlng);
                    }
                }
*/


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

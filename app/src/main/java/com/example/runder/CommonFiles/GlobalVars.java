package com.example.runder.CommonFiles;



import android.location.Location;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.runder.CommonFiles.CBS.CBforLOc;
import com.example.runder.Models.ChoosingGroupModel;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class GlobalVars {
    public int count=0;
    public int viewType;
    public static final String MAPVIEW_BUNDLE_KEY= String.valueOf(R.string.key);
    public static GoogleMap map;
    public static GoogleMap mapForChoosing;
    public static GoogleMap mapForManaging;
    public static final String WEATHER_KEY="316ea7f012fc730bbb8ff809e6a18d24";
    public static boolean isHave= false;
    public static List<MarkerAndMail> markerAndMails = new ArrayList<>();
    public static String GLOBALTEAMID = "";
    private static int defColor = 0;
    public static Marker markerGlobal;
    public static Hashtable<String ,Marker > userAndMarker= new Hashtable<String ,Marker>();
    public static int numOfActive=0;
    public static PolylineOptions polylineOptions;
    public static ArrayList<MarkerOptions> MARKERSFORMANAGING=new ArrayList<>();
    public static LatLng currentTeamLatlng= null;
    public static Calendar calendar= null;
    public static SimpleDateFormat dateFormat;
    public static Date date= null;
    public static long prevTime=0;
    public static RunnersModelFull currentRunner= new RunnersModelFull();
    public static HashMap<String,Marker> mainActivityMarkers= new HashMap<>();
    public static HashMap<String ,Marker> managingActivityMarkers= new HashMap<>();
    public static HashMap<String ,Marker> mainActivityGroupsMarkers= new HashMap<>();
    public static HashMap<String,Marker> choosingGroupActivityMarkers= new HashMap<>();
    public static Location teamLoc= null;
    public static ChoosingGroupModel currentGroup= new ChoosingGroupModel();
    public final static String  StatusForNotRunning = "Not in active run";
    public final static String  StatusForChoosing = "Running group selection";
    public final static String  StatusForManager = "Manager of an active run";
    public final static String  StatusForMember = "Member of an active run";
    public static String returnHebrewStatus(String statusGroup){
        String ret = "";
        switch (statusGroup){
            case StatusForNotRunning:
                ret= "לא בריצה פעילה";
                break;
            case StatusForChoosing:
                ret = "בחירת קבוצת ריצה";
                break;
            case StatusForManager:
                ret= "מנהל קבוצת ריצה";
                break;
            case StatusForMember:
                ret= "חבר בקבוצת ריצה";
                break;

        }
        return ret;
    }
    public static Marker groupMarker= null;
    public static Marker oldMarker=null;
    public static int numberOfGroups=0;
    public static PolylineOptions polyLineOptionsGlobal= new PolylineOptions().clickable(true);
    public static Polyline polylineForManaging=null;
    public static TextView runTime,runDistance;
    public static String passedTime;
    public static String statusGroup = "";
    public static  ValueEventListener myEvent;
    public static double TotalDistance =0;
    public static int GlobalSeconds=0,GlobalMinutes=0,GlobalHours=0;
    public static CBforLOc cBforLOc;
    public static int numTotal=0;
    public static int numOfActives=0;
    public static boolean isInDebug= false;
    public static String TwiloSID= "ACaccd3694eb430fa0c2b1c694964f3a13";
    public static String TwiloToken= "e53d13b39eba002c05ba0e779fc7f9b9";
    public static String TwiloNumber= "+13158883874";
    public static String returnId(String email){
        return email.replace('@',' ').replace('.',' ');
    }
    public static ValueEventListener isGroupEvent= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    public static ValueEventListener choosingGroupActivityValueEvent= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    public static ValueEventListener newMainActivityValueEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public static ValueEventListener MainActivityCallBack = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    public static ChildEventListener MainActivityChildDeleted= new ChildEventListener() {

        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }

    };
    public static ValueEventListener ManagingGroupActivityEventListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public GlobalVars() {
    }

    public static float CastToNumber(long num1,long num2,long adder) {
        float res= 0;
        float queient=  (num1-num2)/(1000*60);
        res= res+queient;
        res+=adder;
        return res;


    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }






}

package com.example.runder.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
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
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class IWantRunActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner ageSpinner,distanceSpinner;
    NumberPicker speedPicker;
    EditText avgSpeed;
    EditText ageDifference;
    String distanceText;
    ImageButton iwantRun;
    //ImageButton refresh;
    ImageButton back;
    String email;
    String avgDistanceStr;
    String ageStr;
    static CircleImageView image;
    static TextView name;
    TextView mesages;
    private AlertDialog.Builder builder2;
    private AlertDialog dialog2;
    RecyclerView newRec;
    private LinearLayoutManager linearLayoutManager2;
    FirebaseRecyclerAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_run);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*recyclerView=findViewById(R.id.LV_Team);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new IWantRunAdapter());*/
        Intent i = getIntent();
        FireBaseReader reader= new FireBaseReader(this);
        GlobalVars.currentRunner.setRunningStatus(GlobalVars.StatusForChoosing);
        email= i.getStringExtra("EMAIL");
        reader.setRunningStatus(GlobalVars.currentRunner.getRunningStatus(),email);
        back= findViewById(R.id.back_btn_iwantrun);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent= new Intent(IWantRunActivity.this,MainActivity.class);
                backIntent.putExtra("EMAIL",email);
                FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.myEvent);
                finish();
                startActivity(backIntent);
            }
        });
        reader.checkAndRemoveUserFromTeam(email);



        setLayoutToViews();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog2();
            }
        });
        ArrayAdapter<CharSequence> adapterNew = ArrayAdapter.createFromResource(this,
                R.array.avg_distance_array, android.R.layout.simple_spinner_item);
        adapterNew.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(adapterNew);
        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    onNothingSelected(adapterView);
                }
                else {
                    distanceText=distanceSpinner.getSelectedItem().toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    onNothingSelected(adapterView);
                }
                else {
                    ageStr=ageSpinner.getSelectedItem().toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        reader.getCurrentUser(email);
        // Create an ArrayAdapter using the string array and a default spinner layout



        iwantRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speedPicker.getValue()==0 ||
                        distanceText.equals("מרחק ריצה ממוצע")|| ageStr.equals("טווח גילאים")
                ) {
                    Toast.makeText(IWantRunActivity.this, "You didnt" +
                            "set all the attributes, try again", Toast.LENGTH_SHORT).show();

                }

                else {

                    Intent intoChoosing = new Intent(IWantRunActivity.this,ChoosingGroupActivity.class);

                    if(speedPicker.getValue()<=0
                            && !ageStr.equals("טווח גילאים") ){
                        Toast.makeText(IWantRunActivity.this, "one of the attributes can't be sent, try again", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        intoChoosing.putExtra("AGE",ageStr);
                        intoChoosing.putExtra("EMAIL",email);
                        intoChoosing.putExtra("AVGDISTANCE",distanceText);
                        intoChoosing.putExtra("AVGSPEED",speedPicker.getValue());
                        FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.myEvent);
                        finish();
                        startActivity(intoChoosing);
                    }

                }



            }
        });

    }



    @SuppressLint("NewApi")
    public void setLayoutToViews(){

        speedPicker= findViewById(R.id.SpeedNumPicker);
        speedPicker.setWrapSelectorWheel(true);
        speedPicker.setMinValue(2);
        speedPicker.setMaxValue(20);
        speedPicker.setTextSize(85);
        speedPicker.setSelectionDividerHeight(100);
      /*  speedPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // Set the text size here
                //int textSize = R.dimen.text_box_txt_size_large; // Change this to your desired text size
                int textSize= 20;
                TextView textView = (TextView) speedPicker.getChildAt(0);
                textView.setTextSize(textSize);
                return String.format("%02d", value);
            }
        });*/
        ageSpinner= findViewById(R.id.AgeSpinner);
        distanceSpinner= findViewById(R.id.DistanceSpinnerWantRUn);
        iwantRun= (ImageButton) findViewById(R.id.IWantRunBtnIwantRun);
        //refresh= (ImageButton) findViewById(R.id.RefreshBtnIWantRun);
        image= (CircleImageView) findViewById(R.id.PictureWantRunActivity);
        name= (TextView) findViewById(R.id.NameWantRun);


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




    public static void setValueToView(RunnersModelFull model1, Context context){

        Glide.with(context).load(model1.getPicture()).placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                error(R.drawable.oneperson).into(image);
        name.setText(model1.getFirstSecondName());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference().child("Runners").removeEventListener(GlobalVars.myEvent);
    }
}
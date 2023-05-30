package com.example.runder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GoogleSignUpActivity extends AppCompatActivity {

    ImageButton btnAdd;
    EditText date,avgSpeed;
    Spinner avgDistance,gender;
    String serialNumber;
    Long count;
    private String dateStr,avgSpeedStr,avgDistanceStr,genderStr;
    public  static GlobalVars globalVars;
    //TODO 5- Defining more views here- even check why the hell the recycleView here doesn't work
    // and add it here



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Receiving the data from the prev activity
        Intent i= getIntent();
        btnAdd= findViewById(R.id.BtnAdd);
        date=findViewById(R.id.editTextDateGoogle);
        avgSpeed=findViewById(R.id.AvgSpeedEditTextGoogle);
        avgDistance = (Spinner) findViewById(R.id.AvgDistanceSpinerGoogle);
        gender= findViewById(R.id.GenderSpinnerGoogle);
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapterGender);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    onNothingSelected(adapterView);
                    //Toast.makeText(GoogleSignUpActivity.this, "Choose how you defines yourself ", Toast.LENGTH_SHORT).show();
                }
                else {
                    genderStr=adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.avg_distance_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        avgDistance.setAdapter(adapter);
         //Setting the values to the firebase
        avgDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Saving the selected value
                if(i==0){
                    onNothingSelected(adapterView);
                    //Toast.makeText(GoogleSignUpActivity.this, "Choose an avg distances", Toast.LENGTH_SHORT).show();
                }
                else {
                    avgDistanceStr=adapterView.getItemAtPosition(i).toString();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                mDatabaseRef.child("Runners").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        count= dataSnapshot.getChildrenCount();
                        serialNumber=setSerialNumber(count);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dateStr=date.getText().toString();
                avgSpeedStr=avgSpeed.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                DateFormat dateCheckUpdate = new SimpleDateFormat("dd/MM/yyyy");
                String dateCheck = dateCheckUpdate.format(currentTime);
                DateFormat hourCheckUpdate= new SimpleDateFormat("HH:mm");
                String hourCheck= hourCheckUpdate.format(currentTime);

                Map<String,Object> currentUserMap = new HashMap<>();
                currentUserMap.put("FirstSecondName",i.getStringExtra("NAME"));
                currentUserMap.put("Email",i.getStringExtra("EMAIL"));
                currentUserMap.put("AvgDistance",avgDistanceStr);
                currentUserMap.put("AvgSpeed",Double.valueOf(avgSpeedStr));
                currentUserMap.put("SerialNumber",serialNumber);
                currentUserMap.put("Picture",i.getStringExtra("PICTURE"));
                currentUserMap.put("LocationLastUpdateHour",hourCheck);
                currentUserMap.put("LocationLastUpdateDate",dateCheck);
                currentUserMap.put("Gender",genderStr);
                currentUserMap.put("BirthDate",dateStr);
                currentUserMap.put("ActiveTeam","No");

                Map<String,Object> positionMap= new HashMap<>();
                positionMap.put("Latitude",31.887273);
                positionMap.put("Longitude",35.005479);
                currentUserMap.put("CurrentPosition",positionMap);

                FirebaseDatabase.getInstance().getReference().child("Runners").child(i.getStringExtra("EMAIL").replace('@',' ').replace('.',' ')).setValue(currentUserMap).addOnCompleteListener(task -> {
                    Toast.makeText(GoogleSignUpActivity.this, "New runner created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(GoogleSignUpActivity.this,MainActivity.class);
                    intent.putExtra("EMAIL",i.getStringExtra("EMAIL"));
                    finish();
                    startActivity(intent);
                }).addOnFailureListener(e -> Toast.makeText(GoogleSignUpActivity.this, "Error while insertion" +e.toString(), Toast.LENGTH_SHORT).show());

                /*Map<String,Object> map = new HashMap<>();
                map.put("First&Second Name",i.getStringExtra("NAME"));
                map.put("Email",i.getStringExtra("EMAIL"));
                map.put("Avg Distance",avgDistanceStr);
                map.put("Avg Speed",avgSpeedStr);
                map.put("Serial Number",serialNumber);
                map.put("Picture",i.getStringExtra("PICTURE"));
                FirebaseDatabase.getInstance().getReference().child("Runners").push().setValue(map).addOnSuccessListener(unused -> {
                    Toast.makeText(GoogleSignUpActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(GoogleSignUpActivity.this,MainActivity.class);
                    intent.putExtra("EMAIL",i.getStringExtra("EMAIL"));
                    finish();
                    startActivity(intent);
                }).addOnFailureListener(e -> Toast.makeText(GoogleSignUpActivity.this, "Error while insertion", Toast.LENGTH_SHORT).show());*/

            }
        });


    }
    public String setSerialNumber(Long count){
        return "r"+count.toString();

    }
}
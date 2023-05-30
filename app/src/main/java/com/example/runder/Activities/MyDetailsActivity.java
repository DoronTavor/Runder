package com.example.runder.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.runder.CommonFiles.ReadersAndNetworks.FireBaseReader;
import com.example.runder.Models.RunnersModelFull;
import com.example.runder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDetailsActivity extends AppCompatActivity {

    public static TextView firstSecName,email,age,gender;
    public static CircleImageView picture;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        firstSecName= findViewById(R.id.FirstSecName);
        email= findViewById(R.id.EmailTextViewMy);
        age = findViewById(R.id.MyAge);
        gender=findViewById(R.id.MyGender);
        picture= (CircleImageView) findViewById(R.id.MyPic);
        Intent i =getIntent();
        String myMail= i.getStringExtra("EMAIL");
        FireBaseReader fireBaseReader = new FireBaseReader(MyDetailsActivity.this);
        fireBaseReader.getCurrentUser(myMail);
        back= findViewById(R.id.BackDetails);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyDetailsActivity.this,MainActivity.class);
                i.putExtra("EMAIL",myMail);
                finish();
                startActivity(i);
            }
        });


    }
    public static void setValueToTheViews(RunnersModelFull runnersModelFull){
        firstSecName.setText(runnersModelFull.getFirstSecondName());
        email.setText(runnersModelFull.getEmail());
        Date date = null;
        int agei;
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/YYYY");
        try {
            date = format.parse(runnersModelFull.getBirthDate());
            Date currentTime = Calendar.getInstance().getTime();
            if(currentTime.getMonth()<date.getMonth()){
                agei= (currentTime.getYear()-1)- date.getYear();
            }
            else {
                agei= currentTime.getYear()- date.getYear();
            }
            age.setText(String.valueOf(agei));

        } catch (ParseException e) {
            e.printStackTrace();
            age.setText("ERORR");
        }

        gender.setText(runnersModelFull.getGender());
        Glide.with(picture.getContext()).load(runnersModelFull.getPicture()).placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop().
                error(R.drawable.oneperson).into(picture);

    }
}
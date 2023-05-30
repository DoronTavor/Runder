package com.example.runder.Holders;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runder.Activities.GoogleSignUpActivity;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.CommonFiles.MyRecyclerView;
import com.example.runder.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class GoogleSignUpViewHolder extends MyRecyclerView.ViewHolder {
    EditText birthdate,picture,address,city,country,height,id,phone,weight,maxSpeed,avgSpeed;
    private Context context;
    private MaterialSpinner gender;
    private MaterialSpinner avgDistance,maxDistance;

    public GoogleSignUpViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        setItemViaViewType(itemView);
        this.context=context;
    }

    // public static void setGender(@NonNull View itemView){
     //   GoogleSignUpActivity.avgDistance=(Spinner) itemView.findViewById(R.id.AvgDistanceSpiner);
      //  GoogleSignUpActivity.maxDistance=(Spinner) itemView.findViewById(R.id.MaxDistanceSpinner);
      //  GoogleSignUpActivity.gender=(Spinner) itemView.findViewById(R.id.AvgDistanceSpiner);
   // }

    public void setItemViaViewType(View itemView){
        int i = GoogleSignUpActivity.globalVars.viewType;
//        Toast.makeText(context, i, Toast.LENGTH_SHORT).show();
        switch (i){
            case 0:
                birthdate= itemView.findViewById(R.id.editTextDate);
            case 1:
                gender=itemView.findViewById(R.id.GenderSpiner);
                gender.setItems(R.array.gender_array);
            case 2:
                avgDistance= itemView.findViewById(R.id.AvgDistanceSpiner);
                avgDistance.setItems(R.array.avg_distance_array);
            case 3:
                avgSpeed=itemView.findViewById(R.id.AvgSpeedEditText);
            case 4:
                picture=itemView.findViewById(R.id.PictureEditText);
            case 5:
                address=itemView.findViewById(R.id.Address);
            case 6:
                city=itemView.findViewById(R.id.City);
            case 7:
                country= itemView.findViewById(R.id.Country);
            case 8:
                height=itemView.findViewById(R.id.Height);
            case 9:
                id=itemView.findViewById(R.id.ID);
            case 10:
                maxDistance=itemView.findViewById(R.id.MaxDistanceSpinner);
                maxDistance.setItems(R.array.avg_distance_array);
            case 11:
                maxSpeed=itemView.findViewById(R.id.MaxSpeedText);
            case 12:
                phone=itemView.findViewById(R.id.PhoneNumberEditText);
            case 13:
                weight=itemView.findViewById(R.id.Weight);

        }

    }



}

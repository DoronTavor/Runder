package com.example.runder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.runder.Adapters.Adapters.RegularSignUpAdapter;
import com.example.runder.R;

public class RegularSignUp extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_sign_up);
        recyclerView=findViewById(R.id.LVG_Add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RegularSignUpAdapter());
    }
}
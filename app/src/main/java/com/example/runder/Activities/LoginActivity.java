package com.example.runder.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.runder.CommonFiles.ReadersAndNetworks.TimeAndDate;
import com.example.runder.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    ImageButton googleLoginBtn;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseDatabase db;
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Define Google attributes
        myDialog= new Dialog(this);
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);
        db = FirebaseDatabase.getInstance();
        //signOut();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        GoogleSignInAccount last= GoogleSignIn.getLastSignedInAccount(this);

        if(last!=null){
            OpenVideoScreen(last.getDisplayName(),last);


        }
        // Signing out from the last user signed in

        googleLoginBtn = findViewById(R.id.GoogleLog);
        //Logging in to the system
        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });




    }
    public void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
    private void SignIn() {
        Intent signInIntent= gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }
    //Will be called automatically when the user signed in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            //Checking if the process is done well
            try {
                task.getResult(ApiException.class);
                OpenVideoScreen(task.getResult().getDisplayName(),task.getResult());

            } catch (ApiException e) {
                Toast.makeText(this, "SOMETHING WRONG", Toast.LENGTH_SHORT).show();
                Log.d("Checking",e.toString());
            }
        }
    }
    public int getVideoTime(String uri){
        MediaPlayer mp = MediaPlayer.create(this, Uri.parse(uri));
        int duration = mp.getDuration();
        mp.release();
        return (int) TimeUnit.MILLISECONDS.toMillis(duration);
    }

    public void OpenVideoScreen(String name, GoogleSignInAccount account){
        myDialog.setContentView(R.layout.newwelcome);

        String timeStr = "android.resource://"+getPackageName()+"/"+R.raw.videotostartnew;
        int time = getVideoTime(timeStr);
        Log.d("TIME", String.valueOf(time));
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        //Creating the Toast object
        TextView messageTitle= (TextView) myDialog.findViewById(R.id.MessageTitle);
        VideoView videoView = (VideoView) myDialog.findViewById(R.id.videoView);
        Uri uri = Uri.parse(timeStr);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.start();
        messageTitle.setText(String.format("%s  %s", TimeAndDate.returnMessageByHour(), "\n"+name));
        myDialog.show();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                myDialog.dismiss();
                checkUserSignedInFB(account);
            }
        });



    }
    //This method checks if the user was in the system
    public void checkUserSignedInFB(GoogleSignInAccount googleSignInAccount){
        DatabaseReference databaseRunnersReference = FirebaseDatabase.getInstance().getReference().child("Runners");

        // Check if the user in firebase by his email
        databaseRunnersReference.orderByChild("Email").equalTo(googleSignInAccount.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            // Email Exist in system, go directly to the main activity
                            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("EMAIL",googleSignInAccount.getEmail());
                            intent.putExtra("Name",googleSignInAccount.getDisplayName());
                            finish();
                            startActivity(intent);

                        }
                        else { // Email not Exist in system, go to the GoogleSignUpActivity to fill the rest of the data
                            // that wasn't given by the GoogleUser
                            Intent intent = new Intent(LoginActivity.this,GoogleSignUpActivity.class);
                            // TODO P1- To delete if works properly
                           /* String [] valuesToIntent= new String[3];
                            valuesToIntent[0]= googleSignInAccount.getEmail();
                            valuesToIntent[1]= googleSignInAccount.getDisplayName();
                            valuesToIntent[2]=googleSignInAccount.getPhotoUrl().toString();*/
                            intent.putExtra("EMAIL",googleSignInAccount.getEmail());
                            intent.putExtra("NAME",googleSignInAccount.getDisplayName());
                            intent.putExtra("PICTURE",googleSignInAccount.getPhotoUrl().toString());
                            finish();
                            startActivity(intent);

                        }
                    }
                    //TODO 7- item 22 in Excel

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();


                    }
                });




    }
}
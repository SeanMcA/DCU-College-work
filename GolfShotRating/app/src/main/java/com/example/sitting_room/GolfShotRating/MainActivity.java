package com.example.sitting_room.GolfShotRating;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    public static String LoggedInId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        LoggedInId = sharedPreferences.getString("key", "notLoggedIn");
        Log.i(TAG, "isLoggedIn is: " + LoggedInId);


        //If the user is logged in then disable 'Continue as Guest' button.
        if(LoggedInId.equals("notLoggedIn")) {
            Log.i(TAG, "isLoggedIn is: " + LoggedInId);
        }
        else{

            Button guest = (Button) findViewById(R.id.gotToAfterLoginGuest);
            guest.setVisibility(View.GONE);

            LoginScript.loginID=LoggedInId;
            Log.i(TAG, "MainAct LoginID is: " + LoginScript.loginID);

            Intent intentGoToNewRound = new Intent(MainActivity.this,AfterLoginGuest.class);
            startActivity(intentGoToNewRound);
        }


    }//onCreate






    public void goToLogin(View view){
        Log.i(TAG, "gotToLogin started");
        Intent intentGoToLogin = new Intent(MainActivity.this,Login.class);
        startActivity(intentGoToLogin);
    }


    public void goToRegister(View view){
        Log.i(TAG, "gotToRegister started");
        Intent intentGoToRegister = new Intent(MainActivity.this,RegisterScreen.class);
        startActivity(intentGoToRegister);
    }



    public void goToAfterLoginGuest(View view){
        Log.i(TAG, "goToAfterLoginGuest started");
        Intent intentGoToNewRound = new Intent(MainActivity.this,AfterLoginGuest.class);
        startActivity(intentGoToNewRound);
    }


    public void goToTutorial(View view){
        Log.i(TAG, "goTogoToTutorial started");
        Intent intentGoToNewRound = new Intent(MainActivity.this,Tutorial.class);
        startActivity(intentGoToNewRound);
    }



}//class MainActivity

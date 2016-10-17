package com.example.sitting_room.GolfShotRating;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MappingScreen extends AppCompatActivity {


    SharedPreferences sharedpreferences;
    public static String yes = "yes";
    public static String no = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }//onCreate


    /**
     * Setset isMapped to yes and put in Shared_preferences.
     * Then start the Intent to go to the ShotInputScreen activity.
     * @param view The view that was clicked.
     */
    public void mapGreenClicked(View view){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(MappingScreen.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isMapping",yes );
        editor.apply();

        Intent intentGoToShotInputScreen = new Intent(MappingScreen.this,ShotInputScreen.class);
        startActivity(intentGoToShotInputScreen);
    }


    /**
     * Put set isMapped to no and put in Shaerd_preferences.
     * Then start the Intent to go to the ShotInputScreen activity.
     * @param view The view that was clicked.
     */
    public void NOTmapGreenClicked(View view){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(MappingScreen.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isMapping",no );
        editor.apply();

        Intent intentGoToShotInputScreen = new Intent(MappingScreen.this,ShotInputScreen.class);
        startActivity(intentGoToShotInputScreen);
    }

}//class MappingScreen

package com.example.sitting_room.GolfShotRating;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class JoinLeague extends AppCompatActivity {

    EditText league_code;
    TextView joinResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_league);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(sentLeagueName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        league_code = (EditText)findViewById(R.id.enter_league_code);
        joinResult = (TextView)findViewById(R.id.joinResult);


    }//onCreate


    /**
     * This method sends the league code that the user entered and send it tothe ExportLeagueCode class.
     * @param view The view that was clicked.
     */
    public void submitLeagueCode(View view){
        String leagueCode = league_code.getText().toString();
        new ExportLeagueCode(this).execute(leagueCode, LoginScript.loginID);

    }

}//class JoinLeague

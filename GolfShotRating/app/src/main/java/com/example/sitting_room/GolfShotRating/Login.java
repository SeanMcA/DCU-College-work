package com.example.sitting_room.GolfShotRating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private EditText usernameField,passwordField;
    private TextView loginResultTextview;
    private static final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        usernameField = (EditText)findViewById(R.id.loginUsername);
        passwordField = (EditText)findViewById(R.id.loginPassword);
        loginResultTextview = (TextView)findViewById(R.id.loginResult);

    }//onCreate


    /**
     * This method calls the LoginScript class and passes the usernama and password
     * that the user entered.
     * @param view The view that was clicked
     */
    public void login(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        Log.i(TAG, "Username is : " + username);
        new LoginScript(this,loginResultTextview).execute(username,password);
    }


    /**
     * This method starts the Intent to go to theFotgotDetailsScreen.
     * @param view The view that was clicked.
     */
    public void ForgotLoginDteails(View view){
        Intent intentGoToForgotDetailsScreen = new Intent(Login.this,ForgotDetailsScreen.class);
        startActivity(intentGoToForgotDetailsScreen);
    }

}//class Login

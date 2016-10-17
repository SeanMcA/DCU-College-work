package com.example.sitting_room.GolfShotRating;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterScreen extends AppCompatActivity {


    EditText registerPasswordEditText , registerUsernameEditText, confirmPasswordEditText, registerEmailEditText;
    TextView loginRegisterResultTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        registerUsernameEditText = (EditText)findViewById(R.id.registerUsername);
        registerPasswordEditText = (EditText)findViewById(R.id.registerPassword);
        confirmPasswordEditText = (EditText)findViewById(R.id.confirmPassword);
        registerEmailEditText = (EditText)findViewById(R.id.registerEmail);
        loginRegisterResultTextview = (TextView)findViewById(R.id.registerResult);

    }//onCreate

    public void register(View view){
        String username = registerUsernameEditText.getText().toString();
        String password = registerPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String email = registerEmailEditText.getText().toString();

        if(password.equals(confirmPassword))
        {
            new RegisterScript(this, loginRegisterResultTextview).execute(username, password, email);
        }
        else
        {
            loginRegisterResultTextview.setText(R.string.Passwords_do_not_match);
        }
    }




}//class RegisterScreen

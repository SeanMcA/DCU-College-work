package com.example.sitting_room.GolfShotRating;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.security.SecureRandom;
import java.util.Random;

public class ForgotDetailsScreen extends AppCompatActivity {
    private static Context mContext;
    public static String newPassword;
    public static String userEmail;
    EditText useremailEdittext;

    private static final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_details_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

       useremailEdittext = (EditText)findViewById(R.id.forgotDetailsEmail);


    }//onCreate


    /**
     * Gets the email address that the user entered and send this along with a new random
     * password to the web server to retrieve the users username and enter their new password.
     * @param view The view that was clicked
     */
    public void SendNewDetails(View view){
        userEmail = useremailEdittext.getText().toString();
        newPassword = generateRandomPassword();
        Log.i(TAG,"newpassword is: " + newPassword);
        new NewDetailsScript(this).execute(userEmail,newPassword);
    }


    /**
     * Generates a new password for the user.
     * @return Returns the new password
     */
    public static String generateRandomPassword()
    {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";
        Random RANDOM = new SecureRandom();
        String pw = "";
        for (int i=0; i<8; i++)
        {
            int index = (int)(RANDOM.nextDouble()*letters.length());
            pw += letters.substring(index, index+1);
        }
        return pw;
    }

    /**
     * Send an email to the user with their username and a new password.
     * @param username The username retrieved from the web server that matches the email address the user entered.
     */
    protected static void sendEmail(String username) {
        Log.i(TAG,"FDS Send email started");
        Log.i(TAG,"FDS user email is: " + userEmail);
        String[] TO = {userEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your login details");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your Username is: "
                + username + "\n"
                + "Your new password is: "
                + newPassword + "\n");

        Intent i = Intent.createChooser(emailIntent, "Send mail...");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
            Log.i(TAG, "Finished sending email");

    }



}//class ForgotDetailsScreen

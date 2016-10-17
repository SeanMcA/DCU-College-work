package com.example.sitting_room.GolfShotRating;

//Original code for 'posting' to a URL obtained from http://www.tutorialspoint.com/android/android_php_mysql.htm


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;


public class LoginScript extends AsyncTask<String,Void,String>{

    private Context context;
    private TextView loginResultTextview;
    public static String loginID = "0";
    private static final String TAG = "TEST";


    public LoginScript(Context context, TextView loginResultTextview) {
        this.context = context;
        this.loginResultTextview = loginResultTextview;
        String PREF_NAME = "prefs";
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }





    protected void onPreExecute(){
        Log.i(TAG, "LoginScript started");

    }



    @Override
    protected String doInBackground(String... arg0) {


        try{
            String username = arg0[0];
            String password = arg0[1];

            //String link="http://zelusit.com/androidLoginApp.php";
            String link="http://zelusit.com/androidlogin.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }


    @Override
    protected void onPostExecute(String result){
        loginID = result;
        Log.i(TAG, "Returned login is: " + loginID);
        if(result.equals("0"))
        {
            //Log.i(TAG, "No login returned");
            loginResultTextview.setText(R.string.loginResults);
        }
        else
        {

            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("key", loginID);
            editor.apply();

            context.startActivity(new Intent(context, AfterLoginGuest.class));
        }
    }//onPostExecute



}
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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;


public class RegisterScript extends AsyncTask<String,Void,String>{

    private Context context;
    private TextView loginRegisterResultTextview;
    private static final String TAG = "TEST";


    public RegisterScript(Context context, TextView loginRegisterResultTextview) {
        this.context = context;
        this.loginRegisterResultTextview = loginRegisterResultTextview;
        String PREF_NAME = "prefs";
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }





    protected void onPreExecute(){
        Log.i(TAG, "RegisterScript started");

    }



    @Override
    protected String doInBackground(String... arg0) {


        try{
            String username = arg0[0];
            String password = arg0[1];
            String email = arg0[2];

            String link="http://zelusit.com/androidRegister.php";
            String data  = URLEncoder.encode("sentUsername", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("sentPassword", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("sentEmail", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

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
        //Log.i(TAG, "Returned login is: " + loginID);
        Log.i(TAG, "Returned register result is: " + result);
        if(result.equals("Username is not available"))
        {
            //Log.i(TAG, "No login returned");
            loginRegisterResultTextview.setText(R.string.Username_is_already_used);
        }
        else if(result.equals("Email is not available"))
        {
            loginRegisterResultTextview.setText(R.string.The_Email_address_has_already_been_used);
        }
        else
        {
            loginRegisterResultTextview.setText(R.string.You_have_been_registered);
            context.startActivity(new Intent(context, Login.class));
        }
    }//onPostExecute



}
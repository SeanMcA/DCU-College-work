package com.example.sitting_room.GolfShotRating;

//Original code for 'posting' to a URL obtained from http://www.tutorialspoint.com/android/android_php_mysql.htm


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class NewDetailsScript extends AsyncTask<String,Void,String>{

    private Context context;
    private static final String TAG = "TEST";


    public NewDetailsScript(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){
        Log.i(TAG, "NewDetailsScript started");

    }


    /**
     * This method send the new password to the url along with the useremail address.
     * This is done so that the new email generated is inserted into the row with the same email
     * as the one being sent.
     * @param arg0 The arguments sent in va the calling statement.
     * @return The result returned.
     */
    @Override
    protected String doInBackground(String... arg0) {


        try{
            String userEmail = arg0[0];
            String newPassword = arg0[1];

            String link="http://zelusit.com/androidNewDetails.php";
            String data  = URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(userEmail, "UTF-8");
            data += "&" + URLEncoder.encode("newPassword", "UTF-8") + "=" + URLEncoder.encode(newPassword, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

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


    /**
     *
     * @param result The result returned from the url.
     */
    @Override
    protected void onPostExecute(String result){
        //userName = result;
        Log.i(TAG, "NDS result: " + result);
        if(result.equals("That email address is not on record."))
        {
            Log.i(TAG, "No login returned");
        }
        else
        {
            Log.i(TAG,"Sending to sendEmail");
            //context.startActivity(new Intent(context, AfterLoginGuest.class));
            ForgotDetailsScreen.sendEmail(result);
        }
    }//onPostExecute



}
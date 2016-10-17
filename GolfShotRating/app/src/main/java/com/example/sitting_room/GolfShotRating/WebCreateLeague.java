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
import android.widget.Toast;

public class WebCreateLeague  extends AsyncTask<String,Void,String>{
    private Context context;
    private static final String TAG = "TEST";



    public WebCreateLeague(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){
        Log.i(TAG, "WebCreateLeague started");
    }

    @Override
    protected String doInBackground(String... arg0) {

        try{
            String league_Name = arg0[0];
            String leagueType = arg0[1];
            String sDate = arg0[2];
            String eDate = arg0[3];
            String userID = arg0[4];



            String link="http://zelusit.com/androidCreateNewLeagueScript.php";
            String data  = URLEncoder.encode("leagueName", "UTF-8") + "=" + URLEncoder.encode(league_Name, "UTF-8");
            data += "&" + URLEncoder.encode("leagueType", "UTF-8") + "=" + URLEncoder.encode(leagueType, "UTF-8");
            data += "&" + URLEncoder.encode("sDate", "UTF-8") + "=" + URLEncoder.encode(sDate, "UTF-8");
            data += "&" + URLEncoder.encode("eDate", "UTF-8") + "=" + URLEncoder.encode(eDate, "UTF-8");
            data += "&" + URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");

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
            //Log.i(TAG, "ExportShots progress: " + progress);
            return sb.toString();

        }//try

        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }//catch
    }//else

    @Override
    protected void onPostExecute(String result){
        Log.i(TAG,"result is: " + result);
        if(result.equals("New league created successfully")){
            Toast.makeText(context, "New league created successfully", Toast.LENGTH_SHORT).show();

            context.startActivity(new Intent(context, ListOfLeagues.class));
        }
        else{
            Toast.makeText(context, "Sorry but your league could not be created." + result, Toast.LENGTH_SHORT).show();
        }
    }



}

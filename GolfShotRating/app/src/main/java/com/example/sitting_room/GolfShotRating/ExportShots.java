package com.example.sitting_room.GolfShotRating;

//Original code for posting to a URL obtained from http://www.tutorialspoint.com/android/android_php_mysql.htm


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

public class ExportShots extends AsyncTask<String,Void,String>{
    private Context context;
    private static final String TAG = "TEST";
    public static int returnedinfo = 0;



    public ExportShots(Context context) {
        this.context = context;
 }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {

            try{
                String latitude = arg0[0];
                String longitude = arg0[1];
                String hit_from = arg0[2];
                String shot_score = arg0[3];
                String hole = arg0[4];
                String shot_number = arg0[5];
                String shot_distance = arg0[6];
                String round_id = arg0[7];
                String userId = arg0[8];
                Log.i(TAG, "ExportShots lat: " + latitude);

                String link="http://zelusit.com/exportScript.php";
                String data  = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8");
                data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8");
                data += "&" + URLEncoder.encode("hit_from", "UTF-8") + "=" + URLEncoder.encode(hit_from, "UTF-8");
                data += "&" + URLEncoder.encode("shot_score", "UTF-8") + "=" + URLEncoder.encode(shot_score, "UTF-8");
                data += "&" + URLEncoder.encode("hole", "UTF-8") + "=" + URLEncoder.encode(hole, "UTF-8");
                data += "&" + URLEncoder.encode("shot_number", "UTF-8") + "=" + URLEncoder.encode(shot_number, "UTF-8");
                data += "&" + URLEncoder.encode("shot_distance", "UTF-8") + "=" + URLEncoder.encode(shot_distance, "UTF-8");
                data += "&" + URLEncoder.encode("round_id", "UTF-8") + "=" + URLEncoder.encode(round_id, "UTF-8");
                data += "&" + URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8");

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
        //this.statusField.setText("Export Successful");
        //this.roleField.setText(result);
        returnedinfo++;
        Log.i(TAG, "returned info: " + returnedinfo);
        if(returnedinfo==RoundSummary.totalrows) {
            context.startActivity(new Intent(context, ListOfRounds.class));

        }
    }



}

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

public class ExportRound  extends AsyncTask<String,Void,String>{
    private Context context;
    public static String roundID;
    private static final String TAG = "TEST";
    public static int returnedinfo = 0;



    public ExportRound(Context context)
    {
        this.context = context;
    }

    protected void onPreExecute(){
        Log.i(TAG, "ExportRound Activity started");
    }


    /**
     * This method exports the round data to the web based database via a url.
     *
     * @param arg0 The paramaters sent in via the calling statment.
     * @return The result returned.
     */
    @Override
    protected String doInBackground(String... arg0) {

        try{
            String userId = arg0[0];
            String course_name = arg0[1];
            String roundDate = arg0[2];
            String sg_driving = arg0[3];
            String sg_long_game = arg0[4];
            String sg_short_game = arg0[5];
            String sg_putting = arg0[6];
            roundID = arg0[7];

            Log.i(TAG, "ExportRound Activity roundDate is: " + roundDate);



            String link="http://zelusit.com/exportRoundScript.php";
            String data  = URLEncoder.encode("sg_driving", "UTF-8") + "=" + URLEncoder.encode(sg_driving, "UTF-8");
            data += "&" + URLEncoder.encode("sg_long_game", "UTF-8") + "=" + URLEncoder.encode(sg_long_game, "UTF-8");
            data += "&" + URLEncoder.encode("sg_short_game", "UTF-8") + "=" + URLEncoder.encode(sg_short_game, "UTF-8");
            data += "&" + URLEncoder.encode("sg_putting", "UTF-8") + "=" + URLEncoder.encode(sg_putting, "UTF-8");
            data += "&" + URLEncoder.encode("userId", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8");
            data += "&" + URLEncoder.encode("roundDate", "UTF-8") + "=" + URLEncoder.encode(roundDate, "UTF-8");
            data += "&" + URLEncoder.encode("course_name", "UTF-8") + "=" + URLEncoder.encode(course_name, "UTF-8");

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


    /**
     * This method updates the SQLite database to show the round has been exported.
     * It then starts the Intent to call the ListOfRounds activity.
     * @param result The result of the round export.
     */
    @Override
    protected void onPostExecute(String result){
        Log.i(TAG,"ER onPostExecute started.");
        returnedinfo++;
        Log.i(TAG, "returned info: " + returnedinfo);
        Integer intRoundID = Integer.parseInt(roundID);
        DatabaseHelper dbHandler = new DatabaseHelper(context);
        dbHandler.updateExport(intRoundID);
        //context.startActivity(new Intent(context, ListOfRounds.class));
    }



}

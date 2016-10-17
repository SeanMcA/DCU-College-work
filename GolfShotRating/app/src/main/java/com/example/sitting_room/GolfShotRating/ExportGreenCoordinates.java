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

public class ExportGreenCoordinates  extends AsyncTask<String,Void,String>{
    private Context context;
    private static final String TAG = "TEST";


    public ExportGreenCoordinates(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){
        Log.i(TAG, "ExportGreenCoordinated Activity started");
    }


    /**
     * This method takes the string of coordinates for all the 'greens' and the ID of the course
     * and sends them to the url.
     * @param arg0 The arguments sent via the calling statement.
     * @return The returned result.
     */
    @Override
    protected String doInBackground(String... arg0) {

        try{
            String joinedCoordinatesString = arg0[0];
            String courseIDString = arg0[1];
            Log.i(TAG,"EGC joined: " + joinedCoordinatesString);
            Log.i(TAG,"EGC courseID: " + courseIDString);

            String link="http://zelusit.com/AndroidGreenCoordinates.php";
            String data  = URLEncoder.encode("joinedCoordinatesString", "UTF-8") + "=" + URLEncoder.encode(joinedCoordinatesString, "UTF-8");
            data += "&" + URLEncoder.encode("courseIDString", "UTF-8") + "=" + URLEncoder.encode(courseIDString, "UTF-8");

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

    }



}

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

public class ExportLeagueCode  extends AsyncTask<String,Void,String>{
    private Context context;
    private static final String TAG = "TEST";



    public ExportLeagueCode(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){
        Log.i(TAG, "WebCreateLeague started");
    }


    /**
     * The league code and user id are sent to the url.
     * @param arg0 The paramaters sent via the calling statement. In this case the league code and user id.
     * @return The returned result.
     */
    @Override
    protected String doInBackground(String... arg0) {

        try{
            String league_code = arg0[0];
            String userID = arg0[1];

            Log.i(TAG,"Sendingleague code: " + league_code);

            String link="http://zelusit.com/androidJoinLeague.php";
            String data  = URLEncoder.encode("leagueCode", "UTF-8") + "=" + URLEncoder.encode(league_code, "UTF-8");
            data += "&" + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");

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
     * Gets the result of the league export.
     * If the user joined the league the Intent to start the ListOfLeagues activity is called.
     * If the user could not join the league then they are informed of this.
     * @param result The result returned.
     */
    @Override
    protected void onPostExecute(String result){
        Log.i(TAG,"result is: " + result);
        if(result.equals("You have joined this league.")){
            Toast.makeText(context, "You have joined this league", Toast.LENGTH_SHORT).show();

            context.startActivity(new Intent(context, ListOfLeagues.class));
        }
        else{
            Toast.makeText(context, "Sorry but you could not join this league.", Toast.LENGTH_SHORT).show();
        }
    }

}

package com.example.sitting_room.GolfShotRating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoundSummary extends AppCompatActivity {

    private static final String TAG = "TEST";
    public static Double roundDrivingRating;
    public static Double roundPuttingRating;
    public static int thisRoundID;
    public static String courseID;
    public static String isMapping;
    public static String no = "no";
    public static int totalrows;
    public static int exported;
    private static String DATABASE_TABLE_SHOT = "shot";
    private static String DATABASE_TABLE_ROUND = "round";
    private static String ROUND_ID = "round_id";
    private static String ROW_ID = "_id";
    private static Double roundedDrivingRating;
    private static Double roundedPuttingRating;
    private static Double roundedLongGameRating;
    private static Double roundedShortGameRating;
    private static Double roundedTotalStrokeGained;
    private static Integer numberOfAwfulShots;
    public static String formatted_start_date;
    public static String[] taggedPinsArray;

    Button exportButton;
    public static final String MyPREFERENCES = "MyPrefs";
    public static String LoggedInId;
    SharedPreferences sharedpreferences;


    ProgressDialog progressBar;
    private Handler progressBarbHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_summary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);




        exported = getIntent().getIntExtra("exported", 0);
        Log.i(TAG,"Exported is: " + exported);
        exportButton = (Button)findViewById(R.id.roundSummaryExport);
        //if the round has already been exported then hide the export button.
        if(exported == 1)
        {
            exportButton.setVisibility(View.GONE);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        LoggedInId = sharedPreferences.getString("key", "0");

        thisRoundID = sharedPreferences.getInt("roundID", 0);

        courseID = sharedPreferences.getString("courseID", "");

        isMapping = sharedPreferences.getString("isMapping", "");

        //If the user is not logges in ie they are a guest...hide the export button.
        if(LoggedInId.equals("0")) {
            exportButton.setVisibility(View.GONE);
        }

        //thisRoundID = getIntent().getIntExtra("roundID", 0);
        Log.i(TAG, "RoundSummary RoundID received from SP: " + thisRoundID);

        //Log.i(TAG, "RoundSummary loginId: " + LoginScript.loginID);
        Log.i(TAG, "RoundSummary SP loginId: " + LoggedInId);

        Log.i(TAG, "RoundSummary SP isMapping: " + isMapping);


        try {
            Log.i(TAG, "RoundSummary TRY - started");

            SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
            //We don’t need to write to the database so we’re using getReadableDatabase().
            SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();
            //Use cursor to get number of rows where hole_counter equals X

            String query1 = "SELECT SUM(shot_score) from shot WHERE round_id = " + thisRoundID + " and hit_from = 'tee_shot_driver';" ;
            Log.i(TAG, "Summary Query is: " + query1);
            Cursor cursor = db.rawQuery(query1, null);
            if (cursor.moveToFirst()) {
                roundDrivingRating = cursor.getDouble(0);
                roundedDrivingRating = (double)Math.round(roundDrivingRating * 100d) / 100d;
                Log.i(TAG, "Driving rating is: " + roundedDrivingRating);
            }
            String query2 = "SELECT SUM(shot_score) from shot WHERE round_id = " + thisRoundID + " and hit_from = 'green';";
            cursor = db.rawQuery(query2, null);
            if (cursor.moveToFirst()) {
                roundPuttingRating = cursor.getDouble(0);
                roundedPuttingRating = (double)Math.round(roundPuttingRating * 100d) / 100d;
                Log.i(TAG, "Putting rating is: " + roundedPuttingRating);
            }

            String query3 = "SELECT SUM(shot_score) from shot WHERE round_id = " + thisRoundID + " AND shot_distance" +
                    " <= 250 AND shot_distance >= 100;";
            cursor = db.rawQuery(query3, null);
            if (cursor.moveToFirst()) {
                Double roundLongGameRating = cursor.getDouble(0);
                roundedLongGameRating = (double)Math.round(roundLongGameRating * 100d) / 100d;
                Log.i(TAG, "Long game rating is: " + roundedLongGameRating);
            }

            String query4 = "SELECT SUM(shot_score) from shot WHERE round_id = " + thisRoundID + " AND shot_distance" +
                    " <= 99 AND shot_distance >= 0 AND NOT hit_from = 'green';";
            //Log.i(TAG, "Short game query: " + query4);
            cursor = db.rawQuery(query4, null);
            if (cursor.moveToFirst()) {
                Double roundShortGameRating = cursor.getDouble(0);
                roundedShortGameRating = (double)Math.round(roundShortGameRating * 100d) / 100d;
                Log.i(TAG, "Short game rating is: " + roundedShortGameRating);
            }



            String query5 = "SELECT COUNT(shot_score) from shot WHERE round_id = " + thisRoundID + " AND shot_score > 0.8;";
            //Log.i(TAG, "Awful shots query: " + query5);
            cursor = db.rawQuery(query5, null);
            if (cursor.moveToFirst()) {
                numberOfAwfulShots = cursor.getInt(0);
                Log.i(TAG, "Number of awful shots is: " + numberOfAwfulShots);
            }

            String query6 = "SELECT SUM(shot_score) from shot WHERE round_id = " + thisRoundID;
            //Log.i(TAG, "Total SG query: " + query6);
            cursor = db.rawQuery(query6, null);
            if (cursor.moveToFirst()) {
                Double roundTotalStrokeGained = cursor.getDouble(0);
                roundedTotalStrokeGained = (double)Math.round(roundTotalStrokeGained * 100d) / 100d;
                Log.i(TAG, "Total SG is: " + roundedTotalStrokeGained);
            }

            DatabaseHelper dbHandler = new DatabaseHelper(RoundSummary.this);
            dbHandler.addScoresToDb(thisRoundID, roundedDrivingRating, roundedLongGameRating, roundedPuttingRating, roundedShortGameRating, numberOfAwfulShots);

            cursor.close();

        }//try
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "RS Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //String scoreString = getApplicationContext().getString(R.string.rounded_Driving_Rating, roundedDrivingRating);

        TextView textViewDrivingSummary = (TextView) findViewById(R.id.round_summary_textview_driving_rating);
        textViewDrivingSummary.setText("" + roundedDrivingRating);

        TextView textviewLongGameRating = (TextView) findViewById(R.id.round_summary_textview_long_game_rating);
        textviewLongGameRating.setText("" + roundedLongGameRating);

        TextView textviewPuttingRating = (TextView) findViewById(R.id.round_summary_textview_short_game_rating);
        textviewPuttingRating.setText("" + roundedShortGameRating);

        TextView textviewShortGameRating = (TextView) findViewById(R.id.round_summary_textview_putting_rating);
        textviewShortGameRating.setText("" + roundedPuttingRating);

        TextView textviewAwfulShots = (TextView) findViewById(R.id.round_summary_textview_awful_shots);
        textviewAwfulShots.setText("" + numberOfAwfulShots);

        TextView textviewTotalSG = (TextView) findViewById(R.id.round_summary_textview_total_sg);
        textviewTotalSG.setText("" + roundedTotalStrokeGained);



        //if this course is being mapped then get the coordinates for each green and
        //call the ExportGreenCoordinates class.
        if (isMapping.equals("yes")) {
            //gets the green coordinates from Shared_preferences and puts them in an array.
            taggedPinsArray = new String[18];
            for (Integer i = 1; i < 19; i++) {
                String coordinates = sharedPreferences.getString("hole_" + i + "_pinCoord", "null");
                taggedPinsArray[i - 1] = coordinates;
            }

            //Here the elements of the array are joined up into one comma seperated string.
            String myJoined = strJoin(taggedPinsArray, ",");
            Log.i(TAG, "myJoined: " + myJoined);

            new ExportGreenCoordinates(this).execute(myJoined, courseID);


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isMapping", no);
            editor.apply();
        }//if

    }//onCreate


    /**
     * This method joins the the array together into a comma seperated string.
     * @param aArr The array sent in. In this case the array with the coordinates.
     * @param sSep The string seperator. In this case: ,
     * @return The String of array elements joined up with comma seperators.
     */
    public static String strJoin(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }



    public void returnToListOfRounds(View view){
        //Log.i(TAG, "returnToListOfRounds started**");
        Intent intentReturnToListOfRounds = new Intent(RoundSummary.this,ListOfRounds.class);
        startActivity(intentReturnToListOfRounds);
    }


    /**
     * This method deletes the round data
     * @param view the view clicked.
     */
    public void deleteRound(View view){
        //Log.i(TAG, "deleteRound started**");

        //AlertDialog to check if user actually wants to delete the round
        //and its not a case of clicking button by accident.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to delete this round?");

        //builder.setMessage("Some message...")
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.i(TAG,"RS roundID: " + thisRoundID);
                        SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(RoundSummary.this);
                        //We don’t need to write to the database so we’re using getReadableDatabase().
                        SQLiteDatabase db = myTestDatabaseHelper.getWritableDatabase();
                        //Use cursor to get number of rows where hole_counter equals X
                        db.delete(DATABASE_TABLE_SHOT, ROUND_ID + "=" + thisRoundID, null);
                        db.delete(DATABASE_TABLE_ROUND, ROW_ID + "=" + thisRoundID, null);

                        Intent intentGoToListOfRounds = new Intent(RoundSummary.this, ListOfRounds.class);
                        startActivity(intentGoToListOfRounds);
                    }
                })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

    AlertDialog alertDialog = builder.create();
    alertDialog.show();

    }//deleteRound




    /**
     * This method is called when the 'Export' button is clicked.
     * It checks to see if there is an active internet connection and
     * if there is, it calls the ExportShots method.
     * @param view The view that was clicked
     */
    public void checkConnectionAndExport(View view) {
        Log.i(TAG, "checkConnectionAndExport started");
        if(checkInternetConenction()) {
            //ExportShots();
            ExportRoundMethod();
        }else{
            Toast toast = Toast.makeText(this,"NO INTERNET CONNECTION", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }//checkConnectionAndExport



    private boolean checkInternetConenction() {
        Log.i(TAG, "checkInternetConenction started");
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                //Toast.makeText(this, " Connected to Wifi.", Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                //Toast.makeText(this, " Connected to Data plan.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }//checkInternetConenction


    /**
     * This method is used to Export the golf round to a web based database.
     * It also calls the ExportShots methos to export the individual shots.
     */
    public void ExportRoundMethod(){
        Log.i(TAG, "ExportRoundMethod started");

        SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
        //We don’t need to write to the database so we’re using getReadableDatabase().
        SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();
        String query = "select * from round where _id = " + thisRoundID + ";" ;

        Log.i(TAG, "Export Query is: " + query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            //iterate over rows

            //String loginIDAsString = Integer.toString(LoginScript.loginID);
            String start_date = cursor.getString(1);
                Double sg_driving = cursor.getDouble(2);
            String sg_drivingAsString = Double.toString(sg_driving);
                Double sg_long_game = cursor.getDouble(3);
            String sg_long_gameAsString = Double.toString(sg_long_game);
                Double sg_putting = cursor.getDouble(4);
            String sg_puttingAsString = Double.toString(sg_putting);
                Double sg_short_game = cursor.getDouble(5);
            String sg_short_gameAsString = Double.toString(sg_short_game);
            String course_name = cursor.getString(9);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date d = dateFormat.parse(start_date);
                formatted_start_date = dateFormat.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "start date is: " + formatted_start_date);
            String roundIDString = Integer.toString(thisRoundID);


                new ExportRound(this).execute(LoggedInId, course_name, formatted_start_date, sg_drivingAsString, sg_long_gameAsString, sg_short_gameAsString, sg_puttingAsString, roundIDString);

        }//if
        cursor.close();
        ExportShots();
    }//ExportRoundMethod


    /**
     * This method exports the individual shots to a web based database.
     */
    public void ExportShots(){
        Log.i(TAG, "Export started**");

        //Sets the counter for returnedinfo to 0
        ExportShots.returnedinfo = 0;

        SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
        //We don’t need to write to the database so we’re using getReadableDatabase().
        SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();
        //Use cursor to get number of rows where hole_counter equals X

        String queryrows = "select COUNT(_id) from shot where round_id = " + thisRoundID + ";" ;
        //Log.i(TAG, "Export Query is: " + query1);
        Cursor cursor = db.rawQuery(queryrows, null);
        if (cursor.moveToFirst()) {
            totalrows = cursor.getInt(0);
        }
        cursor.close();

        //Log.i(TAG, "1a TOTAL ROWS: " + totalrows);
        // create and display a new ProgressBarDialog
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Exporting Round");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(totalrows);
        progressBar.show();

        new Thread(new Runnable() {

            public void run() {
                //Log.i(TAG, "while loop started");
                while (ExportShots.returnedinfo < totalrows) {

                    Log.d(TAG, "RS TOTAL ROWS: " + totalrows);//!!


                    // Update the progress bar
                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(ExportShots.returnedinfo);
                        }
                    });
                }//while

                // if the file is downloaded,
                if (ExportShots.returnedinfo >= totalrows) {
                    //Log.i(TAG, "ExportShots.progress is: " + ExportShots.progress);
                    // sleep 2 seconds, so that you can see the 100%
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // and then close the progressbar dialog
                    progressBar.dismiss();
                }
            }
        }).start();

        myTestDatabaseHelper = new DatabaseHelper(this);
        //We don’t need to write to the database so we’re using getReadableDatabase().
        db = myTestDatabaseHelper.getReadableDatabase();
        //Use cursor to get number of rows where hole_counter equals X

        String query1 = "select * from shot where round_id = " + thisRoundID + ";" ;
        //Log.i(TAG, "Export Query is: " + query1);
        cursor = db.rawQuery(query1, null);
        if (cursor.moveToFirst()) {
            //iterate over rows
            for (int i = 0; i < cursor.getCount(); i++) {
                Double latitude = cursor.getDouble(1);
                String latAsString = Double.toString(latitude);
                Double longitude = cursor.getDouble(2);
                String longAsString = Double.toString(longitude);
                String hit_from = cursor.getString(3);
                Double shot_score = cursor.getDouble(4);
                String shotScoreAsString = Double.toString(shot_score);
                Integer hole = cursor.getInt(5);
                String holeAsString = Integer.toString(hole);
                Integer shot_number = cursor.getInt(6);
                String shotNumberAsString = Integer.toString(shot_number);
                Double shot_distance = cursor.getDouble(7);
                String shotDistAsString = Double.toString(shot_distance);
                Integer round_id = cursor.getInt(8);
                String roundIDAsString = Integer.toString(round_id);
                //Log.i(TAG, "latAsString is: " + latAsString);
                cursor.moveToNext();
                new ExportShots(this).execute(latAsString, longAsString, hit_from, shotScoreAsString, holeAsString, shotNumberAsString, shotDistAsString, roundIDAsString, LoggedInId);

            }//for

        }//if

    }//ExportShots method





    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed RS started**");
        Intent intentReturnToListOfRounds = new Intent(RoundSummary.this,ListOfRounds.class);
        startActivity(intentReturnToListOfRounds);
    }

}//RoundSummary class

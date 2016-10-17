package com.example.sitting_room.GolfShotRating;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class OnGreen extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

    private static final String TAG = "TEST";
    public static long POLLING_FREQ = 0;//milliseconds.
    private Vibrator vibe;
    private GestureDetectorCompat mDetector;
    private int shotNumber;
    private static int holeNumber;
    public static int shot_counter = 1;
    public static int roundID;
    public static double shotLat;
    public static double shotLong;
    public static double lastLat;
    public static double lastLong;
    public static int lastRowId;
    public static int thisRow;
    public static double distanceBallWasHit;
    public static int arrayCounter = 0;
    public static int firstId;
    public static int pinTagged = 0;
    public static Integer i;
    public static String hit_from;
    public static int penalty;
    public static double distanceRating;
    public static int roundedDistance;
    public static int numberOfRows;
    public static double distance_rating;
    public static double[] shot_distance_rating_array;
    public static double[] final_distance_array;
    public static double[] final_ratings_array;


    public static String isMapping;
    private static ImageView mappingButton;
    TextView greenShotNumber;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        vibe = (Vibrator) OnGreen.this.getSystemService(Context.VIBRATOR_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_green);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        isMapping = sharedPreferences.getString("isMapping", "");
        //Log.i(TAG,"OG isMapping: " + isMapping);


        shotNumber = getIntent().getIntExtra("shot_number", 0);
        Log.i(TAG, "OG Recieved shot number: " + shotNumber);

        roundID = getIntent().getIntExtra("roundID", 0);
        //Log.i(TAG, "Green Recieved roundID: " + roundID);

        holeNumber = getIntent().getIntExtra("hole_number", 0);
        //Log.i(TAG, "O.G. Recieved hole number: " + holeNumber);

        greenShotNumber = (TextView)findViewById(R.id.greenShotNumber);
        greenShotNumber.setText("HOLE " + holeNumber + "- SHOT " + shotNumber);

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);



        //If th euser is not mapping the green then hide the Tagging button.
        if(isMapping.equals("no"))
        {
            mappingButton = (ImageView)findViewById(R.id.tagGreenButton);
            mappingButton.setVisibility(View.GONE);
        }
    }//onCreate



    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        //Log.i(TAG, "onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //mGestureText.setText("onFling " + e1.getX() + " - " + e2.getX());

        if (e1.getX() < e2.getX()) {
            //Log.d(TAG, "Left to Right swipe performed");
            //ShotInputScreen ShotInputScreen = new ShotInputScreen();
            vibe.vibrate(300);
            addShotFromGreenToDB();

            /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 10 seconds
                }
            }, 1000);*/
            //AddFlagData();
        }

        if (e1.getX() > e2.getX()) {
            //Log.d(TAG, "Right to Left swipe performed");
            vibe.vibrate(300);
            addShotFromGreenToDB();
        }

        if (e1.getY() < e2.getY()) {
            //Log.d(TAG, "Up to Down swipe performed");
        }

        if (e1.getY() > e2.getY()) {
            //Log.d(TAG, "Down to Up swipe performed");
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //Log.i(TAG, "onLongPress: " + event.toString());
        //vibe.vibrate(300);
        //addShotFromGreenToDB();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        //Log.i(TAG, "onScroll: " + e1.toString() + e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.i(TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //Log.i(TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        //Log.i(TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        //Log.i(TAG, "onDoubleTapEvent: " + event.toString());
        //vibe.vibrate(300);
        //AddFlagData();
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        //Log.i(TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }


    /**
     * This method check that if the user has agreed to TAG the green that they have indeed Tagged this green before ending the hole.
     * If they have then the device vibrates and the AddFlagData method is called.
     * @param view The view that was clicked
     */
    public void flagButton(View view){
        if(isMapping.equals("yes") & pinTagged == 0)
        {
            Toast toast = Toast.makeText(OnGreen.this, "You have to TAG the green before clicking the FLAG icon.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            vibe.vibrate(300);
            AddFlagData();
        }
    }




    public void addShotFromGreenToDB() {
        DatabaseHelper dbHandler = new DatabaseHelper(OnGreen.this);

        Double latitude_coord = ShotInputScreen.latitude;
        Double longitude_coord = ShotInputScreen.longitude;
        Integer hole = ShotInputScreen.hole_counter;

        Integer roundID = ShotInputScreen.roundID;
        String place = "green";

        dbHandler.addShotToDB(latitude_coord, longitude_coord, place, hole, shotNumber, roundID);
        shotNumber++;
        Log.i(TAG,"OG Shot number is now: " + shotNumber);
        greenShotNumber.setText("HOLE " + holeNumber + "- SHOT " + shotNumber);
        //TextView holeNumberTextview = (TextView) findViewById(R.id.hole_number);
        //holeNumberTextview.setText("HOLE " + hole_counter + "- SHOT " + shot_counter);
    }//addShotFromGreenToDB


    public void AddFlagData() {
        //Log.i(TAG, "AddFlagData - started");
        //AlertDialog to check if user actually wants to submit shot
        //and its not a case of clicking button by accident.

        //Log.i(TAG, "hole counter 1 is: " + ShotInputScreen.hole_counter);
        //Submit the position of the Flag.
        DatabaseHelper dbHandler = new DatabaseHelper(OnGreen.this);

        Double latitude_coord = ShotInputScreen.latitude;
        Double longitude_coord = ShotInputScreen.longitude;
        String place = "flag";
        //Integer shot = 5;

        dbHandler.addShotToDB(latitude_coord, longitude_coord, place, ShotInputScreen.hole_counter, shotNumber, ShotInputScreen.roundID);
        calculateShotScore();
    }//AddFlagData

    public void calculateShotScore(){
        Log.i(TAG, "calculateShotScore started.");
        try {
            //Log.i(TAG, "hole counter 2 is: " + hole_counter);
            SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(OnGreen.this);
            //We don’t need to write to the database so we’re using getReadableDatabase().
            SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();

            //Get the location of the Flag.
            //All shot distances will be calculated to this point.
            String query10 = "SELECT _id, latitude, longitude FROM shot WHERE hole = " + holeNumber + " AND round_id = " + roundID + ";";
            //Log.i(TAG, "query10 is: " + query10);
            Cursor cursor = db.rawQuery(query10, null);
            if (cursor.moveToFirst()) {
                //Log.i(TAG, "Move to last started");
                firstId = cursor.getInt(0);
                //Log.i(TAG, "First Id is: " + firstId);
            }
            //move cursor to last record...where flag is.
            if (cursor.moveToLast()) {
                //Log.i(TAG, "Move to last started");
                lastLat = cursor.getDouble(1);
                lastLong = cursor.getDouble(2);
                lastRowId = cursor.getInt(0);
            }
            cursor.close();

            //Use cursor to get the number of rows/shots for the relevant hole and
            //initialise the shot_distance_rating array.
            String query = "Select COUNT(_id) AS rows from shot WHERE hole = " + holeNumber + " AND round_id = " + roundID + ";";
            //Log.i(TAG, "QUERY 1 is: " + query1);
            Cursor cursor1 = db.rawQuery(query, null);
            if (cursor1.moveToFirst()) {
                numberOfRows = cursor1.getInt(0);
                //Log.i(TAG, "Number of rows/shots for this hole: " + numberOfRows);
                shot_distance_rating_array = new double[numberOfRows];
            }
            cursor1.close();


            //Use cursor to get _id, lat and long from shot table where hole = hole_counter
            String query11 = "SELECT _id, latitude, longitude, hit_from, penalty FROM shot WHERE hole = " + holeNumber + " AND round_id = " + roundID + ";";
            //Log.i(TAG, "QUERY 11 is: " + query11);
            Cursor cursor2 = db.rawQuery(query11, null);

            // Iterate through the results one at a time
            // until the last row is reached
            // i.e. each 'while loop' is for each shot on the hole
            while (cursor2.moveToNext()) {
                thisRow = cursor2.getInt(0);

                //if this is not the last row / shot
                if (thisRow != lastRowId) {
                    shotLat = cursor2.getDouble(1);
                    shotLong = cursor2.getDouble(2);
                    hit_from = cursor2.getString(3);
                    penalty = cursor2.getInt(4);
                    //Log.i(TAG, "Hit_from is: '" + hit_from + "'");


                    //send coordinates to calculateDistanceFromCoordinates method to calculate distance.
                    //Log.i(TAG, "shotLat is: " + shotLat);
                    //Log.i(TAG, "shotLong is: " + shotLong);
                    //Log.i(TAG, "lastLat is: " + lastLat);
                    //Log.i(TAG, "lastLat is: " + lastLong);
                    distanceBallWasHit = calculateDistanceFromCoordinates(shotLat, shotLong, lastLat, lastLong);


                    //Log.i(TAG, "************************************************************");
                    //Log.i(TAG, "DISTANCE RETURNED IS:  : " + distanceBallWasHit + " yards.");
                    //Log.i(TAG, "************************************************************");

                    //Submit the distances (in yards) to the databases.
                    //If shot was a putt then multiply distance by 3 to get distance in feet.
                    if (hit_from.equals("green")) {
                        //Log.i(TAG, "1 HIT FROM WAS GREEN!!!!!!!!!!!!!!!!!!");
                        distanceBallWasHit = distanceBallWasHit * 3;
                    }
                    DatabaseHelper dbHandler2 = new DatabaseHelper(OnGreen.this);
                    dbHandler2.addDistancesToDB(distanceBallWasHit, thisRow);

                    //Calls relevant method to round off distance.
                    //Distances are rounded to nearest distance bracket and returned as an integer.
                    if (hit_from.equals("green")) {
                        //Log.i(TAG, "2  HIT FROM WAS GREEN!!!!!!!!!!!!!!!!!!");
                        //Send distance the ball was hit to the roundOfPuttingDistance method
                        // which rounds off distance to nearest distance increment in feet and returns an integer.
                        roundedDistance = RoundOffPuttingDistance(distanceBallWasHit);
                        //Log.i(TAG, "Green Rounded distance is: " + roundedDistance);
                        //Sends the rounded off distance to the getPuttingDistanceRating method
                        //which gets a rating for the distance from the putt_baseline table.
                        distance_rating = GetPuttingDistanceRating(roundedDistance);
                        //Log.i(TAG, "Green Distance rating from table is: " + distance_rating);
                    } else {
                        //Log.i(TAG, "HIT FROM WAS Fairway!!!!!!!!!!!!!!!!!!");
                        //Send distance the ball was hit to the roundOfStrokeDistance method
                        // which rounds off distance to nearest 20 yard increment and returns an integer.
                        roundedDistance = RoundOffStrokeDistance(distanceBallWasHit);
                        //Log.i(TAG, "Fairway Rounded distance is: " + roundedDistance);
                        //Sends the rounded off distance to the getStrokeDistanceRating method
                        //which gets a rating for the distance from the stroke_baseline table.
                        distance_rating = GetStrokeDistanceRating(roundedDistance, hit_from);
                        //Log.i(TAG, "Fairway Distance rating from table is: " + distance_rating);
                    }
                    //put distance_rating into array here.
                    //Log.i(TAG, "ArrayCounter is : " + arrayCounter);
                    shot_distance_rating_array[arrayCounter] = distance_rating;
                    //Log.i(TAG, "Value in shot_distance_rating array poition "
                            //+ arrayCounter + " is:" + shot_distance_rating_array[arrayCounter]);
                    arrayCounter++;

                }//if (thisRow != lastRowId)
                else {  //this is the last row / shot
                    shot_distance_rating_array[arrayCounter] = 0.0;
                    arrayCounter = 0;//reset arrayCounter
                    //Last row for this hole has been reached so calculate shot scores.
                    //Log.i(TAG, "Row Ids are the same...this is the last row");
                    final_ratings_array = CalcScoreFromArray(shot_distance_rating_array);

                    //Log.i(TAG, "***VALUE IN final_distance_array 2 " + CalcScoreFromArray(shot_distance_rating)[0]);
                    //Log.i(TAG, "***VALUE IN final_distance_array 0 " + final_ratings_array[0]);
                    //Log.i(TAG, "***VALUE IN final_distance_array 1 " + final_ratings_array[1]);
                    //Log.i(TAG, "***VALUE IN final_distance_array 2 " + final_ratings_array[2]);
                    for (int counter = 0; counter < final_ratings_array.length; counter++) {
                        int Id = firstId + counter;
                        DatabaseHelper dbHandler = new DatabaseHelper(OnGreen.this);
                        dbHandler.addFinalRatingToDB(Id, final_distance_array[counter]);
                    }//for
                }//else

            }//while (finished going through distances and getting ratings.)
            cursor2.close();

            db.close();
            POLLING_FREQ = 5000;


            // If the hole number is less than 18 then create the Intent and
            // start the activity to get the hole summary and send the number
            // of the hole. If the hole number is 18
            // then create the Intent to get the round summary
            if (holeNumber < 18) {//change to 18 after testing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                Log.i(TAG, "O.G. Sending to HoleSummary: " + holeNumber);
                //Log.i(TAG, "O.G. Hole_counter is: " + hole_counter);
                Intent intentGetHoleSummary = new Intent(OnGreen.this, HoleSummary.class);
                intentGetHoleSummary.putExtra("HoleNumber", holeNumber);
                intentGetHoleSummary.putExtra("NumberOfShots", shotNumber);
                intentGetHoleSummary.putExtra("roundID", roundID);
                Log.i(TAG, "Sending to HoleSummary roundID: " + roundID);
                startActivity(intentGetHoleSummary);
            } else {
                Log.i(TAG, "Sending to RoundSummary");
                if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    ShotInputScreen.mLocationManager.removeUpdates(ShotInputScreen.mLocationListener);
                }
                Intent intentGetRoundSummary = new Intent(OnGreen.this, RoundSummary.class);
                intentGetRoundSummary.putExtra("roundID", roundID);
                startActivity(intentGetRoundSummary);
            }




        }//try
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(OnGreen.this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }//calculateShotScore


    /**
     * This function converts decimal degrees to radians.
     * @param deg The degrees to be converted into Radians.
     * @return Result in Radians
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }




    /**
     * This function converts radians to decimal degrees.
     * @param rad The Radians to be converted into degrees.
     * @return Results in Degress
     */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public void goToGreen(View view){
        Intent intentGoToGreenScreen = new Intent(OnGreen.this,OnGreen.class);
        //Log.i(TAG, "goToGreen started");
        intentGoToGreenScreen.putExtra("roundID", roundID);
        intentGoToGreenScreen.putExtra("shot_number", shot_counter);
        intentGoToGreenScreen.putExtra("hole_number", holeNumber);
        //Log.i(TAG, "Sending to GREEN sn: " + shot_counter);
        startActivity(intentGoToGreenScreen);

    }


    /**
     * This method is called when the user clicks the image to TAG the green.
     * A dialogue box check that they didi intend to click the button.
     * The coordinates are recorded in shared preferences, the device vibrates and the pinTagged
     * variable is set to 1 to indicate that the green is Tagged. This is done to stop the user
     * finisheing the hole without tagging the green.
     * @param view The view that was clicked.
     */
    public void tagGreen(View view)
    {
        //AlertDialog to check if user actually wants to tag this green
        //and its not a case of clicking button by accident.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to TAG this as the center of the green?");
        //builder.setMessage("Some message...")
        builder.setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String coordinates = ShotInputScreen.latitude + "," + ShotInputScreen.longitude;
                        SharedPreferences sharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(OnGreen.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("hole_" + holeNumber + "_pinCoord", coordinates);
                        editor.apply();
                        pinTagged = 1;

                        vibe.vibrate(300);
                        mappingButton = (ImageView)findViewById(R.id.tagGreenButton);
                        mappingButton.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * This method calculates the actual distance between the GPS coordinates and returns the distance in yards.
     * The formula used is the 'haversine' formula.
     * @param Lat The latitude coordinate from where the ball was hit.
     * @param Long The longitude coordinate from where the ball was hit.
     * @param lastLat The latitude coordinate where the flag is.
     * @param lastLong The longitude coordinate where the flag is.
     * @return dist: the distance in yards from the ball to the flag.
     */
    private static double calculateDistanceFromCoordinates(Double Lat, Double Long, Double lastLat, Double lastLong) {
        double theta = Long - lastLong;
        double dist = Math.sin(deg2rad(Lat)) * Math.sin(deg2rad(lastLat)) + Math.cos(deg2rad(Lat)) * Math.cos(deg2rad(lastLat)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        dist = dist * 1000;
        //Log.i(TAG, "The Distance for row " + thisRow + " is:");
        //Log.i(TAG, "M1 - DISTANCE BETWEEN IS:  : " + dist + " meters.");

        dist = dist * 1.09361;
        //Log.i(TAG, "DISTANCE BETWEEN IS:  : " + dist + " yards.");

        return dist;
    }//calculateDistanceFromCoordinates


    /**
     * This method calculates the final score for each shot.
     * It gets the rating for each distance and subtracts the next distance to
     * get a rating of how far the golfer advanced the ball toward the flag.
     * It then subtracts 1 (to account for the stroke the golfer took
     * and puts the final value in the final_distance_array.
     * @param shotArray The array with ratings for the shot distances
     */
    public static double[] CalcScoreFromArray(double[] shotArray){
        final_distance_array = new double[shotArray.length];
        //Log.i(TAG, "CalcScoreFromArray started");
        //Log.i(TAG, "shotArray length is: " + shotArray.length);
        //Log.i(TAG, "Value in CalcScoreFromArray position 0 is: " + shotArray[0]);
        //Log.i(TAG, "Value in CalcScoreFromArray position 1 is: " + shotArray[1]);
        //Log.i(TAG, "Value in CalcScoreFromArray position 2 is: " + shotArray[2]);
        //Log.i(TAG, "Value in CalcScoreFromArray position 2 is: " + shotArray[3]);
        //Log.i(TAG, "***********************************************************");
        for(int y = 0; y < shotArray.length - 1; y++){
            //Log.i(TAG, "shotArray[" + y + "] is: " + shotArray[y]);
            Double newScore = shotArray[y] - shotArray[y + 1];
            //Log.i(TAG, "newScore value LOOP "+ y + " is " + newScore);
            Double finalRating = newScore - 1;
            //Log.i(TAG, "finalRating value LOOP "+ y + " is " + finalRating);
            final_distance_array[y] = finalRating;
            //Log.i(TAG, "Value in final_distance_array[" + y + "] is: " + final_distance_array[y]);
            //Log.i(TAG, "***********************************************************");

        }//for
        return final_distance_array;
    }//CalcScoreFromArray



    //Uses the rounded distance and the lie to retrieve the distance rating from the stroke_baseline table.
    public Double GetStrokeDistanceRating(Integer roundedDistance, String lie){//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //need to change this and add in columns in stroke_baseline table to account for different lies.
        try {
            //lie = "fairway";//this is for testing
            if(lie.equals("leftRough") || lie.equals("rightRough")){
                lie = "rough";
            }
            if(lie.equals("leftTrees") || lie.equals("rightTrees")){
                lie = "recovery";
            }
            if(lie.equals("leftBunker") || lie.equals("rightBunker")){
                lie = "bunker";
            }
            //Log.i(TAG, "getStrokeDistanceRating LIE is: " + lie);
            SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
            SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();

            //Get number of rows/shots for the relevant hole.
            //This number will be used to create the loop to loop through the rows.
            String query = "Select " + lie + " from stroke_baseline WHERE distance = " + roundedDistance + ";";
            //Log.i(TAG, "getStrokeDistanceRating Query is: " + query);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                distanceRating = cursor.getDouble(0);
                //Log.i(TAG, "getStrokeDistanceRating returns: " + distance_rating);
            }
            cursor.close();

        }//try
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(OnGreen.this, "CalacShotScore Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return distanceRating;
    }//getStrokeDistanceRating


    //Uses the rounded distance and the lie to retrieve the distance rating from the stroke_baseline table.
    public Double GetPuttingDistanceRating(Integer roundedDistance){
        try {
            SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
            SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();

            //Get number of rows/shots for the relevant hole.
            //This number will be used to create the loop to loop through the rows.
            String query = "Select ave_putts from putt_baseline WHERE distance = " + roundedDistance + ";";
            //Log.i(TAG, "getPuttingDistanceRating query is: " + query);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                distanceRating = cursor.getDouble(0);
                //firstId = cursor1.getInt(1);
                //Log.i(TAG, "No of ROWS is: " + rows);
                //Log.i(TAG, "ID is: " + firstId);
            }
            cursor.close();

        }//try
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(OnGreen.this, "CalacShotScore Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return distanceRating;
    }//getPuttngDistanceRating




    public static Integer RoundOffStrokeDistance(Double distance) {
        if(distance <=20){
            distance = 20.0;
        }
        else if(distance >=21 && distance <=40){
            distance = 40.0;
        }
        else if(distance >=41 && distance <=60){
            distance = 60.0;
        }
        else if(distance >=61 && distance <=80){
            distance = 80.0;
        }
        else if(distance >=81 && distance <=100){
            distance = 100.0;
        }
        else if(distance >=101 && distance <=120){
            distance = 120.0;
        }
        else if(distance >=121 && distance <=140){
            distance = 140.0;
        }
        else if(distance >=141 && distance <=160){
            distance = 160.0;
        }
        else if(distance >=161 && distance <=180){
            distance = 180.0;
        }
        else if(distance >=181 && distance <=200){
            distance = 200.0;
        }
        else if(distance >=201 && distance <=220){
            distance = 220.0;
        }
        else if(distance >=221 && distance <=240){
            distance = 240.0;
        }
        else if(distance >=241 && distance <=260){
            distance = 260.0;
        }
        else if(distance >=261 && distance <=280){
            distance = 280.0;
        }
        else if(distance >=281 && distance <=300){
            distance = 300.0;
        }
        else if(distance >=300 && distance <=320){
            distance = 320.0;
        }
        else if(distance >=321 && distance <=340){
            distance = 340.0;
        }
        else if(distance >=341 && distance <=360){
            distance = 360.0;
        }
        else if(distance >=361 && distance <=380){
            distance = 380.0;
        }
        else if(distance >=381 && distance <=400){
            distance = 400.0;
        }
        else if(distance >=401 && distance <=420){
            distance = 420.0;
        }
        else if(distance >=421 && distance <=440){
            distance = 440.0;
        }
        else if(distance >=441 && distance <=460){
            distance = 460.0;
        }
        else if(distance >=461 && distance <=480){
            distance = 480.0;
        }
        else if(distance >=481 && distance <=500){
            distance = 500.0;
        }
        else if(distance >=501 && distance <=520){
            distance = 520.0;
        }
        else if(distance >=521 && distance <=540){
            distance = 540.0;
        }
        else if(distance >=541 && distance <=560){
            distance = 560.0;
        }
        else if(distance >=561 && distance <=580){
            distance = 580.0;
        }
        else if(distance >=581 && distance <=600){
            distance = 600.0;
        }
        return distance.intValue();
    }//roundOffDistance


    public static Integer RoundOffPuttingDistance(Double distance) {
        if(distance <=2){
            distance = 2.0;
        }
        else if(distance >=2 && distance <=3){
            distance = 3.0;
        }
        else if(distance >=3 && distance <=4){
            distance = 4.0;
        }
        else if(distance >=4 && distance <=5){
            distance = 5.0;
        }
        else if(distance >=5 && distance <=6){
            distance = 6.0;
        }
        else if(distance >=6 && distance <=7){
            distance = 7.0;
        }
        else if(distance >=7 && distance <=8){
            distance = 8.0;
        }
        else if(distance >=8 && distance <=9){
            distance = 9.0;
        }
        else if(distance >=9 && distance <=10){
            distance = 10.0;
        }
        else if(distance >=10 && distance <=15){
            distance = 15.0;
        }
        else if(distance >=15 && distance <=20){
            distance = 20.0;
        }
        else if(distance >=20 && distance <=30){
            distance = 30.0;
        }
        else if(distance >=30 && distance <=40){
            distance = 40.0;
        }
        else if(distance >=40 && distance <=50){
            distance = 50.0;
        }
        else if(distance >=50 && distance <=60){
            distance = 60.0;
        }
        else if(distance >=60 && distance <=120){
            distance = 90.0;
        }
        return distance.intValue();
    }//roundOffPuttingDistance



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}//class







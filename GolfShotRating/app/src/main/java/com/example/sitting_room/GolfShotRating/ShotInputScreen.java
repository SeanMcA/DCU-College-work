package com.example.sitting_room.GolfShotRating;

//The 'ContextCompat.checkSelfPermission' code for checking location manager permissions is from:
//http://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager

//Check permissions code for location manager developed from code by user eldivino87 on StackOverflow:
//http://stackoverflow.com/questions/33327984/call-requires-permissions-that-may-be-rejected-by-user


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShotInputScreen extends AppCompatActivity {

    public static long POLLING_FREQUENCY = 0;//milliseconds.
    private static float MIN_DISTANCE = 1;//meters

    public static int hole_counter;
    public static int shot_counter;
    public static int roundID;
    public static double latitude;
    public static double longitude;
    public static String place;
    private static Button mydriver;
    private static Button myiron;
    private static Button fairway;
    private static Button rough;
    private static Button trees;
    private static Button bunker;
    private static Button penalty;
    private static Button onGreen;
    public static Double pinLatitude;
    public static Double pinLongitude;
    public static Double distToGreen;
    public static String LoggedInId;
    public static  String isCourseMapped;
    public static LocationManager mLocationManager;
    public static LocationListener mLocationListener;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    private static final String TAG = "TEST";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.i(TAG, "MAIN - onCreate - started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_input_screen);



        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        LoggedInId = sharedPreferences.getString("key", "notLoggedIn");
        isCourseMapped = sharedPreferences.getString("mapped", "");
        roundID = sharedPreferences.getInt("roundID", 0);

        //Log.i(TAG, "Recieved roundID: " + roundID);

        //Log.i(TAG, "SIS LoginID is: " + LoggedInId);
        shot_counter = 0;
        Log.i(TAG, "SIS shot_counter is: " + shot_counter);



        hole_counter = getIntent().getIntExtra("HoleNumber", 0);
        //Log.i(TAG, "Recieved hole number: " + hole_counter);



        //This part disables the buttons and 'grays' them. When the GPS accuracy
        // is 3 meters or better the buttons will be enabled.
        onGreen = (Button)findViewById(R.id.green);
        onGreen.setEnabled(false);
        onGreen.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        penalty = (Button)findViewById(R.id.out_of_bounds);
        penalty.setEnabled(false);
        penalty.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        mydriver = (Button)findViewById(R.id.driver);
        mydriver.setEnabled(false);
        mydriver.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        myiron = (Button)findViewById(R.id.iron);
        myiron.setEnabled(false);
        myiron.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        fairway = (Button)findViewById(R.id.fairway);
        fairway.setEnabled(false);
        fairway.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        rough = (Button)findViewById(R.id.right_rough);
        rough.setEnabled(false);
        rough.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        trees = (Button)findViewById(R.id.right_trees);
        trees.setEnabled(false);
        trees.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

        bunker = (Button)findViewById(R.id.right_bunker);
        bunker.setEnabled(false);
        bunker.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);



        // Define a listener that responds to location updates
        mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };//LocationListener



            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, POLLING_FREQUENCY, MIN_DISTANCE, locationListener);
            //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, POLLING_FREQUENCY, MIN_DISTANCE, locationListener);

            //LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, POLLING_FREQUENCY, MIN_DISTANCE, locationListener);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, POLLING_FREQUENCY, MIN_DISTANCE, mLocationListener);
        }


        if(hole_counter == 0)
        {
            hole_counter = 1;
        }

        TextView holeNumberTextview = (TextView) findViewById(R.id.hole_number);
        //Log.i(TAG, "MAIN holenumberTextView is : " + hole_counter);

        //holeNumberTextview.setText(R.string.Hole + hole_counter);//This or next line not working?!!!
        //holeNumberTextview.setText(R.string.Hole + hole_counter + R.string.Shot + shot_counter);
        holeNumberTextview.setText("Hole: " + hole_counter + " - Shot: " + shot_counter);

        //Checks shared pref to see if course has been mapped. I f'yes then get lat and long for the relevant hole
        if(isCourseMapped.equals("yes"))
        {
            //Checs to see which hole the golfer is playing and retrieves the lat and long for that
            //hole from shared preferences.
            if (hole_counter == 1) {
                String h1latString = sharedPreferences.getString("h1lat", "0");
                pinLatitude = Double.parseDouble(h1latString);
                String h1longString = sharedPreferences.getString("h1long", "0");
                pinLongitude = Double.parseDouble(h1longString);
                Log.i(TAG, "SIS h1lat: " + pinLatitude);
            } else if (hole_counter == 2) {
                String h2latString = sharedPreferences.getString("h2lat", "0");
                pinLatitude = Double.parseDouble(h2latString);
                String h2longString = sharedPreferences.getString("h2long", "0");
                pinLongitude = Double.parseDouble(h2longString);
                Log.i(TAG, "SIS h2lat: " + pinLatitude);
            } else if (hole_counter == 3) {
                String h3latString = sharedPreferences.getString("h3lat", "0");
                pinLatitude = Double.parseDouble(h3latString);
                String h3longString = sharedPreferences.getString("h3long", "0");
                pinLongitude = Double.parseDouble(h3longString);
            } else if (hole_counter == 4) {
                String h4latString = sharedPreferences.getString("h4lat", "0");
                pinLatitude = Double.parseDouble(h4latString);
                String h4longString = sharedPreferences.getString("h4long", "0");
                pinLongitude = Double.parseDouble(h4longString);
            } else if (hole_counter == 5) {
                String h5latString = sharedPreferences.getString("h5lat", "0");
                pinLatitude = Double.parseDouble(h5latString);
                String h5longString = sharedPreferences.getString("h5long", "0");
                pinLongitude = Double.parseDouble(h5longString);
            } else if (hole_counter == 6) {
                String h6latString = sharedPreferences.getString("h6lat", "0");
                pinLatitude = Double.parseDouble(h6latString);
                String h6longString = sharedPreferences.getString("h6long", "0");
                pinLongitude = Double.parseDouble(h6longString);
            } else if (hole_counter == 7) {
                String h7latString = sharedPreferences.getString("h7lat", "0");
                pinLatitude = Double.parseDouble(h7latString);
                String h7longString = sharedPreferences.getString("h7long", "0");
                pinLongitude = Double.parseDouble(h7longString);
            } else if (hole_counter == 8) {
                String h8latString = sharedPreferences.getString("h8lat", "0");
                pinLatitude = Double.parseDouble(h8latString);
                String h8longString = sharedPreferences.getString("h8long", "0");
                pinLongitude = Double.parseDouble(h8longString);
            } else if (hole_counter == 9) {
                String h9latString = sharedPreferences.getString("h9lat", "0");
                pinLatitude = Double.parseDouble(h9latString);
                String h9longString = sharedPreferences.getString("h9long", "0");
                pinLongitude = Double.parseDouble(h9longString);
            } else if (hole_counter == 10) {
                String h10latString = sharedPreferences.getString("h10lat", "0");
                pinLatitude = Double.parseDouble(h10latString);
                String h10longString = sharedPreferences.getString("h10long", "0");
                pinLongitude = Double.parseDouble(h10longString);
            } else if (hole_counter == 11) {
                String h11latString = sharedPreferences.getString("h11lat", "0");
                pinLatitude = Double.parseDouble(h11latString);
                String h11longString = sharedPreferences.getString("h11long", "0");
                pinLongitude = Double.parseDouble(h11longString);
            } else if (hole_counter == 12) {
                String h12latString = sharedPreferences.getString("h12lat", "0");
                pinLatitude = Double.parseDouble(h12latString);
                String h12longString = sharedPreferences.getString("h12long", "0");
                pinLongitude = Double.parseDouble(h12longString);
            } else if (hole_counter == 13) {
                String h13latString = sharedPreferences.getString("h13lat", "0");
                pinLatitude = Double.parseDouble(h13latString);
                String h13longString = sharedPreferences.getString("h13long", "0");
                pinLongitude = Double.parseDouble(h13longString);
            } else if (hole_counter == 14) {
                String h14latString = sharedPreferences.getString("h14lat", "0");
                pinLatitude = Double.parseDouble(h14latString);
                String h14longString = sharedPreferences.getString("h14long", "0");
                pinLongitude = Double.parseDouble(h14longString);
            } else if (hole_counter == 15) {
                String h15latString = sharedPreferences.getString("h15lat", "0");
                pinLatitude = Double.parseDouble(h15latString);
                String h15longString = sharedPreferences.getString("h15long", "0");
                pinLongitude = Double.parseDouble(h15longString);
            } else if (hole_counter == 16) {
                String h16latString = sharedPreferences.getString("h16lat", "0");
                pinLatitude = Double.parseDouble(h16latString);
                String h16longString = sharedPreferences.getString("h16long", "0");
                pinLongitude = Double.parseDouble(h16longString);
            } else if (hole_counter == 17) {
                String h17latString = sharedPreferences.getString("h17lat", "0");
                pinLatitude = Double.parseDouble(h17latString);
                String h17longString = sharedPreferences.getString("h17long", "0");
                pinLongitude = Double.parseDouble(h17longString);
            } else if (hole_counter == 18) {
                String h18latString = sharedPreferences.getString("h18lat", "0");
                pinLatitude = Double.parseDouble(h18latString);
                String h18longString = sharedPreferences.getString("h18long", "0");
                pinLongitude = Double.parseDouble(h18longString);
            }
        }//if




    }//onCreate


    /**
     * This method adds data to the database regarding a shot that was hit.
     * When the button is clicked, the id of the button is checked to see which button it was,
     * then depending on which button was clicked the String variable 'place' is set to the
     * correct value. Then an alert box is displayed to verify that the user wants to submit a shot
     * and that its not a case of a button being clicked in error.
     * If the user confirms that they want to submit the shot then
     * the latitude and longitude are retrieved along with the hole number and the shot number
     * as well as where the shot was hit from. This information is then sent to the addShot method
     * in the DatabaseHelper class.
     * @param view The view that was clicked.
     */
    public void buttonOnClick(View view)
    {
        switch(view.getId())
        {
            case R.id.driver:
                place = "tee_shot_driver";
                break;
            case R.id.iron:
                place = "tee_shot_iron";
                break;
            case R.id.fairway:
                place = "fairway";
                break;
            case R.id.right_trees:
                place = "rightTrees";
                break;
            case R.id.right_rough:
                place = "rightRough";
                break;
            case R.id.right_bunker:
                place = "rightBunker";
                break;
            case R.id.green:
                place = "green";
                NewRound.POLLING_FREQ = 2000;//Sets the GPS polling frequency to 2 seconds.
                break;
        }//switch


        //AlertDialog to check if user actually wants to submit shot
        //and its not a case of clicking button by accident.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to submit this shot?");

        //builder.setMessage("Some message...")
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //If user clicks 'ok' then run this code to send lat and long
                        //to addProduct method which is located in the DatabaseHelper class.
                        DatabaseHelper dbHandler = new DatabaseHelper(ShotInputScreen.this);

                        Double latitude_coord = latitude;
                        Double longitude_coord = longitude;

                        dbHandler.addShotToDB(latitude_coord, longitude_coord, place, hole_counter, shot_counter, roundID);
                        shot_counter++;
                        TextView holeNumberTextview = (TextView) findViewById(R.id.hole_number);
                        holeNumberTextview.setText("Hole: " + hole_counter + " - Shot: "+ shot_counter);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }//buttonOnClick




    /**
     * Upon change of location the coordinates are send to this method,
     * where the latitude and longitude variables are updated     *
     * @param loc The location coordinates.
     */
    private void makeUseOfNewLocation(Location loc) {

        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        float accuracy = loc.getAccuracy();
        //TextView accuracyTextView = (TextView)findViewById(R.id.accuracy);
        //accuracyTextView.setText("Accuracy is: " + accuracy);
        ImageView accuracyView = (ImageView)findViewById(R.id.gps_image);



        if(accuracy <= 5){
            POLLING_FREQUENCY = 1000;
            accuracyView.setImageResource(R.drawable.gps_green);

            penalty.setEnabled(true);
            penalty.getBackground().setColorFilter(null);

            onGreen.setEnabled(true);
            onGreen.getBackground().setColorFilter(null);

            mydriver.setEnabled(true);
            mydriver.getBackground().setColorFilter(null);

            myiron.setEnabled(true);
            myiron.getBackground().setColorFilter(null);

            fairway.setEnabled(true);
            fairway.getBackground().setColorFilter(null);

            rough.setEnabled(true);
            rough.getBackground().setColorFilter(null);

            trees.setEnabled(true);
            trees.getBackground().setColorFilter(null);

            bunker.setEnabled(true);
            bunker.getBackground().setColorFilter(null);
        }
        else {
            accuracyView.setImageResource(R.drawable.gps_red);
            mydriver = (Button)findViewById(R.id.driver);
            mydriver.setEnabled(false);
            mydriver.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

            myiron = (Button)findViewById(R.id.iron);
            myiron.setEnabled(false);
            myiron.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

            fairway = (Button)findViewById(R.id.fairway);
            fairway.setEnabled(false);
            fairway.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

            rough = (Button)findViewById(R.id.right_rough);
            rough.setEnabled(false);
            rough.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

            trees = (Button)findViewById(R.id.right_trees);
            trees.setEnabled(false);
            trees.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

            bunker = (Button)findViewById(R.id.right_bunker);
            bunker.setEnabled(false);
            bunker.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        }

        //only calculate distance if course has been mapped
        if(isCourseMapped.equals("yes")) {
            distToGreen = calculateDistanceFromCoordinates(latitude, longitude, pinLatitude, pinLongitude);
            Integer distToGreenInt = distToGreen.intValue();

            TextView distanceToGreenTV = (TextView) findViewById(R.id.distanceToGreen);
            distanceToGreenTV.setText("Distance to Green: " + distToGreenInt + " yards.");
        }
    }//makeUseOfNewLocation


    public void goToGreen(View view){
        Intent intentGoToGreenScreen = new Intent(ShotInputScreen.this,OnGreen.class);
        //Log.i(TAG, "goToGreen started");
        MIN_DISTANCE = 1;
        intentGoToGreenScreen.putExtra("roundID", roundID);
        intentGoToGreenScreen.putExtra("shot_number", shot_counter);
        intentGoToGreenScreen.putExtra("hole_number", hole_counter);
        //Log.i(TAG, "Sending to GREEN sn: " + shot_counter);
        startActivity(intentGoToGreenScreen);

    }

    /**
     * This method finds the id of the row /shot just entered and calls the
     * addPenaltyShotToDB method in the DatabaseHelper class which puts a 1 in the penalty
     * column of that shots row.
     * @param view The view that was clicked
     */
    public void addPenaltyShot(View view)    {
        //AlertDialog to check if user actually wants to submit shot
        //and its not a case of clicking button by accident.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to submit this shot?");

        //builder.setMessage("Some message...")
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(ShotInputScreen.this);
                        SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();

                        Integer penShotNum = shot_counter - 1;
                        String queryAPS = "SELECT _id FROM shot WHERE hole = "
                                            + hole_counter + " AND round_id = "
                                            + roundID
                                            + " AND shot_number = "
                                            + penShotNum  + ";";
                        Log.i(TAG, "QUERY APS is: " + queryAPS);
                        Cursor cursorAPS = db.rawQuery(queryAPS, null);

                        //Iterate through the results until the last row is reached
                        while (cursorAPS.moveToNext()) {
                            Integer thisRow = cursorAPS.getInt(0);

                            Log.i(TAG,"pen row is: " + thisRow);
                            DatabaseHelper dbHandler = new DatabaseHelper(ShotInputScreen.this);
                            dbHandler.addPenaltyShotToDB(thisRow, 1);
                        }//while
                        cursorAPS.close();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }//addPenaltyShot


    /**
     * This method calculates the actual distance between the GPS coordinates and returns the distance in yards.
     * The formula used is the 'haversine' formula.
     * @param Lat The latitude coordinate from where the ball was hit.
     * @param Long The longitude coordinate from where the ball was hit.
     * @param lastLat The latitude coordinate where the green is.
     * @param lastLong The longitude coordinate where the green is.
     * @return dist: the distance in yards from the ball to the green.
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
        //Log.i(TAG, "DISTANCE TO GREEN IS:  : " + dist + " yards.");

        return dist;
    }//calculateDistanceFromCoordinates


    /**
     * This function converts decimal degrees to radians.
     * @param deg The degrees to be converted into Radians.
     * @return Result in Radians
     */
    private static double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }



    /**
     * This function converts radians to decimal degrees.
     * @param rad The Radians to be converted into degrees.
     * @return Results in Degress
     */
    private static double rad2deg(double rad)
    {
        return (rad * 180 / Math.PI);
    }


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

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed RS started**");
    }



}//class


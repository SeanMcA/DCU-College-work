package com.example.sitting_room.GolfShotRating;

//Check permissions code for location manager developed from code by user eldivino87 on StackOverflow:
//http://stackoverflow.com/questions/33327984/call-requires-permissions-that-may-be-rejected-by-user

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewRound extends AppCompatActivity implements OnItemSelectedListener{
    TextView info;
    ListView listview;
    public static String today;
    public static String today2;
    public static int roundID;
    private static String item;
    private static String courseID;
    public static String coursePin;
    public static String yes = "yes";
    public static String no = "no";
    private static String selectedCourseName;
    public static String[] courseIDsArray;
    public static String[] courseNamesArray;
    public static String[] coursePinsArray;
    public static ArrayList<String> pins;
    public static Integer POLLING_FREQ = 0;
    TextView selected_course;
    public LocationManager mLocationManager;
    public LocationListener mLocationListener;
    private Vibrator vibe;

    private static double latitude;
    private static double longitude;
    private static final String TAG = "TEST";

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public static String LoggedInId;

    private static final String TAG_COURSE_INFO = "course_info";
    private static final String TAG_FACILITY_NAME = "facility_name";
    private static final String TAG_COURSE_NAME = "name";
    private static final String TAG_COURSE_ID = "course_id";
    private static final String TAG_COURSE_PINS = "pins";


    public static String  h1lat;
    public static String  h1long;
    public static String  h2lat;
    public static String  h2long;
    public static String  h3lat;
    public static String  h3long;
    public static String  h4lat;
    public static String  h4long;
    public static String  h5lat;
    public static String  h5long;
    public static String  h6lat;
    public static String  h6long;
    public static String  h7lat;
    public static String  h7long;
    public static String  h8lat;
    public static String  h8long;
    public static String  h9lat;
    public static String  h9long;
    public static String  h10lat;
    public static String  h10long;
    public static String  h11lat;
    public static String  h11long;
    public static String  h12lat;
    public static String  h12long;
    public static String  h13lat;
    public static String  h13long;
    public static String  h14lat;
    public static String  h14long;
    public static String  h15lat;
    public static String  h15long;
    public static String  h16lat;
    public static String  h16long;
    public static String  h17lat;
    public static String  h17long;
    public static String  h18lat;
    public static String  h18long;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_round);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        vibe = (Vibrator) NewRound.this.getSystemService(Context.VIBRATOR_SERVICE);

        Toast toast= Toast.makeText(getApplicationContext(),
                "Please wait while we find Golf Courses in your location.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 150);
        toast.show();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        LoggedInId = sharedPreferences.getString("key", "0");

        Log.i(TAG, "NewRound LoginID is: " + LoggedInId);



        selected_course = (TextView)findViewById(R.id.selected_course);

        // Define a listener that responds to location updates
        mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
                //lm.removeUpdates(this);
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };//LocationListener

        // Acquire reference to the LocationManager
        if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
            finish();





        Date date = Calendar.getInstance().getTime();
        DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        today2 = formatter2.format(date);
        info = (TextView) findViewById(R.id.info);
        //info.setText("" + today2);

        Log.i(TAG,"NewRound loginId: " + LoginScript.loginID);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("0");
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");
        categories.add("11");
        categories.add("12");
        categories.add("13");
        categories.add("14");
        categories.add("15");
        categories.add("16");
        categories.add("17");
        categories.add("18");
        categories.add("19");
        categories.add("20");
        categories.add("21");
        categories.add("22");
        categories.add("23");
        categories.add("24");
        categories.add("25");
        categories.add("26");
        categories.add("27");
        categories.add("28");
        categories.add("29");
        categories.add("30");
        categories.add("31");
        categories.add("32");
        categories.add("33");
        categories.add("34");
        categories.add("35");
        categories.add("36");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // Calling async task to get json

    }//onCreate


    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        }

    }//onResume


    @Override
    protected void onPause() {
        super.onPause();
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            mLocationManager.removeUpdates(mLocationListener);
        }

    }//onPause

    private void makeUseOfNewLocation(Location loc) {
        latitude = loc.getLatitude();
        Log.i(TAG,"lat is: " + latitude);
        longitude = loc.getLongitude();
        float accuracy = loc.getAccuracy();
        Log.i(TAG, "Accuracy is: " + accuracy);
        if(accuracy <= 500){
            if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                mLocationManager.removeUpdates(mLocationListener);
            }
            new GetCourses().execute();

        }//if
    }//makeUseOfNewLocation




    /**
     * Async task class to get json data.
     */
    private class GetCourses extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> CourseList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(NewRound.this);
            pDialog.setMessage("Getting courses near you...");
            pDialog.setCancelable(false);
            pDialog.show();
        }//onPreExecute

        @Override
        protected Void doInBackground(Void... arg0) {
            String url = "http://zelusit.com/androidGetCourses.php?latitude=" + latitude + "&longitude=" + longitude;
            //url = "http://zelusit.com/androidGetCourses.php?latitude=53.31822909&longitude=-6.25564142";
            //Log.i(TAG,"Courses URL is: " + url);
            // Creating service handler class instance
            LeagueRequest webreq = new LeagueRequest();

            // Making a request to url and getting response
            //The json string jsonStr is the response as seen in logcat.
            String jsonStr = webreq.makeWebServiceCall(url, LeagueRequest.GET);

            //Log.d("Response: ", "> " + jsonStr);
            Log.i(TAG, "Json String: " + jsonStr);

            //Sends the jsonStr to the ParseJson method and the result is called courseList
            CourseList = getCourses(jsonStr);
            Log.i(TAG, "course List: " + CourseList);

            return null;
        }


        //After Json data is retrieved and Parsed the data is sent to listView for display.
        @Override
        protected void onPostExecute(Void result) {
            listview =(ListView)findViewById(R.id.list);
            super.onPostExecute(result);
            //listview = (ListView) findViewById(R.id.list);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


            ListAdapter adapter = new SimpleAdapter(
                    NewRound.this, CourseList,
                    R.layout.list_item_course_list, new String[]{TAG_FACILITY_NAME, TAG_COURSE_NAME}, new int[]{R.id.facility_name, R.id.course_name});
            //The new String above says which info goes in which TextView


            if(CourseList != null) {
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //Log.i(TAG, "League id from array is: " + leagueIDsArray[position]);

                        //Intent intentIndividualLeagues = new Intent(NewRound.this, ShotInputScreen.class);
                        //intentIndividualLeagues.putExtra("courseID", courseIDsArray[position]);
                        Log.i(TAG, "Course ID: " + courseIDsArray[position]);
                        courseID = courseIDsArray[position];
                        selectedCourseName = courseNamesArray[position];
                        //Log.i(TAG, "Sending LeagueId: " + leagueIDsArray[position]);
                        //Log.i(TAG, "Sending LeagueTYPE: " + leagueTypeArray[position]);
                        //startActivity(intentIndividualLeagues);
                        selected_course.setText("" + selectedCourseName);
                        vibe.vibrate(300);


                        //if course is mapped (there is data in the coursePinsArray at the position of the selected course)
                        //then create an ArrayList called pins. The comma seperated text is split at the commas
                        //and each value is put into an array element. The elements are then put into shared preferences.
                        //Mapped is 'yes' if the course is mapped and 'no' if it is not.
                        if (coursePinsArray[position].isEmpty()) {
                            Log.i(TAG, "Course not mapped putting no in SP");
                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(NewRound.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("mapped", no);
                            editor.putString("courseID", courseID);
                            editor.apply();
                        } else {
                            Log.i(TAG, "Course  mapped putting YES in SP");
                            pins = new ArrayList<>(Arrays.asList(coursePinsArray[position].split(",")));

                            //Log.i(TAG, "NR hole 1 lat is:'" + coursePinsArray[position] + "'");

                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(NewRound.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("mapped", yes);
                            editor.putString("isMapping", no);
                            editor.putString("h1lat", h1lat = pins.get(0));
                            editor.putString("h1long", h1long = pins.get(1));
                            editor.putString("h2lat", h2lat = pins.get(2));
                            editor.putString("h2long", h2long = pins.get(3));
                            editor.putString("h3lat", h3lat = pins.get(4));
                            editor.putString("h3long", h3long = pins.get(5));
                            editor.putString("h4lat", h4lat = pins.get(6));
                            editor.putString("h4long", h4long = pins.get(7));
                            editor.putString("h5lat", h5lat = pins.get(8));
                            editor.putString("h5long", h5long = pins.get(9));
                            editor.putString("h6lat", h6lat = pins.get(10));
                            editor.putString("h6long", h6long = pins.get(11));
                            editor.putString("h7lat", h7lat = pins.get(12));
                            editor.putString("h7long", h7long = pins.get(13));
                            editor.putString("h8lat", h8lat = pins.get(14));
                            editor.putString("h8long", h8long = pins.get(15));
                            editor.putString("h9lat", h9lat = pins.get(16));
                            editor.putString("h9long", h9long = pins.get(17));
                            editor.putString("h10lat", h10lat = pins.get(18));
                            editor.putString("h10long", h10long = pins.get(19));
                            editor.putString("h11lat", h11lat = pins.get(20));
                            editor.putString("h11long", h11long = pins.get(21));
                            editor.putString("h12lat", h12lat = pins.get(22));
                            editor.putString("h12long", h12long = pins.get(23));
                            editor.putString("h13lat", h13lat = pins.get(24));
                            editor.putString("h13long", h13long = pins.get(25));
                            editor.putString("h14lat", h14lat = pins.get(26));
                            editor.putString("h14long", h14long = pins.get(27));
                            editor.putString("h15lat", h15lat = pins.get(28));
                            editor.putString("h15long", h15long = pins.get(29));
                            editor.putString("h16lat", h16lat = pins.get(30));
                            editor.putString("h16long", h16long = pins.get(31));
                            editor.putString("h17lat", h17lat = pins.get(32));
                            editor.putString("h17long", h17long = pins.get(33));
                            editor.putString("h18lat", h18lat = pins.get(34));
                            editor.putString("h18long", h18long = pins.get(35));
                            editor.apply();
                        }//if/else


                    }//onItemClick
                });

            }
        }//onPostExecute

    }//class Get courses





    private ArrayList<HashMap<String, String>> getCourses(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> CourseList = new ArrayList<>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray courses = jsonObj.getJSONArray(TAG_COURSE_INFO);

                //initialise arrays
                courseIDsArray = new String[courses.length()];
                courseNamesArray = new String[courses.length()];
                coursePinsArray = new String[36];

                // looping through All courses
                for (int i = 0; i < courses.length(); i++) {
                    //Log.i(TAG, "courses length is: " + courses.length());
                    JSONObject c = courses.getJSONObject(i);

                    String courseFacilityName = c.getString(TAG_FACILITY_NAME);
                    String courseName = c.getString(TAG_COURSE_NAME);
                    courseID = c.getString(TAG_COURSE_ID);
                    coursePin = c.getString(TAG_COURSE_PINS);
                    //courseCity = c.getString(TAG_COURSE_CITY);

                    //Log.i(TAG, "Course facility name is: " + courseFacilityName);
                    //Log.i(TAG, "Course name is: " + courseName);
                    //Log.i(TAG, "Course pin is: " + coursePin);
                    //Log.i(TAG, "league id is: " + leagueid);

                    // tmp hashmap for single course
                    HashMap<String, String> course = new HashMap<>();

                    // adding each child node to HashMap key => value
                    course.put(TAG_FACILITY_NAME, courseFacilityName);
                    course.put(TAG_COURSE_NAME, courseName);
                    course.put(TAG_COURSE_ID,courseID);


                    courseIDsArray[i] = courseID;
                    courseNamesArray[i] = courseName;
                    coursePinsArray[i] = coursePin;
                    // adding course to courses list
                    CourseList.add(course);
                }
                return CourseList;
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.i(TAG,"No Working" + e.printStackTrace());
                return null;
            }
        } else {
            Log.i(TAG, "ServiceHandler, Couldn't get any data from the url");
            return null;
        }
    }//ParsonJson


    /**
     * Spinner select
     * @param parent The parent view
     * @param view The view selected
     * @param position the position of the item clicked
     * @param id The id of the item clicked
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Log.i(TAG, "Selected item is: " + item);
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }




    /**
     * This method gets the current date and time as well as the name of the course
     * that the user input. It send this data to the addNewRoundToDB method
     * in the DatabaseHelper class. The getRound method is then called.
     * @param view The view that was clicked
     */
    public void putInfoInDB(View view){
        today = getDate();
        String handicap = item;

        DatabaseHelper dbHandler = new DatabaseHelper(NewRound.this);
        dbHandler.addNewRoundToDB(today, selectedCourseName, handicap, LoggedInId);

        getRoundID();
    }


    /**
     * This method gets the date and time the round was created. The time is used
     * as well as the date because more than one round could be played in a day.
     * @return The date and time.
     */
    public String getDate(){
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        today = formatter.format(date);
        return today;
    }


    /**
     * This method is used to get the id of the round just created. This id will be
     * sent to the the ShotInputScreen activity and the id will be put into the SQLite database
     * to identify which round each shot is connected with. This roundID is then put in an
     * intent and the ShotInputScreen activity is started.
     */
    public void getRoundID(){
        try {
            Log.i(TAG, "getRoundID - started");

            SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
            //We don’t need to write to the database so we’re using getReadableDatabase().
            SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();
            //Use cursor to get number of rows where hole_counter equals X

            String query1 = "SELECT _id FROM round WHERE date = '" + today + "';";
            //Log.i(TAG, "Summary Query is: " + query1);
            Cursor cursor = db.rawQuery(query1, null);
            if (cursor.moveToFirst()) {
                roundID = cursor.getInt(0);
                Log.i(TAG, "roundId is: " + roundID);
            }

            cursor.close();

        }//try
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("roundID", roundID);
        editor.apply();

        String isCourseMapped = sharedPreferences.getString("mapped", "");
        if(isCourseMapped.equals("yes"))
        {
            Log.i(TAG, "Course is mapped");
            Intent intentGoToShotInputScreen = new Intent(NewRound.this, ShotInputScreen.class);
            //Log.i(TAG, "RoundID sent to InputScreen is: " + roundID);
            intentGoToShotInputScreen.putExtra("roundID", roundID);
            intentGoToShotInputScreen.putStringArrayListExtra("pinsArray", pins);
            startActivity(intentGoToShotInputScreen);
        }
        else
        {
            Log.i(TAG, "Course is NOT mapped");
            Intent intentGoToMappingScreen = new Intent(NewRound.this, MappingScreen.class);
            //Log.i(TAG, "RoundID sent to InputScreen is: " + roundID);
            intentGoToMappingScreen.putExtra("roundID", roundID);
            startActivity(intentGoToMappingScreen);
        }

    }//getRoundID

}//class

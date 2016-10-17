package com.example.sitting_room.GolfShotRating;

//Original code for retrieving json data from website was written by Muhammad Bilal
//and obtained from http://mobilesiri.com/json-parsing-in-android-using-android-studio/ 


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class ListOfLeagues extends AppCompatActivity {


    private static final String TAG = "TEST";
    public static  String jsonStr;
    public static Integer daysLeft;
    public static String stringDaysLeft;
    public static String[] leagueNamesArray;
    public static Integer[] leagueIDsArray;
    public static String[] leagueTypeArray;
    public static String[] leagueStartDateArray;
    public static String[] leagueEndDateArray;
    public static String LoggedInId;
    ListView listview;


    // JSON Node names
    private static final String TAG_lEAGUEINFO = "league_info";//this is the name at the start of the json data returned by url
    private static final String TAG_LEAGUE_NAME = "league_name";
    private static final String TAG_LEAGUE_DAYS_LEFT = "days_left";
    private static final String TAG_LEAGUEID = "league_id";
    private static final String TAG_LEAGUE_START_DATE = "start_date";
    private static final String TAG_LEAGUE_END_DATE = "finish_date";
    private static final String TAG_LEAGUE_TYPE = "type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_leagues);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        LoggedInId = sharedPreferences.getString("key", "notLoggedIn");


        // Calling async task to get json
        new GetLeagues().execute();
    }//onCreate

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetLeagues extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> leagueList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ListOfLeagues.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * This runs in the background.
         * It uses the webreq method in the LeagueRequest class to get the data from the url.
         * It then sends this data to the ParseJSON method to get an ArrayList of the leagues.
         * Each position in the ArrayList 'leagueList' will contain a HashMap containing information on one league.
         * @param arg0
         * @return null
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            LeagueRequest webreq = new LeagueRequest();

            String url = "http://zelusit.com/androidLeagueList.php?loginid=" + LoggedInId;
            Log.i(TAG, "lol SCRIPT IS: " + url);
            // Making a request to url and getting response
            //The json string jsonStr is the response as seen in logcat.
            jsonStr = webreq.makeWebServiceCall(url, LeagueRequest.GET);

            //Log.d("Response: ", "> " + jsonStr);
            Log.i(TAG, "Json String: " + jsonStr);

            //Sends the jsonStr to the ParseJson method and the result is called leagueList
                leagueList = ParseJSON(jsonStr);
                //Log.i(TAG, "league List: " + leagueList);

            return null;
        }


        /**
         * After the json data is returened and Parsed. If there is any data in the leagueList ArrayList then
         * this means that leagues were returned.
         * The leagues are displayed and if the user is not part of any leagues then the user
         * is sent back to the AfterLoginGuest page.
         * @param result The Parsed json data.
         */
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute Json String: " + jsonStr);

                listview = (ListView) findViewById(R.id.list);
                super.onPostExecute(result);
                //listview = (ListView) findViewById(R.id.list);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();


            //If leagueList is not null the user is a memeber of at least one league.
            //If it is null then they are not a member of any leagues.
            if (leagueList != null) {
                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new SimpleAdapter(
                        ListOfLeagues.this, leagueList,
                        R.layout.list_item, new String[]{TAG_LEAGUE_NAME, TAG_LEAGUE_TYPE, TAG_LEAGUE_DAYS_LEFT, TAG_LEAGUEID}, new int[]{R.id.name, R.id.league_type, R.id.league_days_left, R.id.league_code});
                //The new String above says which info goes in which TextView

                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //Log.i(TAG, "League id from array is: " + leagueIDsArray[position]);

                        Intent intentIndividualLeagues = new Intent(ListOfLeagues.this, IndividualLeague.class);
                        intentIndividualLeagues.putExtra("leagueID", leagueIDsArray[position]);
                        intentIndividualLeagues.putExtra("leagueType", leagueTypeArray[position]);
                        intentIndividualLeagues.putExtra("leagueStartDate", leagueStartDateArray[position]);
                        intentIndividualLeagues.putExtra("leagueEndDate", leagueEndDateArray[position]);
                        intentIndividualLeagues.putExtra("leagueName", leagueNamesArray[position]);
                        //Log.i(TAG, "Sending LeagueId: " + leagueIDsArray[position]);
                        //Log.i(TAG, "Sending LeagueTYPE: " + leagueTypeArray[position]);
                        startActivity(intentIndividualLeagues);

                    }//onItemClick
                });
            }
            else
            {
               Toast toast= Toast.makeText(getApplicationContext(),
                        "You are not a member of any leagues yet.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 150);
                toast.show();
            }

        }//onPostExecute

    }//class getLeagues


    /**
     * This method takes the json data returned from the web database and iteraties through it
     * and extracts the relevant information. For each iteration it puts the data in a HashMap called leagues and then
     * puts that HashMap in to an ArrayList called leagueList.
     * Data is also put into arrays which are used to store data that will be need but not displayed.
     * @param json The json data returned from the web database.
     * @return Teh arrayList containing HashMaps of the paresd json data.
     */
    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                //ArrayList for ListView
                ArrayList<HashMap<String, String>> leagueList = new ArrayList<>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray leagues = jsonObj.getJSONArray(TAG_lEAGUEINFO);


                //initialise arrays.
                leagueNamesArray = new String[leagues.length()];
                leagueIDsArray = new Integer[leagues.length()];
                leagueTypeArray = new String[leagues.length()];
                leagueStartDateArray = new String[leagues.length()];
                leagueEndDateArray = new String[leagues.length()];

                //loop through all leagues
                for (int i = 0; i < leagues.length(); i++) {
                    Log.i(TAG, "leagues length is: " + leagues.length());
                    JSONObject c = leagues.getJSONObject(i);

                    String leagueName = c.getString(TAG_LEAGUE_NAME);
                    Integer leagueid = c.getInt(TAG_LEAGUEID);
                    String leagueStartDate = c.getString(TAG_LEAGUE_START_DATE);
                    String leagueEndDate = c.getString(TAG_LEAGUE_END_DATE);
                    String leagueType = c.getString(TAG_LEAGUE_TYPE);
                    String leagueIdAsString = Integer.toString(leagueid);
                    String leagueCode = "Code: " + leagueIdAsString;


                    //Convert date String to Date object and send to daysLeft method
                    //The result is checked to see if the end date has passed and if it has
                    //let the user know. If it has not then let the user know how many days are left
                    //beofre the league will end
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date endDate = df.parse(leagueEndDate);
                        daysLeft = daysBetween(endDate);
                        if(daysLeft >= 0)
                        {
                            stringDaysLeft = "(" + daysLeft.toString() + " days left.)";
                        }
                        else
                        {
                            stringDaysLeft = "League finished.";
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    String leagueTypeDisplay;
                    if(leagueType.equals("sg_driving")){
                        leagueTypeDisplay = "Driving league";
                    }else if(leagueType.equals("sg_long_game")){
                        leagueTypeDisplay = "Long Game League";
                    }else if(leagueType.equals("sg_short_game")){
                        leagueTypeDisplay = "Short Game League";
                    }else if(leagueType.equals("sg_putting")){
                        leagueTypeDisplay = "Putting League";
                    }else leagueTypeDisplay ="";
                    Log.i(TAG,"League type is: " + leagueTypeDisplay);

                    //Log.i(TAG, "league id is: " + leagueid);

                    // tmp hashmap for single league. This will be put in to the leagueList ArrayList.
                    HashMap<String, String> league = new HashMap<>();

                    // adding each child node to HashMap key => value
                    league.put(TAG_LEAGUE_NAME, leagueName);
                    league.put(TAG_LEAGUE_TYPE, leagueTypeDisplay);
                    league.put(TAG_LEAGUEID, leagueCode);
                    league.put(TAG_LEAGUE_DAYS_LEFT, stringDaysLeft);


                    //Put league ids in an array and the positions will be used when a league
                    //is clicked in the listview to identify the id for the league which has been clicked.
                    leagueNamesArray[i] = leagueName;
                    leagueIDsArray[i] = leagueid;
                    leagueTypeArray[i] = leagueType;
                    leagueStartDateArray[i] = leagueStartDate;
                    leagueEndDateArray[i] = leagueEndDate;




                    // adding the HashMap league to ArrayList leagueslist.
                    leagueList.add(league);
                }
                return leagueList;
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
     * This method calculates the number of days between today and the end of a league.
     * @param d2 the end date of the league.
     * @return The number of days between today and the end of the league.
     */
    public int daysBetween(Date d2){
        //Log.i(TAG,"daysBetween started");
        Date d1 = Calendar.getInstance().getTime();
        //Log.i(TAG,"todays date: " + d1);
        return (int)((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));

    }


    /**
     * This method contains the Intent to start the Create a new league.
     * @param view The view that was clicked
     */
    public void NewLeague(View view){
        Intent intentGoTOCreateNewLeague = new Intent(ListOfLeagues.this,CreateNewLeague.class);
        startActivity(intentGoTOCreateNewLeague);
    }


    /**
     * This method contains the Intent to join a league.
     * @param view The view that was clicked
     */
    public void joinLeague(View view){
        Intent intentGoToJoinLeague = new Intent(ListOfLeagues.this,JoinLeague.class);
        startActivity(intentGoToJoinLeague);
    }


    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed RS started**");
        Intent intentReturnToAFG = new Intent(ListOfLeagues.this,AfterLoginGuest.class);
        startActivity(intentReturnToAFG);
    }

}//class ListOfLeagues

package com.example.sitting_room.GolfShotRating;

//Original code for retrieving json data from website was written by Muhammad Bilal
//and obtained from http://mobilesiri.com/json-parsing-in-android-using-android-studio/ 


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class IndividualLeague extends AppCompatActivity {

    private static final String TAG = "TEST";
    public static int sentLeagueID;
    public static String sentLeagueStartDate;
    public static String sentLeagueEndDate;
    public static String sentLeagueName;

    public static String sentLeagueType;
    TextView toolbarTitle;
    ListView listview;
    private static String url;


    // JSON Node names
    private static final String TAG_lEAGUEINFO = "league_info";//this is the name at the start of the json data returned by url
    private static final String TAG_USER_NAME = "Name";
    private static final String TAG_USER_SCORE = "score";
    //private static final Integer TAG_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_league);



        sentLeagueID = getIntent().getIntExtra("leagueID", 0);
        sentLeagueType = getIntent().getStringExtra("leagueType");
        sentLeagueName = getIntent().getStringExtra("leagueName");
        sentLeagueStartDate = getIntent().getStringExtra("leagueStartDate");
        sentLeagueEndDate = getIntent().getStringExtra("leagueEndDate");
        Log.i(TAG,"Received league end date is :" + sentLeagueEndDate);
        url = "http://zelusit.com/androidIndividualLeague.php?individualLeagueID="  + sentLeagueID
                                                            + "&individualLeagueType=" + sentLeagueType
                                                            + "&individualLeagueStartDate=" + sentLeagueStartDate
                                                            + "&individualLeagueEndDate=" + sentLeagueEndDate;
        Log.i(TAG,"URL for individual league is: " + url);

        toolbarTitle = (TextView)findViewById(R.id.Individual_league_toolbar_title);
        toolbarTitle.setText(sentLeagueName);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(sentLeagueName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Calling async task to get json
        new GetStudents().execute();
    }//onCreate



    /**
     * Async task class to get json data.
     */
    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> leagueList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(IndividualLeague.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * This method uses the webreq method in the League Request class to get data from the url.
         * This data is then sent to the ParseJSON method in this class adn the ArrayList leagueList is returned.
         * Each position in this ArrayList contains a HashMap that has the data an individual player in a league.
         * @param arg0
         * @return null
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            LeagueRequest webreq = new LeagueRequest();

            // Making a request to url and getting response
            //The json string jsonStr is the response as seen in logcat.
            String jsonStr = webreq.makeWebServiceCall(url, LeagueRequest.GET);

            Log.i(TAG, "Json String: " + jsonStr);

            //Sends the jsonStr to the ParseJson method and the result is called studentList
            leagueList = ParseJSON(jsonStr);
            Log.i(TAG, "Player List and scores: " + leagueList);

            return null;
        }



        /**
         * After Json data is retrieved and Parsed the data is sent to ListView for display.
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            listview =(ListView)findViewById(R.id.list);
            super.onPostExecute(result);
            //listview = (ListView) findViewById(R.id.list);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            /**
             * If there is data in the ArrayList then update parsed JSON data into ListView.
             * If there is no data in the ArrayList then there are no scores for this league and
             * a toast method tells the user this.
             * */
            if (leagueList != null) {
                ListAdapter adapter = new SimpleAdapter(
                        IndividualLeague.this, leagueList,
                        R.layout.list_item_individual_league, new String[]{TAG_USER_NAME, TAG_USER_SCORE}, new int[]{R.id.player_name, R.id.league_score});
                //The new String above says which info goes in which TextView

                listview.setAdapter(adapter);
            }
            else
            {
                Toast toast= Toast.makeText(getApplicationContext(),
                        "There are no scores to show yet.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 150);
                toast.show();
            }


        }//onPostExecute

    }//class Get Students


    /**
     * This method takes the Json data returned from the url and iterates through it.
     * On each iteration it extract the required information and puts it in a HashMap.
     * The HashMap is then put into an ArrayList called leagueList. Each position in the
     * ArrayList contains one HashMap.
     * @param json The json data retrieved from the url via the web request.
     * @return The ArrayList containing the HashMaps.
     */
    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> leagueList = new ArrayList<>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray students = jsonObj.getJSONArray(TAG_lEAGUEINFO);

                // looping through All Students
                //student length would be number of leagues in our case

                for (int i = 0; i < students.length(); i++) {
                    //Log.i(TAG, "Students length is: " + students.length());
                    JSONObject c = students.getJSONObject(i);

                    String username = c.getString(TAG_USER_NAME);
                    String userscore = c.getString(TAG_USER_SCORE);


                    //Log.i(TAG, "league id is: " + leagueid);

                    // tmp hashmap for single student
                    HashMap<String, String> student = new HashMap<>();

                    // adding each child node to HashMap key => value
                    student.put(TAG_USER_NAME, username);
                    student.put(TAG_USER_SCORE, userscore);


                    // adding student to students list
                    leagueList.add(student);
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

}//class IndividualLeague

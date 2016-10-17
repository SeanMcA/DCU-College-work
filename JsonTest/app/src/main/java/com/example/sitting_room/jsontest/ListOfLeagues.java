package com.example.sitting_room.jsontest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ListOfLeagues extends Activity {


    private static final String TAG = "TEST";
    private static String leagueName;
    private static Integer leagueid;
    private static String leagueType;
    public static Integer[] leagueIDsArray;
    public static String[] leagueTypeArray;
    ListView listview;

    // URL to get leagues in JSON format
    private static String url = "http://zelusit.com/androidLeagueList.php?loginid=1";

    // JSON Node names
    private static final String TAG_lEAGUEINFO = "league_info";//this is the name at the start of the json data returned by url
    private static final String TAG_LEAGUE_NAME = "league_name";
    private static final String TAG_LEAGUE_POSITION = "position";
    private static final String TAG_LEAGUEID = "league_id";
    private static final String TAG_LEAGUE_TYPE = "type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_leagues);


        // Calling async task to get json
        new GetStudents().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;
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

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            //The json string jsonStr is the response as seen in logcat.
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);
            //Log.i(TAG, "Json String: " + jsonStr);

            //Sends the jsonStr to the ParseJson method and the result is called studentList
            studentList = ParseJSON(jsonStr);
            //Log.i(TAG, "Student List: " + studentList);

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
                    ListOfLeagues.this, studentList,
                    R.layout.list_item, new String[]{TAG_LEAGUE_NAME, TAG_LEAGUE_POSITION}, new int[]{R.id.name, R.id.mobile});
            //The new String above says which info goes in which TextView

            listview.setAdapter(adapter);



            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //Log.i(TAG, "League id from array is: " + leagueIDsArray[position]);

                    Intent intentIndividualLeagues = new Intent(ListOfLeagues.this,IndividualLeague.class);
                    intentIndividualLeagues.putExtra("leagueID", leagueIDsArray[position]);
                    intentIndividualLeagues.putExtra("leagueType", leagueTypeArray[position]);
                    //Log.i(TAG, "Sending LeagueId: " + leagueIDsArray[position]);
                    //Log.i(TAG, "Sending LeagueTYPE: " + leagueTypeArray[position]);
                    startActivity(intentIndividualLeagues);

                }//onItemClick
            });

        }//onPostExecute

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray students = jsonObj.getJSONArray(TAG_lEAGUEINFO);

                // looping through All Students
                //student length would be number of leagues in our case
                leagueIDsArray = new Integer[students.length()];
                leagueTypeArray = new String[students.length()];
                for (int i = 0; i < students.length(); i++) {
                    //Log.i(TAG, "Students length is: " + students.length());
                    JSONObject c = students.getJSONObject(i);

                    leagueName = c.getString(TAG_LEAGUE_NAME);
                    String leaguePosition = c.getString(TAG_LEAGUE_POSITION);
                    leagueid = c.getInt(TAG_LEAGUEID);
                    leagueType = c.getString(TAG_LEAGUE_TYPE);

                    //Log.i(TAG, "league id is: " + leagueid);

                    // tmp hashmap for single student
                    HashMap<String, String> student = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    student.put(TAG_LEAGUE_NAME, leagueName);
                    student.put(TAG_LEAGUE_POSITION, leaguePosition);
                    //student.put(TAG_EMAIL, email);
                    //student.put(TAG_PHONE_MOBILE, mobile);


                    //Put league ids in an array and the positions will be used when a league
                    //is clicked in the listview to identify the id for the league which has been clicked.
                    leagueIDsArray[i] = leagueid;
                    leagueTypeArray[i] = leagueType;




                    // adding student to students list
                    studentList.add(student);
                }
                return studentList;
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

}

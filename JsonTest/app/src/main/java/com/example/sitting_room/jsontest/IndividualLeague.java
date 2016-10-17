package com.example.sitting_room.jsontest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IndividualLeague extends Activity {

    private static final String TAG = "TEST";
    private static String leagueName;
    private static Integer leagueid;
    public static Integer[] leagueIDsArray;
    public static int sentLeagueID;
    public static String sentLeagueType;
    ListView listview;

    // URL to get contacts JSON

    private static String url;

    // JSON Node names
    private static final String TAG_lEAGUEINFO = "league_info";//this is the name at the start of the json data returned by url
    private static final String TAG_USER_NAME = "Name";
    private static final String TAG_USER_SCORE = "score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_leagues);

        sentLeagueID = getIntent().getIntExtra("leagueID", 0);
        sentLeagueType = getIntent().getStringExtra("leagueType");
        //Log.i(TAG,"Received league TYPE is :" + sentLeagueType);
        url = "http://zelusit.com/androidIndividualLeague.php?individualLeagueID=" + sentLeagueID + "&individualLeagueType=" + sentLeagueType;
        //Log.i(TAG,"URL is: " + url);

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
            pDialog = new ProgressDialog(IndividualLeague.this);
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

            //Log.d("Response: ", "> " + jsonStr);
            Log.i(TAG, "Json String: " + jsonStr);

            //Sends the jsonStr to the ParseJson method and the result is called studentList
            studentList = ParseJSON(jsonStr);
            Log.i(TAG, "Student List: " + studentList);

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
                    IndividualLeague.this, studentList,
                    R.layout.list_item, new String[]{TAG_USER_NAME, TAG_USER_SCORE}, new int[]{R.id.name, R.id.mobile});
            //The new String above says which info goes in which TextView

            listview.setAdapter(adapter);



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

                for (int i = 0; i < students.length(); i++) {
                    //Log.i(TAG, "Students length is: " + students.length());
                    JSONObject c = students.getJSONObject(i);

                    String username = c.getString(TAG_USER_NAME);
                    String userscore = c.getString(TAG_USER_SCORE);


                    //Log.i(TAG, "league id is: " + leagueid);

                    // tmp hashmap for single student
                    HashMap<String, String> student = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    student.put(TAG_USER_NAME, username);
                    student.put(TAG_USER_SCORE, userscore);


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


package com.example.sitting_room.GolfShotRating;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ListOfRounds extends AppCompatActivity {
    public static String reportDate;
    public static int round_id;
    public static String LoggedInId;
    public static String dateAsString;
    public static String roundIDString;
    public static String exportedString = "3";
    public static int exported;
    private static final String TAG_ROUND_ID = "roundID";
    private static final String TAG_DATE = "date";
    private static final String TAG_COURSE = "course";
    private static final String TAG_EXPORTED = "exported";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public static Integer[] roundIdArray;
    public static Integer[] exportedArray;
    ArrayList<HashMap<String, String>> roundList;
    ListView listview;
    private static final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_rounds);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        LoggedInId = sharedPreferences.getString("key", "0");


        roundList = getRounds();
        Log.i(TAG,"roundList: " + roundList.toString());
        //Log.i(TAG, "this is my array" + Arrays.toString(roundIdArray));

        displayRounds(roundList);



    }//onCreate


    /**
     * This method takes the ArrayList containg the HashMaps of the rounds and using the adapter it diaplays them
     * via a ListView.
     * An OnItemClickListener is attached to track which round is clicked.
     * The Id of the position that was clicked is retrieved from the roundIdArray
     * and this Id is put in to shared_preferences.
     * The Intent to start the RoundSummary activity is then called.
     * @param roundList The ArrayList containing the HahMaps of the rounds.
     */
    public void displayRounds(ArrayList roundList){

        Log.i(TAG, "displayRounds started");
        listview = (ListView) findViewById(R.id.myList);
        ListAdapter adapter = new SimpleAdapter(
                ListOfRounds.this, roundList,
                R.layout.list_item_rounds, new String[]{TAG_COURSE, TAG_DATE}, new int[]{R.id.courseName, R.id.roundDate});
        //The new String above says which info goes in which TextView

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Log.i(TAG,"Clicked position: " + position);
                Integer rid = roundIdArray[position];

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(ListOfRounds.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("roundID", rid);
                editor.apply();

                Intent intentRoundSummaryScreen = new Intent(ListOfRounds.this, RoundSummary.class);
                intentRoundSummaryScreen.putExtra("roundID", rid);
                Log.i(TAG, "ListOfRounds Sending RoundId: " + rid);
                intentRoundSummaryScreen.putExtra("exported", exportedArray[position]);
                startActivity(intentRoundSummaryScreen);


            }//onItemClick
        });

    }


    /**
     * This method gets the rounds from the SQLite database.
     * It uses a cursor to loop through the data and extracts the required data.
     * This data is then put into HashMaps which are put into an ArrayList.
     * Data is also put into arrays which are used to store data that will be need but not displayed.
     * @return The ArrayList containing the HashMaps of the rounds.
     */
    private ArrayList<HashMap<String, String>> getRounds() {

        // Get data from sqlite database and add them to the table
        // Open the database for reading
        SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();
        db.beginTransaction();


        ArrayList<HashMap<String, String>> roundList = new ArrayList<>();

        String selectQuery = "SELECT * FROM round WHERE user_id=" + LoggedInId + ";";
        Log.i(TAG, "ListOfRounds round query: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,null);
        roundIdArray = new Integer[cursor.getCount()];
        exportedArray = new Integer[cursor.getCount()];
        if(cursor.getCount() >0)
        {
            for(int i = 0; i <cursor.getCount(); i++) {
                cursor.moveToNext();
                round_id = cursor.getInt(cursor.getColumnIndex("_id"));
                exported = cursor.getInt(cursor.getColumnIndex("exported"));
                Log.i(TAG,"retrieved exported: " + exported);
                exportedString = Integer.toString(exported);
                roundIDString = Integer.toString(round_id);
                String courseName = cursor.getString(cursor.getColumnIndex("course"));
                String s = cursor.getString(cursor.getColumnIndex("date"));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date d;
                try {
                    d = dateFormat.parse(s);
                    reportDate = dateFormat.format(d);
                    dateAsString = reportDate.toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                }




                //Log.i(TAG,"**CourseName**: " + courseName);
                //Log.i(TAG,"**roundDate**: " + dateAsString);

                HashMap<String, String> league = new HashMap<>();
                // adding each child node to HashMap key => value
                league.put(TAG_ROUND_ID, roundIDString);
                league.put(TAG_COURSE, courseName);
                league.put(TAG_DATE, dateAsString);
                league.put(TAG_EXPORTED, exportedString);

                roundIdArray[i] = round_id;
                exportedArray[i] = exported;
                // adding HashList to ArrayList
                roundList.add(league);

            }//for

        }//if

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        // End the transaction.
        db.close();
        // Close database
        return roundList;



    }//ArrayList




    public void returnToMain(View view){
        Log.i(TAG, "returnToMain started**");
        Intent intentReturnToMain = new Intent(ListOfRounds.this,AfterLoginGuest.class);
        startActivity(intentReturnToMain);
    }


}//class listOfRounds
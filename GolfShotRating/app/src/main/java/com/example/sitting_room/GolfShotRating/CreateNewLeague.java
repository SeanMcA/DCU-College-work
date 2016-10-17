package com.example.sitting_room.GolfShotRating;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateNewLeague extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String league_type;
    public static Integer league_duration;
    public static String league_duration_type;
    private EditText leagueName;
    private static final String TAG = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_league);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(sentLeagueName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        leagueName = (EditText)findViewById(R.id.new_league_name);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.league_spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("");
        categories.add("Driving League");
        categories.add("Long game League (250 - 100yds");
        categories.add("Short Game League (100yds and in)");
        categories.add("Putting league");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);





        // Spinner2 element
        Spinner spinner2 = (Spinner) findViewById(R.id.league_duration_spinner);

        // Spinner click listener
        spinner2.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories2 = new ArrayList<>();
        categories2.add("");
        categories2.add("1 Month");
        categories2.add("2 Months");
        categories2.add("3 Months");
        categories2.add("4 Months");
        categories2.add("6 Months");
        categories2.add("1 Year");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories2);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter2);


    }//onCreate


    /**
     *  This method identifies which item in the spinner was clicked.
     * @param parent The parent view
     * @param view The view that was clicked
     * @param position The position of the item that was clicked.
     * @param id The id of the item that was clicked.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spinner = (Spinner) parent;
        String item;
        if(spinner.getId() == R.id.league_spinner)
        {
            // On selecting a spinner item
            item = parent.getItemAtPosition(position).toString();
            if (item.equals("Driving league")) {
                league_type = "sg_driving";
            } else if (item.equals("Long game League (250 - 100yds")) {
                league_type = "sg_long_game";
            } else if (item.equals("Short Game League (100yds and in)")) {
                league_type = "sg_short_game";
            } else if (item.equals("Putting league")) {
                league_type = "sg_putting";
            }else {
                league_type = "sg_driving";
            }
            hideKeyboard();
        }//if
        else if(spinner.getId() == R.id.league_duration_spinner) {
            item = parent.getItemAtPosition(position).toString();
            if (item.equals("1 Month")) {
                league_duration = 1;
                league_duration_type = "MONTH";
            } else if (item.equals("2 Months")) {
                league_duration = 2;
                league_duration_type = "MONTH";
            } else if (item.equals("3 Months")) {
                league_duration = 3;
                league_duration_type = "MONTH";
            } else if (item.equals("4 Months")) {
                league_duration = 4;
                league_duration_type = "MONTH";
            } else if (item.equals("6 Months")) {
                league_duration = 6;
                league_duration_type = "MONTH";
            } else if (item.equals("1 Year")) {
                league_duration = 12;
                league_duration_type = "MONTH";
            }else {
                league_duration = 2;
                league_duration_type = "MONTH";
            }

                hideKeyboard();

        }//else if

        // Showing selected spinner item
        Log.i(TAG, "League type: " + league_type);
        Log.i(TAG, "League duration is: " + league_duration);

    }//onItemSelected
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    /**
     * Method to hide the softKeyboard.
     */
    public void hideKeyboard(){
        Log.i(TAG,"hideKeyboard started.");
        //inputManager.hideSoftInputFromWindow(new View(this).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        InputMethodManager inputManager = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * This method gets todays date and duration of the league to find the leagues end date.
     * It then calls the WebCreateLeague class and passes the league name, type, start dand end dates as well as the users ID.
     * @param view the view that was clicked.
     */
    public void SubmitNewLeague(View view){
        //get todays date
        Date cDate = new Date();
        String sDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, league_duration);
        java.util.Date dt = cal.getTime();
        String eDate = new SimpleDateFormat("yyyy-MM-dd").format(dt);
        Log.i(TAG,"End Date is: " + eDate);


        Log.i(TAG,"Date is: " + sDate);
        String leagueType = league_type;
        String league_Name = leagueName.getText().toString();
        Log.i(TAG,"create league id: " + LoginScript.loginID);

        new WebCreateLeague(this).execute(league_Name, leagueType, sDate, eDate, LoginScript.loginID);
    }

}//class CreateNewLeague

package com.example.sitting_room.GolfShotRating;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HoleSummary extends AppCompatActivity {
    private static final String TAG = "TEST";
    public static int holeNumber;
    public static int roundID;
    public static int numberOfShots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole_summary);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String loggedInId = sharedPreferences.getString("key", "notLoggedIn");

        holeNumber = getIntent().getIntExtra("HoleNumber", 0);
        Log.i(TAG, "H.S. holeNumber is: " + holeNumber);

        int summary_NumberOfShots = getIntent().getIntExtra("NumberOfShots", 0);
        numberOfShots = summary_NumberOfShots - 2;
        Log.i(TAG, "HoleSummary Number of shots: " + numberOfShots);

        roundID = getIntent().getIntExtra("roundID", 0);
        Log.i(TAG, "RoundID: " + roundID);

        Log.i(TAG,"HoleSummary loginId: " + LoginScript.loginID);
        Log.i(TAG,"HoleSummary pref: " + loggedInId);

        TextView holeSummaryHeader = (TextView)findViewById(R.id.hole_summary_header);
        holeSummaryHeader.setText("Hole " + holeNumber);

        // Reference to TableLayout
        TableLayout tableLayout=(TableLayout)findViewById(R.id.tablelayout);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText={"Hit From", "Shot Score"};
        for(String c:headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);


        // Get data from sqlite database and add them to the table
        // Open the database for reading
        SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
        //We don’t need to write to the database so we’re using getReadableDatabase().
        SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try
        {
            String selectQuery = "SELECT * FROM shot WHERE hole = " + holeNumber + " AND round_id =" + roundID +";";
            Log.i(TAG, "HoleSummary query is: " + selectQuery);
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount() >0)
            {
                while (cursor.moveToNext()) {
                    // Read columns data
                    String hit_from = cursor.getString(cursor.getColumnIndex("hit_from"));
                    if(hit_from.equals("green"))
                    {
                        hit_from = "putt";
                    }
                    else if(hit_from.equals("rightRough"))
                    {
                        hit_from = "Rough";
                    }
                    else if(hit_from.equals("rightTrees"))
                    {
                        hit_from = "Trees";
                    }
                    else if(hit_from.equals("rightBunker"))
                    {
                        hit_from = "Bunker";
                    }
                    else if(hit_from.equals("tee_shot_iron"))
                    {
                        hit_from = "Tee Shot Iron";
                    }
                    else if(hit_from.equals("tee_shot_driver"))
                    {
                        hit_from = "Tee Shot Driver";
                    }
                    String shot_score = cursor.getString(cursor.getColumnIndex("shot_score"));

                    // data rows
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    String[] colText={hit_from, shot_score};
                    for(String text:colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        //tv.setGravity(Gravity.CENTER);
                        tv.setGravity(Gravity.LEFT);
                        tv.setTextSize(16);
                        tv.setPadding(5, 5, 255, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);

                }

            }
            db.setTransactionSuccessful();
            cursor.close();
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {
            db.endTransaction();
            // End the transaction.
            //db.close();
            // Close database
        }



        //This part is for when the device is in landscape mode
        TableLayout tableLayout1=(TableLayout)findViewById(R.id.tablelayout1);
        TableLayout tableLayout2=(TableLayout)findViewById(R.id.tablelayout2);
        TableLayout tableLayout3=(TableLayout)findViewById(R.id.tablelayout3);
        TableLayout tableLayout4=(TableLayout)findViewById(R.id.tablelayout4);
        TableLayout tableLayout5=(TableLayout)findViewById(R.id.tablelayout5);
        TableLayout tableLayout6=(TableLayout)findViewById(R.id.tablelayout6);
        TableLayout tableLayout7=(TableLayout)findViewById(R.id.tablelayout7);
        TableLayout tableLayout8=(TableLayout)findViewById(R.id.tablelayout8);
        TableLayout tableLayout9=(TableLayout)findViewById(R.id.tablelayout9);
        TableLayout tableLayout10=(TableLayout)findViewById(R.id.tablelayout10);
        TableLayout tableLayout11=(TableLayout)findViewById(R.id.tablelayout11);
        TableLayout tableLayout12=(TableLayout)findViewById(R.id.tablelayout12);
        TableLayout tableLayout13=(TableLayout)findViewById(R.id.tablelayout13);
        TableLayout tableLayout14=(TableLayout)findViewById(R.id.tablelayout14);
        TableLayout tableLayout15=(TableLayout)findViewById(R.id.tablelayout15);
        TableLayout tableLayout16=(TableLayout)findViewById(R.id.tablelayout16);
        TableLayout tableLayout17=(TableLayout)findViewById(R.id.tablelayout17);
        TableLayout tableLayout18=(TableLayout)findViewById(R.id.tablelayout18);

        TableLayout tl[] = new TableLayout[18];
        tl[0] = tableLayout1;
        tl[1] = tableLayout2;
        tl[2] = tableLayout3;
        tl[3] = tableLayout4;
        tl[4] = tableLayout5;
        tl[5] = tableLayout6;
        tl[6] = tableLayout7;
        tl[7] = tableLayout8;
        tl[8] = tableLayout9;
        tl[9] = tableLayout10;
        tl[10] = tableLayout11;
        tl[11] = tableLayout12;
        tl[12] = tableLayout13;
        tl[13] = tableLayout14;
        tl[14] = tableLayout15;
        tl[15] = tableLayout16;
        tl[16] = tableLayout17;
        tl[17] = tableLayout18;



        for(int i = 0; i <=17; i++) {
            // Get data from sqlite database and add them to the table
            // Open the database for reading
            myTestDatabaseHelper = new DatabaseHelper(this);
            //We don’t need to write to the database so we’re using getReadableDatabase().
            db = myTestDatabaseHelper.getReadableDatabase();
            // Start the transaction.
            db.beginTransaction();

            try {
                String selectQuery = "SELECT * FROM shot WHERE hole = " + i + " AND round_id =" + roundID + ";";
                //Log.i(TAG, "HoleSummary query is: " + selectQuery);

                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.getCount() > 0)
                {
                    while (cursor.moveToNext())
                    {
                        // Read columns data
                        String hit_from = cursor.getString(cursor.getColumnIndex("hit_from"));
                        if(hit_from.equals("green"))
                        {
                            hit_from = "putt";
                        }
                        else if(hit_from.equals("rightRough"))
                        {
                            hit_from = "Rough";
                        }
                        else if(hit_from.equals("rightTrees"))
                        {
                            hit_from = "Trees";
                        }
                        else if(hit_from.equals("rightBunker"))
                        {
                            hit_from = "Bunker";
                        }
                        else if(hit_from.equals("tee_shot_iron"))
                        {
                            hit_from = "Tee Shot Iron";
                        }
                        else if(hit_from.equals("tee_shot_driver"))
                        {
                            hit_from = "Tee Shot Driver";
                        }
                        String shot_score = cursor.getString(cursor.getColumnIndex("shot_score"));

                        // data rows
                        TableRow row = new TableRow(this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
                        String[] colText = {hit_from, shot_score};
                        for (String text : colText)
                        {
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(16);
                            tv.setPadding(5, 5, 155, 5);
                            tv.setText(text);
                            row.addView(tv);
                        }
                        tl[i-1].addView(row);

                    }//while

                }//if
                db.setTransactionSuccessful();
                cursor.close();

            }//try
            catch (SQLiteException e) {
                e.printStackTrace();

            }//catch
            finally
            {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }//finally
        }//for

    }//onCreate



    public void returnToMain(View view){
        Log.i(TAG, "returnToMain started**");
        Intent intentReturnToMain = new Intent(HoleSummary.this,ShotInputScreen.class);
        holeNumber++;
        Log.i(TAG, "H.S. sending back: " + holeNumber);
        intentReturnToMain.putExtra("HoleNumber", holeNumber);
        intentReturnToMain.putExtra("roundID", roundID);
        startActivity(intentReturnToMain);
    }


    public void endRound(View view){
        Log.i(TAG, "end round started**");
        Intent intentGetRoundSummary = new Intent(HoleSummary.this, RoundSummary.class);
        intentGetRoundSummary.putExtra("roundID", roundID);
        startActivity(intentGetRoundSummary);
    }



}//class

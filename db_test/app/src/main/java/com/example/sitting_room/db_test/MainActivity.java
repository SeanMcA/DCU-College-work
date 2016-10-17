package com.example.sitting_room.db_test;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a cursor
        try {
            SQLiteOpenHelper myTestDatabaseHelper = new DatabaseHelper(this);
            //We don’t need to write to the database so we’re using getReadableDatabase().
            SQLiteDatabase db = myTestDatabaseHelper.getReadableDatabase();

            //cursor get values in new string from table
            Cursor cursor = db.query ("stroke_baseline",
                    new String[] {"_id", "distance", "tee_shot_driver", "tee_shot_iron" , "fairway"}, null, null,null, null,null);

            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
//Get the distance and tee shot driver details from the cursor
                String distance_result = cursor.getString(1);//data from position 1 (starts at position 0
                String tee_shot_driver_result = cursor.getString(4);



//Populate the distance name
                TextView name = (TextView)findViewById(R.id.myTextView2);
                name.setText(distance_result);

//Populate the tee shot driver description
                TextView description = (TextView)findViewById(R.id.myTextView4);
                description.setText(tee_shot_driver_result);

            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }


    }//onCreate



    //****************************************************************************************
    public void AddData (View view) {
        DatabaseHelper dbHandler = new DatabaseHelper(this);

        Double product = 200.424;

        dbHandler.addProduct(product);
    }

    //************************************************************************************************

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

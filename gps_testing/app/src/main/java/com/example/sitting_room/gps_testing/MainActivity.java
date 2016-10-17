package com.example.sitting_room.gps_testing;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static double latitude;
    public static double longitude;
    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate - started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        final LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);

            }



            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };



        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

// Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);



    }//onCreate


    /**
     * Upon change of location the coordinates are send to this method,
     * where the latitude and longitude variables are updated
     * @param loc The location coordinates.
     */
    private void makeUseOfNewLocation(Location loc) {
        Log.i(TAG, "makeUseOfNewLocation - started");
        //maybe just update lat and longvariables here?????????????
        //then send them to another method???????????????????
        //TextView v = (TextView)this.findViewById(R.id.gps_output);
        //String text = "lat: " + loc.getLatitude() + ",\nlong: " + loc.getLongitude();
        //v.setText(text);
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        Log.i(TAG, "makeUseOfNewLocation - lat is: " + latitude);
    }

    /**
     * Upon the button being clicked this method gets the current latitude and longitude
     * and puts them into the database.
     * @param V The view that was clicked
     */
    public void getCoordinates(View V){
        Log.i(TAG, "getCoordinates - started");
        TextView v = (TextView)findViewById(R.id.gps_output);
        String text = "LATITUDE: " + latitude + ",\nLONGITUDE: " + longitude;
        Log.i(TAG, "getCoordinates - lat is: " + latitude);
        v.setText(text);
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
}

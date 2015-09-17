package com.huangzhii.www.v2v_v8;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zhi on 3/29/2015.
 */
public class MyHistoryMapsActivity extends FragmentActivity {
    int myDriverID = 0;
    int myTripID = 0;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TextView driverID;
    TextView tripID;
    Button stopButton;
    int count = 0;
    double latitude = 0;
    double longitude = 0;
    String jsonresp;
    String urlstr;
    private Handler handler = new Handler(); // use for get Lat Lng every same time.
    JSONGetMyTripHistory p;
    ArrayList<Marker> myMarkerList = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhistorymaps);
        setUpMapIfNeeded();

        driverID = (TextView) findViewById(R.id.tv_driver);
        tripID = (TextView) findViewById(R.id.tv_tripID);
        stopButton = (Button) findViewById(R.id.back_button);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        myDriverID = bundle.getInt("driverID");
        myTripID = bundle.getInt("tripID");
        driverID.setText("Driver ID: " + myDriverID);
        tripID.setText("Trip ID: " + myTripID);
        JSONGetMyTripHistory.HistoryVehicleDriverID.clear();
        JSONGetMyTripHistory.HistoryVehicleTripID.clear();
        JSONGetMyTripHistory.HistoryVehicleLatitude.clear();
        JSONGetMyTripHistory.HistoryVehicleLongitude.clear();
        JSONGetMyTripHistory.HistoryVehicleTime.clear();
        urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/V2V_view_trip_history.php?driver=" + myDriverID + "&tripID=" + myTripID;
        //Log.v("testhz", urlstr);
        p = new JSONGetMyTripHistory(MyHistoryMapsActivity.this);
        p.execute(urlstr);// get data from JSON

        handler.post(runnable);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
    }

    //above all are the basic Google Map set up.
    //now is my Functions.

    public void buttonHandler_back(View v)
    {
        handler.removeCallbacks(runnable);
        finish();
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            if (p.getVehicleDriverID().size() > 0) {

                while (count < p.getVehicleDriverID().size()) {
                    latitude = (double) p.getVehicleLatitude().get(count);
                    longitude = (double) p.getVehicleLongitude().get(count);
                    int driverID = (int) p.getVehicleDriverID().get(count);
                    int tripID = (int) p.getVehicleTripID().get(count);
                    String time = (String) p.getVehicleTime().get(count);
                    //marker add
                    Marker friends = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Driver: " + driverID + " Time: " + time)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.dotred)));
                    myMarkerList.add(friends);

                    LatLng latLng = new LatLng(latitude, longitude);
                    // Show the current location in Google Map
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    // Zoom in the Google Map
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    count++;
                }
            }
            else{
                handler.postDelayed(this, 1000);
            }
        }
    };

    public ArrayList<Vehicle> setReturnText(String returnText) {
        jsonresp = returnText;
        ArrayList<Vehicle> recordStrings = new ArrayList<Vehicle>();

        JSONArray myJsonArray;
        JSONObject myJsonObject;
        try {
            myJsonArray = new JSONArray(jsonresp);
            for (int i = 0; i < jsonresp.length() ; i++){
                myJsonObject = myJsonArray.getJSONObject(i);
                Vehicle v = new Vehicle();
                v.setDriverID(myJsonObject.getInt("driver"));
                v.setTripID(myJsonObject.getInt("tripID"));
                v.setLatitude(myJsonObject.getDouble("latitude"));
                v.setLongitude(myJsonObject.getDouble("longitude"));
                v.setTime(myJsonObject.getString("time"));
                recordStrings.add(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recordStrings;
    }

}

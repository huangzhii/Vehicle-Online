package com.huangzhii.www.v2v_v8;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
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

public class SuperviseMapsActivity extends FragmentActivity {

    int myDriverID = 0;
    //int myLastTripID = 0;
    //int myTripID = 0;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TextView driverID;
    //TextView tripID;
    TextView latText;
    TextView lngText;
    TextView timeCount;
    TextView textMessage;
    TextView messageDetail;
    Button stopButton;
    Button trackButton;
    Button historyButton;
    LinearLayout messageLayout;
    int timeLooper = 0;
    int count = 0;
    double latitude = 0;
    double longitude = 0;
    public static int tracking = 1; // 1 means zoom in, 0 means user can drag map
    public static int viewHistory = 0; // 1 means contains history.
    String jsonresp;
    String urlstr;
    private Handler handler = new Handler(); // use for get Lat Lng every same time.
    JSONSuperviseGetFriendsLocation p;
    ArrayList<Marker> friendMarkerList = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisemap);
        setUpMapIfNeeded();

        count = 0;
        latText = (TextView) findViewById(R.id.latText);
        lngText = (TextView) findViewById(R.id.lngText);
        driverID = (TextView) findViewById(R.id.tv_driver);
        //tripID = (TextView) findViewById(R.id.tv_tripID);
        timeCount = (TextView) findViewById(R.id.time);
        textMessage = (TextView) findViewById(R.id.text_message);
        messageDetail = (TextView) findViewById(R.id.message_detail);
        messageLayout = (LinearLayout) findViewById(R.id.message_layout);
        stopButton = (Button) findViewById(R.id.back_button);
        trackButton = (Button) findViewById(R.id.trackButton);
        trackButton.getBackground().setColorFilter(0xFF14FF00, PorterDuff.Mode.MULTIPLY);//green
        historyButton = (Button) findViewById(R.id.button_history);
        historyButton.getBackground().setColorFilter(0xFF868686, PorterDuff.Mode.MULTIPLY);//grey
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        myDriverID = bundle.getInt("driverID");

        driverID.setText("Driver ID: " + myDriverID);
        messageLayout.setBackgroundColor(Color.GRAY);

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
        mMap.setMyLocationEnabled(true);
    }

    //above all are the basic Google Map set up.
    //now is my Functions.

    public void buttonHandler_back(View v)
    {
        handler.removeCallbacks(runnable);
        finish();
    }

    public void tracking(View v)
    {
        if (tracking == 0){
            tracking = 1;//start tracking and zoom in
            trackButton.getBackground().setColorFilter(0xFF14FF00, PorterDuff.Mode.MULTIPLY);//green
        }
        else{
            tracking = 0;//stop tracking and zoom out
            trackButton.getBackground().setColorFilter(0xFF868686, PorterDuff.Mode.MULTIPLY);//default color
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }
    public void setHistoryButton(View v)
    {
        if (viewHistory == 0){
            viewHistory = 1;//start tracking and zoom in
            historyButton.getBackground().setColorFilter(0xFF14FF00, PorterDuff.Mode.MULTIPLY);//green
        }
        else{
            viewHistory = 0;//stop tracking and zoom out
            historyButton.getBackground().setColorFilter(0xFF868686, PorterDuff.Mode.MULTIPLY);//default color
        }
    }

    @Override
    public void onBackPressed() {
        /*
        handler.removeCallbacks(runnable);
        urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/V2V_finish_driving.php?driver=" + myDriverID;
        simpleRequest = new POSTSimpleRequest(MapsActivity.this);
        simpleRequest.execute(urlstr);
        finish();
        */
    }
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            if (viewHistory == 0){
                for (int i = 0; i<friendMarkerList.size(); i++) {
                    friendMarkerList.get(i).setVisible(false);
                }
            }
            if (viewHistory == 1){
                for (int i = 0; i<friendMarkerList.size(); i++) {
                    friendMarkerList.get(i).setVisible(true);
                }
            }

            timeCount.setText("time: " + count/60 + ":" + count%60);
            count++;
            urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/V2V_supervise_getLocation.php?driver=" + myDriverID;
            //Log.v("testhz", urlstr);
            p = new JSONSuperviseGetFriendsLocation(SuperviseMapsActivity.this);
            p.execute(urlstr);// get data from JSON

            for(int i = 0; i<p.getVehicleDriverID().size(); i++){
                latitude = (double) p.getVehicleLatitude().get(i);
                longitude = (double) p.getVehicleLongitude().get(i);
                int driverID = (int) p.getVehicleDriverID().get(i);
                int tripID = (int) p.getVehicleTripID().get(i);
                String time = (String) p.getVehicleTime().get(i);
                //marker add
                Marker friends = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Driver: " + driverID + " Time: " + time)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.dotred)));
                friendMarkerList.add(friends);
            }
            latText.setText("Latitude: " + Double.toString(latitude));
            lngText.setText("Longitude: " + Double.toString(longitude));
            LatLng latLng = new LatLng(latitude, longitude);
            if (tracking == 1) { // is tracking
                // Show the current location in Google Map
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                // Zoom in the Google Map
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            }

            timeLooper++;
            handler.postDelayed(this, 1000);
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
                v.setDriverID(myJsonObject.getInt("driverID"));
                v.setTripID(myJsonObject.getInt("tripID"));
                v.setLatitude(myJsonObject.getDouble("currentLatitude"));
                v.setLongitude(myJsonObject.getDouble("currentLongitude"));
                v.setTime(myJsonObject.getString("lastUpdateTime"));
                recordStrings.add(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recordStrings;
    }

}

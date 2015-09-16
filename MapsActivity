package com.huangzhii.www.v2v_v8;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    int myDriverID = 0;
    int myLastTripID = 0;
    int myTripID = 0;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TextView driverID;
    TextView tripID;
    TextView latText;
    TextView lngText;
    TextView timeCount;
    TextView directionText;
    TextView textMessage;
    TextView messageDetail;
    TextView text_speed;
    Button stopButton;
    Button trackButton;
    Button historyButton;
    Button policeButton;
    Button slowButton;
    Button accidentButton;
    Button trafficButton;
    private Sensor orientSensor;
    private SensorManager mSensorManager;
    LinearLayout messageLayout;
    int timeLooper = 0;
    int count = 0;
    double latitude = 0;
    double longitude = 0;
    double direction = 0;
    double speed = 0;
    public static int tracking = 1; // 1 means zoom in, 0 means user can drag map
    public static int viewHistory = 0; // 1 means contains history.
    String jsonresp;
    String urlstr;
    private Handler handler = new Handler(); // use for get Lat Lng every same time.

    POSTCoordinate p;
    JSONGetEvent ev;
    POSTSimpleRequest simpleRequest;
    LocationManager locationManager;
    String locationProvider;
    //sound
    private SoundPool soundPool;
    private int soundID;
    boolean plays = false, loaded = false;
    AudioManager audioManager;
    float actVolume, maxVolume, volume;

    ArrayList<Marker> myMarkerList = new ArrayList<Marker>();
    ArrayList<Polygon> myDirectionList = new ArrayList<Polygon>();
    ArrayList<Marker> friendMarkerList = new ArrayList<Marker>();
    ArrayList<Polygon> friendDirectionList = new ArrayList<Polygon>();
    ArrayList<Event> eventList = new ArrayList<Event>();
    ArrayList<Marker> eventMarker = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        //sound
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;
        //Hardware buttons setting to adjust the media sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.alarm2, 1);
        //sound
        count = 0;
        latText = (TextView) findViewById(R.id.latText);
        lngText = (TextView) findViewById(R.id.lngText);
        driverID = (TextView) findViewById(R.id.tv_driver);
        tripID = (TextView) findViewById(R.id.tv_tripID);
        timeCount = (TextView) findViewById(R.id.time);
        directionText = (TextView) findViewById(R.id.direction);
        textMessage = (TextView) findViewById(R.id.text_message);
        messageDetail = (TextView) findViewById(R.id.message_detail);
        text_speed = (TextView) findViewById(R.id.text_speed);
        messageLayout = (LinearLayout) findViewById(R.id.message_layout);
        stopButton = (Button) findViewById(R.id.back_button);
        trackButton = (Button) findViewById(R.id.trackButton);
        trackButton.getBackground().setColorFilter(0xFF14FF00, PorterDuff.Mode.MULTIPLY);//green
        historyButton = (Button) findViewById(R.id.button_history);
        historyButton.getBackground().setColorFilter(0xFF868686, PorterDuff.Mode.MULTIPLY);//grey
        policeButton = (Button) findViewById(R.id.button_police);
        policeButton.getBackground().setColorFilter(0xFC0077E5, PorterDuff.Mode.MULTIPLY);
        slowButton = (Button) findViewById(R.id.button_slow);
        slowButton.getBackground().setColorFilter(0xFFFFD100, PorterDuff.Mode.MULTIPLY);
        accidentButton = (Button) findViewById(R.id.button_accident);
        accidentButton.getBackground().setColorFilter(0xFFBB0000, PorterDuff.Mode.MULTIPLY);
        trafficButton = (Button) findViewById(R.id.button_traffic);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        myDriverID = bundle.getInt("driverID");
        myLastTripID = MainPanelActivity.getMyLastTripID();
        myTripID = myLastTripID + 1;

        driverID.setText("Driver ID: " + myDriverID);
        tripID.setText("Trip ID:" + myTripID);
        messageLayout.setBackgroundColor(Color.GRAY);
        //renew tripID
        MainPanelActivity.renewMyLastTripID();
        urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/V2V_renew_current_tripID.php?driver=" + myDriverID + "&tripID=" + myTripID;
        simpleRequest = new POSTSimpleRequest(MapsActivity.this);
        simpleRequest.execute(urlstr);

        //renew tripID end
        //Direction Sensor
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        orientSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(OrientSensorListener, orientSensor,SensorManager.SENSOR_DELAY_GAME);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Get the name of the best provider
        locationProvider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider, 50, 1, locationListener); // 50 ms, 1 meter, precise enough

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
        //mMap.setMyLocationEnabled(true);
    }

    //above all are the basic Google Map set up.
    //now is my Functions.

    public void buttonHandler_back(View v)
    {
        handler.removeCallbacks(runnable);
        tracking = 1; // 1 means zoom in, 0 means user can drag map
        viewHistory = 0; // 1 means contains history.
        mSensorManager.unregisterListener(OrientSensorListener);// unregister direction sensor
        urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/V2V_finish_driving.php?driver=" + myDriverID;
        simpleRequest = new POSTSimpleRequest(MapsActivity.this);
        simpleRequest.execute(urlstr);
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

    public void policeButton(View v)
    {
        urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/reportEvent.php?event=police&reporter=" + myDriverID + "&latitude=" + latitude + "&longitude=" + longitude;
        simpleRequest = new POSTSimpleRequest(MapsActivity.this);
        simpleRequest.execute(urlstr);
        String msg = "Report police success!";
        textMessage.setText(msg);
        messageLayout.setBackgroundColor(Color.rgb(68,152,249));
    }
    public void slowButton(View v)
    {
        urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/reportEvent.php?event=slow&reporter=" + myDriverID + "&latitude=" + latitude + "&longitude=" + longitude;
        simpleRequest = new POSTSimpleRequest(MapsActivity.this);
        simpleRequest.execute(urlstr);
        String msg = "Report slow success!";
        textMessage.setText(msg);
        messageLayout.setBackgroundColor(Color.rgb(255,219,34));
    }
    public void accidentButton(View v)
    {
        urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/reportEvent.php?event=accident&reporter=" + myDriverID + "&latitude=" + latitude + "&longitude=" + longitude;
        simpleRequest = new POSTSimpleRequest(MapsActivity.this);
        simpleRequest.execute(urlstr);
        String msg = "Report accident success!";
        textMessage.setText(msg);
        messageLayout.setBackgroundColor(Color.rgb(252,115,115));
    }
    public void trafficButton(View v)
    {
        Log.v("testhz","traffic status: " + mMap.isTrafficEnabled());
        if(mMap.isTrafficEnabled()) {
            mMap.setTrafficEnabled(false);
        }
        else{
            mMap.setTrafficEnabled(true);
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
            /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();
            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            // Get Current Location
            Location myLocation = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 50, 1, locationListener); // 50 ms, 1 meter, precise enough
            */mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (latitude != 0 && longitude != 0) {
                LatLng latLng = new LatLng(latitude, longitude);
                if (tracking == 1) { // is tracking
                    // Show the current location in Google Map
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    // Zoom in the Google Map
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                }
                if (viewHistory == 0){
                    for (int i = 0; i<myMarkerList.size(); i++){
                        myMarkerList.get(i).setVisible(false);
                        myDirectionList.get(i).setVisible(false);
                    }
                    for (int i = 0; i<friendMarkerList.size(); i++) {
                        friendMarkerList.get(i).setVisible(false);
                        friendDirectionList.get(i).setVisible(false);
                    }
                }
                if (viewHistory == 1){
                    for (int i = 0; i<myMarkerList.size(); i++){
                        myMarkerList.get(i).setVisible(true);
                        myDirectionList.get(i).setVisible(true);
                    }
                    for (int i = 0; i<friendMarkerList.size(); i++) {
                        friendMarkerList.get(i).setVisible(true);
                        friendDirectionList.get(i).setVisible(false);
                    }
                }
                Marker m = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Driver: " + myDriverID)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.dotred)));
                myMarkerList.add(m);

                double p1y = latitude + 0.00004*Math.cos((direction+45)*0.017453) + 0.00004;
                double p1x = longitude + 0.00004*Math.sin((direction+45)*0.017453)*1.33;
                double p2y = latitude + 0.00004*Math.cos((direction-45)*0.017453) + 0.00004;
                double p2x = longitude + 0.00004*Math.sin((direction-45)*0.017453)*1.33;
                double p3y = latitude + 0.00010*Math.cos((direction)*0.017453) + 0.00004;
                double p3x = longitude + 0.00010*Math.sin((direction)*0.017453)*1.33;

                Polygon polygon = mMap.addPolygon(new PolygonOptions()
                        .add(new LatLng(p1y, p1x), new LatLng(p2y, p2x), new LatLng(p3y, p3x))
                        .strokeColor(Color.RED)
                        .fillColor(Color.RED)
                        .strokeWidth((float)0.001));
                myDirectionList.add(polygon);

                timeCount.setText("time: " + count/60 + ":" + count%60);
                count++;
                urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/V2V_record_coordinate.php" + "?driver="
                        + myDriverID +"&trip="+myTripID+"&lat="+latitude+"&lng="+longitude + "&tripCount=" + count + "&direction=" + (int)direction + "&speed=" + (int)speed;
                //Log.v("testhz", urlstr);
                p = new POSTCoordinate(MapsActivity.this);
                p.execute(urlstr);// get data from JSON
                for(int i = 0; i<p.getVehicleDriverID().size(); i++){
                    double lat = (double) p.getVehicleLatitude().get(i);
                    double lng = (double) p.getVehicleLongitude().get(i);
                    int driverID = (int) p.getVehicleDriverID().get(i);
                    int tripID = (int) p.getVehicleTripID().get(i);
                    String time = (String) p.getVehicleTime().get(i);
                    int dir = (int) p.getVehicleDirection().get(i);
                    //marker add
                    Marker friends = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title("Driver: " + driverID + "\nTime: " + time + "\nDirection: " + dir + "°")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.dotgreen)));
                    friendMarkerList.add(friends);
                    double p1yFriends = latitude + 0.00004*Math.cos((dir+45)*0.017453) + 0.00004;
                    double p1xFriends = longitude + 0.00004*Math.sin((dir+45)*0.017453)*1.33;
                    double p2yFriends = latitude + 0.00004*Math.cos((dir-45)*0.017453) + 0.00004;
                    double p2xFriends = longitude + 0.00004*Math.sin((dir-45)*0.017453)*1.33;
                    double p3yFriends = latitude + 0.00010*Math.cos((dir)*0.017453) + 0.00004;
                    double p3xFriends = longitude + 0.00010*Math.sin((dir)*0.017453)*1.33;

                    Polygon polygonFriends = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng(p1yFriends, p1xFriends), new LatLng(p2yFriends, p2xFriends), new LatLng(p3yFriends, p3xFriends))
                            .strokeColor(Color.GREEN)
                            .fillColor(Color.GREEN)
                            .strokeWidth((float)0.001));
                    friendDirectionList.add(polygonFriends);

                    // ---- collision detect
                    double distance = ((latitude-lat)*(latitude-lat)+(longitude-lng)*(longitude-lng))*100000000;
                    if(distance < 40){
                        String msg = "Driver " + driverID + " is nearing to you!";
                        textMessage.setText(msg);
                        messageLayout.setBackgroundColor(Color.RED);

                        if (loaded) {
                            soundPool.play(soundID, volume, volume, 1, 0, 1f);
                        }
                    }
                    else {
                        String msg = "New message";
                        textMessage.setText(msg);
                        messageLayout.setBackgroundColor(Color.GRAY);
                    }
                    // ---- collision detect
                }
                // Get Events
                urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/getEvents.php";
                ev = new JSONGetEvent(MapsActivity.this);
                ev.execute(urlstr);// get data from JSON
                eventList.clear();
                eventList = ev.getEventList();
                for (int i = 0; i<eventMarker.size(); i++) {
                    eventMarker.get(i).remove();
                    eventMarker.clear();
                }
                for(int i = 0; i<eventList.size(); i++){
                    Log.v("testhz","event List: " + "size: " + eventList.size() + " current: " + i + " " + eventList.get(i).getEvent().toString());
                    if (eventList.get(i).getEvent().equals("police")) {
                        Marker event = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(eventList.get(i).getLatitude(), eventList.get(i).getLongitude()))
                                .title("Reporter: " + eventList.get(i).getReporter() + "\nTime: " + eventList.get(i).getTime() + "\nType: " + eventList.get(i).getEvent())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.event_police)));
                        eventMarker.add(event);
                    }
                    else if (eventList.get(i).getEvent().equals("slow")) {
                        Marker event = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(eventList.get(i).getLatitude(), eventList.get(i).getLongitude()))
                                .title("Reporter: " + eventList.get(i).getReporter() + "\nTime: " + eventList.get(i).getTime() + "\nType: " + eventList.get(i).getEvent())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.event_slow)));
                        eventMarker.add(event);
                    }
                    else if (eventList.get(i).getEvent().equals("accident")) {
                        Marker event = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(eventList.get(i).getLatitude(), eventList.get(i).getLongitude()))
                                .title("Reporter: " + eventList.get(i).getReporter() + "\nTime: " + eventList.get(i).getTime() + "\nType: " + eventList.get(i).getEvent())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.event_accident)));
                        eventMarker.add(event);
                    }
                }
            }
            timeLooper++;
            handler.postDelayed(this, 1000);// delay
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
                v.setSpeed(myJsonObject.getInt("velocity"));
                v.setDirection(myJsonObject.getInt("direction"));
                recordStrings.add(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recordStrings;
    }
    public ArrayList<Event> eventSetReturnText(String returnText) {
        jsonresp = returnText;
        ArrayList<Event> recordStrings = new ArrayList<Event>();

        JSONArray myJsonArray;
        JSONObject myJsonObject;
        try {
            myJsonArray = new JSONArray(jsonresp);

            for (int i = 0; i < jsonresp.length() ; i++){
                myJsonObject = myJsonArray.getJSONObject(i);
                Event v = new Event();
                v.setEvent(myJsonObject.getString("event"));
                v.setLatitude(myJsonObject.getDouble("latitude"));
                v.setLongitude(myJsonObject.getDouble("longitude"));
                v.setReporter(myJsonObject.getInt("reporter"));
                v.setTime(myJsonObject.getString("time"));
                recordStrings.add(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recordStrings;
    }

    public void setReturnTextSimple(String returnText) {

    }


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            //Log.v("testhz","new location                        " + location.getLatitude());
            latitude = location.getLatitude();
            // Get longitude of the current location
            longitude = location.getLongitude();
            speed = location.getSpeed()*2.25; // m/s to mph
            text_speed.setText("" + (int)speed);
            Log.v("testhz", "latitude: " + latitude);
            Log.v("testhz","longitude: " + longitude);
            latText.setText("Lat: " + Double.toString(latitude));
            lngText.setText("Lon: " + Double.toString(longitude));
        }
    };

    private SensorEventListener OrientSensorListener=new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor,int accuracy){
        }
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
                direction =event.values[SensorManager.DATA_X];

                //Log.v("testhz", "Current direction: " + direction);
                directionText.setText("direct: " + Integer.toString((int)direction) + "\u00B0");
                //myPositionOverlay.setDegree(x);
                //mMapView.postInvalidate();// refresh mapview
            }
        }
    };
}

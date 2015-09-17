package com.huangzhii.www.v2v_v8;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Zhi on 3/5/2015.
 */
public class POSTCoordinate extends AsyncTask<String, Void, String>
{
    public static ArrayList vehicleDriverID = new ArrayList();
    public static ArrayList vehicleTripID = new ArrayList();
    public static ArrayList vehicleLatitude = new ArrayList();
    public static ArrayList vehicleLongitude = new ArrayList();
    public static ArrayList vehicleTime = new ArrayList();
    public static ArrayList vehicleSpeed = new ArrayList();
    public static ArrayList vehicleDirection = new ArrayList();
    public static ArrayList<Vehicle> recordString = new ArrayList<Vehicle>();
    private MapsActivity myFindActivity; //our reference back to the
    //main GUI thread
    public POSTCoordinate(MapsActivity activity_in)
    {
        myFindActivity = activity_in;
    }
    @Override
    protected String doInBackground(String... stringArray)
    {
        String urlStr = stringArray[0];
        String responseStr = "";
        HttpClient client = new DefaultHttpClient();
        HttpGet myRequest = new HttpGet(urlStr);
        try {

            HttpResponse response = client.execute(myRequest);
            BufferedReader in = null;

            in = new BufferedReader(new
                    InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");

            String line = (String) "";
            String NL = (String) System.getProperty("line.separator");
            while ((line = (String) in.readLine()) != null)
            {
                sb.append(line + NL);
            }
            in.close();
            responseStr = sb.toString();
        } catch (Exception e) {
            Log.v("GET", "ERROR!" + e.toString());
        }
        return(responseStr);
    }

    protected void onPostExecute(String s)
    {
        recordString = myFindActivity.setReturnText(s);

        vehicleDriverID.clear();
        vehicleTripID.clear();
        vehicleLatitude.clear();
        vehicleLongitude.clear();
        vehicleTime.clear();
        vehicleSpeed.clear();
        vehicleDirection.clear();
        for(int c = 0; c<recordString.size(); c++){
            vehicleDriverID.add(recordString.get(c).getDriverID());
            vehicleTripID.add(recordString.get(c).getTripID());
            vehicleLatitude.add(recordString.get(c).getLatitude());
            vehicleLongitude.add(recordString.get(c).getLongitude());
            vehicleTime.add(recordString.get(c).getTime());
        }
    }

    public ArrayList getVehicleDriverID(){
        return vehicleDriverID;
    }
    public ArrayList getVehicleTripID(){
        return vehicleTripID;
    }
    public ArrayList getVehicleLatitude(){
        return vehicleLatitude;
    }
    public ArrayList getVehicleLongitude(){
        return vehicleLongitude;
    }
    public ArrayList getVehicleTime(){
        return vehicleTime;
    }
    public ArrayList getVehicleSpeed(){
        return vehicleSpeed;
    }
    public ArrayList getVehicleDirection(){
        return vehicleDirection;
    }
}

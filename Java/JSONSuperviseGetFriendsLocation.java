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
 * Created by Zhi on 3/29/2015.
 */
public class JSONSuperviseGetFriendsLocation extends AsyncTask<String, Void, String>
{
    public static ArrayList SuperviseVehicleDriverID = new ArrayList();
    public static ArrayList SuperviseVehicleTripID = new ArrayList();
    public static ArrayList SuperviseVehicleLatitude = new ArrayList();
    public static ArrayList SuperviseVehicleLongitude = new ArrayList();
    public static ArrayList SuperviseVehicleTime = new ArrayList();
    ArrayList<Vehicle> recordString = new ArrayList<Vehicle>();
    private SuperviseMapsActivity myFindActivity; //our reference back to the
    //main GUI thread
    public JSONSuperviseGetFriendsLocation(SuperviseMapsActivity activity_in)
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

        SuperviseVehicleDriverID.clear();
        SuperviseVehicleTripID.clear();
        SuperviseVehicleLatitude.clear();
        SuperviseVehicleLongitude.clear();
        for(int c = 0; c<recordString.size(); c++){
            SuperviseVehicleDriverID.add(recordString.get(c).getDriverID());
            SuperviseVehicleTripID.add(recordString.get(c).getTripID());
            SuperviseVehicleLatitude.add(recordString.get(c).getLatitude());
            SuperviseVehicleLongitude.add(recordString.get(c).getLongitude());
            SuperviseVehicleTime.add(recordString.get(c).getTime());
        }
    }

    public ArrayList getVehicleDriverID(){
        return SuperviseVehicleDriverID;
    }
    public ArrayList getVehicleTripID(){
        return SuperviseVehicleTripID;
    }
    public ArrayList getVehicleLatitude(){
        return SuperviseVehicleLatitude;
    }
    public ArrayList getVehicleLongitude(){
        return SuperviseVehicleLongitude;
    }
    public ArrayList getVehicleTime(){
        return SuperviseVehicleTime;
    }
}

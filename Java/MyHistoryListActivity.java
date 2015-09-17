package com.huangzhii.www.v2v_v8;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhi on 3/29/2015.
 */
public class MyHistoryListActivity extends ListActivity {
    private Handler handler = new Handler();
    JSONMyHistoryList p;
    int myDriverID = 0;
    String jsonresp;
    ArrayList<Vehicle> myTrips = new ArrayList<Vehicle>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhistorylist);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        myDriverID = bundle.getInt("driverID");
        //JSON
        String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/getMyTripHistory.php?driverID=" + myDriverID;
        p = new JSONMyHistoryList(MyHistoryListActivity.this);
        p.execute(urlstr);// get data from JSON

        handler.post(runnable);//initialize
    }


    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            if(p.getTripList().size() > 0 ){
                myTrips = p.getTripList();
                List<Map<String, String>> data = new ArrayList<Map<String, String>>();


                for (int i = 0; i < myTrips.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("tripID", Integer.toString(myTrips.get(i).getTripID()));
                    map.put("startTime", "Trip start from " + myTrips.get(i).getTime());
                    data.add(map);
                }
                setListAdapter(new SimpleAdapter(MyHistoryListActivity.this, data, android.R.layout.simple_list_item_2,
                        new String[]{"tripID", "startTime"},
                        new int[]{android.R.id.text1, android.R.id.text2}
                ));


            }
            else {
                handler.postDelayed(this, 1000);
            }
        }
    };

    public ArrayList setReturnText(String returnText) {
        jsonresp = returnText;
        ArrayList<Vehicle> tripList = new ArrayList<Vehicle>();
        JSONArray myJsonArray;
        JSONObject myJsonObject;
        try {
            myJsonArray = new JSONArray(jsonresp);

            for (int i = 0; i < jsonresp.length(); i++){
                myJsonObject = myJsonArray.getJSONObject(i);
                Vehicle current = new Vehicle();
                current.setTripID(myJsonObject.getInt("tripID"));
                current.setTime(myJsonObject.getString("time"));
                tripList.add(current);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tripList;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(this, "you just click " + texts[position], Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, MyHistoryMapsActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("driverID",myDriverID);
        myBundle.putInt("tripID", myTrips.get(position).getTripID());
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }
}

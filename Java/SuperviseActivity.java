package com.huangzhii.www.v2v_v8;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
public class SuperviseActivity extends ListActivity {
    private Handler handler = new Handler();
    JSONSuperviseFriendsList p;
    int myDriverID = 0;
    String jsonresp;
    ArrayList<Friend> myFriends = new ArrayList<Friend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervise);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        myDriverID = bundle.getInt("driverID");
        //JSON
        String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/getFriendsList.php?driverID=" + myDriverID;
        p = new JSONSuperviseFriendsList(SuperviseActivity.this);
        p.execute(urlstr);// get data from JSON

        handler.post(runnable);//initialize
    }


    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            if(p.getFriendsIDs().size() > 0 ){
                myFriends = p.getFriendsIDs();
                List<Map<String, String>> data = new ArrayList<Map<String, String>>();


                for (int i = 0; i < myFriends.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("driverID", Integer.toString(myFriends.get(i).getDriverID()) + myFriends.get(i).getIsDrivingStatus());
                    map.put("email", myFriends.get(i).getEmail());
                    //Log.v("testhz", "----------" + Integer.toString(myFriends.get(i).getDriverID()));
                    //Log.v("testhz", "----------" + myFriends.get(i).getEmail());
                    data.add(map);
                }
                setListAdapter(new SimpleAdapter(SuperviseActivity.this, data, android.R.layout.simple_list_item_2,
                        new String[]{"driverID", "email"},
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
        ArrayList<Friend> friendsID = new ArrayList<Friend>();
        JSONArray myJsonArray;
        JSONObject myJsonObject;
        try {
            myJsonArray = new JSONArray(jsonresp);

            for (int i = 0; i < jsonresp.length(); i++){
                myJsonObject = myJsonArray.getJSONObject(i);
                Friend current = new Friend();
                current.setDriverID(myJsonObject.getInt("driverID"));
                current.setIsDriving(myJsonObject.getInt("isDriving"));
                if(current.getIsDriving() == 1){
                    current.setIsDrivingStatus("  Online");
                }
                else{
                    current.setIsDrivingStatus("  Offline");
                }
                current.setEmail(myJsonObject.getString("email"));
                friendsID.add(current);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return friendsID;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(this, "you just click " + texts[position], Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, SuperviseMapsActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("driverID", myFriends.get(position).getDriverID());
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }
}

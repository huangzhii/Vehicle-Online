package com.huangzhii.www.v2v_v8;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhi on 3/25/2015.
 */
public class FriendsManagerActivity extends ListActivity {
    EditText driverID_in;
    int myDriverID = 0;
    String jsonresp;
    JSONgetFriendsRequestList p;
    JSONFriendsAddDelete addDelete;
    ArrayList<Friend> myFriends = new ArrayList<Friend>();
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsmanager);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        myDriverID = bundle.getInt("driverID");
        driverID_in = (EditText) findViewById(R.id.driverID_in);

        //JSON
        String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/getFriendsRequestList.php?driverID=" + myDriverID;
        p = new JSONgetFriendsRequestList(FriendsManagerActivity.this);
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
                    map.put("driverID", Integer.toString(myFriends.get(i).getDriverID()));
                    map.put("email", myFriends.get(i).getEmail());
                    //Log.v("testhz", "----------" + Integer.toString(myFriends.get(i).getDriverID()));
                    //Log.v("testhz", "----------" + myFriends.get(i).getEmail());
                    data.add(map);
                }
                setListAdapter(new SimpleAdapter(FriendsManagerActivity.this, data, android.R.layout.simple_list_item_2,
                        new String[]{"driverID", "email"},
                        new int[]{android.R.id.text1, android.R.id.text2}
                ));
            }
            else {
                handler.postDelayed(this, 1000);
            }
        }
    };

    public void jumpToFriendsManager(View v){
        Intent myIntent = new Intent(this, FriendsManagerActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("driverID", myDriverID);
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }

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
        Intent myIntent = new Intent(this, FriendsRequestDetailActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("myDriverID", myDriverID);
        myBundle.putInt("driverID", myFriends.get(position).getDriverID());
        myBundle.putString("email", myFriends.get(position).getEmail());
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }

    public void addFriends(View v){
        int driverNumber = 0;
        if (driverID_in.getText().toString().length() == 5) {
            driverNumber = Integer.parseInt(driverID_in.getText().toString());
            if (driverNumber == myDriverID){
                String msg = "You cannot add yourself!";
                Toast.makeText(FriendsManagerActivity.this, msg, Toast.LENGTH_LONG).show();
            }
            else {
                String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/friendsAddDelete.php?type=add&myDriverID=" + myDriverID + "&driverID=" + driverNumber;
                addDelete = new JSONFriendsAddDelete(FriendsManagerActivity.this);
                addDelete.execute(urlstr);// get data from JSON
                String msg = "Request driver " + driverNumber + " submit";
                Toast.makeText(FriendsManagerActivity.this, msg, Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            String msg = "Invalid driver ID!";
            Toast.makeText(FriendsManagerActivity.this, msg, Toast.LENGTH_LONG).show();
        }
    }
    public void deleteFriends(View v){
        int driverNumber = 0;
        if (driverID_in.getText().toString().length() == 5) {
            driverNumber = Integer.parseInt(driverID_in.getText().toString());
            if (driverNumber == myDriverID){
                String msg = "You cannot delete yourself!";
                Toast.makeText(FriendsManagerActivity.this, msg, Toast.LENGTH_LONG).show();
            }
            else {
                String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/friendsAddDelete.php?type=delete&myDriverID=" + myDriverID + "&driverID=" + driverNumber;
                addDelete = new JSONFriendsAddDelete(FriendsManagerActivity.this);
                addDelete.execute(urlstr);// get data from JSON
                String msg = "Delete driver " + driverNumber + " request submitted";
                Toast.makeText(FriendsManagerActivity.this, msg, Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else {
            String msg = "Invalid driver ID!";
            Toast.makeText(FriendsManagerActivity.this, msg, Toast.LENGTH_LONG).show();
        }
    }
}

package com.huangzhii.www.v2v_v8;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zhi on 3/5/2015.
 */
public class MainPanelActivity extends ActionBarActivity {
    ImageButton startTripButton;
    ImageButton friendsButton;
    ImageButton superviseButton;
    ImageButton logOut;
    TextView driverID;
    TextView account;
    TextView lastTripID;
    String jsonresp;
    public int myDriverID = 0;
    public static int myLastTripID = 0;
    public String myAccount;
    private Handler handler = new Handler();
    POSTRefreshMain p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        //myDriverID = bundle.getInt("driverID");
        myAccount = bundle.getString("account");
        myLastTripID = bundle.getInt("lastTripID");
        startTripButton = (ImageButton) findViewById(R.id.startTrip);
        friendsButton = (ImageButton) findViewById(R.id.button_friend);
        superviseButton = (ImageButton) findViewById(R.id.button_supervise);
        logOut = (ImageButton) findViewById(R.id.button_logout);
        driverID = (TextView) findViewById(R.id.driverID_request);
        account = (TextView) findViewById(R.id.account);
        lastTripID = (TextView) findViewById(R.id.lastTripID);
        account.setText("Your account: " + myAccount);


        String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/V2V_get_new_user_ID.php?email=" + myAccount;
        p = new POSTRefreshMain(MainPanelActivity.this);
        p.execute(urlstr);// get data from JSON
        handler.post(runnable);

    }
    public void logout(View v)
    {
        LoginActivity.loginBoolean = 0;
        finish();
    }
    public void startTrip(View v)
    {
        //handler.removeCallbacks(runnable);
        Intent myIntent = new Intent(this, MapsActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("driverID", myDriverID);
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }

    public void viewMyFriends(View v){
        Intent myIntent = new Intent(this, FriendsActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("driverID", myDriverID);
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }

    public void supervise(View v){
        Intent myIntent = new Intent(this, SuperviseActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("driverID", myDriverID);
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }
    public void myHistoryList(View v){
        Intent myIntent = new Intent(this, MyHistoryListActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("driverID", myDriverID);
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    public static void renewMyLastTripID(){
        myLastTripID++;
    }
    public static int getMyLastTripID(){
        return myLastTripID;
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            if(p.getIDs().size() != 0){
                myDriverID = (int) p.getIDs().get(0);
                if (myLastTripID == 0) {//which means is a new registered user
                    myLastTripID = myDriverID * 1000; //e.g.: 42004000
                }
                driverID.setText("Your driver ID: " + myDriverID);
                lastTripID.setText("Your last trip ID: " + MainPanelActivity.getMyLastTripID());
            }
            handler.postDelayed(this, 1000);// delay
        }
    };

    public ArrayList setReturnText(String returnText) {
        jsonresp = returnText;
        ArrayList IDs = new ArrayList();
        JSONArray myJsonArray;
        JSONObject myJsonObject;
        try {
            myJsonArray = new JSONArray(jsonresp);

            for (int i = 0; i < jsonresp.length(); i++){
                myJsonObject = myJsonArray.getJSONObject(i);
                IDs.add(myJsonObject.getInt("driverID"));
                IDs.add(myJsonObject.getInt("tripID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return IDs;

    }
}

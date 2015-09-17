package com.huangzhii.www.v2v_v8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Zhi on 3/25/2015.
 */
public class FriendsRequestDetailActivity extends ActionBarActivity {
    JSONFriendsAcceptDecline p;
    private int myDriverID = 0;
    private int driverID = 0;
    private String email;
    TextView driverID_TV;
    TextView email_TV;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsrequestdetail);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        myDriverID = bundle.getInt("myDriverID");
        driverID = bundle.getInt("driverID");
        email = bundle.getString("email");

        driverID_TV = (TextView) findViewById(R.id.driverID_request);
        email_TV = (TextView) findViewById(R.id.email_request);
        driverID_TV.setText("" + driverID);
        email_TV.setText("" + email);
    }

    public void accept(View v){
        String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/friendsAcceptDecline.php?type=accept&myDriverID=" + myDriverID + "&driverID=" + driverID;
        p = new JSONFriendsAcceptDecline(FriendsRequestDetailActivity.this);
        p.execute(urlstr);// get data from JSON
        String msg = "Confirm driver " + driverID + " successful";
        Toast.makeText(FriendsRequestDetailActivity.this, msg, Toast.LENGTH_LONG).show();
        finish();
    }

    public void decline(View v){
        String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/friendsAcceptDecline.php?type=decline&myDriverID=" + myDriverID + "&driverID=" + driverID;
        p = new JSONFriendsAcceptDecline(FriendsRequestDetailActivity.this);
        p.execute(urlstr);// get data from JSON
        String msg = "Decline driver " + driverID + " successful";
        Toast.makeText(FriendsRequestDetailActivity.this, msg, Toast.LENGTH_LONG).show();
        finish();
    }
}

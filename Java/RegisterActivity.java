package com.huangzhii.www.v2v_v8;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zhi on 3/5/2015.
 */
public class RegisterActivity extends ActionBarActivity {
    //public static ArrayList<Login> record = new ArrayList<Login>();
    EditText emailtextREG, passwordtextREG;
    String jsonresp;
    Button RegisterButton;
    public int position;
    public String uniqname;
    public String error;
    String thisEmail = "";
    String thisPassword = "";
    private Handler handler = new Handler();
    JSONRegister p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailtextREG = (EditText) findViewById(R.id.email_register);
        passwordtextREG = (EditText) findViewById(R.id.password_register);
        RegisterButton = (Button) findViewById(R.id.register_in);
        //Intent myIntent = this.getIntent();
        //record = (ArrayList<Login>) myIntent.getSerializableExtra("str");


    }

    public void CreateProfile(View v) {


        if (emailtextREG.getText().toString().isEmpty() || passwordtextREG.getText().toString().isEmpty()) {
            AlertDialog alert = new AlertDialog.Builder(RegisterActivity.this).create();
            alert.setTitle("Exception:Incomplete Field(s)");
            alert.setMessage("Looks like you missed a field. Please ensure all fields are filled.");
            alert.show();

        }
        else if ((passwordtextREG.getText().toString().length() < 8) /*|| (!emailtextREG.getText().toString().endsWith("@umich.edu"))*/) {

            if ((passwordtextREG.getText().toString().length() < 8) /*&& (!emailtextREG.getText().toString().endsWith("@umich.edu"))*/) {
                AlertDialog alert = new AlertDialog.Builder(RegisterActivity.this).create();
                alert.setTitle("Exception: Password Length & Invalid Email");
                alert.setMessage("The password MUST be 8 characters in length");
                alert.show();
            } else if (passwordtextREG.getText().toString().length() < 8) {
                AlertDialog alert = new AlertDialog.Builder(RegisterActivity.this).create();
                alert.setTitle("Exception: Password Length");
                alert.setMessage("The Password MUST be 8 characters in length");
                alert.show();

            } else if ((!emailtextREG.getText().toString().endsWith("@umich.edu"))) {
                AlertDialog alert = new AlertDialog.Builder(RegisterActivity.this).create();
                alert.setTitle("Exception: Invalid Email");
                alert.setMessage("The email entered is not a valid UMICH email");
                alert.show();
            }
        }
        else if ((passwordtextREG.getText().toString().length() >= 8) /*&& (emailtextREG.getText().toString().endsWith("@umich.edu"))*/) {
            thisEmail = emailtextREG.getText().toString();
            thisPassword = passwordtextREG.getText().toString();

            /*boolean emailUsedFlag = false;


            for (int i = 0; i < record.size(); i++) {

                if (email.equals(record.get(i).getEmail())) {

                    AlertDialog alert = new AlertDialog.Builder(RegisterActivity.this).create();
                    alert.setTitle("Exception:Email taken");
                    alert.setMessage("An account for this email has already been created");
                    alert.show();
                    emailUsedFlag = true;
                    break;
                }

            }*/

            String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/createUser.php?email=" + thisEmail + "&pwd=" + thisPassword;
            p = new JSONRegister(RegisterActivity.this);
            p.execute(urlstr);// get data from JSON
            handler.post(runnable);
        }
    }
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            if (p.getCheckRegister().equals("good")) {
                String msg = "Congratulations! Your account has been created!";
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                finish();
            }
            else if (p.getCheckRegister().equals("existed")){
                String msg = "This email has existed!";
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
            }
            else {
                handler.postDelayed(this, 1000);// delay
            }
        }
    };



    public String setReturnText(String returnText) {
        jsonresp = returnText;
        JSONArray myJsonArray;
        JSONObject myJsonObject;
        String checkRegister = "";
        try {
            myJsonArray = new JSONArray(jsonresp);
            for (int i = 0; i < jsonresp.length() ; i++){
                myJsonObject = myJsonArray.getJSONObject(i);
                checkRegister = myJsonObject.getString("checkEmail");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return checkRegister;
    }

    public void BacktoLogin(View v) {
        finish();
    }
/*
    public void CheckEmailValidity(){
        position = emailtextREG.getText().toString().indexOf("@");
        uniqname = emailtextREG.getText().toString().substring(0, (position));
        Log.v("ME", "uniqname : " + uniqname);
        String urlstr = "http://www.huangzhii.com/android/access-controller.php?method=get_person&uniqname="+uniqname;
        JSONParser p = new JSONParser(this);
        p.execute(urlstr);
    }

    public void setReturnText(String returnText) {

        apiresp = returnText;

        Log.v("ME", "returned text: " + apiresp);
        JSONObject myJsonObject;



        try {
            myJsonObject = new JSONObject(apiresp);
            JSONObject person = myJsonObject.getJSONObject("person");
            UserData user = new UserData();
            error = (person.getString("errors"));
            Log.v("ME", "error: " + error);
            String aboutMeView = (person.getString("aboutMeView"));
            Log.v("ME", "view: " + aboutMeView);
            JSONArray myJSONArray = myJsonObject.getJSONArray("aliases");


            user.setDepartment((String)myJSONArray.get(0));
            user.setMajor((String)myJSONArray.get(1));


            Log.v("ME", "department: " + user.getMajor());
            Log.v("ME", "JSONLENGTH: " + myJSONArray.length());



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    */
}

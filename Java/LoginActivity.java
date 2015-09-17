package com.huangzhii.www.v2v_v8;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zhi on 3/5/2015.
 */
public class LoginActivity extends ActionBarActivity {
    JSONParser p;
    String jsonresp;
    EditText emailtext, passwordtext;
    Button SignInButton;
    ImageButton RegisterButton;
    private Handler handler = new Handler();

    int alertStatus = 0; // 0 means haven't have any alert.
    public static int loginBoolean = 0; // 0 means haven't login
    String getEmail = "";
    int getDriver = 0;
    int getTripID = 0;


    //public static ArrayList<Login> record = new ArrayList <Login> ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailtext = (EditText)findViewById(R.id.email);
        passwordtext = (EditText)findViewById(R.id.password);
        SignInButton = (Button) findViewById(R.id.sign_in);
        RegisterButton = (ImageButton) findViewById(R.id.register);
        //String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/checkLogin.php?email=huangzz@umich.edu&password=12345678";
        //JSONParser p = new JSONParser(this);
        //p.execute(urlstr);

    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {

            Log.v("testhz","=================login status=======: " + p.getCheckLogin());
            if(p.getCheckLogin() == 1){ // means success
                if (loginBoolean == 0){
                    Intent myIntent = new Intent(LoginActivity.this, MainPanelActivity.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putString("account", getEmail);
                    myBundle.putInt("driverID", getDriver);
                    myBundle.putInt("lastTripID", getTripID);
                    myIntent.putExtras(myBundle);
                    startActivity(myIntent);
                }
                loginBoolean = 1;
            }
            else {
            /*
                if (alertStatus == 0) {
                    AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
                    alert.setTitle("Exception:No account");
                    alert.setMessage("No account could be found with those credentials");
                    alert.show();
                }
                alertStatus = 1;*/
                handler.postDelayed(this, 300);// delay
            }
        }
    };



    public int setReturnText(String returnText) {
        jsonresp = returnText;
        JSONArray myJsonArray;
        JSONObject myJsonObject;
        int checkLogin = 0; // 0 means no account, 1 means login success
        try {
            myJsonArray = new JSONArray(jsonresp);
            //record.clear();
            for (int i = 0; i < jsonresp.length() ; i++){
                myJsonObject = myJsonArray.getJSONObject(i);
                checkLogin = myJsonObject.getInt("checkLogin");
                if(checkLogin == 1){
                    getEmail = myJsonObject.getString("email");
                    getDriver = myJsonObject.getInt("driverID");
                    getTripID = myJsonObject.getInt("tripID");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return checkLogin;
    }


    public void RegisterButtonHandler(View v){

        Intent myIntent = new Intent(this, RegisterActivity.class);
        //myIntent.putExtra("str", record);
        startActivity(myIntent);

    }


    public void SignInButtonHandler (View v){

        String email = emailtext.getText().toString();
        String password = passwordtext.getText().toString();
        String urlstr = "http://www.huangzhii.com/TMC01/AndroidTest/checkLogin.php?email="+ email +"&password=" + password;
        p = new JSONParser(this);
        p.execute(urlstr);
        handler.post(runnable);
        /*
        boolean flag = false;

        for(int i = 0; i < record.size(); i++)
        {
            if (email.equals( record.get(i).getEmail()) && password.equals(record.get(i).getPassword())){

                MainPage(record.get(i).getDriverID(), record.get(i).getLastTripID(), record.get(i).getEmail());
                flag = true;
                break;
            }

        }
        if(flag == false) {
            if(emailtext.getText().toString().isEmpty() || passwordtext.getText().toString().isEmpty())
            {
                AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
                alert.setTitle("Exception:Incomplete Field(s)");
                alert.setMessage("Looks like you missed a field(s). Please ensure all fields are filled.");
                alert.show();
            }
            else {
                AlertDialog alert = new AlertDialog.Builder(LoginActivity.this).create();
                alert.setTitle("Exception:No account");
                alert.setMessage("No account could be found with those credentials");
                alert.show();
            }
        }*/

    }

}

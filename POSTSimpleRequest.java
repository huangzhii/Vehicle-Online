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
public class POSTSimpleRequest extends AsyncTask<String, Void, String>
{
    private MapsActivity myFindActivity; //our reference back to the
    //main GUI thread
    public POSTSimpleRequest(MapsActivity activity_in)
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
        myFindActivity.setReturnTextSimple(s);
    }

}

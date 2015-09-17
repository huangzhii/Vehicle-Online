package com.huangzhii.www.v2v_v8;

import java.io.Serializable;

/**
 * Created by Zhi on 3/5/2015.
 */
public class Login implements Serializable
{
    private String email;
    private String password;
    private int driverID;
    private int lastTripID;

    public Login(){

    }
    public void setEmail(String email_in){
        email = email_in;
    }
    public void setPassword(String password_in){
        password = password_in;
    }
    public void setDriverID(int driverID_in){
        driverID = driverID_in;
    }
    public void setLastTripID(int lastTripID_in){
        lastTripID = lastTripID_in;
    }

    public String getEmail(){
        return(email);
    }
    public String getPassword(){
        return(password);
    }
    public int getDriverID(){
        return(driverID);
    }
    public int getLastTripID(){
        return(lastTripID);
    }

}

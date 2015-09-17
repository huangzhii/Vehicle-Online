package com.huangzhii.www.v2v_v8;

/**
 * Created by Zhi on 3/22/2015.
 */
public class Friend {
    private int driverID;
    private String email;
    private int isDriving;
    private String isDrivingStatus;

    public void setDriverID(int driverID_in){
        driverID = driverID_in;
    }
    public void setEmail(String email_in){
        email = email_in;
    }
    public void setIsDriving(int isDriving_in){
        isDriving = isDriving_in;
    }
    public void setIsDrivingStatus(String isDrivingStatus_in){
        isDrivingStatus = isDrivingStatus_in;
    }

    public int getDriverID(){
        return(driverID);
    }
    public String getEmail(){
        return(email);
    }
    public int getIsDriving(){
        return(isDriving);
    }
    public String getIsDrivingStatus(){
        return(isDrivingStatus);
    }

}

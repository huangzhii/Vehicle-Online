package com.huangzhii.www.v2v_v8;

/**
 * Created by Zhi on 3/5/2015.
 */
public class Vehicle {
    private int driverID;
    private int tripID;
    private double latitude;
    private double longitude;
    private String time;
    private int speed;
    private int direction;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }



    public void setDriverID(int driverID_in){
        this.driverID = driverID_in;
    }

    public void setTripID(int tripID_in){
        this.tripID = tripID_in;
    }
    public void setLatitude(double latitude_in){
        this.latitude = latitude_in;
    }
    public void setLongitude(double longitude_in){
        this.longitude = longitude_in;
    }
    public void setTime(String time_in){
        this.time = time_in;
    }

    public int getDriverID(){
        return driverID;
    }
    public int getTripID(){
        return tripID;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public String getTime() { return time; }

}

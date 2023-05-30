package com.example.runder.Models;

import static java.lang.Double.NaN;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class RunStatisticsModel {
    private double Distance;
    private String myDistance;
    private String myTime;
    private double groupSpeed;
    private double mySpeed;
    private String ElapsedTime;
    private double GroupElapsedTime,MyElapsedTime;
    private String GroupName;

    public RunStatisticsModel() {
    }

    public RunStatisticsModel(double distance, String myDistance, String myTime, double groupSpeed
            , double mySpeed, String elapsedTime,String  GroupName) {
        Distance = distance;
        this.myDistance = myDistance;
        this.myTime = myTime;
        this.groupSpeed = groupSpeed;
        this.mySpeed = mySpeed;
        ElapsedTime = elapsedTime;
        this.GroupName= GroupName;
    }

    public String getElapsedTime() {
        return ElapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        ElapsedTime = elapsedTime;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public void setNewElapsedTIme(){
        if(ElapsedTime.equals("0")){
            this.GroupElapsedTime=0 ;
        }
        else {
            String[] units = ElapsedTime.split(":");
            int hours = Integer.parseInt(units[0]);
            int minutes = Integer.parseInt(units[1]);
            int seconds = Integer.parseInt(units[2]);
            int totalTimeInSeconds = (hours * 60 * 60) + (minutes * 60) + seconds;
            this.GroupElapsedTime=totalTimeInSeconds ;
        }

    }



    public double getGroupElapsedTime() {
        return GroupElapsedTime;
    }

    public double getMyElapsedTime() {
        return MyElapsedTime;
    }

    public void setMyDistance(String myDistance) {
        this.myDistance = myDistance;
    }

    public String getMyTime() {
        return myTime;
    }

    public void setMyTime(String myTime) {
        if(myTime == null){
            this.MyElapsedTime= 0.000;
        }
        else {
            String[] units = myTime.split(":");
            int hours = Integer.parseInt(units[0]);
            int minutes = Integer.parseInt(units[1]);
            int seconds = Integer.parseInt(units[2]);
            int totalTimeInSeconds = (hours * 60 * 60) + (minutes * 60) + seconds;
            this.MyElapsedTime=totalTimeInSeconds ;
        }

    }

    public double getGroupSpeed() {
        return groupSpeed;
    }

    public void setGroupSpeed(double groupSpeed) {
        this.groupSpeed = groupSpeed;
    }

    public double getMySpeed() {
        return mySpeed;
    }

    public void setMySpeed(double mySpeed) {
        this.mySpeed = mySpeed;
    }

    public String getMyDistance() {
        return myDistance;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public void setSpeeds(){
        this.groupSpeed= Double.valueOf(this.Distance)/ Double.valueOf(getGroupElapsedTime());
        this.mySpeed= Double.valueOf(this.myDistance) / Double.valueOf(getMyElapsedTime());
    }

    public void roundNumbers(int decimalPlaces){

        //MyElapsedTime;
        if(Double.isNaN(this.groupSpeed)){
            this.groupSpeed = 0;
        }
        if(Double.isNaN(this.mySpeed)){
            this.mySpeed=0;
        }
        BigDecimal roundedNumber = new BigDecimal(groupSpeed).setScale(decimalPlaces, RoundingMode.HALF_UP);
        this.groupSpeed= roundedNumber.doubleValue();
        roundedNumber = new BigDecimal(this.Distance).setScale(decimalPlaces, RoundingMode.HALF_UP);
        this.Distance= roundedNumber.doubleValue();
        roundedNumber = new BigDecimal(this.mySpeed).setScale(decimalPlaces, RoundingMode.HALF_UP);
        this.mySpeed= roundedNumber.doubleValue();
        roundedNumber = new BigDecimal(this.GroupElapsedTime).setScale(decimalPlaces, RoundingMode.HALF_UP);
        this.GroupElapsedTime= roundedNumber.doubleValue();
        roundedNumber = new BigDecimal(this.MyElapsedTime).setScale(decimalPlaces, RoundingMode.HALF_UP);
        this.MyElapsedTime= roundedNumber.doubleValue();
        roundedNumber = new BigDecimal(Double.valueOf(this.myDistance)).setScale(decimalPlaces, RoundingMode.HALF_UP);
        this.myDistance= String.valueOf(roundedNumber.doubleValue());




    }

}

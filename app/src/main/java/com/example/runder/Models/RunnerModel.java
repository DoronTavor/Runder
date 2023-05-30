package com.example.runder.Models;


import java.util.HashMap;

public class RunnerModel {

    private String AvgDistance;
    private double AvgSpeed;
    private HashMap<String, Double> CurrentPosition;
    private String Email;
    private String FirstSecondName;
    private String Picture;
    private String Gender;
    private String LocationLastUpdateDate;
    private String LocationLastUpdateHour;

    public RunnerModel() {
    }

    public RunnerModel(String avgDistance, double avgSpeed, HashMap<String,
            Double> currentPosition, String email, String firstSecondName,
                       String picture, String gender, String date, String hour) {
        AvgDistance = avgDistance;
        AvgSpeed = avgSpeed;
        CurrentPosition = currentPosition;
        Email = email;
        FirstSecondName = firstSecondName;
        Picture = picture;
        Gender = gender;
        LocationLastUpdateDate = date;
        LocationLastUpdateHour = hour;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAvgDistance() {
        return AvgDistance;
    }

    public void setAvgDistance(String avgDistance) {
        AvgDistance = avgDistance;
    }

    public double getAvgSpeed() {
        return AvgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        AvgSpeed = avgSpeed;
    }



    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstSecondName() {
        return FirstSecondName;
    }

    public void setFirstSecondName(String firstSecondName) {
        FirstSecondName = firstSecondName;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public HashMap<String, Double> getCurrentPosition() {
        return CurrentPosition;
    }

    public void setCurrentPosition(HashMap<String, Double> currentPosition) {
        CurrentPosition = currentPosition;
    }

    public String getLocationLastUpdateDate() {
        return LocationLastUpdateDate;
    }

    public void setLocationLastUpdateDate(String locationLastUpdateDate) {
        LocationLastUpdateDate = locationLastUpdateDate;
    }

    public String getLocationLastUpdateHour() {
        return LocationLastUpdateHour;
    }

    public void setLocationLastUpdateHour(String locationLastUpdateHour) {
        LocationLastUpdateHour = locationLastUpdateHour;
    }

    @Override
    public String toString() {
        return "RunnerModel{" +
                "AvgDistance='" + AvgDistance + '\'' +
                ", AvgSpeed=" + AvgSpeed +
                ", CurrentPosition=" + CurrentPosition +
                ", Email='" + Email + '\'' +
                ", FirstSecondName='" + FirstSecondName + '\'' +
                ", Picture='" + Picture + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Date='" + LocationLastUpdateDate + '\'' +
                ", Hour='" + LocationLastUpdateHour + '\'' +
                '}';
    }
}

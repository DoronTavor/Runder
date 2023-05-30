package com.example.runder.Models;

import java.util.HashMap;

public class RunnersModelFull {
    private String FirstSecondName;
    private String ID;
    private String Email;
    //private String password;
    private String BirthDate;
    private String Gender;
    private String AvgDistance;
    private double AvgSpeed;
    private String Picture;
    //private String address;
    private HashMap<String,Double> CurrentPosition;
    private String serialNumber;
    private String Status;
    private String maxDistance;
    private String maxSpeed;
    private double weight;
    private double height;
    private String ActiveTeam;
    private String country;
    private String city;
    private String LocationLastUpdateDate;
    private String LocationLastUpdateHour;
    private String RunningStatus;

    public RunnersModelFull(String firstSecondName, String email, String birthDate, String avgDistance, double avgSpeed, String picture, HashMap<String,Double> currentPosition,String ID) {
        this.FirstSecondName = firstSecondName;
        this.Email = email;
        this.BirthDate = birthDate;
        this.AvgDistance = avgDistance;
        this.AvgSpeed = avgSpeed;
        this.Picture = picture;
        this.CurrentPosition = currentPosition;
        this.ID= ID;
    }
    public RunnersModelFull(RunnersModelFull modelFull){
       this.FirstSecondName = modelFull.getFirstSecondName();
       this.Email = modelFull.getEmail();
       this.BirthDate =modelFull.getBirthDate();
       this.AvgDistance =modelFull.getAvgDistance();
       this.AvgSpeed =modelFull.getAvgSpeed();
       this.Picture =modelFull.getPicture();
       this.CurrentPosition =modelFull.getCurrentPosition();
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public RunnersModelFull() {
    }

    public RunnersModelFull(String firstSecondName, String email,  String birthDate, String gender,
                            String  avgDistance, double avgSpeed, String picture, HashMap<String,Double> currentPosition,
                            String serialNumber, String status, String  maxDistance, String  maxSpeed, double weight,
                            double height, String activeTeam, String country, String city,String ID,String RunningStatus) {
        this.FirstSecondName = firstSecondName;
        this.Email = email;
        //this.password = password;
        this.BirthDate = birthDate;
        this.Gender = gender;
        this.AvgDistance = avgDistance;
        this.AvgSpeed = avgSpeed;
        this.Picture = picture;
        this.CurrentPosition = currentPosition;
        this.serialNumber = serialNumber;
        this.Status = status;
        this.maxDistance = maxDistance;
        this.maxSpeed = maxSpeed;
        this.weight = weight;
        this.height = height;
        this.ActiveTeam = activeTeam;
        this.country = country;
        this.city = city;
        this.ID= ID;
        this.RunningStatus= RunningStatus;
    }

    public RunnersModelFull(String firstSecondName, String ID, String email,String RunningStatus, String birthDate, String gender, String avgDistance, double avgSpeed, String picture, HashMap<String,Double> currentPosition, String serialNumber, String status, String maxDistance, String maxSpeed, double weight, double height, String activeTeam, String country, String city, String locationLastUpdateDate, String locationLastUpdateHour) {
        FirstSecondName = firstSecondName;
        this.ID = ID;
        Email = email;
        this.RunningStatus= RunningStatus;
        BirthDate = birthDate;
        Gender = gender;
        AvgDistance = avgDistance;
        AvgSpeed = avgSpeed;
        Picture = picture;
        CurrentPosition = currentPosition;
        this.serialNumber = serialNumber;
        this.Status = status;
        this.maxDistance = maxDistance;
        this.maxSpeed = maxSpeed;
        this.weight = weight;
        this.height = height;
        this.ActiveTeam = activeTeam;
        this.country = country;
        this.city = city;
        LocationLastUpdateDate = locationLastUpdateDate;
        LocationLastUpdateHour = locationLastUpdateHour;
    }

    public String getFirstSecondName() {
        return FirstSecondName;
    }

    public void setFirstSecondName(String firstSecondName) {
        this.FirstSecondName = firstSecondName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }



    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.BirthDate = birthDate;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getAvgDistance() {
        return AvgDistance;
    }

    public void setAvgDistance(String avgDistance) {
        this.AvgDistance = avgDistance;
    }

    public double getAvgSpeed() {
        return AvgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.AvgSpeed = avgSpeed;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        this.Picture = picture;
    }



    public HashMap<String,Double> getCurrentPosition() {
        return CurrentPosition;
    }

    public void setCurrentPosition(HashMap<String,Double> currentPosition) {
        this.CurrentPosition = currentPosition;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getActiveTeam() {
        return ActiveTeam;
    }

    public void setActiveTeam(String activeTeam) {
        this.ActiveTeam = activeTeam;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getRunningStatus() {
        return RunningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        RunningStatus = runningStatus;
    }

    @Override
    public String toString() {
        return "RunnersModelFull{" +
                "FirstSecondName='" + FirstSecondName + '\'' +
                ", Email='" + Email + '\'' +
                ", BirthDate='" + BirthDate + '\'' +
                ", Gender='" + Gender + '\'' +
                ", AvgDistance='" + AvgDistance + '\'' +
                ", AvgSpeed=" + AvgSpeed +
                ", Picture='" + Picture + '\'' +
                ", CurrentPosition=" + CurrentPosition +
                ", LocationLastUpdateDate='" + LocationLastUpdateDate + '\'' +
                ", LocationLastUpdateHour='" + LocationLastUpdateHour + '\'' +
                '}';
    }
}
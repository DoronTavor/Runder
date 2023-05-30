package com.example.runder.Models;

import java.util.HashMap;

public class ManagingModel extends CommunityModel{
    private String ID;
    private String ActiveTeam;
    String Email;
    private HashMap<String,Double> CurrentPosition;



    public ManagingModel() {
        super();
    }

    public ManagingModel(String firstSecondName, String birthDate, String gender, String picture, String address, String ID
    ,String ActiveTeam,HashMap<String,Double> currentPosition,String Email) {
        super(firstSecondName, birthDate, gender, picture, address);
        this.ID = ID;
        this.ActiveTeam= ActiveTeam;
        this.CurrentPosition=currentPosition;
        this.Email=Email;

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getActiveTeam() {
        return ActiveTeam;
    }

    public void setActiveTeam(String activeTeam) {
        ActiveTeam = activeTeam;
    }

    public HashMap<String, Double> getCurrentPosition() {
        return CurrentPosition;
    }

    public void setCurrentPosition(HashMap<String, Double> currentPosition) {
        CurrentPosition = currentPosition;
    }
}

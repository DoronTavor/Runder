package com.example.runder.Models;

public class CommunityModel {
    private String FirstSecondName;
    private String BirthDate;
    private String Gender;
    private String Picture;
    private String Address;
    public CommunityModel(String firstSecondName, String birthDate, String gender, String picture, String address) {
        this.FirstSecondName = firstSecondName;
        this.BirthDate = birthDate;
        this.Gender = gender;

        this.Picture = picture;
        this.Address = address;
    }

    public CommunityModel() {
    }

    public String getFirstSecondName() {
        return FirstSecondName;
    }

    public void setFirstSecondName(String firstSecondName) {
        this.FirstSecondName = firstSecondName;
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



    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        this.Picture = picture;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }
}

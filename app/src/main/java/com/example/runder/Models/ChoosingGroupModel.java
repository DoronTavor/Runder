package com.example.runder.Models;

import java.util.HashMap;

public class ChoosingGroupModel {
    private String GroupID;
    private String ManagerName;
    private HashMap<String,Double> CurrentPosition;
    private HashMap<String,String> RunnersIds;
    private String  RunningDistance;
    private Double RunningSpeed;
    private String Status;
    private String CurrentMessage;
    private String GroupName;
    private String  AgeRange;
    private String Image;
    private String Hour;
    private String ElapsedTime;
    private double Distance;


    public ChoosingGroupModel() {
    }

    public ChoosingGroupModel(String groupID, String managerName, HashMap<String, Double> currentPosition, HashMap<String, String> runnersIds, String  runningDistance, Double runningSpeed, String status, String currentMessage, String  ageRange,
                              String Image,String Hour,String ElapsedTime) {
        GroupID = groupID;
        ManagerName = managerName;
        CurrentPosition = currentPosition;
        RunnersIds = runnersIds;
        RunningDistance = runningDistance;
        RunningSpeed = runningSpeed;
        Status = status;
        CurrentMessage = currentMessage;
        this.GroupName= ManagerName + "'s Team";
        this.AgeRange= ageRange;
        this.Image= Image;
        this.Hour=Hour;
        this.ElapsedTime=ElapsedTime;

    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getElapsedTime() {
        return ElapsedTime;
    }

    public void setElapsedTime(String ElapsedTime) {
        ElapsedTime = ElapsedTime;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getManagerName() {
        return ManagerName;
    }

    public void setManagerName(String managerName) {
        ManagerName = managerName;
    }

    public HashMap<String, Double> getCurrentPosition() {
        return CurrentPosition;
    }

    public void setCurrentPosition(HashMap<String, Double> currentPosition) {
        CurrentPosition = currentPosition;
    }

    public HashMap<String, String> getRunnersIds() {
        return RunnersIds;
    }

    public void setRunnersIds(HashMap<String, String> runnersIds) {
        RunnersIds = runnersIds;
    }

    public String getRunningDistance() {
        return RunningDistance;
    }

    public void setRunningDistance(String runningDistance) {
        RunningDistance = runningDistance;
    }

    public Double getRunningSpeed() {
        return RunningSpeed;
    }

    public void setRunningSpeed(Double runningSpeed) {
        RunningSpeed = runningSpeed;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCurrentMessage() {
        return CurrentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        CurrentMessage = currentMessage;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String  getAgeRange() {
        return AgeRange;
    }

    public void setAgeRange(String ageRange) {
        AgeRange = ageRange;
    }

    @Override
    public String toString() {
        return "ChoosingGroupModel{" +
                "GroupID='" + GroupID + '\'' +
                ", ManagerName='" + ManagerName + '\'' +
                ", CurrentPosition=" + CurrentPosition +
                ", RunnersIds=" + RunnersIds +
                ", RunningDistance=" + RunningDistance +
                ", RunningSpeed=" + RunningSpeed +
                ", Status='" + Status + '\'' +
                ", CurrentMessage='" + CurrentMessage + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", AgeRange=" + AgeRange +
                ", Image='" + Image + '\'' +
                '}';
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> group = new HashMap<>();
        group.put("GroupID",GroupID);
        group.put("ManagerName",ManagerName);
        group.put("CurrentPosition",CurrentPosition);
        group.put("RunnersIds",RunnersIds);
        group.put("RunningDistance",RunningDistance);
        group.put("RunningSpeed",RunningSpeed);
        group.put("Status",Status);
        group.put("CurrentMessage",CurrentMessage);
        group.put("GroupName",GroupName);
        group.put("AgeRange",AgeRange);
        group.put("Image",Image);
        group.put("Hour",Hour);
        group.put("ElapsedTime",ElapsedTime);
        group.put("Distance",Distance);

        return group;


    }
}

package com.example.runder.CommonFiles;

public class MyRunConfiguration {
    private static final MyRunConfiguration myRunConfiguration= new MyRunConfiguration();
    private String groupID;
    public static MyRunConfiguration getInstance(){
        return myRunConfiguration;

    }
    private MyRunConfiguration(){

    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}

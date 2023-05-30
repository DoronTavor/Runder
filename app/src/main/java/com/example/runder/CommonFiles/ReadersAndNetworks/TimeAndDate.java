package com.example.runder.CommonFiles.ReadersAndNetworks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeAndDate {
    public static String returnMessageByHour(){
        String ret = "";
        DateFormat timeFormat = new SimpleDateFormat("HH");
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Calendar rightNow= Calendar.getInstance();
        int hour= Integer.parseInt(timeFormat.format(rightNow.getTime()));
        if( hour< 12){
            ret = "בוקר טוב";
        }
        else if(hour<17){
            ret= "אחר הצהריים טובים";

        }
        else if(hour <22){
            ret= "ערב טוב";
        }
        else if(hour<=23){
            ret= "לילה טוב";
        }

        return ret;
    }
}

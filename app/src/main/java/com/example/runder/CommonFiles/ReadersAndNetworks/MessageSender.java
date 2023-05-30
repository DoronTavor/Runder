package com.example.runder.CommonFiles.ReadersAndNetworks;

import android.util.Base64;
import android.util.Log;

import com.example.runder.CommonFiles.CBS.FinishSending;
import com.example.runder.CommonFiles.GlobalVars;
import com.example.runder.Models.RunStatisticsModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageSender {
    public static void sendSMS(String numberToSent, RunStatisticsModel statisticsModel, FinishSending finishSending){
        String messageStr=  GlobalVars.currentRunner.getFirstSecondName() +" finished a run of: "+ statisticsModel.getMyDistance()+" Meters in: "+
                (int) statisticsModel.getMyElapsedTime()/3600 +" Hours, " +((int) statisticsModel.getMyElapsedTime()%3600)/60 +" minutes and "+
                ((int) statisticsModel.getMyElapsedTime()%3600)%60 + " Seconds. the speed is: "+statisticsModel.getMySpeed() + "meter/ seconds. well done!";
        try {
            // Replace with your Twilio credentials
            String accountSid = GlobalVars.TwiloSID;
            String authToken = GlobalVars.TwiloToken;

            // Set your Twilio phone number and recipient phone number
            String fromPhoneNumber = GlobalVars.TwiloNumber;
            String toPhoneNumber = "+972"+numberToSent.substring(1);

            // Set the Twilio API URL
            String twilioUrl = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";

            // Create the POST request payload
            String postData = "From=" + fromPhoneNumber + "&To=" + toPhoneNumber + "&Body="+messageStr;

            // Create the URL object and open the connection
            URL url = new URL(twilioUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the HTTP method to POST
            connection.setRequestMethod("POST");

            // Set the basic authentication header
            String credentials = accountSid + ":" + authToken;
            String authHeader = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
            }
            connection.setRequestProperty("Authorization", authHeader);

            // Enable output and input streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Set the request payload
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(postData);
            outputStream.flush();
            outputStream.close();

            // Get the response from the server
            int responseCode = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            // Print the response
            Log.d("CHECKWHATSAPP","Response Code: " + responseCode);
            Log.d("MSG","Response Body: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

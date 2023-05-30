package com.example.runder.CommonFiles.ReadersAndNetworks;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.runder.Activities.MainActivity;
import com.example.runder.Activities.ManagingGroupActivity;
import com.example.runder.CommonFiles.CBS.CBForTemp;
import com.example.runder.CommonFiles.GlobalVars;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;


public class TempReader extends Activity {
    Context context;

    public TempReader(Context context) {
        this.context = context;
    }
    public void getTemp(LatLng latLng){
         String lat;
         String  lon;
        FusedLocationProviderClient client;
        client = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{ACCESS_FINE_LOCATION}, 1);
            }
        }
        double latitude= latLng.latitude;
        lat= String.valueOf(latitude);

        double longitude=latLng.longitude;
        lon= String.valueOf(longitude);
        Log.d("CITY",lat+"  "+lon);
        if(context.getClass()== MainActivity.class){
            WeatherByLatLon(lat,lon);
        }
        if(context.getClass()== ManagingGroupActivity.class){
            weatherForManaging(lat,lon);
        }

    }
    public void weatherStats(LatLng latLng, CBForTemp cbForTemp){
        String lat;
        String  lon;
        FusedLocationProviderClient client;
        client = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{ACCESS_FINE_LOCATION}, 1);
            }
        }
        double latitude= latLng.latitude;
        lat= String.valueOf(latitude);

        double longitude=latLng.longitude;
        lon= String.valueOf(longitude);
        weatherForStatsManaging( lat, lon,cbForTemp);
    }

    private void weatherForStatsManaging(String lat, String lon,CBForTemp cb) {
        final String[] ret = {""};
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+ GlobalVars.WEATHER_KEY +"&units=metric")
                .get().build();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response=client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(context, "ERORR", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String data=response.body().string();
                    try {

                        JSONObject json=new JSONObject(data);

                        JSONArray list=json.getJSONArray("list");
                        JSONObject objects = list.getJSONObject(0);
                        JSONArray array=objects.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description=object.getString("description");
                        String icons=object.getString("icon");

                        Date currentDate=new Date();
                        String dateString=currentDate.toString();
                        String[] dateSplit=dateString.split(" ");
                        String date=dateSplit[0]+", "+dateSplit[1] +" "+dateSplit[2];

                        JSONObject Main=objects.getJSONObject("main");
                        double temparature=Main.getDouble("temp");
                        String Temp=Math.round(temparature)+"°C";
                        ret[0] =Temp;
                        double Humidity=Main.getDouble("humidity");
                        String hum=Math.round(Humidity)+"%";
                        double FeelsLike=Main.getDouble("feels_like");
                        String feelsValue=Math.round(FeelsLike)+"°";

                        JSONObject Wind=objects.getJSONObject("wind");
                        String windValue=Wind.getString("speed")+" "+"km/h";

                        JSONObject CityObject=json.getJSONObject("city");
                        String City=CityObject.getString("name");
                        Log.d("CITY",City);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cb.presentTemp(Temp);
                            }
                        });




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }




            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void  weatherForManaging(String lat,String lon){
        final String[] ret = {""};
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+ GlobalVars.WEATHER_KEY +"&units=metric")
                .get().build();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response=client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(context, "ERORR", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String data=response.body().string();
                    try {

                        JSONObject json=new JSONObject(data);

                        JSONArray list=json.getJSONArray("list");
                        JSONObject objects = list.getJSONObject(0);
                        JSONArray array=objects.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description=object.getString("description");
                        String icons=object.getString("icon");

                        Date currentDate=new Date();
                        String dateString=currentDate.toString();
                        String[] dateSplit=dateString.split(" ");
                        String date=dateSplit[0]+", "+dateSplit[1] +" "+dateSplit[2];

                        JSONObject Main=objects.getJSONObject("main");
                        double temparature=Main.getDouble("temp");
                        String Temp=Math.round(temparature)+"°C";
                        ret[0] =Temp;
                        double Humidity=Main.getDouble("humidity");
                        String hum=Math.round(Humidity)+"%";
                        double FeelsLike=Main.getDouble("feels_like");
                        String feelsValue=Math.round(FeelsLike)+"°";

                        JSONObject Wind=objects.getJSONObject("wind");
                        String windValue=Wind.getString("speed")+" "+"km/h";

                        JSONObject CityObject=json.getJSONObject("city");
                        String City=CityObject.getString("name");
                        Log.d("CITY",City);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ManagingGroupActivity.setWeather(Temp);
                            }
                        });




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }




            });
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void  WeatherByLatLon(String lat,String lon){
        final String[] ret = {""};
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+ GlobalVars.WEATHER_KEY +"&units=metric")
                .get().build();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response=client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(context, "ERORR", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String data=response.body().string();
                    try {

                        JSONObject json=new JSONObject(data);

                        JSONArray list=json.getJSONArray("list");
                        JSONObject objects = list.getJSONObject(0);
                        JSONArray array=objects.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description=object.getString("description");
                        String icons=object.getString("icon");

                        Date currentDate=new Date();
                        String dateString=currentDate.toString();
                        String[] dateSplit=dateString.split(" ");
                        String date=dateSplit[0]+", "+dateSplit[1] +" "+dateSplit[2];

                        JSONObject Main=objects.getJSONObject("main");
                        double temparature=Main.getDouble("temp");
                        String Temp=Math.round(temparature)+"°C";
                        ret[0] =Temp;
                        double Humidity=Main.getDouble("humidity");
                        String hum=Math.round(Humidity)+"%";
                        double FeelsLike=Main.getDouble("feels_like");
                        String feelsValue=Math.round(FeelsLike)+"°";

                        JSONObject Wind=objects.getJSONObject("wind");
                        String windValue=Wind.getString("speed")+" "+"km/h";

                        JSONObject CityObject=json.getJSONObject("city");
                        String City=CityObject.getString("name");
                        Log.d("CITY",City);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.setWeather(Temp);
                            }
                        });




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }




            });
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}

package com.example.runder.CommonFiles.ReadersAndNetworks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingMethods {
    public  static String getAddress(LatLng latLng, Context context){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String address;

        String ret="";
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            ret= address;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static String getCity(LatLng latLng, Context context){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String city;

        String ret="";
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            city = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            ret= city;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static String getCountry(LatLng latLng, Context context){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String country;

        String ret="";
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            country = addresses.get(0).getCountryName(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            ret= country;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}

package com.example.runder.CommonFiles;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerAndMail {
    private MarkerOptions markerOptions;
    private String email;

    public MarkerAndMail(MarkerOptions markerOptions, String email) {
        this.markerOptions = markerOptions;
        this.email = email;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "MarkerAndMail{" +
                "markerOptions=" + markerOptions.getPosition() +
                ", email='" + email + '\'' +
                '}';
    }
}

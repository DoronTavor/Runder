package com.example.runder.Models;

public class Position {

        double Latitude;
        double Longitude;

        public Position() {
        }

        public Position(double latitude, double longitude) {
            Latitude = latitude;
            Longitude = longitude;
        }

        public double getLatitude() {
            return Latitude;
        }

        public void setLatitude(double latitude) {
            Latitude = latitude;
        }

        public double getLongitude() {
            return Longitude;
        }

        public void setLongitude(double longitude) {
            Longitude = longitude;
        }

    }


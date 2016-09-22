package com.openshift.evg.roadshow.parks.model;

import java.util.List;

/**
 * Created by jmorales on 18/08/16.
 */
public class Coordinates {
    private String latitude;
    private String longitude;

    public Coordinates() {
    }

    public Coordinates(String lat, String lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public Coordinates(List<?> position){
        if (position.size()>0)
            this.latitude = position.get(0).toString();
        if (position.size()>1)
            this.longitude = position.get(1).toString();
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String lat) {
        this.latitude = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String lng) {
        this.longitude = lng;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "lat='" + latitude + '\'' +
                ", lng='" + longitude + '\'' +
                '}';
    }
}

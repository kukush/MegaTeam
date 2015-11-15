package com.example.root.megateam.model;




import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by fra on 14/11/15.
 */
public class Person implements Serializable {
    private final static long serialVersionUID = 1; // See Nick's comment below

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {

        return lon;
    }

    double lat, lon;

    public String getNickname() {
        return nickname;
    }

    public  Person() {}

    public String getUniquieId() {
        return uniquieId;
    }

    public LatLng getPosition() {
        return new LatLng(lat,lon);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUniquieId(String uniquieId) {
        this.uniquieId = uniquieId;
    }

    public void setPosition(LatLng position) {
        this.lat = position.latitude;
        this.lon = position.longitude;
    }

    String nickname;
    String uniquieId;




    public Person(String nickname, String uniquieId, LatLng position) {
        this.nickname = nickname;
        this.uniquieId = uniquieId;

        this.lat = position.latitude;
        this.lon = position.longitude;
    }


    @Override
    public String toString() {
        return "Person{" +
                "nickname='" + nickname + '\'' +
                ", uniquieId='" + uniquieId + '\'' +
                ", position=" + lat + "," + lon +
                '}';
    }
}
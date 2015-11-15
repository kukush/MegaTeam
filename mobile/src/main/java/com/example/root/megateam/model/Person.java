package com.example.root.megateam.model;



import com.google.android.gms.maps.model.LatLng;


/**
 * Created by fra on 14/11/15.
 */
public class Person {
    public String getNickname() {
        return nickname;
    }

    public  Person() {}

    public String getUniquieId() {
        return uniquieId;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUniquieId(String uniquieId) {
        this.uniquieId = uniquieId;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    String nickname;
    String uniquieId;
    LatLng position;



    public Person(String nickname, String uniquieId, LatLng position) {
        this.nickname = nickname;
        this.uniquieId = uniquieId;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Person{" +
                "nickname='" + nickname + '\'' +
                ", uniquieId='" + uniquieId + '\'' +
                ", position=" + position +
                '}';
    }
}
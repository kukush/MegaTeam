package com.example.root.megateam;

/**
 * Created by Manuel ELO'O on 14/11/2015.
 */

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.root.megateam.model.Person;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class NotificationClass extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_one);
        CharSequence s = "Inside the activity of Notification one";
        int id=0;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            s = "error";
        }
        else {
            id = extras.getInt("notificationId");
        }
        TextView t = (TextView) findViewById(R.id.text1);
        s = s+"with id = "+id;
        t.setText(s);
        NotificationManager myNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // remove the notification with the specific id
        myNotificationManager.cancel(id);
    }

    /*private  static List<Person> mData = new ArrayList<Person>();
    public void setupData() {
        LatLng loc1= new LatLng(+34.67,-50.90);
        mData.add(new Person("kukush","ku123",loc1));
        LatLng loc2= new LatLng(+35.67,-56.90);
        mData.add(new Person("bini", "bi123", loc2));
        LatLng loc3= new LatLng(+36.67,-60.90);
        mData.add(new Person("luka", "lu123", loc3));
        LatLng loc4= new LatLng(+34.67,-56.90);
        mData.add(new Person("Admin", "lu123", loc3));
    }*/

    public static boolean check_members(ArrayList<Person> mData )

    {


        int x= 10; int y =0;
        int[] dis = new int[3] ;
        dis[0] = 1;    dis[1] = 2;  dis[2] = 3;
        // Person item = mData.get().getNickname();


        //DistanceCalculator()
        boolean  result  = true;

        double lat1=0.0;double lont1=0.0;double lat2 =0.0;double lng2=0.0;
        //  lat1 and lon1  is fixed
        for (int i = 0; i < mData.size(); i++)
        {       /// set the point of ref as  Admin
            if(mData .get(i).getNickname().equals("Admin")) {
                lat1 = mData.get(i).getLat();
                lont1 = mData.get(i).getLon();
            }
            else  {
                lat2=mData.get(i).getLat();
                lng2= mData.get(i).getLon();
            }
            double distance=  DistanceCalculator(lat1,lont1,lat2,lng2,"K");

            if(distance>0.005) {return false ;}

        }
        return  result;
    }

    public static double  DistanceCalculator(double lat1, double lon1, double lat2, double lon2, String unit)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }


    ///    This function converts decimal degrees to radians
    private static   double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}

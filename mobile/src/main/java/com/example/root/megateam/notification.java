package com.example.root.megateam;

/**
 * Created by Manuel ELO'O on 14/11/2015.
 */
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;

import com.example.root.megateam.model.Person;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class notification extends Activity {

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
    public static   boolean check_members()
    {    int x= 10; int y =0;
        int[] dis = new int[3] ;
        dis[0] = 1;    dis[1] = 2;  dis[2] = 3;
        //DistanceCalculator()
        boolean  result  = true;
        ArrayList<Person> aList = new ArrayList<Person>() ;
        for(int a= 0 ; a<3 ;a++)
        {
            Person xx = new Person();
            xx.setNickname("Admin");
            aList.add(xx);}

        //  lat1 and lon1  is fixed
        for (int i = 0; i < aList.size(); i++)
        {       /// set the point of ref as  Admin
              /*  String  name=aList.get(i).getNickname();
               if(name =="Admin")
               { LatLng adminlatlog= aList.get(i).getPosition();
                   double lat1=adminlatlog.latitude;
                   double lng1= adminlatlog.longitude;
               }
             else  {
                   double lat2=aList.get(i).getPosition().latitude;
                   double lng2= aList.get(i).getPosition().longitude;
                  }*/
                   //if(aList .get(1).equals("Admin"))
             /// x=DistanceCalculator(lat1, lng1, lat2, lng2, user1log)

            if(x==0)
            {    x= 10;
            }
            else{
                y =dis[i];
            }
            if(y >  x)
            { result = false;
                break;
            }

        }
        return  result;
    }

    public double  DistanceCalculator(double lat1, double lon1, double lat2, double lon2, String unit)
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
    private  double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    // This function converts radians to decimal degrees
    private  double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}

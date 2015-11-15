package com.example.root.megateam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import com.example.root.megateam.adapter.UserAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    //String[] elements = {"List Item 1", "List Item 2","List Item 3","List Item 4","List Item 5"};
    private List<Person> mData = new ArrayList<Person>();
    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupData();
        // Get the list component from the layout of the activity
        WearableListView listView=
                (WearableListView)findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        mAdapter = new UserAdapter(mData);
        listView.setAdapter(mAdapter);

        // Set a click listener
        listView.setClickListener(this);
    }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v){
        Integer tag=(Integer)v.itemView.getTag();
        Person item = mData.get(v.getPosition());
        Intent mapIntent = new Intent(this,MapActivity.class);
        mapIntent.putExtra("Location",item.getPosition());
        startActivity(mapIntent);
        // use this data to complete some action ...
    }

    public void setupData() {
       LatLng loc1= new LatLng(+34.67,-56.90);
        mData.add(new Person("kukush","ku123",loc1));

        LatLng loc2= new LatLng(+34.67,-56.90);
        mData.add(new Person("bini","bi123",loc2));


        LatLng loc3= new LatLng(+34.67,-56.90);
        mData.add(new Person("luka","lu123",loc3));
    }


    @Override
    public void onTopEmptyRegionClick(){
    }
}
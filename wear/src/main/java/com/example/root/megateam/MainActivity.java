package com.example.root.megateam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.example.root.megateam.adapter.UserAdapter;
import com.example.root.megateam.model.Constants;
import com.example.root.megateam.model.Person;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WearableActivity
        implements WearableListView.ClickListener,
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Sample dataset for the list
    //String[] elements = {"List Item 1", "List Item 2","List Item 3","List Item 4","List Item 5"};
    private List<Person> mData = new ArrayList<Person>();
    private UserAdapter mAdapter;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setupData();
        // Get the list component from the layout of the activity
        WearableListView listView=
                (WearableListView)findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        mAdapter = new UserAdapter(mData);
        listView.setAdapter(mAdapter);

        // Set a click listener
        listView.setClickListener(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setAmbientEnabled();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String previousGroup = settings.getString("group", null);
        if(previousGroup!=null)
            updateData(previousGroup);



    }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v){
        Integer tag=(Integer) v.itemView.getTag();
        Person item = mData.get(v.getAdapterPosition());
        Intent mapIntent = new Intent(this,MapActivity.class);
        mapIntent.putExtra("person", item);
        startActivity(mapIntent);
        // use this data to complete some action ...
    }

    /*public void setupData() {
       LatLng loc1= new LatLng(+34.67,-56.90);
        mData.add(new Person("kukush","ku123",loc1));

        LatLng loc2= new LatLng(+34.67,-56.90);
        mData.add(new Person("bini", "bi123", loc2));


        LatLng loc3= new LatLng(+34.67,-56.90);
        mData.add(new Person("luka", "lu123", loc3));
    }*/

    public void updateData(String j) {
        Log.d("WTT", "updateData - DATA=" + j);
        ArrayList<Person> people = null;
        try {
            people = (ArrayList<Person>) Constants.fromString(j);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i=0; i<people.size(); i++) {
            mData.add(people.get(i));
        }

        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Wearable.DataApi.removeListener(mGoogleApiClient, this);
        //mGoogleApiClient.disconnect();
    }

    @Override
    public void onTopEmptyRegionClick(){
        Log.d("WTT", "onTopEmptyRegionClick");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("WTT", "onConnected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("WTT", "onConnectionSuspended");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("WTT", "onDataChanged");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/people") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();

                    String data = dataMap.getString("group");


                    SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("group", data);
                    editor.commit();



                    updateData(data);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("WTT", "onConnectionFailed");
    }
}
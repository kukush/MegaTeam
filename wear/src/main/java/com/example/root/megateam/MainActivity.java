package com.example.root.megateam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import com.example.root.megateam.adapter.UserAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
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


    }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v){
        Integer tag=(Integer) v.itemView.getTag();
        Person item = mData.get(v.getPosition());
        Intent mapIntent = new Intent(this,MapActivity.class);
        mapIntent.putExtra("Location", item.getPosition());
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
        Gson gson = new Gson();
        ArrayList<Person> people = gson.fromJson(j, ArrayList.class);

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
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onTopEmptyRegionClick(){
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/people") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    updateData(dataMap.getString("group"));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
package com.example.root.megateam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DismissOverlayView;

import com.example.root.megateam.model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * Created by marwa on 14/11/2015.
 */
public class MapActivity extends WearableActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private MapFragment mMapFragment;
    private DismissOverlayView mDismissOverlay;
    private static final LatLng SYDNEY = new LatLng(-33.85704, 151.21522);
    private GoogleMap mMap;
    private LatLng position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        Person person = (Person) intent.getSerializableExtra("person");
        double latitude = person.getLat();
        double longitude = person.getLon();
        this.position = new LatLng(latitude, longitude);
        mDismissOverlay =
                (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.basic_wear_long_press_intro);
        mDismissOverlay.showIntroIfNecessary();
        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        // Enable ambient support, so the map remains visible in a simplified,
        // low-color display when the user is no longer actively using the app
        // and the app is still visible on the watch face.
        setAmbientEnabled();
        //UiSettings.setZoomControlsEnabled(true);
        //
        GoogleMapOptions options = new GoogleMapOptions();
        options.getZoomGesturesEnabled();


        //display a map of sydney

        String s1= Double.toString(latitude);
        String s2 = Double.toString(longitude);
        String toparse= "geo:" +s1+","+s2;
        Uri gmmIntentUri = Uri.parse(toparse); //we should put the coordinates of a person
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }



    }
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.addMarker(new MarkerOptions().position(position)
                .title("your friend position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
        mMap.setOnMapLongClickListener(this);
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        mDismissOverlay.show();
    }
    /**
     * Starts ambient mode on the map.
     * The API swaps to a non-interactive and low-color rendering of the map
     * when the user is no longer actively using the app.
     */
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        mMapFragment.onEnterAmbient(ambientDetails);
    }
    /**
     * Exits ambient mode on the map.
     * The API swaps to the normal rendering of the map when the user starts
     * actively using the app.
     */
    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        mMapFragment.onExitAmbient();
    }
}
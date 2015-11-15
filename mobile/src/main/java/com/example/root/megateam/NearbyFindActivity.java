package com.example.root.megateam;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.megateam.model.Constants;
import com.example.root.megateam.model.Person;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.ArrayList;

public class NearbyFindActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    String group;
    String nickName;
    String TAG = "WTT";
    int REQUEST_RESOLVE_ERROR = 1234;
    boolean iAmHost = false;
    GoogleApiClient mGoogleApiClient;
    boolean mResolvingError;
    boolean groupFound = false;
    String publishName;


    Message mMessage;
    MessageListener mMessageListener;
    ArrayAdapter<String> arrayAdapter;
    Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://flickering-inferno-9432.firebaseio.com/");

        Intent intent = getIntent();
        group = intent.getStringExtra(Constants.PREF_GROUP_NAME);
        nickName = intent.getStringExtra(Constants.PREF_NICKNAME);
        iAmHost = intent.getBooleanExtra(Constants.PREF_IS_STARTING_GROUP, false);


        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Nearby.MESSAGES_API)
                    .addApiIfAvailable(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

        }catch (Exception x) {
            x.printStackTrace();
        }


        publishName = (iAmHost ? "H§" + nickName + "§" + group : "U§" + nickName + "§none");

        mMessage = new Message(publishName.getBytes());

        if (iAmHost) {
            ((TextView) findViewById(R.id.textViewGroup)).setText(group);
            findViewById(R.id.fabContinue).setVisibility(View.VISIBLE);
            findViewById(R.id.fabContinue).setEnabled(false);
        }

        //

        ListView listView = (ListView) findViewById(R.id.listUsers);
        ArrayList<String> array = new ArrayList<String>();
        array.add(nickName);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.textViewList, array);
        listView.setAdapter(arrayAdapter);


        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                final String s = new String(message.getContent());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.fabContinue).setEnabled(true);

                        String a[] = s.split("§");
                        if (a.length != 3) return;

                        boolean remoteIsHost = ("H".equals(a[0]));
                        String nickName = a[1];
                        String group = a[2];

                        if (!iAmHost && !groupFound && remoteIsHost && !"".equals(group) && group != null) {
                            groupFound = true;
                            ((TextView) findViewById(R.id.textViewGroup)).setText(group);
                            NearbyFindActivity.this.group = group;

                            //??
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NearbyFindActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean(Constants.PREF_IS_STARTING_GROUP, iAmHost);
                            editor.putString(Constants.PREF_GROUP_NAME, group);
                            editor.commit();

                            Toast.makeText(NearbyFindActivity.this, "Found group '" + group + "'", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(NearbyFindActivity.this, "Found user '" + nickName + "'", Toast.LENGTH_LONG).show();

                        try {
                            if (arrayAdapter.getPosition(nickName) <= 0)
                                arrayAdapter.add(nickName);
                        } catch (Exception x) {
                        }

                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                Log.d(TAG, "Found");
            }
        };


        findViewById(R.id.fabContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(NearbyFindActivity.this, "Grouo '" + group + "' created!", Toast.LENGTH_LONG).show();


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NearbyFindActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Constants.PREF_IS_STARTING_GROUP, iAmHost);
                editor.putString(Constants.PREF_GROUP_NAME, group);
                editor.commit();


                ArrayList<Person> people = new ArrayList<Person>();
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    Person person = new Person(arrayAdapter.getItem(i), arrayAdapter.getItem(i), getApproximateLocation());//someday use a real unique id
                    people.add(person);
                }


                myFirebaseRef.child("groups").child(group).setValue(people);


                /*GoogleApiClient wearGoogleApiClient;
                wearGoogleApiClient = new GoogleApiClient.Builder(NearbyFindActivity.this)
                        .addApi(Wearable.API)
                        .addConnectionCallbacks(NearbyFindActivity.this)
                        .addOnConnectionFailedListener(NearbyFindActivity.this)
                        .build();*/

                String j = null;
                try {
                    j = Constants.toString(people);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/people");
                putDataMapReq.getDataMap().putString("group", j);
                PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                PendingResult<DataApi.DataItemResult> pendingResult =
                        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);




                /*editor.putString("serializedGroup", j);
                editor.commit();*/

                // to check notification
                DispayNotification();
                if (NotificationClass.check_members(people)) {
                    SendNotificationtoWear();
                }
                ///////////////////////////

            }
        });

        //startService(new Intent(this, GPSService.class));

    }

    private LatLng getApproximateLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);

        try {
            Location l = locationManager.getLastKnownLocation(bestProvider);
            return new LatLng(l.getLatitude(),l.getLongitude());
        } catch (Exception x) {}
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            // Clean up when the user leaves the activity.
            Nearby.Messages.unpublish(mGoogleApiClient, mMessage)
                    .setResultCallback(new ErrorCheckingCallback("unpublish()"));
            Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener)
                    .setResultCallback(new ErrorCheckingCallback("unsubscribe()"));
        }
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // GoogleApiClient connection callback.
    @Override
    public void onConnected(Bundle connectionHint) {
        Nearby.Messages.getPermissionStatus(mGoogleApiClient).setResultCallback(
                new ErrorCheckingCallback("getPermissionStatus", new Runnable() {
                    @Override
                    public void run() {
                        publishAndSubscribe();
                    }
                })
        );
    }

    // This is called in response to a button tap in the Nearby permission dialog.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Permission granted or error resolved successfully then we proceed
                // with publish and subscribe..
                publishAndSubscribe();
            } else {
                // This may mean that user had rejected to grant nearby permission.
                Log.i(TAG, "Failed to resolve error with code " + resultCode);
            }
        }
    }

    private void publishAndSubscribe() {
        // We automatically subscribe to messages from nearby devices once
        // GoogleApiClient is connected. If we arrive here more than once during
        // an activity's lifetime, we may end up with multiple calls to
        // subscribe(). Repeated subscriptions using the same MessageListener
        // are ignored.
        Nearby.Messages.publish(mGoogleApiClient, mMessage)
                .setResultCallback(new ErrorCheckingCallback("publish()"));
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener)
                .setResultCallback(new ErrorCheckingCallback("subscribe()"));
    }

    /**
     * A simple ResultCallback that logs when errors occur.
     * It also displays the Nearby opt-in dialog when necessary.
     */
    private class ErrorCheckingCallback implements ResultCallback<Status> {
        private final String method;
        private final Runnable runOnSuccess;

        private ErrorCheckingCallback(String method) {
            this(method, null);
        }

        private ErrorCheckingCallback(String method, @Nullable Runnable runOnSuccess) {
            this.method = method;
            this.runOnSuccess = runOnSuccess;
        }

        @Override
        public void onResult(@NonNull Status status) {
            if (status.isSuccess()) {
                Log.i(TAG, method + " succeeded.");
                if (runOnSuccess != null) {
                    runOnSuccess.run();
                }
            } else {
                // Currently, the only resolvable error is that the device is not opted
                // in to Nearby. Starting the resolution displays an opt-in dialog.
                if (status.hasResolution()) {
                    if (!mResolvingError) {
                        try {
                            status.startResolutionForResult(NearbyFindActivity.this,
                                    REQUEST_RESOLVE_ERROR);
                            mResolvingError = true;
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, method + " failed with exception: " + e);
                        }
                    } else {
                        // This will be encountered on initial startup because we do
                        // both publish and subscribe together.
                        Log.i(TAG, method + " failed with status: " + status
                                + " while resolving error.");
                    }
                } else {
                    Log.e(TAG, method + " failed with : " + status
                            + " resolving error: " + mResolvingError);
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }




    @Override
    public void onConnectionSuspended(int i) {
        Log.d(Constants.TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(Constants.TAG, "Connection Failed");
    }


    protected void DispayNotification() {

        // Invoking the default notification service
        NotificationManager myNotificationManager;
        int notificationIdOne = 111;
        int notificationIdTwo = 112;
        int numMessagesOne = 0;
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("New Message with explicit intent");
        mBuilder.setContentText("New message  to your wear ");

        mBuilder.setTicker("Explicit: Newotification Received!");
        mBuilder.setSmallIcon(R.drawable.abc_btn_check_material);
        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(++numMessagesOne);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationClass.class);
        resultIntent.putExtra("notificationId", notificationIdOne);

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent

        stackBuilder.addParentStack(NotificationClass.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT //can only be used once
                );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        myNotificationManager.notify(notificationIdOne, mBuilder.build());
    }

    public void SendNotificationtoWear()
    {int notificationId = 001;
        int notificationIdTwo = 112;
        boolean  eventId=true;
// Build intent for notification content
        Intent viewIntent = new Intent(this, NotificationClass.class);
        viewIntent.putExtra("notificationIdTwo", eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);
//////////////////////////////////////////
        Intent actionIntent = new Intent(this, MapActivity.class);
        PendingIntent actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


// Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_cast_off_light,
                        getString(R.string.accept), actionPendingIntent)
                        .build();
        /////////////////////////////
        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_template_icon_bg )
                        .setContentTitle("Group Member left")
                        .setContentText("Show Map")
                        .setContentIntent(viewPendingIntent);







// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

    }


}


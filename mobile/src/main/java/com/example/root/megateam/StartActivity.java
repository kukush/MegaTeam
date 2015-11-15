package com.example.root.megateam;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.megateam.model.Constants;

public class StartActivity extends AppCompatActivity {


    String previousGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


             // to check notification
        DispayNotification();
        if(notification.check_members())
        {
            SendNotificationtoWear();
        }
        ///////////////////////////



        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        previousGroup = settings.getString(Constants.PREF_GROUP_NAME, null);

        if(previousGroup!=null) {
            findViewById(R.id.buttonUse).setEnabled(true);
            ((Button)findViewById(R.id.buttonUse)).setText("See My Group '" + previousGroup + "'");
        }

        findViewById(R.id.buttonJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nickname = ((TextView) findViewById(R.id.editTextNick)).getText().toString();

                if ("".equals(nickname)) {
                    Toast.makeText(StartActivity.this, "Please enter Nickname!", Toast.LENGTH_SHORT).show();
                    return;
                }


                startNearbyActivity(false, nickname, null);

            }
        });

        findViewById(R.id.buttonUse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(StartActivity.this, "Not Yet Implemented", Toast.LENGTH_SHORT).show();

            }
        });


        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Enter Group Name");

                // Set up the input
                final EditText input = new EditText(StartActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String groupName = input.getText().toString();

                        if ("".equals(groupName)) {
                            Toast.makeText(StartActivity.this, "Please enter Group Name!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        String nickname = ((TextView)findViewById(R.id.editTextNick)).getText().toString();

                        if("".equals(nickname)) {
                            Toast.makeText(StartActivity.this, "Please enter Nickname!", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        startNearbyActivity(true, nickname, groupName);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


    }

    private void startNearbyActivity(boolean host, String nickName, String groupName) {
        Intent intent = new Intent(this, NearbyFindActivity.class);
        intent.putExtra(Constants.PREF_GROUP_NAME, groupName);
        intent.putExtra(Constants.PREF_IS_STARTING_GROUP, host);
        intent.putExtra(Constants.PREF_NICKNAME, nickName);
        startActivity(intent);
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
        Intent resultIntent = new Intent(this, notification.class);
        resultIntent.putExtra("notificationId", notificationIdOne);

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent

        stackBuilder.addParentStack(notification.class);
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
        Intent viewIntent = new Intent(this, notification.class);
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

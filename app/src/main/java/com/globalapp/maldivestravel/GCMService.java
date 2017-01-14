package com.globalapp.maldivestravel;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.kinvey.android.push.KinveyGCMService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Smiley on 7/6/2016.
 */
public class GCMService extends KinveyGCMService {
    @Override
    public void onMessage(String message) {
        try {
            JSONObject details = new JSONObject(message);
            String msg = details.getString("message");
            String title = details.getString("Driver");
            displayNotification(msg,title);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String error) {
        displayNotification(error,getApplicationContext().getResources().getString(R.string.app_name));
    }

    @Override
    public void onDelete(String deleted) {
        displayNotification(deleted,getApplicationContext().getResources().getString(R.string.app_name));
    }

    @Override
    public void onRegistered(String gcmID) {
        displayNotification(gcmID,getApplicationContext().getResources().getString(R.string.app_name));
    }

    @Override
    public void onUnregistered(String oldID) {
        displayNotification(oldID,getApplicationContext().getResources().getString(R.string.app_name));
    }


    //This method will return the WakefulBroadcastReceiver class you define in the next step
    public Class getReceiver() {
        return GCMReceiver.class;
    }


    private void displayNotification(String message,String title) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(message);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}


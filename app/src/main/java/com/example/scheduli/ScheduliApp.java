package com.example.scheduli;

import android.app.Application;

import com.example.scheduli.data.fireBase.DataBaseSetup;
import com.example.scheduli.utils.UpcomingAppointmentNotification;


/***
 * Override class for the application startup actions.
 * Used to make sure that FireBase persistence is enabled for data storage offline.
 */
public class ScheduliApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Set cache for fire database
        DataBaseSetup.setDatabasePersistentOn();

        //Register Notification channels
        UpcomingAppointmentNotification.createNotificationChannel(this);
    }


}

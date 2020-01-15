package com.example.scheduli.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpcomingAppointmentNotification {
    private static final String NOTIFICATION_TAG = "UpcomingAppointment";
    public static final String NOTIFICATION_CHANNEL_ID = "1010101";

    public static NotificationCompat.Builder createNotification(final Context context, final JoinedAppointment appointment) {
        final Resources res = context.getResources();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM, HH:mm");
        final String title = res.getString(R.string.upcoming_appointment_notification_title);
        final String text = res.getString(R.string.upcoming_appointment_notification_main_text,
                appointment.getProviderCompanyName(), simpleDateFormat.format(new Date(appointment.getAppointment().getStart())));

        return getBuilder(context, title, text);
    }

    private static NotificationCompat.Builder getBuilder(Context context, String title, String text) {
        return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_upcoming_appointment)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.upcoming_notification_channel);
            String description = context.getString(R.string.upcoming_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

}

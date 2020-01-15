package com.example.scheduli.ui.appointmentDetails;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.NotificationPublisher;
import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.example.scheduli.utils.DownloadImageTask;
import com.example.scheduli.utils.UpcomingAppointmentNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppointmentDetailsActivity extends BaseMenuActivity {
    public static final String APPOINTMENT_DETAILS = "AppointmentDetailsActivityPassedClass";
    public static final String DETAILS_TAG = "Appointment Details Activity";

    private ImageView profileImage;
    private ImageButton callProviderBtn;
    private TextView providerNameTv;
    private TextView providerProfessionTv;
    private TextView providerAddressTv;
    private TextView providerPhoneTv;
    private TextView serviceNameTv;
    private TextView serviceCostTv;
    private TextView appointmentDate;
    private TextView appointmentTimes;
    private JoinedAppointment joinedAppointment;
    private Button setReminderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        initView();

        fillAppointmentFields();

        callProviderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfEmpty(providerPhoneTv)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + providerPhoneTv.getText().toString()));
                    startActivity(intent);
                } else {
                    Toast.makeText(AppointmentDetailsActivity.this, getString(R.string.provider_missing_phone_error_message), Toast.LENGTH_LONG).show();
                }
            }
        });

        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetNotificationReminderClass().execute(joinedAppointment);
                Toast.makeText(AppointmentDetailsActivity.this, "Set Notification for " + 25 + " before the appointment", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean checkIfEmpty(TextView textView) {
        return textView.getText().toString().isEmpty();
    }

    private void fillAppointmentFields() {
        Log.i(DETAILS_TAG, "Getting information from intent to display in the activity");
        Intent intent = getIntent();
        joinedAppointment = intent.getParcelableExtra(APPOINTMENT_DETAILS);

        new DownloadImageTask(profileImage).execute(joinedAppointment.getProviderImageUrl());
        providerNameTv.setText(joinedAppointment.getProviderCompanyName());
        providerProfessionTv.setText(joinedAppointment.getProviderProfession());
        providerPhoneTv.setText(joinedAppointment.getProviderPhoneNumber());
        providerAddressTv.setText(joinedAppointment.getProviderAddress());
        serviceNameTv.setText(joinedAppointment.getAppointment().getServiceName());
        serviceCostTv.setText(joinedAppointment.getAppointment().getServiceCost());

        Date date = new Date(joinedAppointment.getAppointment().getStart());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        appointmentDate.setText(dateFormat.format(date));

        //set time of appointment
        Date start = new Date(joinedAppointment.getAppointment().getStart());
        Date end = new Date(joinedAppointment.getAppointment().getEnd());
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String timeString = timeFormatter.format(start) + " - " + timeFormatter.format(end);
        appointmentTimes.setText(timeString);
    }

    private void initView() {
        Toolbar mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);

        profileImage = findViewById(R.id.appointment_details_profileImage);
        callProviderBtn = findViewById(R.id.btn_appointment_details_call);
        providerNameTv = findViewById(R.id.tv_appDetails_providerCompany);
        providerProfessionTv = findViewById(R.id.tv_appDetails_providerProffesion);
        providerAddressTv = findViewById(R.id.tv_appDetails_providerAddress);
        providerPhoneTv = findViewById(R.id.tv_appDetails_providerPhone);
        serviceNameTv = findViewById(R.id.tv_appDetails_serviceName);
        serviceCostTv = findViewById(R.id.tv_appDetails_serviceCost);
        appointmentDate = findViewById(R.id.tv_appDetails_appointmentDate);
        appointmentTimes = findViewById(R.id.tv_appDetails_appointmentTimes);
        setReminderButton = findViewById(R.id.btn_details_apppointmet_setReminder);
    }


    private class SetNotificationReminderClass extends AsyncTask<JoinedAppointment, Void, Void> {

        @Override
        protected Void doInBackground(JoinedAppointment... joinedAppointments) {

            for (JoinedAppointment appointment : joinedAppointments) {
                NotificationCompat.Builder builder = UpcomingAppointmentNotification.createNotification(getApplicationContext(), joinedAppointment);
                Notification notification = builder.build();

                scheduleNotification(notification, joinedAppointment, 25); //TODO make dynamic
            }

            return null;
        }

        private void scheduleNotification(Notification notification, JoinedAppointment appointment, int delayInMinutes) {

            Intent notificationIntent = new Intent(AppointmentDetailsActivity.this, NotificationPublisher.class);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            long futureInMillis = SystemClock.elapsedRealtime() + delayInMinutes;

            Calendar beforeAppointment = Calendar.getInstance();
            beforeAppointment.setTimeInMillis(appointment.getAppointment().getStart());
            beforeAppointment.add(Calendar.MINUTE, -1 * delayInMinutes);


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, beforeAppointment.getTimeInMillis(), pendingIntent);
        }

    }


}

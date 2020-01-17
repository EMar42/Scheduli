package com.example.scheduli.ui.appointmentDetails;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.NotificationPublisher;
import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.utils.UpcomingAppointmentNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AppointmentDetailsActivity extends BaseMenuActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    public static final String APPOINTMENT_DETAILS = "AppointmentDetailsActivityPassedClass";
    public static final String DETAILS_TAG = "Appointment Details Activity";

    private Calendar alarmTime;
    private Calendar alarmDate;

    private AppointmentDetailsViewModel detailsViewModel;

    //Controls
    private AppBarConfiguration detailsNavBarConfig;
    private NavController navController;
    BottomNavigationView detailsBottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        detailsViewModel = ViewModelProviders.of(this).get(AppointmentDetailsViewModel.class);

        initView();

        fillAppointmentFields();

        /*
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

        //Setting the date for the alarm
        setAlarmDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getSupportFragmentManager(), "alarm picker");
            }
        });

        //Setting the time for the alarm
        setAlarmTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimerPickerDialogFragment();
                timePicker.show(getSupportFragmentManager(), "alarm picker");
            }
        });

        //Triggering alarm creation
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmDate != null && alarmTime != null) {
                    Calendar timeForAlarm = Calendar.getInstance();
                    timeForAlarm.setTimeInMillis(alarmDate.getTimeInMillis());
                    timeForAlarm.set(Calendar.HOUR_OF_DAY, alarmTime.get(Calendar.HOUR_OF_DAY));
                    timeForAlarm.set(Calendar.MINUTE, alarmTime.get(Calendar.MINUTE));
                    timeForAlarm.set(Calendar.SECOND, 0);

                    new SetNotificationReminderClass(timeForAlarm).execute(joinedAppointment);
                }
            }
        });

         */
    }

    private boolean checkIfEmpty(TextView textView) {
        return textView.getText().toString().isEmpty();
    }

    private void fillAppointmentFields() {
        Log.i(DETAILS_TAG, "Getting information from intent to display in the activity");
        Intent intent = getIntent();
        detailsViewModel.setJoinedAppointment((JoinedAppointment) intent.getParcelableExtra(APPOINTMENT_DETAILS));

//        new DownloadImageAsync(profileImage).execute(joinedAppointment.getProviderImageUrl());
//        providerNameTv.setText(joinedAppointment.getProviderCompanyName());
//        providerProfessionTv.setText(joinedAppointment.getProviderProfession());
//        providerPhoneTv.setText(joinedAppointment.getProviderPhoneNumber());
//        providerAddressTv.setText(joinedAppointment.getProviderAddress());
//        serviceNameTv.setText(joinedAppointment.getAppointment().getServiceName());
//        serviceCostTv.setText(joinedAppointment.getAppointment().getServiceCost());
//
//        Date date = new Date(joinedAppointment.getAppointment().getStart());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        appointmentDate.setText(dateFormat.format(date));
//
//        //set time of appointment
//        Date start = new Date(joinedAppointment.getAppointment().getStart());
//        Date end = new Date(joinedAppointment.getAppointment().getEnd());
//        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
//        String timeString = timeFormatter.format(start) + " - " + timeFormatter.format(end);
//        appointmentTimes.setText(timeString);
    }

    private void initView() {
        Toolbar mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);
        detailsBottomView = findViewById(R.id.appointment_details_bottom_nav);
        detailsNavBarConfig = new AppBarConfiguration.Builder(
                R.id.navigation_appointment_details, R.id.navigation_appointment_provider_details)
                .build();
        navController = Navigation.findNavController(this, R.id.details_fragment_host);
        NavigationUI.setupActionBarWithNavController(this, navController, detailsNavBarConfig);
        NavigationUI.setupWithNavController(detailsBottomView, navController);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar timeForAlarm = Calendar.getInstance();
        timeForAlarm.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timeForAlarm.set(Calendar.MINUTE, minute);
        timeForAlarm.set(Calendar.SECOND, 0);

        this.alarmTime = timeForAlarm;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar dateForAlarm = Calendar.getInstance();
        dateForAlarm.set(Calendar.YEAR, year);
        dateForAlarm.set(Calendar.MONTH, month);
        dateForAlarm.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        this.alarmDate = dateForAlarm;
    }


    private class SetNotificationReminderClass extends AsyncTask<JoinedAppointment, Void, Void> {

        private final Calendar dateForAlarm;

        public SetNotificationReminderClass(Calendar alarmDate) {
            this.dateForAlarm = alarmDate;
        }

        @Override
        protected Void doInBackground(JoinedAppointment... joinedAppointments) {

            for (JoinedAppointment appointment : joinedAppointments) {
                NotificationCompat.Builder builder = UpcomingAppointmentNotification.createNotification(getApplicationContext(), joinedAppointments[0]);
                Notification notification = builder.build();

                scheduleNotification(notification, joinedAppointments[0]);
            }

            return null;
        }

        private void scheduleNotification(Notification notification, JoinedAppointment appointment) {
            Calendar appointmentDate = Calendar.getInstance();
            appointmentDate.setTimeInMillis(appointment.getAppointment().getStart());

            if (dateForAlarm.before(appointmentDate)) {
                Intent notificationIntent = new Intent(getApplicationContext(), NotificationPublisher.class);
                notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NotificationPublisher.NOTIFICATION_COUNT++);
                notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                appointment.getAppointment().setAlarmReminderTime(dateForAlarm.getTimeInMillis());
                UserDataRepository.getInstance().updateUserAppointment(appointment.getAppointment());

                Log.i(DETAILS_TAG, "Setting Alarm for " + dateForAlarm.getTime().toString());
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateForAlarm.getTimeInMillis(), pendingIntent);
            }
        }

    }


}

package com.example.scheduli.ui.appointmentDetails;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.NotificationPublisher;
import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.ui.dialogs.DatePickerDialogFragment;
import com.example.scheduli.ui.dialogs.TimerPickerDialogFragment;
import com.example.scheduli.utils.DownloadImageTask;
import com.example.scheduli.utils.UpcomingAppointmentNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppointmentDetailsActivity extends BaseMenuActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
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
    private Button setAlarmTimeButton;
    private Button setAlarmDateButton;
    private Button setAlarmButton;
    private Calendar alarmTime;
    private Calendar alarmDate;

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
                    Toast.makeText(AppointmentDetailsActivity.this, "Set alarm for " + timeForAlarm.getTime().toString(), Toast.LENGTH_LONG).show();
                }
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

    Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
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
        setAlarmTimeButton = findViewById(R.id.btn_details_apppointmet_pickTime);
        setAlarmDateButton = findViewById(R.id.btn_appointment_details_pickDate);
        setAlarmButton = findViewById(R.id.btn_appointment_details_set_alarm);
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
                NotificationCompat.Builder builder = UpcomingAppointmentNotification.createNotification(getApplicationContext(), joinedAppointment);
                Notification notification = builder.build();

                scheduleNotification(notification, joinedAppointment);
            }

            return null;
        }

        private void scheduleNotification(Notification notification, JoinedAppointment appointment) {
            Calendar appointmentDate = Calendar.getInstance();
            appointmentDate.setTimeInMillis(appointment.getAppointment().getStart());

            if (dateForAlarm.before(appointmentDate)) {
                Intent notificationIntent = new Intent(AppointmentDetailsActivity.this, NotificationPublisher.class);
                notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NotificationPublisher.NOTIFICATION_COUNT++);
                notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.i(DETAILS_TAG, "Setting Alarm for " + dateForAlarm.getTime().toString());
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateForAlarm.getTimeInMillis(), pendingIntent);

                appointment.getAppointment().setAlarmReminderTime(dateForAlarm.getTimeInMillis());
                UserDataRepository.getInstance().updateUserAppointment(appointment.getAppointment());
            }
        }

    }


}

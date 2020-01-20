package com.example.scheduli.ui.appointmentDetails;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AppointmentDetailsActivity extends BaseMenuActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    public static final String APPOINTMENT_DETAILS = "AppointmentDetailsActivityPassedClass";
    public static final String DETAILS_TAG = "Appointment Details Activity";

    private AppointmentDetailsViewModel detailsViewModel;

    //Controls
    private AppBarConfiguration detailsNavBarConfig;
    private NavController navController;
    BottomNavigationView detailsBottomView;
    private Calendar alarmSetDateFromDialog;
    private Calendar alarmSetTimeFromDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        detailsViewModel = ViewModelProviders.of(this).get(AppointmentDetailsViewModel.class);
        initView();
        fillAppointmentFields();
    }

    private void fillAppointmentFields() {
        Log.i(DETAILS_TAG, "Getting information from intent to display in the activity");
        Intent intent = getIntent();
        detailsViewModel.setJoinedAppointment((JoinedAppointment) intent.getParcelableExtra(APPOINTMENT_DETAILS));
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        alarmSetDateFromDialog = Calendar.getInstance();
        alarmSetDateFromDialog.set(Calendar.YEAR, year);
        alarmSetDateFromDialog.set(Calendar.MONTH, month);
        alarmSetDateFromDialog.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        detailsViewModel.setAlarmDate(alarmSetDateFromDialog);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        alarmSetTimeFromDialog = Calendar.getInstance();
        alarmSetTimeFromDialog.set(Calendar.HOUR_OF_DAY, hourOfDay);
        alarmSetTimeFromDialog.set(Calendar.MINUTE, minute);
        alarmSetTimeFromDialog.set(Calendar.SECOND, 0);
        detailsViewModel.setAlarmTime(alarmSetTimeFromDialog);
    }


}

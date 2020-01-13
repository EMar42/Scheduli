package com.example.scheduli.ui.appointmentDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Appointment;

public class AppointmentDetailsActivity extends BaseMenuActivity {
    public static final String APPOINTMENT_DETAILS = "AppointmentDetailsActivityPassedClass";
    public static final String DETAIILS_TAG = "Appointment Details Activity";

    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);


        Intent intent = getIntent();
        Appointment appointment = intent.getParcelableExtra(APPOINTMENT_DETAILS);
        Log.i(DETAIILS_TAG, "Stated details activity loaded appointment " + appointment.getServiceName());

    }
}

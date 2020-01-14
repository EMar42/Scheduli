package com.example.scheduli.ui.appointmentDetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;

public class AppointmentDetailsActivity extends BaseMenuActivity {
    public static final String APPOINTMENT_DETAILS = "AppointmentDetailsActivityPassedClass";
    public static final String DETAIILS_TAG = "Appointment Details Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        initView();


        Intent intent = getIntent();
        JoinedAppointment joinedAppointment = intent.getParcelableExtra(APPOINTMENT_DETAILS);
    }

    private void initView() {
        Toolbar mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);
    }
}

package com.example.scheduli.ui.appointmentDetails;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scheduli.R;

public class AppointmentDetailsActivity extends AppCompatActivity {
    public static final String APPOINTMENT_DETAILS = "AppointmentDetailsActivityPassedClass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
    }
}

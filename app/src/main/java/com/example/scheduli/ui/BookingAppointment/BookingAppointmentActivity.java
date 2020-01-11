package com.example.scheduli.ui.BookingAppointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.scheduli.R;
import com.example.scheduli.data.Provider;

public class BookingAppointmentActivity extends AppCompatActivity {


    TextView company_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        Intent intent = getIntent();

        //TODO: get Provider Object from Extras (Parcel)
        Provider provider = (Provider) intent.getParcelableExtra("provider");

        company_txt = (TextView) findViewById(R.id.textview_chosen_company);
        company_txt.setText(provider.getCompanyName());


    }
}

package com.example.scheduli.ui.BookingAppointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.scheduli.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class BookingAppointmentActivity extends AppCompatActivity {

    DatabaseReference databaseReference ;

    TextView company_txt;
    TextView serviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        Intent intent = getIntent();

        //TODO: get Provider Object through Firebase: users -> provider
        company_txt = (TextView) findViewById(R.id.booking_act_chosen_company);
        serviceName = (TextView) findViewById(R.id.service_name) ;
        company_txt.setText(intent.getStringExtra("companyName"));
        serviceName.setText(intent.getStringExtra("pid"));



        String pid = intent.getStringExtra("pid");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("providers");
//        databaseReference.orderByKey().equalTo(pid);
//        company_txt.setText(databaseReference.getKey());




    }
}

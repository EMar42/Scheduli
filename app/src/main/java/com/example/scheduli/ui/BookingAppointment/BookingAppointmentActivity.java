package com.example.scheduli.ui.BookingAppointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.scheduli.R;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.ServiceAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties

public class BookingAppointmentActivity extends AppCompatActivity {

    DatabaseReference databaseReference ;
    private static final String TAG_BOOKING_ACT = "BookingAppointmentActiv";
    private RecyclerView.LayoutManager mLayout;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Service> servicesList;


    TextView company_txt;
    TextView serviceName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        Intent intent = getIntent();
        String pid = intent.getStringExtra("pid");
        Log.d(TAG_BOOKING_ACT, "Got requested id: " + pid);

        //TODO: get Provider Object through Firebase: users -> provider


        company_txt = (TextView) findViewById(R.id.booking_act_chosen_company);
        company_txt.setText(intent.getStringExtra("companyName"));
        System.out.println("pid: " + intent.getStringExtra("pid"));


        Service service1 = new Service();
        service1.setName("service1");
        Service service2 = new Service();
        service2.setName("service2");
        Service service3 = new Service();
        service3.setName("service3");
        servicesList = new ArrayList<>();

        servicesList.add(service1);
        servicesList.add(service2);
        servicesList.add(service3);


        mLayout = new LinearLayoutManager(this);
        mAdapter = new ServiceAdapter(servicesList);
        recyclerView = (RecyclerView) findViewById(R.id.services_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(mLayout);
recyclerView.setAdapter(mAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("providers").child("services");




    }
}

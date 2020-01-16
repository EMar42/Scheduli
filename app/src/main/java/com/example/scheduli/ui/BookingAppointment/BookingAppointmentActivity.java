package com.example.scheduli.ui.BookingAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.scheduli.R;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.ServiceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties

public class BookingAppointmentActivity extends AppCompatActivity {

    DatabaseReference databaseReference ;

    private static final String TAG_BOOKING_ACT = "BookingAppointmentActiv";
    private RecyclerView.LayoutManager mLayout;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Service> servicesList;
    private String pid;

    TextView company_txt;
    TextView serviceName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);


        init();
    }

    private void init() {
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        Log.d(TAG_BOOKING_ACT, "Got requested id: " + pid);

        company_txt = (TextView) findViewById(R.id.booking_act_chosen_company);
        company_txt.setText(intent.getStringExtra("companyName"));

        servicesList = new ArrayList<>();
        databaseReference =  FirebaseDatabase.getInstance().getReference("providers").child(pid).child("services");
        mLayout = new GridLayoutManager(this,2);
        mAdapter = new ServiceAdapter(servicesList);
        recyclerView = (RecyclerView) findViewById(R.id.services_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);
        getServices();
    }

    private void getServices() {
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            servicesList.clear();

            if(dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Service service = snapshot.getValue(Service.class);
//                    System.out.println("[TEST] service name: " + service.getName()); //TEST
                    servicesList.add(service);

                }
                Log.d(TAG_BOOKING_ACT, "got " + servicesList.size() + " services.");
                /********************************************************/
                //TEST: for testing the recycle view
//                Service service1 = new Service();
//                service1.setName("test1");
//                Service service2 = new Service();
//                service2.setName("test2");
//                Service service3 = new Service();
//                service3.setName("test3");
//                Service service4 = new Service();
//                service4.setName("test4");
//                Service service5 = new Service();
//                service5.setName("test5");
//                servicesList.add(service1);
//                servicesList.add(service2);
//                servicesList.add(service3);
//                servicesList.add(service4);
//                servicesList.add(service5);
                /********************************************************/

                if (!servicesList.isEmpty()) {
//                    adapter = new ProvidersAdapter(providersList);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            Log.e(TAG_BOOKING_ACT,"Something went wrong.. ");
        }
    };
}

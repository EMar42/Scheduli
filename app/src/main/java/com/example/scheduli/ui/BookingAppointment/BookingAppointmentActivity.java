package com.example.scheduli.ui.BookingAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.ServiceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;

@IgnoreExtraProperties

public class BookingAppointmentActivity extends BaseMenuActivity {

    DatabaseReference databaseReference ;
    Button btn_next;

    private static final String TAG_BOOKING_ACT = "BookingAppointmentActiv";
    private RecyclerView.LayoutManager mLayout;
    private ServiceAdapter mAdapter;
    private ArrayList<Service> servicesList;
    private String pid;
    Provider provider;
    private int servicePosition = -1;

    TextView company_txt;
    TextView serviceName;
    RecyclerView recyclerView;
    private Toolbar mainToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);

        init();


        btn_next = (Button) findViewById(R.id.btn_next_2);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(servicePosition >= 0) {
                    Intent intent = new Intent(BookingAppointmentActivity.this, SetAppointmentTime.class);
                    intent.putExtra("provider", provider);
                    intent.putExtra("service", provider.getServices().get(servicePosition));
                    intent.putExtra("position" , servicePosition);
                    intent.putExtra("pid", pid);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Select service first." ,Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void init() {

        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        provider = intent.getParcelableExtra("provider");
        Log.d(TAG_BOOKING_ACT, "Got requested id: " + pid);
        System.out.println("Got provider: " + provider.getCompanyName()); // TEST
        System.out.println("Got provider services: " + provider.getServices()); // TEST

        company_txt = (TextView) findViewById(R.id.set_appointment_time_act_chosen_service);
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

        mAdapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                servicesList.get(position);
                servicePosition = position;
                Log.d(TAG_BOOKING_ACT, "User choose service: [" + position + "] - " + servicesList.get(position).getName());
            }
        });
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

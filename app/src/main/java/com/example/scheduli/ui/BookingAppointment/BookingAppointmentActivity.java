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

import java.util.ArrayList;

@IgnoreExtraProperties

public class BookingAppointmentActivity extends BaseMenuActivity {

    private static final String TAG_BOOKING_ACT = "BookingAppointmentActiv";

    private Toolbar mainToolbar;
    private Button btn_next;
    private Button btn_back;
    private TextView company_txt;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayout;
    private ServiceAdapter mAdapter;

    private ArrayList<Service> servicesList;
    private int servicePosition = -1;
    private String pid;
    private Provider provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);

        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);

        init();


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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {

        btn_next = (Button) findViewById(R.id.btn_next_2);
        btn_back = (Button) findViewById(R.id.btn_back_2);
        company_txt = (TextView) findViewById(R.id.set_appointment_time_act_chosen_service);
        recyclerView = (RecyclerView) findViewById(R.id.services_recycleview);

        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        provider = intent.getParcelableExtra("provider");
        Log.d(TAG_BOOKING_ACT, "Got requested id: " + pid);
        System.out.println("Got provider: " + provider.getCompanyName()); // TEST
        System.out.println("Got provider services: " + provider.getServices()); // TEST

        company_txt.setText(intent.getStringExtra("companyName"));
        servicesList = new ArrayList<>();
        servicesList = provider.getServices();

        mLayout = new GridLayoutManager(this,2);
        mAdapter = new ServiceAdapter(BookingAppointmentActivity.this, servicesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //TODO: why the app didnt provide the position? ()
                Log.d(TAG_BOOKING_ACT, "Clicked position = " + position);
                servicePosition = position;
                servicesList.get(position);
                Log.d(TAG_BOOKING_ACT, "User choose service: [" + position + "] - " + servicesList.get(position).getName());
            }
        });
    }

}

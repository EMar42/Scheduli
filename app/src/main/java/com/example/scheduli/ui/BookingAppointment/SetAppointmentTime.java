package com.example.scheduli.ui.BookingAppointment;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.Sessions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SetAppointmentTime extends BaseMenuActivity {

    DatabaseReference databaseReference ;
    private Toolbar mainToolbar;
    private static final String TAG = "SetAppointmentTime";

    private Service service;
    private Provider provider;
    private List<Sessions> sessionsList;
    private Map<String, ArrayList<Sessions>> dailySessions; // key is a date (day/month/year).
    private RecyclerView.LayoutManager mLayout;
    private String pid;


    private Button next;
    private Button back;
    private TextView serviceChoosen;

    //parent (dates) cycleview (Item)
    private RecyclerView recyclerViewParent;
    private TextView dateText;
    private DateAdapter dateAdapter;

    //child (slots) cycleview (SubItem)
    private RecyclerView recyclerViewChild;
    private SlotAdapter slotAdapter;
    private List<Date> slots;
    private int numOfSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_time);

        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);

        recyclerViewChild = (RecyclerView) findViewById(R.id.recycleview_available_slots);
        serviceChoosen = (TextView) findViewById(R.id.set_appointment_time_act_chosen_service);

//        Sessions session1 = new Sessions( 1546329600000l, 1546344000000l, "FakeID", true);
//        Sessions session2 = new Sessions( 1546333200000l, 1546344000000l, "FakeID", true);
//        Sessions session3 = new Sessions( 1546336800000l, 1546344000000l, "FakeID", true);
//        Sessions session4 = new Sessions( 1546340400000l, 1546344000000l, "FakeID", true);

//        sessionsList.add(session1);
//        sessionsList.add(session2);
//        sessionsList.add(session3);
//        sessionsList.add(session4);

        Intent intent = getIntent();
        provider = intent.getParcelableExtra("provider");
        service = intent.getParcelableExtra("service");
        pid = intent.getStringExtra("pid");
        serviceChoosen.setText(provider.getCompanyName() + " ⌘ " + service.getName());
        dailySessions = service.getDailySessions();


        slots = new ArrayList<>();
        Date date = new Date(1446344000000l);
        slots.add(date);
        Date date1 = new Date(1246354000000l);
        slots.add(date1);
        Date date2 = new Date(1546364000000l);
        slots.add(date2);
        Date date3 = new Date(1046374000000l);
        slots.add(date3);
        recyclerViewParent = (RecyclerView) findViewById(R.id.recycleview_available_slots);
        databaseReference =  FirebaseDatabase.getInstance().getReference("providers").child(pid).child("services");
        mLayout = new GridLayoutManager(this,3);
        slotAdapter = new SlotAdapter(slots);
        recyclerViewChild.setHasFixedSize(true);
        recyclerViewChild.setLayoutManager(mLayout);
        recyclerViewChild.setAdapter(slotAdapter);



    }
}

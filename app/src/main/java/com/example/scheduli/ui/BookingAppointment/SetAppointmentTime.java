package com.example.scheduli.ui.BookingAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.Sessions;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetAppointmentTime extends BaseMenuActivity {

    DatabaseReference databaseReference;
    private Toolbar mainToolbar;
    private static final String TAG = "SetAppointmentTime";


    private Service service;
    private Provider provider;
    private List<Sessions> sessionsList;
    private Map<String, ArrayList<Sessions>> dailySessions; // key is a date (day/month/year).
    private RecyclerView.LayoutManager mLayout;
    private RecyclerView.LayoutManager mLayout2;

    private String pid;
    private int servicePosition;

    private List<Long> testSlots;

    private Button next;
    private Button back;
    private TextView serviceChoosen;
    private TextView datePick;

    //Calendar
    CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    //parent (dates) cycleview (Item)
    private RecyclerView recyclerViewParent;
    private TextView dateText;
    private DateAdapter dateAdapter;
    private List<String> dates;

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


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        dates = new ArrayList<>();
        slots = new ArrayList<>();
        testSlots = new ArrayList<>();


        recyclerViewChild = (RecyclerView) findViewById(R.id.recycleview_available_slots1);
        serviceChoosen = (TextView) findViewById(R.id.set_appointment_time_act_chosen_service);
        datePick = (TextView) findViewById(R.id.date_pick);

        Intent intent = getIntent();
        provider = intent.getParcelableExtra("provider");
        service = intent.getParcelableExtra("service");
        pid = intent.getStringExtra("pid");
        servicePosition = intent.getIntExtra("position", 0);
        System.out.println("Got service: " + service.getName()); // TEST
        System.out.println("Got service sessoins " + "[" + service.getDailySessions().size() + "] :" + service.getDailySessions()); // TEST

        serviceChoosen.setText(provider.getCompanyName() + " ⌘ " + service.getName());
        dailySessions = service.getDailySessions();
        printMap(dailySessions);

        System.out.println("[TEST]: date: " + dates.get(0));

//Event:

        for (int i = 0; i < testSlots.size(); i++) {
            Date date = new Date(testSlots.get(i));
            Event event = new Event(Color.GREEN, date.getTime(), "somethig to do here..");
            compactCalendarView.addEvent(event);

        }
        Event event = new Event(Color.GREEN, 1580162400000L, "somthig to do here");
        compactCalendarView.addEvent(event);


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();


                for (int i = 0; i < dates.size(); i++) {

                    if (dateClicked.getTime() == 1580162400000L) {
                        //TODO: add Formater here
                        datePick.setText(dates.get(i));
                        getSessions(dates.get(0));

                    } else {
                        Toast.makeText(context, String.valueOf(dateClicked.getTime()), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormat.format(firstDayOfNewMonth));

            }
        });


        recyclerViewParent = (RecyclerView) findViewById(R.id.recycleview_available_slots1);
//        recyclerViewChild = (RecyclerView) findViewById(R.id.recycleview_available_slots2);
        databaseReference = FirebaseDatabase.getInstance().getReference("providers").child(pid).child("services");
        mLayout = new GridLayoutManager(this, 3);
//        dateAdapter = new DateAdapter(dates);
        slotAdapter = new SlotAdapter(slots);
        recyclerViewParent.setLayoutManager(mLayout);
        recyclerViewParent.setHasFixedSize(true);
        recyclerViewParent.setAdapter(slotAdapter);

    }


    public void printMap(Map mp) {

        if (mp != null) {
            Iterator it = mp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                dates.add(pair.getKey().toString());
                it.remove(); // avoids a ConcurrentModificationException
            }
        } else {
            System.out.println("[TEST] null map..");
        }
    }

    public void getSessions(String date) {
        int i = 0;
        databaseReference = FirebaseDatabase.getInstance().getReference("providers").child(pid).child("services").child(String.valueOf(servicePosition)).child("dailySessions").child(date);

//        for(i = 0; i< 3 ;  i++) {
        databaseReference.child(String.valueOf("0"));
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
//        }


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            slots.clear();

            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Sessions sessions = snapshot.getValue(Sessions.class);
                    System.out.println("[TEST] sesions start: " + sessions.getStart()); //TEST
                    testSlots.add(sessions.getStart());
                    slots.add(new Date(sessions.getStart()));

                }
                System.out.println("got " + slots.size() + " sessions.");
                /********************************************************/
                //TEST: for testing the recycle view
//        Date date = new Date(1446344000000l);
//        slots.add(date);
//        Date date1 = new Date(1246354000000l);
//        slots.add(date1);
//        Date date2 = new Date(1546364000000l);
//        slots.add(date2);
//        Date date3 = new Date(1046374000000l);
//        slots.add(date3);
                /********************************************************/

                if (!slots.isEmpty()) {
//                    System.out.println(" [TEST] slots is not empty");
                    recyclerViewParent.setAdapter(slotAdapter);
                }
            } else {
                System.out.println("[TEST]: snapshot not exists");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            System.out.println("Something went wrong..");
        }
    };

    private void loadSlotsFromDate(String date) {

//        ArrayList<Sessions> sessions =  new ArrayList<Sessions>();
//        sessions = service.getDailySessions().get(servicePosition);
//        for(int i=0; i<= service.getDailySessions().size() ; i++){
//            if(sessions.get(i).isAvailable()) {
//                slots.add(new Date(sessions.get(i).getStart()));
//            }
//        }


    }
}

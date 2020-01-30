package com.example.scheduli.ui.BookingAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduli.BaseMenuActivity;
import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.Sessions;
import com.example.scheduli.ui.mainScreen.MainActivity;
import com.example.scheduli.utils.UsersUtils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SetAppointmentTime extends BaseMenuActivity {

    DatabaseReference databaseReference;
    private Toolbar mainToolbar;
    private static final String TAG_SET_APPOINTMENT_ACT = "SetAppointmentTime";


    private Service service;
    private Provider provider;
    private Map<String, ArrayList<Sessions>> dailySessions; // key is a date (day/month/year).
    private ArrayList<Sessions> sessionsArrayList;
    private RecyclerView.LayoutManager mLayout;

    private String pid;
    private int servicePosition;

    private Button next;
    private Button back;
    private TextView serviceChoosen;

    //Calendar view
    CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatPrimarry = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    private DateFormat dateFormatFireBase = new SimpleDateFormat("yyyy-MM-dd");

    //cycleview (Item)
    private RecyclerView recyclerViewSlots;
    private List<String> dates;
    private SlotAdapter slotAdapter;
    private List<Date> slots;
    private int slotPosition = -1;
    private Date currentDate = new Date();
    private Sessions currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_time);

        mainToolbar = findViewById(R.id.app_main_toolbar);
        setSupportActionBar(mainToolbar);

        //Calendar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        final Date date = new Date();
        actionBar.setTitle(dateFormatPrimarry.format(date));
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        init();



        //On date change:
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                slots.clear();

//                System.out.println("TEST: picked date: " + dateClicked.getTime());

                for (int i = 0; i < sessionsArrayList.size(); i++) {
                    Date date = new Date(sessionsArrayList.get(i).getStart());

                    if(dateFormatFireBase.format(dateClicked.getTime()).compareTo(dateFormatFireBase.format(date.getTime()))==0){
                        slots.add(new Date(sessionsArrayList.get(i).getStart()));
                        currentDate = new Date(sessionsArrayList.get(i).getStart());
                        currentSession = new Sessions(sessionsArrayList.get(i));


                    }
                }

                recyclerViewSlots.setAdapter(slotAdapter);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatPrimarry.format(firstDayOfNewMonth));
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(slots.get(slotPosition) + " " + "clicked"); /// TEST
                System.out.println("[TEST] s.p: " + servicePosition + " date: " + dateFormatFireBase.format(currentDate) + " position: " + slotPosition);
                Date sessionDate = new Date(slots.get(slotPosition).getTime());

                for (int i = 0; i < sessionsArrayList.size(); i++) {
                    if (slots.get(slotPosition).getTime() == sessionsArrayList.get(i).getStart()) {

//                        updateSessionDetails(i);
                        showFinishDialog(i);
                    }
                }

            }

        });


        slotAdapter.setOnItemClickListener(new SlotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                slotPosition = position;
                System.out.println("[TEST slot] position = " + position);
                System.out.println("[TEST slot] position = " + slots.get(position));

            }
        });

    }

    private void updateSessionDetails(int i)  {
        DatabaseReference sessionsReference;
        sessionsReference = FirebaseDatabase.getInstance().getReference("providers").child(pid).child("services").child(String.valueOf(servicePosition)).child("dailySessions").child(dateFormatFireBase.format(currentDate)).child(String.valueOf(slotPosition));
        Sessions s;
        s = sessionsArrayList.get(i);
        s.setUserUid(UsersUtils.getInstance().getCurrentUserUid());
        s.setAvailable(false);
        sessionsReference.setValue(s);
    }

    private void setEvents() {
        for(int i=0; i<dates.size();i++) {
            getSessions(dates.get(i));
            Log.d(TAG_SET_APPOINTMENT_ACT,"Getting Evet data from date: " + dates.get(i) );
        }
    }

    private void init() {
        //init Arrays
        dates = new ArrayList<>();
        slots = new ArrayList<>();
        sessionsArrayList = new ArrayList<>();

        //Init buttons and texts
        serviceChoosen = (TextView) findViewById(R.id.set_appointment_time_act_chosen_service);
        next = (Button) findViewById(R.id.btn_next_2);
        back = (Button) findViewById(R.id.btn_back_2);

        //Get Data from previous intent
        Intent intent = getIntent();
        provider = intent.getParcelableExtra("provider");
        service = intent.getParcelableExtra("service");
        pid = intent.getStringExtra("pid");
        servicePosition = intent.getIntExtra("position", 0);
        Log.d(TAG_SET_APPOINTMENT_ACT,"Got service: " + service.getName());
        Log.d(TAG_SET_APPOINTMENT_ACT, "Got service sessoins " + "[" + service.getDailySessions().size() + "] :" + service.getDailySessions());
        serviceChoosen.setText(provider.getCompanyName() + " ⌘ " + service.getName());
        dailySessions = service.getDailySessions();


        //Get dates from given HashMap object
        getDatesFromMap(dailySessions);

        //Set Events for calendar:
        setEvents();

        //Init Recycleview
        recyclerViewSlots = (RecyclerView) findViewById(R.id.recycleview_available_slots);
        mLayout = new GridLayoutManager(this, 3);
        slotAdapter = new SlotAdapter(getApplicationContext(), slots);
        recyclerViewSlots.setLayoutManager(mLayout);
        recyclerViewSlots.setHasFixedSize(true);
        recyclerViewSlots.setAdapter(slotAdapter);
    }


    public void getDatesFromMap(Map map) {

        if (map != null) {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
//                System.out.println(pair.getKey() + " : " + pair.getValue()); //TEST
                dates.add(pair.getKey().toString());
                it.remove();
            }
        } else {
            Log.d(TAG_SET_APPOINTMENT_ACT, "getDatesFromMap: null map");
        }
    }

    public void getSessions(String index) {
        int i = 0;
        databaseReference = FirebaseDatabase.getInstance().getReference("providers").child(pid).child("services").child(String.valueOf(servicePosition)).child("dailySessions").child(index);
        databaseReference.child(index);
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            slots.clear();

            if (dataSnapshot.exists()) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Sessions sessions = snapshot.getValue(Sessions.class);
                    System.out.println("[TEST] sesions start: " + sessions.getStart()); //TEST
                    if(sessions.isAvailable()) {
                        sessionsArrayList.add(sessions);
                        slots.add(new Date(sessions.getStart()));

                        Event event = new Event(Color.GREEN, sessions.getStart(), service.getName() + " slot");
                        compactCalendarView.addEvent(event);
                    }

                }


                if (!slots.isEmpty()) {
//                    System.out.println(" [TEST] slots is not empty");
//                    recyclerViewSlots.setAdapter(slotAdapter);
                }
            } else {
                Log.d(TAG_SET_APPOINTMENT_ACT, "Snapshot is not exists");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            Log.d(TAG_SET_APPOINTMENT_ACT, "onCancellelled: Something went wrong..");
        }
    };


    public void showFinishDialog(final int i){
        Dialog dialog = new Dialog(SetAppointmentTime.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialod_finish_booking);
        Button dialogBtn = dialog.findViewById(R.id.dialog_finish_btn);
        TextView dialogText = dialog.findViewById(R.id.text_finish_detail);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd d hh:mm a");
        dialogText.setText("You booked an appointment with: " + service.getName()+ "\nat: " + dateFormat.format(slots.get(slotPosition)));
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SetAppointmentTime.this, "clicked", Toast.LENGTH_SHORT).show();
                updateSessionDetails(i);
                Intent intent = new Intent(SetAppointmentTime.this, MainActivity.class);
                startActivity(intent);
            }
        });
        next.setEnabled(false);
        next.setBackgroundColor(getResources().getColor(R.color.colorClickedButton));

        dialog.show();
    }


}

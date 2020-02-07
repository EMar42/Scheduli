package com.example.scheduli.ui.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.scheduli.R;
import com.example.scheduli.data.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class SetServiceScheduleActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText from1, from2, from3, from4, from5, from6, from7, to1, to2, to3, to4, to5, to6, to7;
    private Button singupButton, backButton;
    private Switch sw1, sw2, sw3, sw4, sw5, sw6, sw7;
    private EditText[] from = {from1, from2, from3, from4, from5, from6, from7};

    private Service service;
    private ArrayList<Switch> switchesOnToggle;
    private TimePickerDialog timePickerDialog;
    private int currentMinutes, currentHours;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_service_schedule);

        initView();


        singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateService();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToProvider();
            }
        });
    }


    private void goBackToProvider() {
        //TODO: finish back
    }

    private void updateService() {
        //TODO : submit new service
        Toast.makeText(getBaseContext(), "Toggle state " + getToggleState(sw1), Toast.LENGTH_SHORT).show();
    }


    private boolean getToggleState(Switch sw) {
        return sw.isChecked();
    }

    private void state() {

    }


    private boolean checkIfEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private boolean isFormClear() {
        return checkIfEmpty(from1) && checkIfEmpty(from2) && checkIfEmpty(from3) && checkIfEmpty(from4) && checkIfEmpty(from5)
                && checkIfEmpty(from6) && checkIfEmpty(from7) && checkIfEmpty(to1) && checkIfEmpty(to2) && checkIfEmpty(to3)
                && checkIfEmpty(to4) && checkIfEmpty(to5) && checkIfEmpty(to6) && checkIfEmpty(to7)
                && !getToggleState(sw1) && !getToggleState(sw2) && !getToggleState(sw3) && !getToggleState(sw4)
                && !getToggleState(sw5) && !getToggleState(sw6) && !getToggleState(sw7);
    }


    private void getAllToggleState() {
        if (getToggleState(sw1)) {
            switchesOnToggle.add(sw1);
        }
        if (getToggleState(sw2)) {
            switchesOnToggle.add(sw2);
        }
        if (getToggleState(sw3)) {
            switchesOnToggle.add(sw3);
        }
        if (getToggleState(sw4)) {
            switchesOnToggle.add(sw4);
        }
        if (getToggleState(sw5)) {
            switchesOnToggle.add(sw5);
        }
        if (getToggleState(sw6)) {
            switchesOnToggle.add(sw6);
        }
        if (getToggleState(sw7)) {
            switchesOnToggle.add(sw7);
        }
    }

    private void getFormInfo() {
        for (Switch s : switchesOnToggle) {
            //TODO : check out from[x] and to[x]
        }
    }


    @Override
    public void onClick(View v) {
        calendar = Calendar.getInstance();
        currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinutes = calendar.get(Calendar.MINUTE);
        Log.i("blablabla", "This is current id : " + v.getId());
        switch (v.getId()) {
            case R.id.from1:
                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        from1.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, currentHours, currentMinutes, true);

        }
        timePickerDialog.show();

    }

    private void initView() {

        Intent intent = getIntent();
        service = intent.getParcelableExtra("service");
        from1 = findViewById(R.id.ed_from_sunday);
        from2 = findViewById(R.id.ed_from_monday);
        from3 = findViewById(R.id.ed_from_tuesday);
        from4 = findViewById(R.id.ed_from_wednesday);
        from5 = findViewById(R.id.ed_from_thursday);
        from6 = findViewById(R.id.ed_from_friday);
        from7 = findViewById(R.id.ed_from_saturday);
        to1 = findViewById(R.id.ed_to_sunday);
        to1.setEnabled(false);
        to2 = findViewById(R.id.ed_to_monday);
        to2.setEnabled(false);
        to3 = findViewById(R.id.ed_to_tuesday);
        to3.setEnabled(false);
        to4 = findViewById(R.id.ed_to_wednesday);
        to4.setEnabled(false);
        to5 = findViewById(R.id.ed_to_thursday);
        to5.setEnabled(false);
        to6 = findViewById(R.id.ed_to_friday);
        to6.setEnabled(false);
        to7 = findViewById(R.id.ed_to_saturday);
        to7.setEnabled(false);
        sw1 = findViewById(R.id.switch_sunday);
        sw2 = findViewById(R.id.switch_monday);
        sw3 = findViewById(R.id.switch_tuesday);
        sw4 = findViewById(R.id.switch_wednesday);
        sw5 = findViewById(R.id.switch_thursday);
        sw6 = findViewById(R.id.switch_friday);
        sw7 = findViewById(R.id.switch_saturday);
        singupButton = findViewById(R.id.btn_schedule_service);
        singupButton.setOnClickListener(this);
        backButton = findViewById(R.id.btn_back_service);

    }


}

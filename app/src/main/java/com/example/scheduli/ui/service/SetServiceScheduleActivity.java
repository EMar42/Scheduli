package com.example.scheduli.ui.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.scheduli.R;
import com.example.scheduli.data.Sessions;
import com.example.scheduli.data.TimeValidator;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.WorkDay;
import com.example.scheduli.ui.provider.ProviderActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class SetServiceScheduleActivity extends AppCompatActivity {

    private String SET_SERVICE_SCHEDULE_TAG = "SERVICE_SCHEDULE";

    private EditText from1, from2, from3, from4, from5, from6, from7, to1, to2, to3, to4, to5, to6, to7;
    private Button singupButton, backButton;
    private Switch sw1, sw2, sw3, sw4, sw5, sw6, sw7;
    private EditText[] from = {from1, from2, from3, from4, from5, from6, from7};

    private Service service;
    private ArrayList<Switch> switchesOnToggle;
    private TimePickerDialog timePickerDialog;
    private int currentMinutes, currentHours;
    private Calendar calendar;

    private Map<String, WorkDay> workingDays; // key is of type DayOfWeek enum
    private Map<String, ArrayList<Sessions>> dailySessions; // key is a date (day/month/year).


    private TimeValidator timeValidator = new TimeValidator();


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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("If you Quit now your data wont be saved")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), AddServiceActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateService() {
        //TODO : submit new service
        Toast.makeText(getBaseContext(), "string : " + from1.getText().toString(), Toast.LENGTH_SHORT).show();
        getAllToggleState();
        displayErrorToUserIfThereIsOne();


        if (formValid()) {

        }
    }

    private boolean checkDurationIsValid(String from, String to, int duration) {

        Date d1 = null;
        Date d2 = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        try {
            d1 = format.parse(from);
            Log.i(SET_SERVICE_SCHEDULE_TAG, "This is D1 : " + d1);

            d2 = format.parse(to);
            Log.i(SET_SERVICE_SCHEDULE_TAG, "This is D2 : " + d2);
        } catch (Exception e) {
            System.err.println(e);
        }

        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        Log.i(SET_SERVICE_SCHEDULE_TAG, "Hours : " + diffHours + " Minutes : " + diffMinutes);

        long result = diffHours * 60 + diffMinutes;
        Log.i(SET_SERVICE_SCHEDULE_TAG, "This is diff : " + diff);
        Log.i(SET_SERVICE_SCHEDULE_TAG, "This is result : " + result);
        Log.i(SET_SERVICE_SCHEDULE_TAG, "resualt : " + result + " duration : " + service.getSingleSessionInMinutes());
        if (result > duration) {
            return true;
        }
        return false;
    }



    private boolean formValid() {

        if (switchesOnToggle.size() > 0) {
            if (sw1.isChecked()) {
                if (!checkIfEmpty(from1) && !checkIfEmpty(to1) && timeValidator.isValid(from1.getText().toString())
                        && timeValidator.isValid(to1.getText().toString()) && timeValidator.compareDates(from1.getText().toString(), to1.getText().toString())
                        && checkDurationIsValid(from1.getText().toString(), to1.getText().toString(), service.getSingleSessionInMinutes())) {

                } else
                    return false;
            }
            if (sw2.isChecked()) {
                if (!checkIfEmpty(from2) && !checkIfEmpty(to2) && timeValidator.isValid(from2.getText().toString())
                        && timeValidator.isValid(to2.getText().toString()) && timeValidator.compareDates(from2.getText().toString(), to2.getText().toString())) {
                } else
                    return false;
            }
            if (sw3.isChecked()) {
                if (!checkIfEmpty(from3) && !checkIfEmpty(to3) && timeValidator.isValid(from3.getText().toString())
                        && timeValidator.isValid(to3.getText().toString()) && timeValidator.compareDates(from3.getText().toString(), to3.getText().toString())) {
                } else
                    return false;
            }
            if (sw4.isChecked()) {
                if (!checkIfEmpty(from4) && !checkIfEmpty(to4) && timeValidator.isValid(from4.getText().toString())
                        && timeValidator.isValid(to4.getText().toString()) && timeValidator.compareDates(from4.getText().toString(), to4.getText().toString())) {
                } else
                    return false;
            }
            if (sw5.isChecked()) {
                if (!checkIfEmpty(from5) && !checkIfEmpty(to5) && timeValidator.isValid(from5.getText().toString())
                        && timeValidator.isValid(to5.getText().toString()) && timeValidator.compareDates(from5.getText().toString(), to5.getText().toString())) {
                } else
                    return false;
            }
            if (sw6.isChecked()) {
                if (!checkIfEmpty(from6) && !checkIfEmpty(to6) && timeValidator.isValid(from6.getText().toString())
                        && timeValidator.isValid(to6.getText().toString()) && timeValidator.compareDates(from6.getText().toString(), to6.getText().toString())) {
                } else
                    return false;
            }
            if (sw7.isChecked()) {
                if (!checkIfEmpty(from7) && !checkIfEmpty(to7) && timeValidator.isValid(from7.getText().toString())
                        && timeValidator.isValid(to7.getText().toString()) && timeValidator.compareDates(from7.getText().toString(), to7.getText().toString())) {
                } else
                    return false;
            }
        }
        return false;
    }

    private void displayErrorToUserIfThereIsOne() {
        Log.i(SET_SERVICE_SCHEDULE_TAG, "From :" + from1.getText() + " " + from1.getText().toString());
        if (sw1.isChecked()) {
            if (checkIfEmpty(from1))
                from1.setError("You must enter Time");
            else if (checkIfEmpty(to1))
                to1.setError("You must enter Time");
            else if (!timeValidator.isValid(from1.getText().toString()))
                from1.setError("Time is Invalid");
            else if (!timeValidator.isValid(to1.getText().toString()))
                from1.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from1.getText().toString(), to1.getText().toString()))
                to1.setError("Must be Higher then before time");
            else if (!checkDurationIsValid(from1.getText().toString(), to1.getText().toString(), service.getSingleSessionInMinutes())) {
                to1.setError("The time can't be shorter then Service Duration");
            }
        }
        if (sw2.isChecked()) {
            if (checkIfEmpty(from2) && checkIfEmpty(to2))
                from2.setError("You must enter Time");
            else if (!timeValidator.isValid(from2.getText().toString()))
                from2.setError("Time is Invalid");
            else if (!timeValidator.isValid(to2.getText().toString()))
                from2.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from2.getText().toString(), to2.getText().toString()))
                to2.setError("Must be smaller then before time");
        }
        if (sw3.isChecked()) {
            if (checkIfEmpty(from3) && checkIfEmpty(to3))
                from3.setError("You must enter Time");
            else if (!timeValidator.isValid(from3.getText().toString()))
                from3.setError("Time is Invalid");
            else if (!timeValidator.isValid(to3.getText().toString()))
                from3.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from3.getText().toString(), to3.getText().toString()))
                to3.setError("Must be smaller then before time");
        }
        if (sw4.isChecked()) {
            if (checkIfEmpty(from4) && checkIfEmpty(to4))
                from4.setError("You must enter Time");
            else if (!timeValidator.isValid(from4.getText().toString()))
                from4.setError("Time is Invalid");
            else if (!timeValidator.isValid(to4.getText().toString()))
                from4.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from4.getText().toString(), to4.getText().toString()))
                to4.setError("Must be smaller then before time");
        }
        if (sw5.isChecked()) {
            if (checkIfEmpty(from5) && checkIfEmpty(to5))
                from5.setError("You must enter Time");
            else if (!timeValidator.isValid(from5.getText().toString()))
                from5.setError("Time is Invalid");
            else if (!timeValidator.isValid(to5.getText().toString()))
                from5.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from5.getText().toString(), to5.getText().toString()))
                to5.setError("Must be smaller then before time");
        }
        if (sw6.isChecked()) {
            if (checkIfEmpty(from6) && checkIfEmpty(to6))
                from6.setError("You must enter Time");
            else if (!timeValidator.isValid(from6.getText().toString()))
                from6.setError("Time is Invalid");
            else if (!timeValidator.isValid(to6.getText().toString()))
                from6.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from6.getText().toString(), to6.getText().toString()))
                to6.setError("Must be smaller then before time");
        }
        if (sw7.isChecked()) {
            if (checkIfEmpty(from7) && checkIfEmpty(to7))
                from7.setError("You must enter Time");
            else if (!timeValidator.isValid(from7.getText().toString()))
                from7.setError("Time is Invalid");
            else if (!timeValidator.isValid(to7.getText().toString()))
                from7.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from7.getText().toString(), to7.getText().toString()))
                to7.setError("Must be smaller then before time");
        }
    }

    private boolean checkSessionInMinutesValid(EditText from, EditText to, int singleSessionInMinutes) {
        String ssim = Integer.toString(singleSessionInMinutes);


        return false;
    }


    private boolean getToggleState(Switch sw) {
        return sw.isChecked();
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
        switchesOnToggle = new ArrayList<>();
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

    private String getFromBySwitch(Switch sw) {
        if (sw1.equals(sw)) {
            return from1.toString();
        } else if (sw2.equals(sw)) {
            return from2.toString();
        } else if (sw3.equals(sw)) {
            return from3.toString();
        } else if (sw4.equals(sw)) {
            return from4.toString();
        } else if (sw5.equals(sw)) {
            return from5.toString();
        } else if (sw6.equals(sw)) {
            return from6.toString();
        } else if (sw7.equals(sw)) {
            return from7.toString();
        }
        return null;
    }

    private String getToBySwitch(Switch sw) {
        if (sw1.equals(sw)) {
            return to1.toString();
        } else if (sw2.equals(sw)) {
            return to2.toString();
        } else if (sw3.equals(sw)) {
            return to3.toString();
        } else if (sw4.equals(sw)) {
            return to4.toString();
        } else if (sw5.equals(sw)) {
            return to5.toString();
        } else if (sw6.equals(sw)) {
            return to6.toString();
        } else if (sw7.equals(sw)) {
            return to7.toString();
        }
        return null;
    }

    private void getToggleFromAndTo() {
        String from, to;
        for (Switch s : switchesOnToggle) {
            from = getFromBySwitch(s);
            to = getToBySwitch(s);
        }
    }


    private boolean inputTimeValid(EditText from, EditText to) {
        TimeValidator timeValidator = new TimeValidator();
        if (timeValidator.isValid(from.getText().toString()) && timeValidator.isValid(to.getText().toString())) {
            return true;
        }
        return false;
    }


//    @Override
//    public void onClick(View v) {
//        calendar = Calendar.getInstance();
//        currentHours = calendar.get(Calendar.HOUR_OF_DAY);
//        currentMinutes = calendar.get(Calendar.MINUTE);
//        Log.i("blablabla", "This is current id : " + v.getId());
//        switch (v.getId()) {
//            case R.id.from1:
//                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        from1.setText(String.format("%02d:%02d", hourOfDay, minute));
//                    }
//                }, currentHours, currentMinutes, true);
//
//        }
//        timePickerDialog.show();
//    }


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
        to2 = findViewById(R.id.ed_to_monday);
        to3 = findViewById(R.id.ed_to_tuesday);
        to4 = findViewById(R.id.ed_to_wednesday);
        to5 = findViewById(R.id.ed_to_thursday);
        to6 = findViewById(R.id.ed_to_friday);
        to7 = findViewById(R.id.ed_to_saturday);
        sw1 = findViewById(R.id.switch_sunday);
        sw2 = findViewById(R.id.switch_monday);
        sw3 = findViewById(R.id.switch_tuesday);
        sw4 = findViewById(R.id.switch_wednesday);
        sw5 = findViewById(R.id.switch_thursday);
        sw6 = findViewById(R.id.switch_friday);
        sw7 = findViewById(R.id.switch_saturday);
        singupButton = findViewById(R.id.btn_schedule_service);
        backButton = findViewById(R.id.btn_back_service);
    }

}

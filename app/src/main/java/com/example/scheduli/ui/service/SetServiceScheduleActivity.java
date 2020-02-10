package com.example.scheduli.ui.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.ArrayMap;
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

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SetServiceScheduleActivity extends AppCompatActivity {

    private String SET_SERVICE_SCHEDULE_TAG = "SERVICE_SCHEDULE";

    private EditText from1, from2, from3, from4, from5, from6, from7, to1, to2, to3, to4, to5, to6, to7;
    private Button singupButton, backButton;
    private Switch sw1, sw2, sw3, sw4, sw5, sw6, sw7;
    private EditText[] from = {from1, from2, from3, from4, from5, from6, from7};

    private Service service;
    private ArrayList<Switch> switchesOnToggle;
    private Calendar calendar = Calendar.getInstance();

    private Map<String, WorkDay> workingDays = new HashMap<>(); // key is of type DayOfWeek enum
    private Map<String, ArrayList<Sessions>> dailySessions; // key is a date (day/month/year).
    private ArrayList<Sessions> currentSession;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");


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

        if (!isFormClear()) {
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
    }


    private void updateService() {
        //TODO : submit new service

        getAllToggleState();
        displayErrorToUserIfThereIsOne();
        Log.i(SET_SERVICE_SCHEDULE_TAG, "get minutes from D " + switchesOnToggle);
        int min = getMinutesFromEditText(from1);
        int hou = getHoursFromEditText(from1);
        Toast.makeText(getBaseContext(), "min : " + min + " hour : " + hou, Toast.LENGTH_SHORT).show();
        getMinutesFromEditText(from1);


        if (isFormValid()) {
            for (Switch s : switchesOnToggle) {

                createDailySessions(s);
                createWorkingDays(s);
            }
        }
    }


    private void createWorkingDays(Switch s) {
        Calendar cal1, cal2;

        switch (s.getText().toString()) {
            case "Sun":
                Date d = Calendar.getInstance().getTime();
                cal1 = Calendar.getInstance();
                cal2 = Calendar.getInstance();
                cal1.set(Calendar.HOUR, getHoursFromEditText(from1));
                cal1.set(Calendar.MINUTE, getMinutesFromEditText(from1));
                cal2.set(Calendar.HOUR, getHoursFromEditText(to1));
                cal2.set(Calendar.MINUTE, getMinutesFromEditText(to1));
                workingDays.put("1", new WorkDay(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
                break;
            case "Mon":
                cal1 = Calendar.getInstance();
                cal2 = Calendar.getInstance();
                cal1.set(Calendar.HOUR, getHoursFromEditText(from2));
                cal1.set(Calendar.MINUTE, getMinutesFromEditText(from2));
                cal2.set(Calendar.HOUR, getHoursFromEditText(to2));
                cal2.set(Calendar.MINUTE, getMinutesFromEditText(to2));
                workingDays.put("2", new WorkDay(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
                break;

            case "Tue":
                cal1 = Calendar.getInstance();
                cal2 = Calendar.getInstance();
                cal1.set(Calendar.HOUR, getHoursFromEditText(from3));
                cal1.set(Calendar.MINUTE, getMinutesFromEditText(from3));
                cal2.set(Calendar.HOUR, getHoursFromEditText(to3));
                cal2.set(Calendar.MINUTE, getMinutesFromEditText(to3));
                workingDays.put("3", new WorkDay(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
                break;

            case "Wed":
                cal1 = Calendar.getInstance();
                cal2 = Calendar.getInstance();
                cal1.set(Calendar.HOUR, getHoursFromEditText(from4));
                cal1.set(Calendar.MINUTE, getMinutesFromEditText(from4));
                cal2.set(Calendar.HOUR, getHoursFromEditText(to4));
                cal2.set(Calendar.MINUTE, getMinutesFromEditText(to4));
                workingDays.put("4", new WorkDay(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
                break;

            case "Thu":
                cal1 = Calendar.getInstance();
                cal2 = Calendar.getInstance();
                cal1.set(Calendar.HOUR, getHoursFromEditText(from5));
                cal1.set(Calendar.MINUTE, getMinutesFromEditText(from5));
                cal2.set(Calendar.HOUR, getHoursFromEditText(to5));
                cal2.set(Calendar.MINUTE, getMinutesFromEditText(to5));
                workingDays.put("5", new WorkDay(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
                break;

            case "Fri":
                cal1 = Calendar.getInstance();
                cal2 = Calendar.getInstance();
                cal1.set(Calendar.HOUR, getHoursFromEditText(from6));
                cal1.set(Calendar.MINUTE, getMinutesFromEditText(from6));
                cal2.set(Calendar.HOUR, getHoursFromEditText(to6));
                cal2.set(Calendar.MINUTE, getMinutesFromEditText(to6));
                workingDays.put("6", new WorkDay(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
                break;

            case "Sat":
                cal1 = Calendar.getInstance();
                cal2 = Calendar.getInstance();
                cal1.set(Calendar.HOUR, getHoursFromEditText(from7));
                cal1.set(Calendar.MINUTE, getMinutesFromEditText(from7));
                cal2.set(Calendar.HOUR, getHoursFromEditText(to7));
                cal2.set(Calendar.MINUTE, getMinutesFromEditText(to7));
                workingDays.put("7", new WorkDay(cal1.getTimeInMillis(), cal2.getTimeInMillis()));
                break;
        }
    }


    private boolean checkDurationIsValid(String from, String to, int duration) {

        Date d1 = null;
        Date d2 = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        try {
            d1 = format.parse(from);
//            Log.i(SET_SERVICE_SCHEDULE_TAG, "This is D1 : " + d1);

            d2 = format.parse(to);
//            Log.i(SET_SERVICE_SCHEDULE_TAG, "This is D2 : " + d2);
        } catch (Exception e) {
            System.err.println(e);
        }

        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

//        Log.i(SET_SERVICE_SCHEDULE_TAG, "Hours : " + diffHours + " Minutes : " + diffMinutes);

        long result = diffHours * 60 + diffMinutes;
//        Log.i(SET_SERVICE_SCHEDULE_TAG, "This is diff : " + diff);
//        Log.i(SET_SERVICE_SCHEDULE_TAG, "This is result : " + result);
//        Log.i(SET_SERVICE_SCHEDULE_TAG, "resualt : " + result + " duration : " + service.getSingleSessionInMinutes());
        if (result >= duration) {
            return true;
        }
        return false;
    }


    private int getMinutesFromEditText(EditText et) {

        Date d = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            d = format.parse(et.getText().toString());

        } catch (Exception e) {
            System.err.println(e);
        }
        long minutes = d.getTime() / (60 * 1000) % 60;

        return (int) minutes;
    }


    private int getHoursFromEditText(EditText et) {
        Date d = null;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        try {
            d = format.parse(et.getText().toString());
        } catch (Exception e) {
            System.err.println(e);
        }
        long hours = (d.getTime() / (60 * 60 * 1000)) % 24;

        return (int) hours;
    }


    private boolean isFormValid() {

        if (switchesOnToggle.size() > 0) {
            if (sw1.isChecked()) {
                if (!(!checkIfEmpty(from1) && !checkIfEmpty(to1) && timeValidator.isValid(from1.getText().toString())
                        && timeValidator.isValid(to1.getText().toString()) && timeValidator.compareDates(from1.getText().toString(), to1.getText().toString())
                        && checkDurationIsValid(from1.getText().toString(), to1.getText().toString(), service.getSingleSessionInMinutes())))
                    return false;
            }
            if (sw2.isChecked()) {
                if (!(!checkIfEmpty(from2) && !checkIfEmpty(to2) && timeValidator.isValid(from2.getText().toString())
                        && timeValidator.isValid(to2.getText().toString()) && timeValidator.compareDates(from2.getText().toString(), to2.getText().toString())
                        && checkDurationIsValid(from2.getText().toString(), to2.getText().toString(), service.getSingleSessionInMinutes())))
                    return false;
            }
            if (sw3.isChecked()) {
                if (!(!checkIfEmpty(from3) && !checkIfEmpty(to3) && timeValidator.isValid(from3.getText().toString())
                        && timeValidator.isValid(to3.getText().toString()) && timeValidator.compareDates(from3.getText().toString(), to3.getText().toString())
                        && checkDurationIsValid(from3.getText().toString(), to3.getText().toString(), service.getSingleSessionInMinutes())))
                    return false;
            }
            if (sw4.isChecked()) {
                if (!(!checkIfEmpty(from4) && !checkIfEmpty(to4) && timeValidator.isValid(from4.getText().toString())
                        && timeValidator.isValid(to4.getText().toString()) && timeValidator.compareDates(from4.getText().toString(), to4.getText().toString())
                        && checkDurationIsValid(from4.getText().toString(), to4.getText().toString(), service.getSingleSessionInMinutes())))
                    return false;
            }
            if (sw5.isChecked()) {
                if (!(!checkIfEmpty(from5) && !checkIfEmpty(to5) && timeValidator.isValid(from5.getText().toString())
                        && timeValidator.isValid(to5.getText().toString()) && timeValidator.compareDates(from5.getText().toString(), to5.getText().toString())
                        && checkDurationIsValid(from5.getText().toString(), to5.getText().toString(), service.getSingleSessionInMinutes())))
                    return false;
            }
            if (sw6.isChecked()) {
                if (!(!checkIfEmpty(from6) && !checkIfEmpty(to6) && timeValidator.isValid(from6.getText().toString())
                        && timeValidator.isValid(to6.getText().toString()) && timeValidator.compareDates(from6.getText().toString(), to6.getText().toString())
                        && checkDurationIsValid(from6.getText().toString(), to6.getText().toString(), service.getSingleSessionInMinutes())))
                    return false;
            }
            if (sw7.isChecked()) {
                if (!(!checkIfEmpty(from7) && !checkIfEmpty(to7) && timeValidator.isValid(from7.getText().toString())
                        && timeValidator.isValid(to7.getText().toString()) && timeValidator.compareDates(from7.getText().toString(), to7.getText().toString())
                        && checkDurationIsValid(from7.getText().toString(), to7.getText().toString(), service.getSingleSessionInMinutes())))
                    return false;
            }
            return true;
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
                to1.setError("Must be Later then before time");
            else if (!checkDurationIsValid(from1.getText().toString(), to1.getText().toString(), service.getSingleSessionInMinutes())) {
                to1.setError("The time can't be shorter then Service Duration");
            }
        }
        if (sw2.isChecked()) {
            if (checkIfEmpty(from2))
                from2.setError("You must enter Time");
            else if (checkIfEmpty(to2))
                to2.setError("You must enter Time");
            else if (!timeValidator.isValid(from2.getText().toString()))
                from2.setError("Time is Invalid");
            else if (!timeValidator.isValid(to2.getText().toString()))
                from2.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from2.getText().toString(), to2.getText().toString()))
                to2.setError("Must be Later then before time");
            else if (!checkDurationIsValid(from2.getText().toString(), to2.getText().toString(), service.getSingleSessionInMinutes())) {
                to2.setError("The time can't be shorter then Service Duration");
            }
        }
        if (sw3.isChecked()) {
            if (checkIfEmpty(from3))
                from3.setError("You must enter Time");
            else if (checkIfEmpty(to3))
                to3.setError("You must enter Time");
            else if (!timeValidator.isValid(from3.getText().toString()))
                from3.setError("Time is Invalid");
            else if (!timeValidator.isValid(to3.getText().toString()))
                from3.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from3.getText().toString(), to3.getText().toString()))
                to3.setError("Must be Later then before time");
            else if (!checkDurationIsValid(from3.getText().toString(), to3.getText().toString(), service.getSingleSessionInMinutes())) {
                to3.setError("The time can't be shorter then Service Duration");
            }
        }
        if (sw4.isChecked()) {
            if (checkIfEmpty(from4))
                from4.setError("You must enter Time");
            else if (checkIfEmpty(to4))
                to4.setError("You must enter Time");
            else if (!timeValidator.isValid(from4.getText().toString()))
                from4.setError("Time is Invalid");
            else if (!timeValidator.isValid(to4.getText().toString()))
                from4.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from4.getText().toString(), to4.getText().toString()))
                to4.setError("Must be Later then before time");
            else if (!checkDurationIsValid(from4.getText().toString(), to4.getText().toString(), service.getSingleSessionInMinutes())) {
                to4.setError("The time can't be shorter then Service Duration");
            }
        }
        if (sw5.isChecked()) {
            if (checkIfEmpty(from5))
                from5.setError("You must enter Time");
            else if (checkIfEmpty(to5))
                to4.setError("You must enter Time");
            else if (!timeValidator.isValid(from5.getText().toString()))
                from5.setError("Time is Invalid");
            else if (!timeValidator.isValid(to5.getText().toString()))
                from5.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from5.getText().toString(), to5.getText().toString()))
                to5.setError("Must be Later then before time");
            else if (!checkDurationIsValid(from5.getText().toString(), to5.getText().toString(), service.getSingleSessionInMinutes())) {
                to5.setError("The time can't be shorter then Service Duration");
            }
        }
        if (sw6.isChecked()) {
            if (checkIfEmpty(from6))
                from6.setError("You must enter Time");
            else if (checkIfEmpty(to6))
                to6.setError("You must enter Time");
            else if (!timeValidator.isValid(from6.getText().toString()))
                from6.setError("Time is Invalid");
            else if (!timeValidator.isValid(to6.getText().toString()))
                from6.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from6.getText().toString(), to6.getText().toString()))
                to6.setError("Must be Later then before time");
            else if (!checkDurationIsValid(from6.getText().toString(), to6.getText().toString(), service.getSingleSessionInMinutes())) {
                to6.setError("The time can't be shorter then Service Duration");
            }
        }
        if (sw7.isChecked()) {
            if (checkIfEmpty(from7))
                from7.setError("You must enter Time");
            else if (checkIfEmpty(to7))
                to7.setError("You must enter Time");
            else if (!timeValidator.isValid(from7.getText().toString()))
                from7.setError("Time is Invalid");
            else if (!timeValidator.isValid(to7.getText().toString()))
                from7.setError("Time is Invalid");
            else if (!timeValidator.compareDates(from7.getText().toString(), to7.getText().toString()))
                to7.setError("Must be Later then before time");
            else if (!checkDurationIsValid(from7.getText().toString(), to7.getText().toString(), service.getSingleSessionInMinutes())) {
                to7.setError("The time can't be shorter then Service Duration");
            }
        }
    }


    public void createDailySessions(Switch s) {

        int sessionSpan = service.getSingleSessionInMinutes();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar halfYearCal = Calendar.getInstance();
        halfYearCal.add(Calendar.MONTH, 6);
        Calendar startCal = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();


        switch (s.getText().toString()) {
            case "Sun":
                cal.set(Calendar.HOUR_OF_DAY, getHoursFromEditText(from1));
                cal.set(Calendar.MINUTE, getMinutesFromEditText(from1));
                long start = cal.getTimeInMillis();
                cal.set(Calendar.HOUR_OF_DAY, getHoursFromEditText(to1));
                cal.set(Calendar.MINUTE, getMinutesFromEditText(to1));

                long end = cal.getTimeInMillis();
                Date sessionsSpanDate = new Date(TimeUnit.MINUTES.toMillis(sessionSpan));
                Date workingHours = new Date(end - start);
                System.out.println(workingHours.getTime());

                int numOfSlots = (int) ((workingHours.getTime() / (1000 * 60)) / sessionSpan);

                DateFormat formatter = new SimpleDateFormat("HH:mm");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC+2"));
                ArrayList<Date> dates = new ArrayList<>();

                int i = 0;

                Log.i(SET_SERVICE_SCHEDULE_TAG, "start month : " + startCal.get(Calendar.MONTH) +
                        " halfYear month " + halfYearCal.get(Calendar.MONTH));

                while (i <= numOfSlots) {

                    Date date = new Date(start);
                    dates.add(date);
                    start += sessionsSpanDate.getTime();
                    //String sloTime = formatter.format(dates.get(i).getTime());
                    System.out.println(formatter1.format(new Date(date.getTime())));
                    System.out.println("long of time in mili " + date.getTime());

                    i++;
                    //System.out.println("slot from: " + sloTime);
                }


            case "Mon":

            case "Tue":

            case "Wed":

            case "Thu":

            case "Fri":

            case "Sat":

        }

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

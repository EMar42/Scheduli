package com.example.scheduli.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeValidator {
    private String dateFormat;

    public TimeValidator() {
        this.dateFormat = "HH:mm";
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean compareDates(String d1, String d2) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(d1);
            date2 = sdf.parse(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1.before(date2)){
            return true;
        }
        return false;
    }
}

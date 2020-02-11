package com.example.scheduli.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeValidator {
    private String dateFormat;

    public TimeValidator() {
        this.dateFormat = "HH:mm";
    }

    public boolean isValid(String dateStr) {
        Pattern p = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*");
        Matcher m = p.matcher(dateStr);
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        if (m.matches()){
            return true;
        }
        else
            return false;
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

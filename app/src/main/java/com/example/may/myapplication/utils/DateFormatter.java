package com.example.may.myapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by May on 4/4/2018.
 */

public class DateFormatter {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    static SimpleDateFormat wholeDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.getDefault());
    static SimpleDateFormat monthFormat = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    public static Date fullStringToDate(String date) {
        try {
            return wholeDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toDateFormat(int year, int monthOfYear, int dayOfMonth) {
        return dateFormat.format(new Date(year, monthOfYear, dayOfMonth));
    }

    public static String toDateFormat(long date) {
        return dateFormat.format(new Date(date));
    }

    public static String toTimeFormat(long date) {
        return timeFormat.format(new Date(date));
    }

    public static String toMonthFormat(Date date) {
        return monthFormat.format(date);
    }
}

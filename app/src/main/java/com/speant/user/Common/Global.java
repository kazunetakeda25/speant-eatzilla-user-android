package com.speant.user.Common;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;

import com.speant.user.Models.DateData;
import com.speant.user.Models.TimeData;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static io.fabric.sdk.android.Fabric.TAG;

public class Global {

    public static final String SOCKET_URL = "ws://167.71.153.176:3000";
    public static final String DELIVERYBOY_ID = "DELIVERYBOY_ID";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String FROM_TYPE = "FROM_TYPE";

    public static void preventTwoClick(final View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
            }
        }, 500);
    }

    public static String getDateFromString(String dateString, String perviousformat, String newFormat) {
        DateFormat oldDateFormat = new SimpleDateFormat(perviousformat, Locale.ENGLISH);
        DateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        Date olddate = null;
        String newDate = null;
        try {
            olddate = oldDateFormat.parse(dateString);
            newDate = newDateFormat.format(olddate);
        } catch (ParseException e) {
            Log.e("Global", "getDateFromString:ParseException " + e);
        }

        return newDate;
    }


    public static String getAddress(Activity activity, LatLng latLng) {
        String currentAddress = null;

        Geocoder geocoder = new Geocoder(activity);

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String locality = addressList.get(0).getAddressLine(0).trim();
                String country = addressList.get(0).getCountryName().trim();

                if (!locality.isEmpty() && !country.isEmpty())
                    currentAddress = locality;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentAddress;

    }

    public static String getCity(Activity activity, LatLng latLng) {
        String currentAddress = null;

        Geocoder geocoder = new Geocoder(activity);

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String locality = addressList.get(0).getLocality().trim();


                if (!locality.isEmpty())
                    currentAddress = locality;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentAddress;

    }

    public static String convert(String time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm:ss"); //HH for hour of the day (0 - 23)
        Date d;
        String returnDate = null;
        try {
            d = f1.parse(time);
            DateFormat f2 = new SimpleDateFormat("h:mm a");
            returnDate = f2.format(d).toLowerCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    //set Black and grey color in same string
    public static String setMultipleColorText(String firstText, String secondText) {
        String text = "<font color=#c9adad>" + firstText + "</font> </br> <font color=#000000>" + secondText + "</font>";
        return text;
    }

    public static String getLocationCountryCode(Activity activity, LatLng latLng) {
        String countryCode = null;

        Geocoder geocoder = new Geocoder(activity);

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                countryCode = addressList.get(0).getCountryCode().trim();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryCode;
    }

    public static List<TimeData> getTimeList(String fromInterval, String toInterval, String is_weekend) {
        java.sql.Time startTime;
        java.sql.Time endTime;
        java.sql.Time currentTime;
        java.sql.Time finalStartTime;
        List<TimeData> timeList = new ArrayList<>();
        List<java.sql.Time> intervals = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(fromInterval);
            date2 = sdf.parse(toInterval);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        currentTime = new java.sql.Time(Calendar.getInstance().getTimeInMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int startHours = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinutes = calendar.get(Calendar.MINUTE);
        int startSeconds = calendar.get(Calendar.SECOND);

        calendar.setTime(date2);
        int endHours = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinutes = calendar.get(Calendar.MINUTE);
        int endSeconds = calendar.get(Calendar.SECOND);

        Date date = new Date();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, startHours);
        calendar.set(Calendar.MINUTE, startMinutes);
        calendar.set(Calendar.SECOND, startSeconds);
        Log.e(TAG, "getTimeList:calendar startTime"+calendar.getTimeInMillis());
        startTime = new java.sql.Time(calendar.getTimeInMillis());

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, endHours);
        calendar.set(Calendar.MINUTE, endMinutes);
        calendar.set(Calendar.SECOND, endSeconds);
        Log.e(TAG, "getTimeList:calendar endTime"+calendar.getTimeInMillis());
        endTime = new java.sql.Time(calendar.getTimeInMillis());

        /*Log.e(TAG, "getTimeList:startTime " + startTime);
        Log.e(TAG, "getTimeList:endTime " + endTime);
        Log.e(TAG, "getTimeList:currentTime " + currentTime);*/

        Calendar calStart = Calendar.getInstance();
        if (startTime.after(currentTime)) {
            calStart.setTime(startTime);
            intervals.add(startTime);
            finalStartTime = startTime;
        } else {
            calStart.setTime(currentTime);
            intervals.add(currentTime);
            finalStartTime = currentTime;
        }

        Log.e(TAG, "getTimeList:finalStartTime " + finalStartTime);

        if (finalStartTime.after(endTime)){
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, endHours);
            calendar.set(Calendar.MINUTE, endMinutes);
            calendar.set(Calendar.SECOND, endSeconds);
            Log.e(TAG, "getTimeList:finalendTime " + calendar.getTimeInMillis());
            endTime = new java.sql.Time(calendar.getTimeInMillis());
        }

        /*Log.e(TAG, "getTimeList:nextTimeAdded startTime" + startTime);
        Log.e(TAG, "getTimeList:nextTimeAdded currentTime" + currentTime);
        Log.e(TAG, "getTimeList:nextTimeAdded endTime" + endTime);
        Log.e(TAG, "getTimeList:nextTimeAdded endTime" + calStart.getTime());*/

        Log.e(TAG, "getTimeList:nextTimeAdded endTime" + calStart.getTime());
        Log.e(TAG, "getTimeList:nextTimeAdded endTime" + calendar.getTime());

        while (calStart.getTime().before(calendar.getTime())) {
//            Log.e(TAG, "getTimeList: nextTimeAdded" + calStart.getTime());
            calStart.add(Calendar.MINUTE, 30);
            intervals.add(new java.sql.Time(calStart.getTimeInMillis()));
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        String timevalue;
        for (int i = 0; i < intervals.size(); i++) {
            java.sql.Time time = intervals.get(i);
            System.out.println(sdf1.format(time));
            timevalue = sdf1.format(time);
            TimeData timeData = new TimeData();
            timeData.setTime(timevalue);
            timeData.setType(is_weekend);
            Log.e(TAG, "getTimeList:sdf.format time " + timevalue);

            timeList.add(timeData);
        }

        return timeList;

        ///////-------------------------------------------

        /*List<TimeData> timeList = new ArrayList<>();

        List<java.sql.Time> intervals = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(fromInterval);
            date2 = sdf.parse(toInterval);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("in milliseconds: " + date1.getTime());
        System.out.println("in milliseconds: " + date2.getTime());


        // These constructors are deprecated and are used only for example
        java.sql.Time startTime = new java.sql.Time(date1.getTime());

        java.sql.Time endTime = new java.sql.Time(date2.getTime());

        intervals.add(startTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        while (cal.getTime().before(endTime)) {
            cal.add(Calendar.MINUTE, 30);
            intervals.add(new java.sql.Time(cal.getTimeInMillis()));
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");

        String timevalue;
        for (int i = 0; i < intervals.size(); i++) {
            java.sql.Time time = intervals.get(i);
            System.out.println(sdf1.format(time));
            timevalue = sdf1.format(time);
            TimeData timeData = new TimeData();
            timeData.setTime(timevalue);
            timeData.setType(is_weekend);
         *//* if (i == 0) {
                timevalue = timevalue + "\n" + "Open Time";
            } else if (i == intervals.size() - 1) {
                timevalue = timevalue + "\n" + "Close Time";
            }*//*
            Log.e(TAG, "getTimeList:sdf.format time " + timevalue);
            timeList.add(timeData);
        }

        return timeList;*/
    }

    public static List<DateData> getDateList(String format) {
        List<DateData> dateList = new ArrayList<>();
        SimpleDateFormat df1 = new SimpleDateFormat(format);

        String dateString = df1.format(new Date(System.currentTimeMillis()));

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        String datevalue;
        for (int i = 0; i < 7; i++) {
            datevalue = df1.format(cal1.getTime());

            DateData dateData = new DateData();
            dateData.setDate(datevalue);

            String tempDate = Global.getDateFromString(datevalue, "yyyy-MM-dd", "MMM d" + "\n" + "EEE");
            if (tempDate.contains("Sat") || tempDate.contains("Sun")) {
                dateData.setType("0");
            } else {
                dateData.setType("1");
            }

            dateList.add(dateData);
            Log.e(TAG, "getDateList:sdf.format time " + datevalue);

            cal1.add(Calendar.DATE, 1);
        }


        return dateList;
    }
}

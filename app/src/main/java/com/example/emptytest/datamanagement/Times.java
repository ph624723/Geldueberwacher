package com.example.emptytest.datamanagement;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class Times {

    private static Times timesInstance = null;

    private Times(){
    }

    private Calendar getCalendar(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal;
    }

    public static Times getTimesInstance(){
        if (timesInstance == null){
            timesInstance = new Times();
        }
        return timesInstance;
    }

    public long getStartOfWeek(int addFromNow){
        Calendar calendar = getCalendar();
        calendar.set(Calendar.DAY_OF_WEEK,2);
        calendar.add(Calendar.WEEK_OF_YEAR,addFromNow);
        return calendar.getTime().getTime();
    }

    public long getEndOfWeek(int addFromNow){
        Calendar calendar = getCalendar();
        calendar.set(Calendar.DAY_OF_WEEK,1);
        calendar.add(Calendar.WEEK_OF_YEAR,addFromNow+1);
        return calendar.getTime().getTime();
    }

    public long getStartOfMonth(int addFromNow){
        Calendar calendar = getCalendar();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.MONTH,addFromNow);
        return calendar.getTime().getTime();
    }

    public long getEndOfMonth(int addFromNow){
        Calendar calendar = getCalendar();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.MONTH,addFromNow+1);
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        return calendar.getTime().getTime();
    }

}

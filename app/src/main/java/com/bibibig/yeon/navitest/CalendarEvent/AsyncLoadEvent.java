package com.bibibig.yeon.navitest.CalendarEvent;

import android.util.Log;

import com.bibibig.yeon.navitest.CalendarList.CalendarInfo;
import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class AsyncLoadEvent extends EventListAsyncTask{
    public final String TAG = "AsyncLoadEvent";
    CalendarInfo calendarInfo;
    java.util.Calendar today;

//    public AsyncLoadEvent(MainActivity activity, CalendarInfo calendarInfo, java.util.Calendar today) {
//        super(activity);
//        this.calendarInfo = calendarInfo;
//        this.today = today;
//    }
//    public AsyncLoadEvent(MainActivity activity, CalendarInfo calendarInfo) {
//        super(activity);
//        this.calendarInfo = activity.selectedcalendar;
//    }

    public AsyncLoadEvent(MainActivity activity) {
        super(activity);
        this.calendarInfo = activity.selectedcalendar;
    }
    //today(calendar) date로 변환하는것에 문제가 있음.
    public DateTime setMax(){
        Log.e(TAG, "setMax()");
        Date date = new Date();
        date.setYear(activity.dyear-1900);
        date.setMonth(activity.dmonth);
        date.setDate(activity.dday);
        date.setHours(24);
        date.setMinutes(0);
        date.setSeconds(0);
        DateTime MaxTime = new DateTime(date, TimeZone.getDefault());
        Log.e("MaxTime", MaxTime.toString());
        return MaxTime;
    }
    public DateTime setMin(){
        Log.e(TAG, "setMin()");
        Date date = new Date();
        date.setYear(activity.dyear-1900);
        date.setMonth(activity.dmonth);
        date.setDate(activity.dday);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        DateTime MinTime = new DateTime(date, TimeZone.getDefault());
        Log.e("MinTime", MinTime.toString());
        return MinTime;
    }
    @Override
    protected void doInBackground() throws IOException {
        Log.e(TAG, "doInBackground():"+ calendarInfo.id);

//        DateTime now = new DateTime(System.currentTimeMillis());
//        List<String> eventStrings = new ArrayList<String>();
//        Events feeds = client.events().list(calendarInfo.id)
//                .setMaxResults(10)
//                .setTimeMax(now)
//                .setOrderBy("startTime")
//                .setSingleEvents(true)
//                .execute();

        Events feeds = mEventclient.events().list(calendarInfo.id)
                .setTimeMin(setMin())
                .setTimeMax(setMax())
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

         model.reset(feeds.getItems());

    }
}
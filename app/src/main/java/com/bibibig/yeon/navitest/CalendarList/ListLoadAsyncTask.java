package com.bibibig.yeon.navitest.CalendarList;

import android.util.Log;

import com.bibibig.yeon.navitest.MainActivity;
import com.bibibig.yeon.navitest.common.BasicInfo;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListLoadAsyncTask extends CalendarListAsyncTask{

    public ListLoadAsyncTask(MainActivity activity) {
        super(activity);
        Log.e(activity.TAG, "ListLoadAsyncTask");
    }

    @Override
    protected void doInBackground() throws IOException {
        Log.e(activity.TAG, "doinBackground");
//        String PageToken = null;

        CalendarList feed = client.calendarList().list().setFields(BasicInfo.FEED_FIELDS).execute();
        model.reset(feed.getItems());

        Log.e(activity.TAG, "doinBackground-END");
    }

    private List<String> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = client.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
        }
        return eventStrings;
    }

}
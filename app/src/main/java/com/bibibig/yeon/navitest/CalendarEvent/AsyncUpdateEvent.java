package com.bibibig.yeon.navitest.CalendarEvent;

import android.util.Log;

import com.bibibig.yeon.navitest.CalendarList.CalendarInfo;
import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;


public class AsyncUpdateEvent extends EventListAsyncTask {
    public final String TAG = "AsyncUpdateEvent";
    CalendarInfo calendarInfo;
    String EventId;
     Event entry;

    public AsyncUpdateEvent(MainActivity activity, Event event) {
        super(activity);
        this.calendarInfo = activity.selectedcalendar;
        this.EventId = event.getId();
        this.entry = event;
    }


    @Override
    protected void doInBackground() throws IOException {
        try {
            Log.e(TAG,"updating..." );
        Event updatedEvent = mEventclient.events().update(calendarInfo.id, EventId, entry).execute();

        } catch (GoogleJsonResponseException e) {
            // 404 Not Found would happen if user tries to delete an already deleted calendar
            if (e.getStatusCode() != 404) {
                throw e;
            }
            model.remove(EventId);
        }
    }
}

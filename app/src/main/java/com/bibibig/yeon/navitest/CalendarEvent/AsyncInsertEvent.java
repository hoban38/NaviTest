package com.bibibig.yeon.navitest.CalendarEvent;

import com.bibibig.yeon.navitest.CalendarList.CalendarInfo;
import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;

/**
 * Created by yeon on 2015-08-31.
 */
public class AsyncInsertEvent extends EventListAsyncTask{
    public final String TAG = "AsyncInsertEvent";
    CalendarInfo calendarInfo;
    private final Event entry;


    public AsyncInsertEvent(MainActivity activity,Event entry) {
        super(activity);
        this.calendarInfo = activity.selectedcalendar;
        this.entry = entry;
    }

    @Override
    protected void doInBackground() throws IOException {
//        Log.e(TAG, "inserting..."+calendarInfo.id );
        Event updateevent = mEventclient.events().insert(calendarInfo.id, entry).execute();
        model.add(updateevent);
    }
}

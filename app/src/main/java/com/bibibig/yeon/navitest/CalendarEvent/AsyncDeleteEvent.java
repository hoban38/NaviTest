package com.bibibig.yeon.navitest.CalendarEvent;

import com.bibibig.yeon.navitest.CalendarList.CalendarInfo;
import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;


public class AsyncDeleteEvent extends EventListAsyncTask {
    public final String TAG = "AsyncInsertEvent";
    CalendarInfo calendarInfo;
    String EventId;

    public AsyncDeleteEvent(MainActivity activity, EventInfo eventInfo) {
        super(activity);
        this.calendarInfo = activity.selectedcalendar;
        EventId = eventInfo.eventid;
    }
    //default를 선택된 캘린더로 변경해주어야 한다.
    @Override
    protected void doInBackground() throws IOException {
        try {
            mEventclient.events().delete(calendarInfo.id, EventId).execute();
        } catch (GoogleJsonResponseException e) {
            // 404 Not Found would happen if user tries to delete an already deleted calendar
            if (e.getStatusCode() != 404) {
                throw e;
            }
        }
        model.remove(EventId);
    }


}

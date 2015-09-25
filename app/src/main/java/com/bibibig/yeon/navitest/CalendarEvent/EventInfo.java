package com.bibibig.yeon.navitest.CalendarEvent;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;


public class EventInfo implements Comparable<EventInfo>, Cloneable{

   public String eventid;
   public String eventtitle;
   public String eventDescription;
   public String eventLocation;

   public  DateTime eventstart;
   public String DefaultTimeZone;
   public String[] recureence;
   public String[] attendees;

    public EventInfo(String id, String summary) {
        this.eventid = id;
        this.eventtitle = summary;
    }

    public EventInfo(Event event){
        update(event);
    }

    public void setLocation(String location) {
        eventLocation = location;
    }

    public void setDescription(String description) {
        eventDescription = description;
    }
    public void setRecureence(String[] recureence) {
        this.recureence = recureence;
    }

    public void setAttendees(String[] attendees) {
        this.attendees = attendees;
    }

    @Override
    public int compareTo(EventInfo other) {
        return eventtitle.compareTo(other.eventtitle);
    }

    //Location~ Attendees update 포함시키기
    public void update(Event event) {
        eventid = event.getId();
        eventtitle = event.getSummary();
        eventstart =event.getStart().getDateTime();
        if(eventstart ==null){
            eventstart = event.getStart().getDate();
        }
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }catch (CloneNotSupportedException exception){

            throw new RuntimeException(exception);
        }
    }
}

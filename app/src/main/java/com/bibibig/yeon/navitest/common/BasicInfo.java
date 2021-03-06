package com.bibibig.yeon.navitest.common;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.tasks.TasksScopes;


public class BasicInfo {

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    public static final int REQUEST_AUTHORIZATION = 1;
    public static final int REQUEST_ACCOUNT_PICKER = 2;
    public static final int ADD_OR_EDIT_CALENDAR_REQUEST = 3;
    public static final int ADD_OR_EDIT_EVENT_REQUEST = 4;
    public static final int ADD_OR_EDIT_TASK_REQUEST =5;

    public static final int EVENT_RESULT_OK =10;
    public static final int TASK_RESULT_OK=11;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String PREF_CREDENTIAL = "credential";
    public static final String PREF_ACCOUNT_NAME = "accountName";

    public static GoogleAccountCredential credential;

    public static final String[] SCOPES = { CalendarScopes.CALENDAR, TasksScopes.TASKS };
    public static String accountnameStr ;

    public static final String CALFIELDS = "id,summary";
    public static final String FEED_FIELDS = "items(" + CALFIELDS + ")";

//    public static final String EVENTFIELDS = "id,summary";
//    public static final String FEED_FIELDS = "items(" + CALFIELDS + ")";
}

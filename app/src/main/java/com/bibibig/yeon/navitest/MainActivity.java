package com.bibibig.yeon.navitest;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bibibig.yeon.navitest.CalendarEvent.AsyncInsertEvent;
import com.bibibig.yeon.navitest.CalendarEvent.AsyncLoadEvent;
import com.bibibig.yeon.navitest.CalendarEvent.AsyncUpdateEvent;
import com.bibibig.yeon.navitest.CalendarEvent.EventInfo;
import com.bibibig.yeon.navitest.CalendarList.AddOrEditCalendarListActivity;
import com.bibibig.yeon.navitest.CalendarList.AsyncDeleteCalendarList;
import com.bibibig.yeon.navitest.CalendarList.AsyncInsertCalendarList;
import com.bibibig.yeon.navitest.CalendarList.AsyncUpdateCalendarList;
import com.bibibig.yeon.navitest.CalendarList.CalendarInfo;
import com.bibibig.yeon.navitest.CalendarList.CalendarModel;
import com.bibibig.yeon.navitest.CalendarList.ListLoadAsyncTask;
import com.bibibig.yeon.navitest.common.BasicInfo;
import com.bibibig.yeon.navitest.task.AddorEditTaskActivity;
import com.bibibig.yeon.navitest.task.AsyncInsertTask;
import com.bibibig.yeon.navitest.task.AsyncLoadTasks;
import com.bibibig.yeon.navitest.task.AsyncUpdateTask;
import com.bibibig.yeon.navitest.task.TaskInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.tasks.model.Task;

import java.util.Arrays;

public class MainActivity extends ActionBarActivity implements EventFragment.OnFragmentInteractionListener {
    public static final String TAG = "MainActivity";

    public com.google.api.services.calendar.Calendar mService;
    public com.google.api.services.tasks.Tasks mTaskService;
    public GoogleAccountCredential credential;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    public CalendarModel CalendarModel = new CalendarModel();
    ArrayAdapter<CalendarInfo> adapter;

    private Toolbar toolbar;
    private DrawerLayout dldrawer;
    private ListView NavList;
    private FrameLayout container;
    private ActionBarDrawerToggle dtToggle;
    public static EventFragment fragEvent;

    private final int CONTEXT_EDIT = 0;
    private final int CONTEXT_DELETE = 1;
    private final int CONTEXT_BATCH_ADD = 2;
    //    SimpleDateFormat dateform;
    public java.util.Calendar today;

    //today
    public int myear;
    public int mmonth ;
    public int mday;
    //datepicker date
    public int dyear;
    public int dmonth ;
    public int dday;

    public CalendarInfo selectedcalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FRAG_MAIN
        fragEvent = EventFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragEvent).commit();

        // NAVIGATION
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dldrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        dtToggle = new ActionBarDrawerToggle(this,dldrawer,R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//            }
//
//            @Override
//            public boolean onOptionsItemSelected(MenuItem item) {
//                return super.onOptionsItemSelected(item);
//            }
        };
        dldrawer.setDrawerListener(dtToggle);
        NavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        container = (FrameLayout) findViewById(R.id.container);

        registerForContextMenu(NavList);


        // Initialize credentials and service.
        Log.e(TAG,"setting SharedPreferences");
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(BasicInfo.SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(BasicInfo.PREF_ACCOUNT_NAME, null));
        Log.e(TAG,"setting mservice");

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Calendar Navigation Bibibig")
                .build();

        mTaskService = new com.google.api.services.tasks.Tasks.Builder(
                transport, jsonFactory, credential)
                .build();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            Log.e(TAG, "onResume");
            setToday();
            sync(null);
        }
//        else {
//              mStatusText.setText("Google Play Services required: " + "after installing, close and relaunch this app.");
//        }
    }
    public void sync(MenuItem view){
        refreshCalendarList();
        new AsyncLoadTasks(MainActivity.this).execute();
    }
    public void setToday(){
        // 나중에 form 써서 바꿀것.
        today = java.util.Calendar.getInstance();
        myear = today.get(java.util.Calendar.YEAR);
        mmonth = today.get(java.util.Calendar.MONTH);
        mday= today.get(java.util.Calendar.DAY_OF_MONTH);

        dyear = today.get(java.util.Calendar.YEAR);
        dmonth = today.get(java.util.Calendar.MONTH);
        dday = today.get(java.util.Calendar.DAY_OF_MONTH);
        fragEvent.Datetxt.setText(new StringBuilder().append(myear).append(".").append(mmonth + 1).append(".").append(mday));

        Log.e(TAG, myear + "." + mmonth + "." + mday);
        Log.e(TAG, dyear + "." + dmonth + "." + dday);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e(TAG, "onPostCreate()");
        dtToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged()");
        dtToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(dtToggle.onOptionsItemSelected(item)){
            Log.e(TAG, "onOptionsItemSelected()");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult(): "+requestCode);
        switch(requestCode) {
            case BasicInfo.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case BasicInfo.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(BasicInfo.PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(),"Account unspecified.",Toast.LENGTH_SHORT).show();
                }
                break;
            case BasicInfo.REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    new AsyncLoadTasks(MainActivity.this).execute();
                } else {
                  //  chooseAccount();
                }
                break;
            case BasicInfo.ADD_OR_EDIT_CALENDAR_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Calendar calendar = new Calendar();
                    calendar.setSummary(data.getStringExtra("summary"));
                    String id = data.getStringExtra("id");
                    if (id == null) {
                        new AsyncInsertCalendarList(this,calendar).execute();
                    } else {
                        calendar.setId(id);
                        new AsyncUpdateCalendarList(this,id,calendar).execute();
                    }
                }
                break;
            case BasicInfo.ADD_OR_EDIT_TASK_REQUEST:
                if (resultCode == BasicInfo.TASK_RESULT_OK) {
                    Task newtask = new Task();
                    newtask.setTitle(data.getStringExtra("Tasktitle"));
                    newtask.setNotes(data.getStringExtra("Tasknote"));

                    String Taskid = data.getStringExtra("Taskid");
                    if (Taskid == null) {

                        new AsyncInsertTask(this, newtask).execute();
                    } else {
                        newtask.setId(data.getStringExtra("Taskid"));
                        new AsyncUpdateTask(this, Taskid, newtask).execute();
                    }
                }
                if(resultCode == BasicInfo.EVENT_RESULT_OK) {
                    Log.e("EVENT_RESULT_OK","EVENT_RESULT_OK");
                    Event newevent = new Event();
                    newevent.setSummary(data.getStringExtra("Eventtitle"))
                            .setDescription(data.getStringExtra("Eventsummary"));

                    String Eventid = data.getStringExtra("Eventid");

                    String Eventstart = data.getStringExtra("Eventstart");
                    DateTime startDateTime = new DateTime(Eventstart);
                    Log.e(TAG,"Datetime: "+ data.getStringExtra("Eventstart"));
                    EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("GMT");
                    EventDateTime end = new EventDateTime().setDateTime(startDateTime).setTimeZone("GMT");
                    newevent.setStart(start);
                    newevent.setEnd(end);
                    //newevent.setEnd(start);
                    //이벤트 생성,수정
                    if(Eventid==null){
                        new AsyncInsertEvent(this, newevent).execute();
                    }else{
                        Log.e("AsyncUpdateEvent","AsyncUpdateEvent");
                        new AsyncUpdateEvent(this,newevent).execute();
                    }
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshCalendarList() {
        if (credential.getSelectedAccountName()==null) {
            Log.e(TAG, "refreshResults(): credential null ");
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                Log.e(TAG, "credential ok,isDeviceOnline()" + isDeviceOnline()+" ->ListLoadAsyncTask(this).execute()");
                new ListLoadAsyncTask(this).execute();
            } else {
                Toast.makeText(getApplicationContext(),"No network connection available.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateResultsCalendarList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (CalendarModel.toSortedArray() == null) {
                    Toast.makeText(getApplicationContext(), "Error retrieving data!", Toast.LENGTH_SHORT).show();
                } else if (CalendarModel.toSortedArray().length == 0) {
                    Toast.makeText(getApplicationContext(), "No data found.", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e(TAG, "updateResultsCalendarList() ");
                    Toast.makeText(getApplicationContext(), "Data retrieved using" + "the Google Calendar API:", Toast.LENGTH_SHORT).show();
                    adapter = new ArrayAdapter<CalendarInfo>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, CalendarModel.toSortedArray()) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.e(TAG, "updateResultsCalendarList().getView()");
                            // by default it uses toString; override to use summary instead
                            TextView view = (TextView) super.getView(position, convertView, parent);
                            CalendarInfo calendarInfo = getItem(position);
                            view.setText(calendarInfo.summary);
                            return view;
                        }
                    };
                    NavList.setAdapter(adapter);
                    NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedcalendar = (CalendarInfo) parent.getAdapter().getItem(position);
                            Log.e("NavList", selectedcalendar.id);
                            new AsyncLoadEvent(MainActivity.this).execute();
                            dldrawer.closeDrawers();
                        }
                    });
                }
            }
        });
    }

    public void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), BasicInfo.REQUEST_ACCOUNT_PICKER);
    }
    public boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    public boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }
    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        MainActivity.this,
                        BasicInfo.REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    public void onAddClick(View view) {
        startAddOrEditCalendarActivity(null);
    }
    private void startAddOrEditCalendarActivity(CalendarInfo calendarInfo) {
        Intent intent = new Intent(getApplicationContext(), AddOrEditCalendarListActivity.class);
        if (calendarInfo != null) {
            intent.putExtra("id", calendarInfo.id);
            intent.putExtra("summary", calendarInfo.summary);
        }

        startActivityForResult(intent, BasicInfo.ADD_OR_EDIT_CALENDAR_REQUEST);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Toast.makeText(getApplication(),v.toString(),Toast.LENGTH_LONG).show();
        if(v==NavList) {
            menu.setHeaderTitle("NAVList");
            menu.add(0, CONTEXT_EDIT, 0, R.string.edit);
            menu.add(0, CONTEXT_DELETE, 0, R.string.delete);
            menu.add(0, CONTEXT_BATCH_ADD, 0, R.string.Batch_add);
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {  //checkbox 형태.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int calendarIndex = (int) info.id;
        if (calendarIndex < adapter.getCount()) {
            final CalendarInfo calendarInfo = adapter.getItem(calendarIndex);
            switch (item.getItemId()) {
                case CONTEXT_EDIT:
                    startAddOrEditCalendarActivity(calendarInfo);
                    return true;
                case CONTEXT_DELETE:
                    new AlertDialog.Builder(MainActivity.this).setTitle(R.string.delete_title)
                            .setMessage(calendarInfo.summary)
                            .setCancelable(true) //false. test
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    new AsyncDeleteCalendarList(MainActivity.this, calendarInfo).execute();
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .create()
                            .show();
                    return true;
//                case CONTEXT_BATCH_ADD: //alertdialog 생성 삭제요망.
//                    List<Calendar> calendars = new ArrayList<Calendar>();
//                    for (int i = 0; i < 3; i++) {
//                        Calendar cal = new Calendar();
//                        cal.setSummary(calendarInfo.summary + " [" + (i + 1) + "]");
//                        calendars.add(cal);
//                    }
//                    new AsyncBatchInsertCalendarList(this, calendars).execute();
//                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    public void pickDate(View view) {
        Toast.makeText(getApplicationContext(),"pickDate",Toast.LENGTH_SHORT).show();
        Dialog datepicker = new DatePickerDialog(MainActivity.this,datesetListener,dyear,dmonth,dday);
        datepicker.show();
    }
    DatePickerDialog.OnDateSetListener datesetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dyear = year;
            dmonth = monthOfYear;
            dday = dayOfMonth;
            redisplayDate();
            if(selectedcalendar!=null) {
                new AsyncLoadEvent(MainActivity.this).execute();
            }
        }
    };

    private void redisplayDate(){
        Log.e(TAG, "redisplayDate() ");
        fragEvent.Datetxt.setText(new StringBuilder().append(dyear).append(".").append(dmonth + 1).append(".").append(dday));

    }

    public void onAddTaskClick(MenuItem view) {
        Log.e("onclick","onAddTaskClick,null");
        startAddOrEditTaskActivity(null);
    }

    public void startAddOrEditTaskActivity(TaskInfo taskinfo) {
        Intent intent = new Intent(getApplicationContext(), AddorEditTaskActivity.class);
        if (taskinfo != null) {
            Log.e("startAddOrEditTask",taskinfo.taskid+","+taskinfo.tasktitle+","+taskinfo.tasknote);
            intent.putExtra("Taskid", taskinfo.taskid);
            intent.putExtra("Tasktitle",taskinfo.tasktitle);
            intent.putExtra("Tasknote", taskinfo.tasknote);
        }

        startActivityForResult(intent, BasicInfo.ADD_OR_EDIT_TASK_REQUEST);
    }

    public void startAddOrEditEventActivity(EventInfo eventinfo) {
        Intent intent = new Intent(getApplicationContext(), AddorEditTaskActivity.class);
        if (eventinfo != null) {
            Log.e("startAddOrEditTask",eventinfo.eventid+","+eventinfo.eventtitle+","+eventinfo.eventstart);
            intent.putExtra("eventid",eventinfo.eventid);
            intent.putExtra("eventtitle", eventinfo.eventtitle);
            intent.putExtra("eventDescription", eventinfo.eventDescription);
            intent.putExtra("eventLocation",eventinfo.eventLocation);
            intent.putExtra("Start",eventinfo.eventstart);
          //  intent.putExtra("End",)
        }
        startActivityForResult(intent, BasicInfo.ADD_OR_EDIT_EVENT_REQUEST);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
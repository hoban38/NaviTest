package com.bibibig.yeon.navitest.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.bibibig.yeon.navitest.R;
import com.bibibig.yeon.navitest.common.BasicInfo;
import com.google.api.services.calendar.model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddorEditTaskActivity extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private EditText EventTitleEdit;
    private EditText EventNoteEdit;
    private EditText EventLocationEdit;
    private TextView EventStartDayEdit;
    private TextView EventStartTimeEdit;
    private TextView EventEndDayEdit;
    private TextView EventEndTimeEdit;
    TextView EventText;

    private EditText TaskTitleEdit;
    private EditText TaskNoteEdit;

    TextView TaskText;

    private String Eventid;
    private String Eventtitle;
    private String Eventnote;
    private String Eventlocation;
    private String EventStartDay;
    private String EventStartTime;
    private String EventEndDay;
    private String EventEndTime;



    private String Taskid;
    private String Tasktitle;
    private String Tasknote;

    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat setFormat;
    private static final String TIME_PATTERN = "a HH:mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addor_edit_task);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        setFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss",Locale.getDefault());

        TabHost tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec specEvent = tabhost.newTabSpec("Tab1").setContent(R.id.EventTab).setIndicator("Event");
        TabHost.TabSpec specTask = tabhost.newTabSpec("Tab2").setContent(R.id.TaskTab).setIndicator("Task");
        tabhost.addTab(specEvent);
        tabhost.addTab(specTask);

        tabhost.getTabWidget().getChildAt(0).getLayoutParams().height =100;
        tabhost.getTabWidget().getChildAt(1).getLayoutParams().height =100;

        LinearLayout LEvent = (LinearLayout) tabhost.getTabWidget().getChildAt(0);
        EventText = (TextView) findViewById(R.id.EventText);
        EventTitleEdit = (EditText) findViewById(R.id.EventTitleEdit);
        EventNoteEdit = (EditText) findViewById(R.id.EventNoteEdit);
        EventLocationEdit = (EditText) findViewById(R.id.EventLocationEdit);

        EventStartDayEdit=(TextView)findViewById(R.id.EventstartdayEdit);
        EventStartTimeEdit=(TextView)findViewById(R.id.EventstarttimeEdit);
        EventEndDayEdit=(TextView)findViewById(R.id.EventEnddayEdit);
        EventEndTimeEdit=(TextView)findViewById(R.id.EventEndtimeEdit);

        update();
//        (EditText)findViewById(R.id.EventAttendeesEdit);
//        (EditText)findViewById(R.id.EventalarmEdit);

        LinearLayout LTask = (LinearLayout) tabhost.getTabWidget().getChildAt(1);
        TaskText= (TextView) findViewById(R.id.TaskText);
        TaskTitleEdit= (EditText) findViewById(R.id.TaskTitleEdit);
        TaskNoteEdit= (EditText) findViewById(R.id.TaskNoteEdit);

        //task수정인 경우.
        Taskid = getIntent().getStringExtra("Taskid");
        if (Taskid != null) {
            //이벤트 수정
            EventText.setText("Edit Event");
            EventTitleEdit.setText(getIntent().getStringExtra("Eventid"));
            EventNoteEdit.setText(getIntent().getStringExtra("Eventnote"));
            //task 수정
            TaskText.setText("Edit Task");
            TaskTitleEdit.setText(getIntent().getStringExtra("Tasktitle"));
            TaskNoteEdit.setText(getIntent().getStringExtra("Tasknote"));
        } else {
            EventText.setText("Add Event");
            TaskText.setText("Add Task");
        }

        //event 수정인경우
        Eventid = getIntent().getStringExtra("eventid");
        if (Eventid != null) {
            //task 수정
            TaskText.setText("Edit Task");
            //에러발생가능성 있음.
            TaskTitleEdit.setText(getIntent().getStringExtra("Taskid"));
            TaskNoteEdit.setText(getIntent().getStringExtra("Tasknote"));

            //이벤트 수정
            EventText.setText("Edit Event");
            EventTitleEdit.setText(getIntent().getStringExtra("eventtitle"));
            EventNoteEdit.setText(getIntent().getStringExtra("eventDescription"));
            EventLocationEdit.setText(getIntent().getStringExtra("eventLocation"));
//            String start = getIntent().getStringExtra("eventstart");
//            EventStartDayEdit.setText();
        } else {
            EventText.setText("Add Event");
            TaskText.setText("Add Task");
        }

        Log.e("AddorEditTaskActivity","EVNET:"+Eventid+","+Eventtitle+","+Eventnote);
        Log.e("AddorEditTaskActivity","TASK:"+ Taskid+","+Tasktitle+","+Tasknote);
    }

    private void update() {
        EventStartDayEdit.setText(dateFormat.format(calendar.getTime()));
        EventStartTimeEdit.setText(timeFormat.format(calendar.getTime()));
    }

    public void onClickstart(View view) {
        switch (view.getId()) {
            case R.id.EventstartdayEdit:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.EventstarttimeEdit:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
        }
    }
    public void onClickend(View view) {
        switch (view.getId()) {
            case R.id.EventEnddayEdit:
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.EventEndtimeEdit:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        setFormat.format(calendar.getTime());
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Log.e("onTimeset","view name: "+view.getId());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }


    public void onSaveEvent(View view) {
        Eventtitle = EventTitleEdit.getText().toString();
        Eventnote =  EventNoteEdit.getText().toString();
        Eventlocation = EventLocationEdit.getText().toString();

        if (Eventtitle.length() > 0) {
            Intent t = new Intent();
            Event addevent = new Event();
            if (Eventid != null) {
                t.putExtra("Eventid", Eventid);


            }
            t.putExtra("Eventtitle", Eventtitle);
            if(Eventnote.length()>0){
                t.putExtra("Eventsummary",Eventnote);

            }
            if(Eventlocation.length()>0){
                t.putExtra("Eventlocation",Eventlocation);
            }
           // if(EventStartDay.length() > 0 && EventStartTime.length() > 0){
            String EventDate =setFormat.format(calendar.getTime());
            Log.e("onSaveEvent",EventDate);
            t.putExtra("Eventstart",EventDate);

            setResult(BasicInfo.EVENT_RESULT_OK, t);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    public void onSaveTask(View view) {
        Tasktitle = TaskTitleEdit.getText().toString();
        Tasknote =  TaskNoteEdit.getText().toString();
        if (Tasktitle.length() > 0) {
            Intent t = new Intent();
            if (Taskid != null) {
                t.putExtra("Taskid",Taskid);
            }
            t.putExtra("Tasktitle", Tasktitle);
            if(Tasknote.length()>0){
                t.putExtra("Tasknote",Tasknote);
            }
            setResult(BasicInfo.TASK_RESULT_OK,t);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }
    public void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

}

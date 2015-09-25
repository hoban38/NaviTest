package com.bibibig.yeon.navitest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bibibig.yeon.navitest.CalendarEvent.AsyncDeleteEvent;
import com.bibibig.yeon.navitest.CalendarEvent.EventInfo;
import com.bibibig.yeon.navitest.CalendarEvent.EventModel;
import com.bibibig.yeon.navitest.task.AsyncDeleteTask;
import com.bibibig.yeon.navitest.task.TaskInfo;
import com.bibibig.yeon.navitest.task.TaskModel;
import com.bibibig.yeon.navitest.task.Task_itemLayout;


public class EventFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public TextView Datetxt;
    public ListView Eventlist;
    public ListView Tasklist;
    public EventModel EventModel = new EventModel();
    public TaskModel taskmodel = new TaskModel();

    private final int CONTEXT_TASK_EDIT = 3;
    private final int CONTEXT_TASK_DELETE = 4;
    private final int CONTEXT_TASK_BATCH_ADD = 5;

    private final int CONTEXT_EVENT_EDIT = 6;
    private final int CONTEXT_EVENT_DELETE = 7;
    private final int CONTEXT_EVENT_BATCH_ADD = 8;

    private static MainActivity mactivity = null;
    public static EventFragment newInstance(MainActivity activity) {
         mactivity  = activity;
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_event, container, false);
        Datetxt = (TextView) rootview.findViewById(R.id.eventtxt);
        Eventlist = (ListView) rootview.findViewById(R.id.eventlist);
        Tasklist = (ListView) rootview.findViewById(R.id.tasktlist);
     //   Datetxt.setText(dateform.format(today));
        registerForContextMenu(Tasklist);
        registerForContextMenu(Eventlist);
        return rootview;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


    ArrayAdapter<EventInfo> eventadapter;
    ArrayAdapter<EventInfo> empty;
    public void updateEventResultsList(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(EventModel.toSortedArray() == null){
                    Toast.makeText(getActivity().getApplicationContext(), "Error Event data!",Toast.LENGTH_SHORT).show();

                }else if(EventModel.toSortedArray().length == 0 ){
                    empty = new ArrayAdapter<EventInfo>(getActivity().getApplicationContext(),
                            R.layout.list, EventModel.toSortedArray()) ;
                    Eventlist.setAdapter(empty);
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Event using!",Toast.LENGTH_LONG).show();
                    eventadapter = new ArrayAdapter<EventInfo>(getActivity().getApplicationContext(),
                            R.layout.list, EventModel.toSortedArray()){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                           TextView view = (TextView) super.getView(position, convertView, parent);
                            EventInfo item = getItem(position);
                            view.setText(item.eventtitle);
                            return view ;
                        }
                    };
                    Eventlist.setAdapter(eventadapter);
                }
            }
        });
    }

    ArrayAdapter<TaskInfo> empty1;
    ArrayAdapter<TaskInfo> taskadapter;
    public void updateTaskResultsList(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (taskmodel.toSortedArray() == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Task data!", Toast.LENGTH_SHORT).show();

                } else if (taskmodel.toSortedArray().length == 0) {
                    empty1 = new ArrayAdapter<TaskInfo>(getActivity().getApplicationContext(),
                            R.layout.list, taskmodel.toSortedArray());
                    Tasklist.setAdapter(empty1);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Task using!", Toast.LENGTH_LONG).show();
                    taskadapter = new ArrayAdapter<TaskInfo>(getActivity().getApplicationContext(), R.layout.task_item, taskmodel.toSortedArray()) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
//                            TextView view = (TextView) super.getView(position, convertView, parent);
                            Task_itemLayout taskLayout = new Task_itemLayout(getActivity().getApplicationContext());
                            TaskInfo taskinfo = getItem(position);

                            taskLayout.setidText(taskinfo.taskid);
                            taskLayout.settitleText(taskinfo.tasktitle);
                            taskLayout.setNotetext(taskinfo.tasknote);
                            return taskLayout;
                        }

                    };
                    Tasklist.setAdapter(taskadapter);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Toast.makeText(getActivity().getApplication(),v.toString(),Toast.LENGTH_LONG).show();
        if(v==Eventlist) {
            menu.setHeaderTitle("EventList");
            menu.add(0, CONTEXT_EVENT_EDIT, 0, R.string.event_edit);
            menu.add(0, CONTEXT_EVENT_DELETE, 0, R.string.event_delete);
            menu.add(0, CONTEXT_EVENT_BATCH_ADD, 0, R.string.event_Batch_add);
        }
        if(v==Tasklist) {
            menu.setHeaderTitle("TaskList");
            menu.add(0, CONTEXT_TASK_EDIT, 0, R.string.task_edit);
            menu.add(0, CONTEXT_TASK_DELETE, 0, R.string.task_delete);
           // menu.add(0, CONTEXT_TASK_BATCH_ADD, 0, R.string.task_Batch_add);
        }
    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int Index = (int) info.id;
        final TaskInfo taskInfo;
        final EventInfo eventInfo;
            switch (item.getItemId()) {
                case CONTEXT_TASK_EDIT:
                   taskInfo = taskadapter.getItem(Index);
                    mactivity.startAddOrEditTaskActivity(taskInfo);
                    return true;
                case CONTEXT_TASK_DELETE:
                    taskInfo = taskadapter.getItem(Index);
                    new AlertDialog.Builder(getActivity()).setTitle(R.string.task_delete_title)
                            .setMessage(taskInfo.tasknote)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new AsyncDeleteTask(mactivity,taskInfo).execute();
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .create()
                            .show();
                    return true;
                case CONTEXT_EVENT_EDIT:
                    eventInfo = eventadapter.getItem(Index);
                    mactivity.startAddOrEditEventActivity(eventInfo);
                    return true;
                case CONTEXT_EVENT_DELETE:
                    eventInfo = eventadapter.getItem(Index);
                    new AlertDialog.Builder(getActivity()).setTitle(R.string.task_delete_title)
                            .setMessage(eventInfo.eventtitle)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new AsyncDeleteEvent(mactivity,eventInfo).execute();
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .create()
                            .show();
                    return true;

            }

        return super.onContextItemSelected(item);
    }


}

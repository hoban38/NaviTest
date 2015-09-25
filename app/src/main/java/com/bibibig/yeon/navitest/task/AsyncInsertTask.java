package com.bibibig.yeon.navitest.task;

import android.util.Log;

import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.services.tasks.model.Task;

import java.io.IOException;

public class AsyncInsertTask extends TaskListAsyncTask {

   // private final String TaskId;
    private final Task entry;

    public AsyncInsertTask(MainActivity activity, Task entry) {
        super(activity);
      //  this.TaskId = TaskId;
        this.entry = entry;
    }

    @Override
    protected void doInBackground() throws IOException {
        Log.e("AsyncInsertTask","inserting...");
        Task task = mTaskService.tasks().insert("@default",entry).setFields("items/title").execute();
        model.add(task);
    }
}

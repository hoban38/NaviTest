package com.bibibig.yeon.navitest.task;

import android.util.Log;

import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.tasks.model.Task;

import java.io.IOException;

public class AsyncUpdateTask extends TaskListAsyncTask {

  private final String TaskId;
  private final Task entry;

 public AsyncUpdateTask(MainActivity Activity, String TaskId,Task entry) {
    super(Activity);
    this.TaskId = TaskId;
    this.entry = entry;
  }

  @Override
  protected void doInBackground() throws IOException {
      Log.e("AsyncUpdateTask","updating...");
    try {
// First retrieve the task to update.
        //사실 불필요..(테스트용)
        Task task = mTaskService.tasks().get("@default", TaskId).execute();
        //task.setStatus("completed");
        Task result = mTaskService.tasks().update("@default", task.getId(), entry).execute();
    } catch (GoogleJsonResponseException e) {
      // 404 Not Found would happen if user tries to delete an already deleted calendar
      if (e.getStatusCode() != 404) {
        throw e;
      }
      model.remove(TaskId);
    }
  }
}

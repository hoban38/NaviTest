package com.bibibig.yeon.navitest.task;

import com.bibibig.yeon.navitest.MainActivity;
import com.bibibig.yeon.navitest.common.Utils;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.tasks.model.Task;

import java.io.IOException;
import java.util.List;

/**
 * Created by yeon on 2015-06-16.
 */
public class AsyncBatchInsertTasks extends TaskListAsyncTask {
    private final List<Task> tasks;
    public AsyncBatchInsertTasks(MainActivity activity, List<Task> tasks) {
        super(activity);
        this.tasks  =tasks;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void doInBackground() throws IOException {
        BatchRequest batch =mTaskService.batch();
        for(Task task : tasks){
            mTaskService.tasks().insert("@default",task).setFields("items/title").queue(batch,new JsonBatchCallback<Task>() {
                @Override
                public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
                    Utils.logAndShowError(activity, activity.TAG, e.getMessage());
                }

                @Override
                public void onSuccess(Task task, HttpHeaders responseHeaders) throws IOException {
                    model.add(task);
                }
            });
            batch.execute();
        }
    }
}

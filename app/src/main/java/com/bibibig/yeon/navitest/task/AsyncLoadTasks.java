/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bibibig.yeon.navitest.task;

import android.util.Log;

import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Tasks;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Asynchronously load the tasks.
 *
 * @author Yaniv Inbar
 */
public class AsyncLoadTasks extends TaskListAsyncTask {
    public final String TAG = "AsyncLoadTasks";

   public AsyncLoadTasks(MainActivity activity) {
        super(activity);
    }


//  @Override
//  protected void doInBackground() throws IOException {
//
//    List<String> result = new ArrayList<String>();
//    List<Task> tasks =  client.tasks().list("@default").setFields("items/title").execute().getItems();
//    if (tasks != null) {
//      for (Task task : tasks) {
//        result.add(task.getTitle());
//      }
//    } else {
//      result.add("No tasks.");
//    }
//    activity.taskslist = result;
//  }

    public DateTime setMax(){
        Log.e(TAG, "setMax()");
        Date date = new Date();
        date.setYear(activity.dyear-1900);
        date.setMonth(activity.dmonth);
        date.setDate(activity.dday);
        date.setHours(24);
        date.setMinutes(0);
        date.setSeconds(0);
        DateTime MaxTime = new DateTime(date, TimeZone.getDefault());
        Log.e("MaxTime", MaxTime.toString());
        return MaxTime;
    }
    public DateTime setMin(){
        Log.e(TAG, "setMin()");
        Date date = new Date();
        date.setYear(activity.dyear-1900);
        date.setMonth(activity.dmonth);
        date.setDate(activity.dday);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        DateTime MinTime = new DateTime(date, TimeZone.getDefault());
        Log.e("MinTime", MinTime.toString());
        return MinTime;
    }

    @Override
    protected void doInBackground() throws IOException {
        Log.e(TAG, "doInBackground()");
        Tasks feed =mTaskService.tasks().list("@default").setFields("items").execute();
       // mTaskService.tasklists().list()
        model.reset(feed.getItems());
    }

//    static void run(TaskSample tasksSample) {
//        new AsyncLoadTasks(tasksSample).execute();
//    }

}

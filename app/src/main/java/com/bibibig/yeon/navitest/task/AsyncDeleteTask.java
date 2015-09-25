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

import com.bibibig.yeon.navitest.CalendarList.CalendarInfo;
import com.bibibig.yeon.navitest.MainActivity;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import java.io.IOException;

/**
 * Asynchronously delete a calendar.
 * 
 * @author Yaniv Inbar
 */
public class AsyncDeleteTask extends TaskListAsyncTask {
  CalendarInfo calendarInfo;
  private final String Taskid;

  public AsyncDeleteTask(MainActivity Activity, TaskInfo taskInfo) {
    super(Activity);
    this.calendarInfo = activity.selectedcalendar;
      Taskid = taskInfo.taskid;
  }

  @Override
  protected void doInBackground() throws IOException {
    try {
      mTaskService.tasks().delete(calendarInfo.id, Taskid).execute();
    } catch (GoogleJsonResponseException e) {
      // 404 Not Found would happen if user tries to delete an already deleted calendar
      if (e.getStatusCode() != 404) {
        throw e;
      }
    }
    model.remove(Taskid);
  }
}

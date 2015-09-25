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

import android.os.AsyncTask;

import com.bibibig.yeon.navitest.MainActivity;
import com.bibibig.yeon.navitest.common.BasicInfo;
import com.bibibig.yeon.navitest.common.Utils;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.io.IOException;


/**
 * Asynchronous task that also takes care of common needs, such as displaying progress,
 * authorization, exception handling, and notifying UI when operation succeeded.
 * 
 * @author Yaniv Inbar
 */
abstract class TaskListAsyncTask extends AsyncTask<Void, Void, Boolean> {

  final MainActivity activity;
  final TaskModel  model;
  final com.google.api.services.tasks.Tasks mTaskService;
//  private final View progressBar;

  TaskListAsyncTask(MainActivity activity) {
    this.activity = activity;
    model =activity.fragEvent.taskmodel;
    mTaskService = activity.mTaskService;
//    progressBar = activity.findViewById(R.id.title_refresh_progress);
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
//    activity.numAsyncTasks++;
//    progressBar.setVisibility(View.VISIBLE);
  }
  abstract protected void doInBackground() throws IOException;
  @Override
  protected final Boolean doInBackground(Void... ignored) {
    try {
      doInBackground();
      return true;
    } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
      activity.showGooglePlayServicesAvailabilityErrorDialog(
          availabilityException.getConnectionStatusCode());
    } catch (UserRecoverableAuthIOException userRecoverableException) {
      activity.startActivityForResult(
          userRecoverableException.getIntent(), BasicInfo.REQUEST_AUTHORIZATION);
    } catch (IOException e) {
      Utils.logAndShow(activity, activity.TAG, e);
    }
    return false;
  }

  @Override
  protected final void onPostExecute(Boolean success) {
    super.onPostExecute(success);
//    if (0 == --activity.numAsyncTasks) {
//      progressBar.setVisibility(View.GONE);
//    }
    if (success) {
      activity.fragEvent.updateTaskResultsList();
    }
  }


}

package com.bibibig.yeon.navitest.CalendarList;

import android.os.AsyncTask;

import com.bibibig.yeon.navitest.MainActivity;
import com.bibibig.yeon.navitest.common.BasicInfo;
import com.bibibig.yeon.navitest.common.Utils;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.io.IOException;

abstract class CalendarListAsyncTask extends AsyncTask<Void,Void,Boolean>{
    final MainActivity activity;
    final com.google.api.services.calendar.Calendar client;

    CalendarModel model;
    CalendarListAsyncTask(MainActivity activity) {
        this.activity = activity;
        client = activity.mService;
        model = activity.CalendarModel;
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
                    userRecoverableException.getIntent(),BasicInfo.REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            Utils.logAndShow(activity, activity.TAG, e);
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        activity.numAsyncTasks++;
//        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected final void onPostExecute(Boolean success) {
        super.onPostExecute(success);
//        if (0 == --activity.numAsyncTasks) {
////            progressBar.setVisibility(View.GONE);
//        }
        if (success) {
            activity.updateResultsCalendarList();
        }
    }

}

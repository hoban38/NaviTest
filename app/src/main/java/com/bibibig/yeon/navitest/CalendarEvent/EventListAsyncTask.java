package com.bibibig.yeon.navitest.CalendarEvent;

import android.os.AsyncTask;

import com.bibibig.yeon.navitest.MainActivity;
import com.bibibig.yeon.navitest.common.BasicInfo;
import com.bibibig.yeon.navitest.common.Utils;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.io.IOException;

abstract class EventListAsyncTask extends AsyncTask<Void,Void,Boolean>{

    public final MainActivity activity;
    public final EventModel model;
    public final com.google.api.services.calendar.Calendar mEventclient;
  //  private final View progressBar;

    EventListAsyncTask(MainActivity activity) {
        this.activity = activity;
        model = activity.fragEvent.EventModel;
        mEventclient = activity.mService;
     //   progressBar = activity.findViewById(R.id.title_refresh_progress);
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
//            progressBar.setVisibility(View.GONE);
//        }
        if (success) {
            activity.fragEvent.updateEventResultsList();
        }
    }
}
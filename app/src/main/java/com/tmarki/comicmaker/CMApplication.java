package com.tmarki.comicmaker;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class CMApplication extends Application {
    Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);
            tracker.enableExceptionReporting(true);
        }
        return tracker;
    }

}

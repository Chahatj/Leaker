package com.chahat.leaker.utils;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by chahat on 10/8/17.
 */

public class ReminderUtilities {

    private static final int REMINDER_INTERVAL_HOUR = 24;
    private static final int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(REMINDER_INTERVAL_HOUR);
    private static final int FLEX_INTERVAL_SECONDS = REMINDER_INTERVAL_SECONDS;
    private static final String REMINDER_JOB_TAG = "news-update-tag";
    private static boolean sInitialized;

    synchronized public static void scheduleDailyNews(Context context){

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(driver);

        Job constraintJob = jobDispatcher.newJobBuilder()
                .setService(DailyNewsUpdateFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS,REMINDER_INTERVAL_SECONDS+FLEX_INTERVAL_SECONDS))
                .setRecurring(true)
                .build();

        jobDispatcher.schedule(constraintJob);
        sInitialized = true;

    }
}

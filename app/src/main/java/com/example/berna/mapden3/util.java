package com.example.berna.mapden3;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import android.support.annotation.RequiresApi;

public class util {

    // schedule the start of the service every 10 - 30 seconds
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, jobservice.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency( 300000); // wait at least
        builder.setOverrideDeadline(600000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        builder.setRequiresDeviceIdle(true); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            jobScheduler = context.getSystemService(JobScheduler.class);
        }
        jobScheduler.schedule(builder.build());
    }

}
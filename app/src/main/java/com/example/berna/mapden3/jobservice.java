package com.example.berna.mapden3;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class jobservice extends JobService {
    public jobservice() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Intent service = new Intent(getApplicationContext(), MyService1.class);
        getApplicationContext().startService(service);
        util.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }


}

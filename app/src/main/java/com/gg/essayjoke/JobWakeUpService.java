package com.gg.essayjoke;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

/**
 * Creator:  GG
 * Time   :  2017/9/25
 * Mail   :  gg.jin.yu@gmail.com
 * Explain:
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {

    private final int mJobWakeUpServiceId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //开启一个轮询任务
        JobInfo.Builder jobInfo = new JobInfo.Builder(mJobWakeUpServiceId, new ComponentName(this, JobWakeUpService.class));

        //设置2s轮询一次
        jobInfo.setPeriodic(2000);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        jobScheduler.schedule(jobInfo.build());

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务，定时轮询，  查看Me

        boolean isServiceAlive = isServiceWork(MessageService.class.getName());

        if (!isServiceAlive) {
            startService(new Intent(this, MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    private boolean isServiceWork(String serviceName) {
        boolean isWork = false;

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = activityManager.getRunningServices(100);
        if (serviceInfoList.size() <= 0)
            return false;

        for (int i = 0; i < serviceInfoList.size(); i++) {
            String mName = serviceInfoList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }

        return isWork;
    }

}

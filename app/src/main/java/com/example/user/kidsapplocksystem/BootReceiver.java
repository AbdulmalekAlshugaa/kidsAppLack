package com.example.user.kidsapplocksystem;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zack on 11/03/2017.
 */

public class BootReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent blockIntent = new Intent(context, NotificationPublisher.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, blockIntent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Date date = calendar.getTime();
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
        }
    }
}
package com.example.user.kidsapplocksystem;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zack on 06/03/2017.
 */

public class UStats {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
    }

    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context) {
        return (UsageStatsManager) context.getSystemService("usagestats");
    }
}

package com.example.user.kidsapplocksystem;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.kidsapplocksystem.localdb.AppModel;
import com.example.user.kidsapplocksystem.localdb.AppModelDao;
import com.example.user.kidsapplocksystem.localdb.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private static final String TAG = "LockApp";
    private static final int NOTIFICATION_ID_FOREGROUND_SERVICE = 21;
    private final String ACTION_SHOW_LAYOUT = "com.example.asus.lock.show_layout";
    private final String ACTION_HIDE_LAYOUT = "com.example.asus.lock.hide_layout";
    ActivityManager am = null;
    WindowManager windowManager;
    LinearLayout mView;
    WindowManager.LayoutParams params;
    boolean isLayoutAdded = false;
    private TextView mTextMessage;
    private String mForegroundApp = "";
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equalsIgnoreCase(ACTION_HIDE_LAYOUT)) {
                    removeBlockedLayout();
                } else if (intent.getAction().equalsIgnoreCase(ACTION_SHOW_LAYOUT)) {
                    showBlockedLayout();
                }
            }
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SHOW_LAYOUT);
        filter.addAction(ACTION_HIDE_LAYOUT);
        registerReceiver(receiver, filter);
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        mView = (LinearLayout) inflater.inflate(R.layout.partial_background, null, false);
        mTextMessage = (TextView) mView.findViewById(R.id.text_block_message);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.END | Gravity.TOP;
        loop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Lock App")
                .setTicker("Lock App")
                .setContentText("Lock App")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID_FOREGROUND_SERVICE,
                notification);

    }
      // This is a background process that check for the current application running
     //It will show the block layout if this application marked for lock  in the local database

    private void loop() {
        final Timer timer = new Timer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        String foregroundApp = getTopPackage();
                        if (!foregroundApp.isEmpty()) {
                            if (isBlockedAppForeground(foregroundApp)) {
                                sendBroadcast(new Intent(ACTION_SHOW_LAYOUT));
                            } else {
                                sendBroadcast(new Intent(ACTION_HIDE_LAYOUT));
                            }
                        }
                    }
                }, 0, 300);
            }
        }).start();
    }

    //Saving the lock apps in the DB for the background process to read
    private boolean isBlockedAppForeground(String foregroundApp) {
        AppModelDao appModelDao = getAppDaoSession().getAppModelDao();
        QueryBuilder<AppModel> queryBuilder =
                appModelDao.queryBuilder().where(AppModelDao.Properties.PackageName.eq(foregroundApp));
        if (queryBuilder.list().size() > 0) {
            AppModel appModel = queryBuilder.list().get(0);
            if (appModel.getIsPermanent()) {
                mForegroundApp = appModel.getAppName();
                return true;
            }
            if (appModel.getStartBlockTime() + appModel.getBlockDuration() > System.currentTimeMillis()) {
                mForegroundApp = appModel.getAppName();
                return true;
            } else {
                appModelDao.delete(appModel);
            }
            return false;
        } else {
            return false;
        }
    }

    @SuppressWarnings("ResourceType")
    private String getTopPackage() {
        String topPackageName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
            long currentTime = System.currentTimeMillis();
            // get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY,
                    currentTime - 1000 * 50, currentTime);
            // search for app with most recent last used time
            if (stats != null) {
                long lastUsedAppTime = 0;
                for (UsageStats usageStats : stats) {
                    if (usageStats.getLastTimeUsed() > lastUsedAppTime) {
                        topPackageName = usageStats.getPackageName();
                        lastUsedAppTime = usageStats.getLastTimeUsed();
                    }
                }
            }
            UsageEvents usageEvents = mUsageStatsManager.queryEvents(currentTime - 50 * 1000, currentTime);
            UsageEvents.Event event = new UsageEvents.Event();
            // get last event
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
            }
            if (topPackageName.equals(event.getPackageName()) &&
                    event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                topPackageName = event.getPackageName();
            }
        } else {
            ActivityManager activityManager = (ActivityManager)
                    getApplicationContext().getSystemService(ACTIVITY_SERVICE);
            @SuppressWarnings("deprecation")
            ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
            topPackageName = foregroundTaskInfo.topActivity.getPackageName();
        }
        return topPackageName;
    }

    private void showBlockedLayout() {
        if (!isLayoutAdded) {
            try {
                if (!TextUtils.isEmpty(mForegroundApp)) {
                    mTextMessage.setText(mForegroundApp + " is blocked by parent lock");
                } else {
                    mTextMessage.setText("This app is blocked by parent lock");
                }
                windowManager.addView(mView, params);
                isLayoutAdded = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeBlockedLayout() {
        if (isLayoutAdded) {
            try {
                windowManager.removeView(mView);
                isLayoutAdded = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private DaoSession getAppDaoSession() {
        return ((MyApplication) getApplication()).getDaoSession();
    }
}

package com.example.user.kidsapplocksystem;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.kidsapplocksystem.localdb.AppModel;

import java.util.ArrayList;
import java.util.List;

public class Manageapps extends AppCompatActivity implements AppAdapter.AppAdapterListener {

    ProgressDialog progress = null;
    List<AppModel> apps;
    private AppAdapter appAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (UStats.getUsageStatsList(Manageapps.this).isEmpty()) {
                    Intent permissionIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(permissionIntent);
                }
            }
        }).start();
        setUpView();
        Intent intent = new Intent(Manageapps.this, MyService.class);
        startService(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
    @Override
    public void onBlockApp(AppModel app, int index) {

    }

    @Override
    public void onTemporaryBlockApp(AppModel appModel) {
        scheduleNotification(this, appModel.getAppName(), appModel.getPackageName(), appModel.getBlockDuration(), appModel.getId().intValue());
    }

    private void setUpView() {
        setTitle("Manage Apps");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_apps);
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        appAdapter = new AppAdapter(this);
        appAdapter.setAppsAdapterListener(this);
        recyclerView.setAdapter(appAdapter);
        progress = ProgressDialog.show(this, "",
                "Loading apps", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                apps = getInstalledApps(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progress != null) {
                            progress.dismiss();
                            appAdapter.setApps(apps);
                        }
                    }
                });
            }
        }).start();
    }

    private void scheduleNotification(Context context, String appName, String packageName, long delay, int notificationId) {
        //delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder MeghanTrainorbuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Lock App")
                .setContentText(String.format("Blocking time has ended for %s", appName))
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        MeghanTrainorbuilder.setContentIntent(activity);

        Notification notification = MeghanTrainorbuilder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

     //Get the list of the installed app in your device
    private ArrayList<AppModel> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppModel> res = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            AppModel newInfo = new AppModel();
            newInfo.setAppName(p.applicationInfo.loadLabel(getPackageManager()).toString());
            newInfo.setPackageName(p.packageName);
            newInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }
}

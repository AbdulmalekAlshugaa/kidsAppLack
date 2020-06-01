package com.example.user.kidsapplocksystem;

import android.app.Application;

import com.example.user.kidsapplocksystem.localdb.DaoMaster;
import com.example.user.kidsapplocksystem.localdb.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by zack on 06/03/2017.
 */

public class MyApplication extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "operators-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

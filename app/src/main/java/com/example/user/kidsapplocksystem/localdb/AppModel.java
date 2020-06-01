package com.example.user.kidsapplocksystem.localdb;

import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by zack on 05/03/2017.
 */

@Entity
public class AppModel {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "appName")
    private String appName = "";
    @Property(nameInDb = "packageName")
    @Index(unique = true)
    private String packageName = "";
    @Transient
    private Drawable icon;
    @Property(nameInDb = "isPermanent")
    private boolean isPermanent;
    @Property(nameInDb = "startBlockTime")
    private long startBlockTime;
    @Property(nameInDb = "blockDuration")
    private long blockDuration;

    @Generated(hash = 393068257)
    public AppModel(Long id, String appName, String packageName,
                    boolean isPermanent, long startBlockTime, long blockDuration) {
        this.id = id;
        this.appName = appName;
        this.packageName = packageName;
        this.isPermanent = isPermanent;
        this.startBlockTime = startBlockTime;
        this.blockDuration = blockDuration;
    }

    @Generated(hash = 1377297746)
    public AppModel() {
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public long getStartBlockTime() {
        return startBlockTime;
    }

    public void setStartBlockTime(long startBlockTime) {
        this.startBlockTime = startBlockTime;
    }

    public long getBlockDuration() {
        return blockDuration;
    }

    public void setBlockDuration(long blockDuration) {
        this.blockDuration = blockDuration;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appname) {
        this.appName = appname;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsPermanent() {
        return this.isPermanent;
    }

    public void setIsPermanent(boolean isPermanent) {
        this.isPermanent = isPermanent;
    }
}

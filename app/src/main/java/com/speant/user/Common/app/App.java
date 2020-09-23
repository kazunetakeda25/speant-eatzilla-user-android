package com.speant.user.Common.app;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.speant.user.Common.localDb.DaoMaster;
import com.speant.user.Common.localDb.DaoSession;
import com.speant.user.Common.paymentUtils.AppEnvironment;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

    public static App mInstance;
    private DaoSession mDaoSession;
    AppEnvironment appEnvironment;

    public static App getInstance() {
        return mInstance;
    }

    //DaoSession to initialise insert table
    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        appEnvironment = AppEnvironment.PRODUCTION;
        //for OfflineStorageModule
        mDaoSession = new DaoMaster(
                new DaoMaster.DevOpenHelper(this, "cart.db").getWritableDb()).newSession();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}

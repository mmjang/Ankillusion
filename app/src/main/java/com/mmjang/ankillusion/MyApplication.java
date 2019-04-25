package com.mmjang.ankillusion;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication extends Application{
    private static Context context;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application = this;
        Bugly.init(getApplicationContext(), "77a9755f20", false);
    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication(){
        return application;
    }}

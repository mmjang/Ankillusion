package com.mmjang.ankillusion;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
    private static Context context;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application = this;
//        LitePalApplication.initialize(context);
//        CrashReport.initCrashReport(getApplicationContext(), "398dc6145b", false);
//        AndroidThreeTen.init(this);
    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication(){
        return application;
    }}

package com.cgg.demo.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.sContext = this;
    }
}

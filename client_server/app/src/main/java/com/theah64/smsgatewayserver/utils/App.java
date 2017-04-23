package com.theah64.smsgatewayserver.utils;

import android.app.Application;

import com.theah64.smsgatewayserver.callbacks.LogListener;

/**
 * Created by theapache64 on 5/1/17.
 */

public class App extends Application {

    public static final boolean IS_DEBUG_MODE = false;
    private LogListener logListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public LogListener getLogListener() {
        return logListener;
    }

    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }
}

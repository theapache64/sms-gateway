package com.theah64.sg.api_server.utils;

import java.util.Date;

/**
 * Created by theapache64 on 11/1/17.
 */
public class Log {

    private static final String LOG_FORMAT = "%s:%s:%s";

    public static void d(String tag, String message) {
        System.out.println(String.format(LOG_FORMAT, new Date(), tag, message));
    }
}

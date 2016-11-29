package com.theah64.sg.api_server.utils;


import com.theah64.sg.api_server.database.tables.BaseTable;

/**
 * Created by theapache64 on 26/11/16.
 */
public class CommonUtils {
    public static boolean parseBoolean(String s) {
        return s != null && s.equals(BaseTable.TRUE);
    }
}

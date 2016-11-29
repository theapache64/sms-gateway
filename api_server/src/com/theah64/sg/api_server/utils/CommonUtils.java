package com.theah64.sg.api_server.utils;


import com.theah64.sg.api_server.database.tables.BaseTable;

/**
 * Created by theapache64 on 26/11/16.
 */
public class CommonUtils {

    private static final String INPUT_EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean parseBoolean(String s) {
        return s != null && s.equals(BaseTable.TRUE);
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches(INPUT_EMAIL_REGEX);
    }
}

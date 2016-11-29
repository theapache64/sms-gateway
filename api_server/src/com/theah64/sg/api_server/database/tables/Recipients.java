package com.theah64.sg.api_server.database.tables;

import org.json.JSONArray;

/**
 * Created by theapache64 on 29/11/16,9:55 AM.
 */
public class Recipients extends BaseTable<Void> {
    private static final Recipients instance = new Recipients();
    public static final String TABLE_NAME = "recipients";


    private Recipients() {
        super(TABLE_NAME);
    }

    public static Recipients getInstance() {
        return instance;
    }

    public JSONArray add(String requestId, JSONArray jaRecipients) {
        
        return null;
    }
}

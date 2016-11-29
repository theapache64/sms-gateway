package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.models.Server;

/**
 * Created by theapache64 on 29/11/16,8:01 AM.
 */
public class Servers extends BaseTable<Server> {
    private static final Servers instance = new Servers();
    public static final String COLUMN_FCM_ID = "fcm_id";

    private Servers() {
        super("servers");
    }

    public static Servers getInstance() {
        return instance;
    }
}

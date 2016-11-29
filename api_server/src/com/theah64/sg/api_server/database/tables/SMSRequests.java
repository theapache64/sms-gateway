package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.models.SMSRequest;

/**
 * Created by theapache64 on 29/11/16,9:57 AM.
 */
public class SMSRequests extends BaseTable<SMSRequest> {
    private static final SMSRequests instance = new SMSRequests();
    public static final String COLUMN_MESSAGE = "message";

    private SMSRequests() {
        super("sms_requests");
    }

    public static SMSRequests getInstance() {
        return instance;
    }

}

package com.theah64.sg.api_server.database.tables;

import com.theah64.sg.api_server.models.SMSRequestStatus;

/**
 * Created by theapache64 on 29/11/16,5:19 PM.
 */
public class SMSRequestStatuses extends BaseTable<SMSRequestStatus> {

    private static final SMSRequestStatuses instance = new SMSRequestStatuses();
    public static final String COLUMN_RECIPIENT_ID = "recipient_id";

    private SMSRequestStatuses() {
        super("sms_request_statuses");
    }

    public static SMSRequestStatuses getInstance() {
        return instance;
    }
}

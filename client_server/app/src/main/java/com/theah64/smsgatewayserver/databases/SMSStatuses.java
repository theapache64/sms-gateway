package com.theah64.smsgatewayserver.databases;

import android.content.Context;

import com.theah64.smsgatewayserver.models.SMSStatus;

/**
 * Created by theapache64 on 9/1/17.
 */

public class SMSStatuses extends BaseTable<SMSStatus> {

    SMSStatuses(Context context, String tableName) {
        super(context, tableName);
    }
}

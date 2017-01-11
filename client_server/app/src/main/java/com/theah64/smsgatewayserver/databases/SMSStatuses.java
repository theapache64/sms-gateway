package com.theah64.smsgatewayserver.databases;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.theah64.smsgatewayserver.models.SMSStatus;

/**
 * Created by theapache64 on 9/1/17.
 */

public class SMSStatuses extends BaseTable<SMSStatus> {

    private SMSStatuses instance;

    private SMSStatuses(Context context) {
        super(context, "sms_statuses");
    }

    public SMSStatuses getInstance(Context context) {
        if (instance == null) {
            instance = new SMSStatuses(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public long add(SMSStatus newInstance, @Nullable Handler handler) {
        return super.add(newInstance, handler);
    }
}

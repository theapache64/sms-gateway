package com.theah64.smsgatewayserver.databases;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.theah64.smsgatewayserver.models.SMSStatus;
import com.theah64.smsgatewayserver.utils.NetworkUtils;


/**
 * Created by theapache64 on 9/1/17.
 * CREATE TABLE sms_statuses(
 * id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 * message_id INTEGER NOT NULL,
 * recipient_id INTEGER NOT NULL,
 * status VARCHAR(50) CHECK (status IN ('DELIVERED_TO_SERVER','SENT_TO_RECIPIENT','DELIVERED_TO_RECIPIENT','FAILED_TO_SEND_TO_RECIPIENT')) NOT NULL,
 * occurred_at INTEGER NOT NULL,
 * created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
 * );
 */
public class SMSStatuses extends BaseTable<SMSStatus> {

    public static final String COLUMN_RECIPIENT_ID = "recipient_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_OCCURRED_AT = "occurred_at";
    private static final String TABLE_NAME_SMS_STATUSES = "sms_statuses";
    private static final String COLUMN_REASON = "reason";
    private static final String X = SMSStatuses.class.getSimpleName();
    private static SMSStatuses instance;

    private SMSStatuses(Context context) {
        super(context, TABLE_NAME_SMS_STATUSES);
    }

    public static SMSStatuses getInstance(Context context) {
        if (instance == null) {
            instance = new SMSStatuses(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public long add(final SMSStatus smsStatus, @Nullable Handler handler) {

        final ContentValues cv = new ContentValues(4);
        cv.put(COLUMN_RECIPIENT_ID, smsStatus.getRecipientId());
        cv.put(COLUMN_STATUS, smsStatus.getStatus());
        cv.put(COLUMN_REASON, smsStatus.getReason());
        cv.put(COLUMN_OCCURRED_AT, smsStatus.getOccurredAt());

        final long statusId = this.getWritableDatabase().insert(TABLE_NAME_SMS_STATUSES, null, cv);
        if (statusId == -1) {
            throw new IllegalArgumentException("Failed to insert new sms status");
        }

        if (NetworkUtils.hasNetwork(getContext())) {
            smsStatus.setId(String.valueOf(statusId));
            SMSStatus.sync(getContext(), smsStatus);
        }

        return statusId;
    }
}

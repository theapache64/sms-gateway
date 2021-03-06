package com.theah64.smsgatewayserver.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theah64.smsgatewayserver.databases.SMSStatuses;
import com.theah64.smsgatewayserver.models.Recipient;
import com.theah64.smsgatewayserver.models.SMSStatus;

public class OnSMSDeliveredReceiver extends BroadcastReceiver {

    private static final String X = OnSMSDeliveredReceiver.class.getSimpleName();

    public OnSMSDeliveredReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(X, "Intent: " + intent);

        final String recipientId = intent.getStringExtra(Recipient.KEY_RECIPIENT_ID);
        Log.d(X, "RecipientID: " + recipientId);


        final SMSStatus smsStatus = new SMSStatus(null, recipientId, null, null, System.currentTimeMillis());
        switch (getResultCode()) {

            case Activity.RESULT_OK:
                smsStatus.setStatus(SMSStatus.STATUS_DELIVERED_TO_RECIPIENT);
                break;

            case Activity.RESULT_CANCELED:
                smsStatus.setStatus(SMSStatus.STATUS_FAILED_TO_DELIVER_TO_RECIPIENT);
                break;

        }

        SMSStatuses.getInstance(context).add(smsStatus, null);

    }
}

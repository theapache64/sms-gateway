package com.theah64.smsgatewayserver.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.theah64.smsgatewayserver.databases.SMSStatuses;
import com.theah64.smsgatewayserver.models.Recipient;
import com.theah64.smsgatewayserver.models.SMSStatus;

public class OnSMSSentReceiver extends BroadcastReceiver {
    private static final String X = OnSMSSentReceiver.class.getSimpleName();

    public OnSMSSentReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(X, "Intent: " + intent);

        final String recipientId = intent.getStringExtra(Recipient.KEY_RECIPIENT_ID);
        Log.d(X, "RecipientID: " + recipientId);
        Log.d(X, "ResultCode: " + getResultCode());

        final SMSStatus smsStatus = new SMSStatus(null, recipientId, null, null, System.currentTimeMillis());

        switch (getResultCode()) {

            case Activity.RESULT_OK:
                smsStatus.setStatus(SMSStatus.STATUS_SENT_TO_RECIPIENT);
                break;

            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                smsStatus.setStatus(SMSStatus.STATUS_FAILED_TO_SEND_TO_RECIPIENT);
                smsStatus.setReason(SMSStatus.REASON_GENERIC_FAILURE);
                break;

            case SmsManager.RESULT_ERROR_NO_SERVICE:
                smsStatus.setStatus(SMSStatus.STATUS_FAILED_TO_SEND_TO_RECIPIENT);
                smsStatus.setReason(SMSStatus.REASON_NO_SERVICE);
                break;

            case SmsManager.RESULT_ERROR_NULL_PDU:
                smsStatus.setStatus(SMSStatus.STATUS_FAILED_TO_SEND_TO_RECIPIENT);
                smsStatus.setReason(SMSStatus.REASON_NULL_PDU);
                break;

            case SmsManager.RESULT_ERROR_RADIO_OFF:
                smsStatus.setStatus(SMSStatus.STATUS_FAILED_TO_SEND_TO_RECIPIENT);
                smsStatus.setReason(SMSStatus.REASON_RADIO_OFF);
                break;
        }

        SMSStatuses.getInstance(context).add(smsStatus, null);
    }
}

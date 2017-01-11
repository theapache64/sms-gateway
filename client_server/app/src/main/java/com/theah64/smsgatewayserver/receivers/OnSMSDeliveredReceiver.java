package com.theah64.smsgatewayserver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theah64.smsgatewayserver.models.Recipient;

public class OnSMSDeliveredReceiver extends BroadcastReceiver {

    private static final String X = OnSMSDeliveredReceiver.class.getSimpleName();

    public OnSMSDeliveredReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(X, "Intent: " + intent);

        final String recipientId = intent.getStringExtra(Recipient.KEY_RECIPIENT_ID);
        Log.d(X, "RecipientID: " + recipientId);


    }
}

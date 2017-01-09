package com.theah64.smsgatewayserver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.theah64.smsgatewayserver.models.Recipient;

public class OnSMSDeliveredReceiver extends BroadcastReceiver {
    public OnSMSDeliveredReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final String recipientId = intent.getStringExtra(Recipient.KEY_RECIPIENT_ID);
        f

    }
}

package com.theah64.smsgatewayserver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnSMSSentReceiver extends BroadcastReceiver {
    private static final String X = OnSMSSentReceiver.class.getSimpleName();

    public OnSMSSentReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(X, intent.getDataString());
    }
}

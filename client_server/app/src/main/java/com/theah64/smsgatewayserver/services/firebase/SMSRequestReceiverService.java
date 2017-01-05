package com.theah64.smsgatewayserver.services.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SMSRequestReceiverService extends FirebaseMessagingService {

    private static final String X = SMSRequestReceiverService.class.getSimpleName();
    private static final String KEY_TYPE = "type";
    private static final String TYPE_LOCATION_REQUEST = "location_request";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(X, "FCM sayssss: " + remoteMessage);
        Map<String, String> payload = remoteMessage.getData();
        Log.i(X, "FCM says : " + payload);

        if (!payload.isEmpty()) {

            final String type = payload.get(KEY_TYPE);

            if (type.equals(TYPE_LOCATION_REQUEST)) {

            } else {
                //TODO: Manage [anything-else] here.
            }
        }
    }


}

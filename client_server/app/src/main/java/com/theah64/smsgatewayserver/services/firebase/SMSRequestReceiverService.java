package com.theah64.smsgatewayserver.services.firebase;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.theah64.smsgatewayserver.callbacks.LogListener;
import com.theah64.smsgatewayserver.models.Recipient;
import com.theah64.smsgatewayserver.receivers.OnSMSDeliveredReceiver;
import com.theah64.smsgatewayserver.receivers.OnSMSSentReceiver;
import com.theah64.smsgatewayserver.utils.App;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMSRequestReceiverService extends FirebaseMessagingService implements LogListener {

    private static final String X = SMSRequestReceiverService.class.getSimpleName();
    private static final String KEY_TYPE = "type";
    private static final String TYPE_SMS_REQUEST = "sms_request";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_RECIPIENTS = "recipients";

    private LogListener callback;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        callback = ((App) getApplicationContext()).getLogListener();

        log("----------------------------");

        Log.d(X, "FCM sayssss: " + remoteMessage);
        Map<String, String> payload = remoteMessage.getData();
        Log.i(X, "FCM says : " + payload);

        log("New FCM Request Received :" + payload);

        if (!payload.isEmpty()) {

            //Sample :  {recipients=[{"recipient":"9895182634","recipient_id":"5"},{"recipient":"9744123123","recipient_id":"6"}], type=sms_request, message=Hello Double}

            final String type = payload.get(KEY_TYPE);

            log("Request type: " + type);

            if (type.equals(TYPE_SMS_REQUEST)) {

                log("Preparing SMS...");

                final String message = payload.get(KEY_MESSAGE);

                log("Message : " + message);

                try {


                    log("Recipients :" + payload.get(KEY_RECIPIENTS));

                    final List<Recipient> recipients = Recipient.parse(new JSONArray(payload.get(KEY_RECIPIENTS)));
                    final SmsManager smsManager = SmsManager.getDefault();

                    final ArrayList<String> parts = smsManager.divideMessage(message);


                    //Sending message
                    for (final Recipient recipient : recipients) {

                        Log.d(X, "Sending message to : " + recipient.toString() + "\nMessage:" + parts.toString());

                        //Martin Garrix started dreaming of becoming a DJ when he was 8.

                        final int partsSize = parts.size();

                        //Building sent intent
                        final ArrayList<PendingIntent> sentIntents = new ArrayList<>(partsSize);
                        final ArrayList<PendingIntent> deliveryIntents = new ArrayList<>(partsSize);


                        for (int i = 0; i < partsSize; i++) {

                            //Building sent intent
                            final Intent sentIntent = new Intent(this, OnSMSSentReceiver.class);
                            sentIntent.putExtra(Recipient.KEY_RECIPIENT_ID, recipient.getId());
                            sentIntents.add(PendingIntent.getBroadcast(this, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT));

                            //Building delivery intents
                            final Intent deliveryIntent = new Intent(this, OnSMSDeliveredReceiver.class);
                            deliveryIntent.putExtra(Recipient.KEY_RECIPIENT_ID, recipient.getId());
                            deliveryIntents.add(PendingIntent.getBroadcast(this, 0, deliveryIntent, PendingIntent.FLAG_UPDATE_CURRENT));

                        }


                        //Sending message
                        smsManager.sendMultipartTextMessage(recipient.getNumber(), null, parts, sentIntents, deliveryIntents);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //TODO; TSH : Manage error here : DAMAGED RECIPIENT DATA

                }

            } else {
                //TODO: Manage [anything-else] here.
            }
        }
    }


    @Override
    public void log(String message) {
        if (callback != null) {
            callback.log(message);
        }
    }
}

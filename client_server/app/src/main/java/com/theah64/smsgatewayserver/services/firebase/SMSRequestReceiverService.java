package com.theah64.smsgatewayserver.services.firebase;

import android.app.PendingIntent;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.theah64.smsgatewayserver.models.Recipient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMSRequestReceiverService extends FirebaseMessagingService {

    private static final String X = SMSRequestReceiverService.class.getSimpleName();
    private static final String KEY_TYPE = "type";
    private static final String TYPE_SMS_REQUEST = "sms_request";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_RECIPIENTS = "recipients";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(X, "FCM sayssss: " + remoteMessage);
        Map<String, String> payload = remoteMessage.getData();
        Log.i(X, "FCM says : " + payload);

        if (!payload.isEmpty()) {

            //Sample :  {recipients=[{"recipient":"9895182634","recipient_id":"5"},{"recipient":"9744123123","recipient_id":"6"}], type=sms_request, message=Hello Double}

            final String type = payload.get(KEY_TYPE);

            if (type.equals(TYPE_SMS_REQUEST)) {

                final String message = payload.get(KEY_MESSAGE);
                try {
                    final List<Recipient> recipients = Recipient.parse(new JSONArray(payload.get(KEY_RECIPIENTS)));
                    final SmsManager smsManager = SmsManager.getDefault();

                    final ArrayList<String> parts = smsManager.divideMessage(message);

                    //Building sent intent
                    final ArrayList<PendingIntent> sentIntents = new ArrayList<>();
                    final ArrayList<PendingIntent> deliveryIntents = new ArrayList<>();

                    for (final Recipient recipient : recipients) {
                        //TODO :GOT TO
                    }

                    //Sending message
                    for (final Recipient recipient : recipients) {
                        Log.d(X, "Sending message to : " + recipient.toString() + "\nMessage:" + parts.toString());


                        //Martin Garrix started dreaming of becoming a DJ when he was 8.


                        //Sending message
                        smsManager.sendMultipartTextMessage(recipient.getNumber(), null, parts, null, null);
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


}

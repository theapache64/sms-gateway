package com.theah64.smsgatewayserver.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 6/1/17.
 */

public class Recipient {

    private static final String KEY_RECIPIENT = "recipient";
    private static final String KEY_RECIPIENT_ID = "recipient_id";
    private final String id, number;

    private Recipient(String id, String number) {
        this.id = id;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public static List<Recipient> parse(JSONArray jaRecipients) throws JSONException {
        final List<Recipient> recipients = new ArrayList<>(jaRecipients.length());
        for (int i = 0; i < jaRecipients.length(); i++) {
            final JSONObject joRecipient = jaRecipients.getJSONObject(i);
            recipients.add(new Recipient(
                    joRecipient.getString(KEY_RECIPIENT_ID),
                    joRecipient.getString(KEY_RECIPIENT)
            ));
        }
        return recipients;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}

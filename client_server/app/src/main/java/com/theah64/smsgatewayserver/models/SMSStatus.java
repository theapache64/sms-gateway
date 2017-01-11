package com.theah64.smsgatewayserver.models;

import android.content.Context;
import android.util.Log;

import com.theah64.smsgatewayserver.databases.SMSStatuses;
import com.theah64.smsgatewayserver.utils.APIRequestBuilder;
import com.theah64.smsgatewayserver.utils.APIRequestGateway;
import com.theah64.smsgatewayserver.utils.APIResponse;
import com.theah64.smsgatewayserver.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.DownloadManager.COLUMN_REASON;

/**
 * Created by theapache64 on 9/1/17.
 */

public class SMSStatus {

    public static final String STATUS_SENT_TO_RECIPIENT = "SENT_TO_RECIPIENT";
    public static final String STATUS_FAILED_TO_SEND_TO_RECIPIENT = "FAILED_TO_SEND_TO_RECIPIENT";
    public static final String REASON_GENERIC_FAILURE = "REASON_GENERIC_FAILURE";
    public static final String REASON_NO_SERVICE = "NO_SERVICE";
    public static final String REASON_NULL_PDU = "NULL_PDU";
    public static final String REASON_RADIO_OFF = "RADIO_OFF";
    public static final String STATUS_DELIVERED_TO_RECIPIENT = "DELIVERED_TO_RECIPIENT";
    public static final String STATUS_FAILED_TO_DELIVER_TO_RECIPIENT = "FAILED_TO_DELIVER_TO_RECIPIENT";
    private static final String X = SMSStatus.class.getSimpleName();

    private String id;
    private final String recipientId;
    private String status;
    private String reason;
    private final long occurredAt;

    public SMSStatus(String id, String recipientId, String status, String reason, long occurredAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.status = status;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }

    public String getReason() {
        return reason;
    }

    public String getId() {
        return id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getStatus() {
        return status;
    }

    public long getOccurredAt() {
        return occurredAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static JSONArray toJSONArray(SMSStatus smsStatus) throws JSONException {
        final JSONArray jaStatuses = new JSONArray();
        jaStatuses.put(toJSONObject(smsStatus));
        return jaStatuses;
    }


    public static JSONArray toJSONArray(List<SMSStatus> statusList) throws JSONException {
        final JSONArray jaStatuses = new JSONArray();
        for (final SMSStatus smsStatus : statusList) {
            jaStatuses.put(toJSONObject(smsStatus));
        }
        return jaStatuses;
    }

    private static JSONObject toJSONObject(SMSStatus smsStatus) throws JSONException {
        final JSONObject joStatus = new JSONObject();

        joStatus.put(SMSStatuses.COLUMN_RECIPIENT_ID, smsStatus.getRecipientId());
        joStatus.put(SMSStatuses.COLUMN_STATUS, smsStatus.getStatus());
        if (smsStatus.getReason() != null) {
            joStatus.put(COLUMN_REASON, smsStatus.getReason());
        }
        joStatus.put(SMSStatuses.COLUMN_OCCURRED_AT, smsStatus.getOccurredAt() + ""); //string fix

        return joStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void sync(final Context context, final List<SMSStatus> statusList) {

        //Syncing the status
        new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String serverKey) throws JSONException {

                final Request statusSyncRequest = new APIRequestBuilder("/add_statuses", serverKey)
                        .addParam("statuses", SMSStatus.toJSONArray(statusList).toString())
                        .build();

                OkHttpUtils.getInstance().getClient().newCall(statusSyncRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String stringResp = OkHttpUtils.logAndGetStringBody(response);
                        try {
                            new APIResponse(stringResp);
                            Log.i(X, stringResp);

                            //Deleting from db
                            SMSStatuses.getInstance(context).delete(SMSStatuses.COLUMN_ID, smsStatus.getId());

                        } catch (APIResponse.APIException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onFailed(String reason) {

            }
        }, false);
    }
}

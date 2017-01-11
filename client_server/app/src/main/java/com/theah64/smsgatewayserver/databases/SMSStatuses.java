package com.theah64.smsgatewayserver.databases;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.theah64.smsgatewayserver.models.SMSStatus;
import com.theah64.smsgatewayserver.utils.APIRequestBuilder;
import com.theah64.smsgatewayserver.utils.APIRequestGateway;
import com.theah64.smsgatewayserver.utils.APIResponse;
import com.theah64.smsgatewayserver.utils.NetworkUtils;
import com.theah64.smsgatewayserver.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by theapache64 on 9/1/17.
 * CREATE TABLE sms_statuses(
 * id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 * message_id INTEGER NOT NULL,
 * recipient_id INTEGER NOT NULL,
 * status VARCHAR(50) CHECK (status IN ('DELIVERED_TO_SERVER','SENT_TO_RECIPIENT','DELIVERED_TO_RECIPIENT','FAILED_TO_SEND_TO_RECIPIENT')) NOT NULL,
 * occurred_at INTEGER NOT NULL,
 * created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
 * );
 */
public class SMSStatuses extends BaseTable<SMSStatus> {

    private static final String COLUMN_RECIPIENT_ID = "recipient_id";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_OCCURRED_AT = "occurred_at";
    private static final String TABLE_NAME_SMS_STATUSES = "sms_statuses";
    private static final String COLUMN_REASON = "reason";
    private static final String X = SMSStatuses.class.getSimpleName();
    private SMSStatuses instance;

    private SMSStatuses(Context context) {
        super(context, TABLE_NAME_SMS_STATUSES);
    }

    public SMSStatuses getInstance(Context context) {
        if (instance == null) {
            instance = new SMSStatuses(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public long add(final SMSStatus smsStatus, @Nullable Handler handler) {

        final ContentValues cv = new ContentValues(4);
        cv.put(COLUMN_RECIPIENT_ID, smsStatus.getRecipientId());
        cv.put(COLUMN_STATUS, smsStatus.getStatus());
        cv.put(COLUMN_REASON, smsStatus.getReason());
        cv.put(COLUMN_OCCURRED_AT, smsStatus.getOccurredAt());

        long statusId = this.getWritableDatabase().insert(TABLE_NAME_SMS_STATUSES, null, cv);
        if (statusId == -1) {
            throw new IllegalArgumentException("Failed to insert new sms status");
        }

        if (NetworkUtils.hasNetwork(getContext())) {
            //Syncing the status
            new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String serverKey) throws JSONException {

                    final JSONArray jaStatuses = new JSONArray();
                    final JSONObject joStatus = new JSONObject();

                    joStatus.put(COLUMN_RECIPIENT_ID, smsStatus.getRecipientId());
                    joStatus.put(COLUMN_STATUS, smsStatus.getStatus());
                    if (smsStatus.getReason() != null) {
                        joStatus.put(COLUMN_REASON, smsStatus.getReason());
                    }
                    joStatus.put(COLUMN_OCCURRED_AT, smsStatus.getOccurredAt());

                    jaStatuses.put(joStatus);

                    final Request statusSyncRequest = new APIRequestBuilder("/add_statuses", serverKey)
                            .addParam("statuses", jaStatuses.toString())
                            .build();

                    OkHttpUtils.getInstance().getClient().newCall(statusSyncRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String stringResp = OkHttpUtils.logAndGetStringBody(response);
                            try {
                                final APIResponse apiResponse = new APIResponse(stringResp);
                                Log.i(X,"")
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

        return statusId;
    }
}

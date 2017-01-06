package com.theah64.smsgatewayserver.async;

import android.content.Context;
import android.util.Log;


import com.theah64.smsgatewayserver.models.Server;
import com.theah64.smsgatewayserver.utils.APIRequestBuilder;
import com.theah64.smsgatewayserver.utils.APIRequestGateway;
import com.theah64.smsgatewayserver.utils.APIResponse;
import com.theah64.smsgatewayserver.utils.OkHttpUtils;
import com.theah64.smsgatewayserver.utils.PrefUtils;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by theapache64 on 28/9/16.
 */

public class FCMSynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {

    private static final String X = FCMSynchronizer.class.getSimpleName();
    private final String newFcmId;
    private final boolean isFCMSynced;

    public FCMSynchronizer(Context context, String serverKey) {
        super(context, serverKey);
        final PrefUtils prefUtils = PrefUtils.getInstance(context);
        this.newFcmId = prefUtils.getString(Server.KEY_FCM_ID);
        this.isFCMSynced = prefUtils.getBoolean(Server.KEY_IS_FCM_SYNCED);
    }

    @Override
    protected synchronized Void doInBackground(String... strings) {

        if (newFcmId != null && !isFCMSynced) {
            new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String serverKey) {

                    final Request fcmUpdateRequest = new APIRequestBuilder("/update_fcm", serverKey)
                            .addParam(Server.KEY_FCM_ID, newFcmId)
                            .build();

                    OkHttpUtils.getInstance().getClient().newCall(fcmUpdateRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                                PrefUtils.getInstance(getContext()).getEditor()
                                        .putBoolean(Server.KEY_IS_FCM_SYNCED, true)
                                        .commit();
                            } catch (JSONException | APIResponse.APIException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFailed(String reason) {
                    Log.e(X, "Failed to update fcm : " + reason);
                }
            }, false);
        }

        return null;
    }
}

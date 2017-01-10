package com.theah64.smsgatewayserver.services.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.theah64.smsgatewayserver.async.FCMSynchronizer;
import com.theah64.smsgatewayserver.models.Server;
import com.theah64.smsgatewayserver.utils.APIRequestGateway;
import com.theah64.smsgatewayserver.utils.PermissionUtils;
import com.theah64.smsgatewayserver.utils.PrefUtils;

public class InstanceIdService extends FirebaseInstanceIdService implements PermissionUtils.Callback {
    private static final String X = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        final String newFcmId = FirebaseInstanceId.getInstance().getToken();
        Log.i(X, "Firebase token refreshed : " + newFcmId);

        final SharedPreferences.Editor prefEditor = PrefUtils.getInstance(this).getEditor();
        prefEditor.putString(Server.KEY_FCM_ID, newFcmId);
        prefEditor.putBoolean(Server.KEY_IS_FCM_SYNCED, false);
        prefEditor.commit();

        new PermissionUtils(this, this, null).begin();
    }

    private void doNormalWork() {

        new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String serverKey) {
                new FCMSynchronizer(InstanceIdService.this, serverKey).execute();
            }

            @Override
            public void onFailed(String reason) {
                Log.e(X, "Reason : " + reason);
            }

        }, false);

    }


    @Override
    public void onAllPermissionGranted() {
        doNormalWork();
    }

    @Override
    public void onPermissionDenial() {
        Log.e(X, "Permission not yet granted");
    }
}

package com.theah64.smsgatewayserver.services.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.theah64.smsgatewayserver.utils.PrefUtils;

public class InstanceIdService extends FirebaseInstanceIdService {
    private static final String X = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        final String newFcmId = FirebaseInstanceId.getInstance().getToken();
        Log.i(X, "Firebase token refreshed : " + newFcmId);

        final SharedPreferences.Editor prefEditor = PrefUtils.getInstance(this).getEditor();
        prefEditor.putString(Employee.KEY_FCM_ID, newFcmId);
        prefEditor.putBoolean(Employee.KEY_IS_FCM_SYNCED, false);
        prefEditor.commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                doNormalWork();
            } else {
                Log.e(X, "Permission not yet granted");
            }

        } else {
            doNormalWork();
        }
    }

    private void doNormalWork() {

        new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String apiKey) {
                new FCMSynchronizer(InstanceIdService.this, apiKey).execute();
            }

            @Override
            public void onFailed(String reason) {

            }
        });

    }


}

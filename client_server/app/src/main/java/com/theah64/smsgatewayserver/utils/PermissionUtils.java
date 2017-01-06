package com.theah64.smsgatewayserver.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.theah64.smsgatewayserver.R;
import com.theah64.smsgatewayserver.activities.SplashActivity;

/**
 * Created by theapache64 on 5/1/17.
 */

public class PermissionUtils {


    public static final int RQ_CODE_ASK_PERMISSION = 1;

    private final Context context;
    private final Callback callback;
    private final Activity activity;

    private static final String[] PERMISSIONS_NEEDED = new String[]{
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS
    };

    public PermissionUtils(Context context) {
        this.context = context;

        if (context instanceof Callback) {
            this.callback = (Callback) context;
        } else {
            throw new IllegalArgumentException("The context must implement PermissionUtils.Callback");
        }

        if (context instanceof Activity) {
            this.activity = (Activity) context;
        } else {
            this.activity = null;
        }

    }

    public void begin() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean isAllPermissionAccepted = true;
            for (final String perm : PERMISSIONS_NEEDED) {
                if (ActivityCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                    isAllPermissionAccepted = false;
                    break;
                }
            }

            if (!isAllPermissionAccepted && activity!=null) {
                activity.requestPermissions(PERMISSIONS_NEEDED, RQ_CODE_ASK_PERMISSION);
            } else {
                callback.onAllPermissionGranted();
            }

        } else {
            callback.onAllPermissionGranted();
        }

    }

    public interface Callback {
        void onAllPermissionGranted();
        void onPermissionDenial();
    }
}

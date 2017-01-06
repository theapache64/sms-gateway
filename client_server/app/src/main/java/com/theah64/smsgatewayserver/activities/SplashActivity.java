package com.theah64.smsgatewayserver.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.theah64.smsgatewayserver.BuildConfig;
import com.theah64.smsgatewayserver.R;
import com.theah64.smsgatewayserver.activities.base.PermissionActivity;
import com.theah64.smsgatewayserver.utils.PermissionUtils;
import com.theah64.smsgatewayserver.utils.PrefUtils;
import com.theah64.smsgatewayserver.utils.SingletonToast;


public class SplashActivity extends PermissionActivity implements PermissionUtils.Callback {

    private static final long SPLASH_DELAY = 1500;


    private static final String X = SplashActivity.class.getSimpleName();
    public static final String KEY_IS_ALL_PERMISSION_SET = "is_all_permission_set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ((TextView) findViewById(R.id.tvAppVersion)).setText(String.format("v%s", BuildConfig.VERSION_NAME));

        new PermissionUtils(this).begin();
    }


    private void doNormalSplashWork() {

        //Setting permission flag to true
        PrefUtils.getInstance(this).getEditor().putBoolean(KEY_IS_ALL_PERMISSION_SET, true).commit();

        //Checking if the api key exists
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();

            }
        }, SPLASH_DELAY);
    }

    @Override
    public void onAllPermissionGranted() {
        doNormalSplashWork();
    }

    @Override
    public void onPermissionDenial() {
        SingletonToast.makeText(SplashActivity.this, R.string.You_must_accept_all_the_permissions).show();
        finish();
    }
}

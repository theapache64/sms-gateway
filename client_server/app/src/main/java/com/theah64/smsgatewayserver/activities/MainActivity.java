package com.theah64.smsgatewayserver.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.theah64.smsgatewayserver.R;
import com.theah64.smsgatewayserver.utils.APIRequestGateway;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private boolean isProcessing = false;
    private FloatingActionButton fab;

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;

        if (isProcessing) {
            fab.hide();
        } else {
            fab.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView tvLog = (TextView) findViewById(R.id.tvLog);
        final ScrollView svMain = (ScrollView) findViewById(R.id.content_main);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isProcessing()) {
                    //Connect to the API
                    log("-------------------------");
                    log("Connecting...");
                    setProcessing(true);

                    new APIRequestGateway(MainActivity.this, new APIRequestGateway.APIRequestGatewayCallback() {
                        @Override
                        public void onReadyToRequest(String serverKey) {
                            log("Connected: " + serverKey);
                            log("-------------------------");
                            setProcessing(false);
                        }

                        @Override
                        public void onFailed(String reason) {
                            log("Connection failed: " + reason);
                            log("-------------------------");
                            setProcessing(false);
                        }
                    });


                } else {
                    //Connecting
                    //Connect to the api
                    log("Request on air...");
                }
            }

            private void log(String s) {
                svMain.scrollTo(0, 0);
                tvLog.setText(new Date().toString() + " : " + s + "\n" + tvLog.getText());
            }
        });
    }


}

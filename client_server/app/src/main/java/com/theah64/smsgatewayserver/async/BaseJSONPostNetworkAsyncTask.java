package com.theah64.smsgatewayserver.async;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by theapache64 on 12/9/16.
 */
abstract class BaseJSONPostNetworkAsyncTask<RESULT> extends AsyncTask<String, Void, RESULT> {

    private final Context context;
    private final String serverKey;

    BaseJSONPostNetworkAsyncTask(Context context, String serverKey) {
        this.context = context;
        this.serverKey = serverKey;
    }

    public Context getContext() {
        return context;
    }

    public String getServerKey() {
        return serverKey;
    }
}

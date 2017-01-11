package com.theah64.smsgatewayserver.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.theah64.smsgatewayserver.models.Server;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * All the auth needed API request must be passed through this gate way.
 * Created by theapache64 on 12/9/16.
 */
public class APIRequestGateway {

    public static final String KEY_SERVER_KEY = "server_key";

    private static final String X = APIRequestGateway.class.getSimpleName();
    private final Activity activity;
    private TelephonyManager tm;

    private static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        } else {
            return manufacturer.toUpperCase() + " " + model;
        }
    }


    public interface APIRequestGatewayCallback {
        void onReadyToRequest(final String serverKey) throws JSONException;

        void onFailed(final String reason);
    }

    private final Context context;
    @NonNull
    private final APIRequestGatewayCallback callback;

    private final boolean isFreshApiRequest;

    private APIRequestGateway(Context context, final Activity activity, @NonNull APIRequestGatewayCallback callback, boolean isFreshApiRequest) {
        this.context = context;
        this.activity = activity;
        this.callback = callback;
        this.isFreshApiRequest = isFreshApiRequest;
        execute();
    }

    public APIRequestGateway(final Activity activity, APIRequestGatewayCallback callback, boolean isFreshApiRequest) {
        this(activity.getBaseContext(), activity, callback, isFreshApiRequest);
    }

    public APIRequestGateway(Context context, APIRequestGatewayCallback callback, boolean isFreshApiRequest) {
        this(context, null, callback, isFreshApiRequest);
    }


    private void register(final Context context) {

        final ProfileUtils profileUtils = ProfileUtils.getInstance(context);

        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final PrefUtils prefUtils = PrefUtils.getInstance(context);

        String fcmId = FirebaseInstanceId.getInstance().getToken();

        if (fcmId == null) {
            Log.d(X, "Live token is null, collecting from pref");
            fcmId = prefUtils.getString(Server.KEY_FCM_ID);
        }

        final String simSerial = tm.getSimSerialNumber();

        if (simSerial != null) {
            //Attaching them with the request
            final Request inRequest = new APIRequestBuilder("/get_server_key", null)
                    .addParamIfNotNull("name", profileUtils.getDeviceOwnerName())
                    .addParam("imei", tm.getDeviceId())
                    .addParam("device_name", getDeviceName())
                    .addParamIfNotNull("email", profileUtils.getPrimaryEmail())
                    .addParam("fcm_id", fcmId)
                    .addParam("sim_serial", simSerial)
                    .build();

            //Doing API request
            OkHttpUtils.getInstance().getClient().newCall(inRequest).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, final IOException e) {
                    e.printStackTrace();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailed(e.getMessage());
                            }
                        });
                    } else {
                        callback.onFailed(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {

                        final APIResponse inResp = new APIResponse(OkHttpUtils.logAndGetStringBody(response));
                        final String serverKey = inResp.getJSONObjectData().getString(KEY_SERVER_KEY);


                        //Saving in preference
                        final SharedPreferences.Editor editor = prefUtils.getEditor();
                        editor.putString(KEY_SERVER_KEY, serverKey);
                        editor.putBoolean(Server.KEY_IS_FCM_SYNCED, true);
                        editor.commit();

                        if (activity != null) {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onReadyToRequest(serverKey);
                                }
                            });

                        } else {
                            callback.onReadyToRequest(serverKey);
                        }
                    } catch (JSONException | APIResponse.APIException e) {
                        e.printStackTrace();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailed(e.getMessage());
                                }
                            });
                        } else {
                            callback.onFailed(e.getMessage());
                        }
                    }
                }
            });
        } else {
            callback.onFailed("No SIM card found");
        }

    }

    private void execute() {

        Log.d(X, "Opening gateway...");

        if (NetworkUtils.hasNetwork(context)) {

            Log.i(X, "Has network");

            final PrefUtils prefUtils = PrefUtils.getInstance(context);
            final String serverKey = prefUtils.getString(KEY_SERVER_KEY);

            if (serverKey != null && !isFreshApiRequest) {

                Log.d(X, "hasServerKey " + serverKey);

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onReadyToRequest(serverKey);
                        }
                    });
                } else {
                    callback.onReadyToRequest(serverKey);
                }

            } else {

                Log.i(X, isFreshApiRequest ? "Refreshing server..." : " Registering server...");

                //Register victim here
                register(context);
            }

        } else {

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailed("No network!");
                    }
                });
            } else {
                callback.onFailed("No network!");
            }

            Log.e(X, "Doesn't have APIKEY and no network!");

        }
    }
}

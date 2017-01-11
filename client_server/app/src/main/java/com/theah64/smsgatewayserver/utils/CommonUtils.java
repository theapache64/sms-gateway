package com.theah64.smsgatewayserver.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

/**
 * Created by theapache64 on 14/12/16.
 */

public class CommonUtils {

    private static final String X = CommonUtils.class.getSimpleName();

    static boolean isSupport(final int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }


    public static String getMIMETypeFromUrl(final File file, final String defaultValue) {

        MimeTypeMap mime = MimeTypeMap.getSingleton();
        int index = file.getName().lastIndexOf('.') + 1;
        String ext = file.getName().substring(index).toLowerCase();
        final String mimeType = mime.getMimeTypeFromExtension(ext);

        if (mimeType != null) {
            return mimeType;
        }

        return defaultValue;
    }

    public static boolean isMyServiceRunning(final Context context, Class<?> serviceClass) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(X, "Service running : " + serviceClass.getName());
                return true;
            }
        }

        Log.e(X, "Service not running : " + serviceClass.getName());
        return false;
    }

    public static String getSanitizedName(final String fileName) {
        return fileName.replaceAll("[^\\w]", "_").replaceAll("[_]{2,}", "_");
    }

    public static String get(int count, @Nullable String start, String symbol, String seperator, @Nullable String end) {

        final StringBuilder sb = new StringBuilder();
        if (start != null) {
            sb.append(start);
        }

        for (int i = 0; i < count; i++) {
            sb.append(symbol);

            if ((i + 1) < count) {
                sb.append(seperator);
            }
        }

        if (end != null) {
            sb.append(end);
        }


        return sb.toString();
    }

    public static String[] toStringArray(JSONArray jaJsonArray) throws JSONException {
        final String[] arr = new String[jaJsonArray.length()];
        for (int i = 0; i < jaJsonArray.length(); i++) {
            arr[i] = jaJsonArray.getString(i);
        }
        return arr;
    }
}

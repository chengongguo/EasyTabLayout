package com.cgg.demo.config;

import android.util.Log;

public class PathParser {
    private static final String TAG = Config.TAG_PREFIX + PathParser.class.getSimpleName();

    public static String getFullPath(String path) {
        if (!path.startsWith("http")) {
            path = Config.BASE_URL_MY_WEBSITE + path;
        }
        Log.i(TAG, "getFullPath() path:" + path);
        return path;
    }
}

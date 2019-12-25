package com.cmlanche.core.utils;

import android.util.Log;


/**
 */
public class Logger {

    private static boolean IS_DEBUG = false;
    public static final String TAG = Utils.tag;

    public static void setDebug(boolean debug) {
        IS_DEBUG = debug;
    }

    public static boolean isDebug() {
        return IS_DEBUG;
    }


    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(Throwable e) {
        Log.e(TAG, Log.getStackTraceString(e));
    }

    public static void e(String message, Throwable e) {
        Log.e(TAG, message, e);
    }

    public static void d(String message) {
        if(IS_DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static void d(String message, Throwable e) {
        if(IS_DEBUG) {
            Log.i(TAG, message, e);
        }
    }
}

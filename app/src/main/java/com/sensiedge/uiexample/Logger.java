package com.sensiedge.uiexample;

import android.util.Log;

/**
 * Created by yuri on 15/11/16.
 */

public class Logger {

    public static void print(String strToPrint) {
        toDebug(strToPrint);
    }

    public static void toDebug(String strToPrint) {
        Log.d("applog", strToPrint);
    }

}

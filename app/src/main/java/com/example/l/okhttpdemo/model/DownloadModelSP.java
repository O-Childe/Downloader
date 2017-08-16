package com.example.l.okhttpdemo.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by L on 2017/8/16.
 */

public class DownloadModelSP {
    private static String DOWNLOAD_PREF = "DownloadData";

    private static String DOWNLOAD_PROGRESS = "progress";

    public static void saveDownloadData(Context context, long downBytes) {
        SharedPreferences sp = context.getSharedPreferences(DOWNLOAD_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putLong(DOWNLOAD_PROGRESS, downBytes);
        e.commit();
    }

    public static long getDownloadData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DOWNLOAD_PREF, Context.MODE_PRIVATE);

        return sp.getLong(DOWNLOAD_PROGRESS, 0L);
    }

}

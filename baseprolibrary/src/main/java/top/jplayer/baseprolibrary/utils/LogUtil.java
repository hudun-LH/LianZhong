package top.jplayer.baseprolibrary.utils;

import android.util.Log;

import com.google.gson.Gson;

import top.jplayer.baseprolibrary.BuildConfig;

/**
 * Created by Obl on 2018/1/13.
 * top.jplayer.baseprolibrary.utils
 */

public class LogUtil {
    public static void net(Object o) {
        if (BuildConfig.DEBUG) {
            if (o instanceof String) {
                Log.e("Obl-Net", o.toString());
                return;
            }
            Log.e("Obl-Net", new Gson().toJson(o));
        }
    }

    public static void e(Object o) {

        if (BuildConfig.DEBUG) {
            if (o instanceof String) {
                Log.e("Obl-Log", o.toString());
                return;
            }
            Log.e("Obl-Log", new Gson().toJson(o));
        }
    }
}

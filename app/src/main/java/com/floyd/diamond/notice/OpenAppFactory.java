package com.floyd.diamond.notice;

import android.content.Context;
import android.util.Log;

import com.floyd.diamond.notice.openapp.OpenNewTask;
import com.floyd.diamond.notice.openapp.OpenTaskProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by floyd on 16-1-10.
 */
public class OpenAppFactory {

    private static final String TAG = "OpenAppFactory";

    private static Map<Integer, OpenAppIntent> appIntentMaps = new HashMap<Integer, OpenAppIntent>();

    static {
        appIntentMaps.put(2001, new OpenTaskProcessor());
        appIntentMaps.put(1001, new OpenNewTask());
    }

    public static OpenAppFactory openAppFactory = new OpenAppFactory();

    public static OpenAppFactory getInstance() {
        return openAppFactory;
    }

    OpenAppIntent createOpenApp(Context context, OpenAppVO openAppVO) {
        int type = openAppVO.notifyType;
        OpenAppIntent openAppIntent = appIntentMaps.get(type);
        if (openAppIntent == null) {
            Log.d(TAG, "not found openAppIntent for type:" + type);
            return null;
        }

        openAppIntent.setOpenAppVO(context, openAppVO);
        return openAppIntent;
    }
}

package com.floyd.diamond;

import android.app.Application;

/**
 * Created by floyd on 15-12-2.
 */
public class IMChannel {

    public static Application mApplication;

    public static Application getApplication() {
        return mApplication;
    }

    public static void setApplication(Application application) {
        mApplication = application;
    }
}

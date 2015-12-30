package com.floyd.diamond.biz.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by floyd on 15-11-29.
 */
public class EnvConstants {

    public static final String imageRootPath = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/diamond/images";

    public static final String thumbRootPath = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/全名模特相册";

    static {
        File f = new File(imageRootPath);
        if (!f.exists()) {
            f.mkdir();
        }

        File th = new File(thumbRootPath);
        if (!th.exists()) {
            th.mkdir();
        }
    }
}

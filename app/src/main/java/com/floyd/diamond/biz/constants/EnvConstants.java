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

    static {
        File f = new File(imageRootPath);
        if (!f.exists()) {
            f.mkdir();
        }
    }
}

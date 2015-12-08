package com.floyd.diamond.biz.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.channel.request.HttpMethod;

import java.util.Map;

/**
 * Created by floyd on 15-11-29.
 */
public class BitmapDownloadFactory {

    public static AsyncJob<Bitmap> getImage(String url, Map<String, String> params, HttpMethod httpMethod) {
        return HttpJobFactory.createHttpJob(url, params, httpMethod).map(new Func<byte[], Bitmap>() {
            @Override
            public Bitmap call(byte[] bytes) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        });
    }
}

package com.floyd.diamond.bean;

import android.content.Context;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MyImageLoader extends ImageLoader {
    /**
     * Constructs a new ImageLoader.
     *
     * @param queue The RequestQueue to use for making image requests.
     */
    public MyImageLoader(RequestQueue queue, String url, View view, Context context) {
        super(queue, MyImageCache.getMyImageCache());
        this.get(url, new MyImageListener(view, context));
    }
}

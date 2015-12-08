package com.floyd.diamond.bean;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2015/12/5.
 */
public class MyImageListener implements ImageLoader.ImageListener {
    private View view;
    private Context context;
    private String path;

    public MyImageListener(View view, Context context) {
        this.view = view;
        path = Environment.getExternalStorageDirectory() + "/" + context.getPackageName();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }


        try {
            Bitmap bitmap = response.getBitmap();
            if (bitmap != null) {
                File imgFile = new File(file, MD5Util.encodeBy32BitMD5(response.getRequestUrl()) + ".jpg");
                FileOutputStream fos = new FileOutputStream(imgFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        if (view != null && view instanceof ImageView) {

//            Drawable drawable = Drawable.createFromPath(path + "/" + MD5Util.encodeBy32BitMD5(response.getRequestUrl()) + ".jpg");
            Bitmap bitmap = response.getBitmap();
            ((ImageView) view).setImageBitmap(bitmap);

        } else {
            Drawable drawable = Drawable.createFromPath(path + "/" + MD5Util.encodeBy32BitMD5(response.getRequestUrl()) + ".jpg");
            view.setBackground(drawable);
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

}

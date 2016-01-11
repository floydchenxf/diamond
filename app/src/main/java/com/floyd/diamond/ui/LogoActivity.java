package com.floyd.diamond.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.ui.activity.IndexGuideActivity;
import com.floyd.diamond.ui.activity.MainActivity;

public class LogoActivity extends Activity {

    private Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_logo);
        } catch (Throwable e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
            }
            setContentView(R.layout.activity_logo);
        }

        //FIXME init

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean hasGuided = LoginManager.hasGuided(LogoActivity.this);
                Intent it = new Intent();
                if (hasGuided) {
                    it.setClass(LogoActivity.this, MainActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                } else {
                    it.setClass(LogoActivity.this, IndexGuideActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                }
                LogoActivity.this.finish();
            }
        }, 1000);

    }

}

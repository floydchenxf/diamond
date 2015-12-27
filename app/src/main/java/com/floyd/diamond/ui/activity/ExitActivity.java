package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;

import com.floyd.diamond.R;

public class ExitActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        android.os.Process.killProcess(Process.myPid());
    }
}

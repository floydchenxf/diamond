package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/28.
 */
public class PersonModifyActivity extends Activity {
    private ImageView touxaing;
    private EditText nickname;
    private TextView gender,birthday,shenxing,work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personmodify);
    }
}

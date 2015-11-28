package com.floyd.diamond.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ModelPersonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelperson);

        init();
    }

    public void init(){
        TextView back= ((TextView) findViewById(R.id.back));
        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

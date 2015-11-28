package com.floyd.diamond.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/24.
 */
public class ChooseActivity extends Activity {
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseactivity);

        initView();
    }
    public void initView(){
        back= ((TextView) findViewById(R.id.left));
        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

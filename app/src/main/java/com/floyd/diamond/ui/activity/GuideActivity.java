package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/25.
 */
public class GuideActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caozuozhiyin);

        init();
    }

    public void init(){
        TextView back= ((TextView) findViewById(R.id.left));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

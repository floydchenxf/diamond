package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.floyd.diamond.R;

public class AboutUsActivity extends Activity implements View.OnClickListener {

    private TextView titleNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        findViewById(R.id.title_back).setOnClickListener(this);
        titleNameView = (TextView) findViewById(R.id.title_name);
        titleNameView.setVisibility(View.VISIBLE);
        titleNameView.setText("关于我们");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                this.finish();
                break;
        }

    }
}

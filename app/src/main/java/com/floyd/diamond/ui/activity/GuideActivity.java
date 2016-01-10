package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/25.
 */
public class GuideActivity extends Activity implements View.OnClickListener {

    private static final String MOTE_GUIDE_URL = "http://test.static.penghongwo.com/model_guidance.html";
    private static final String ABOUT_APP = "http://test.static.penghongwo.com/about_us.html";

    private View moteGuideLayout;
    private View aboutUsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caozuozhiyin);

        findViewById(R.id.left).setOnClickListener(this);
        init();
    }

    public void init(){
        moteGuideLayout = findViewById(R.id.mote_guide_layout);
        moteGuideLayout.setOnClickListener(this);

        aboutUsLayout = findViewById(R.id.aboutus);
        aboutUsLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                this.finish();
                break;
            case R.id.mote_guide_layout:
                Intent intent = new Intent(this, H5Activity.class);
                H5Activity.H5Data h5Data = new H5Activity.H5Data();
                h5Data.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                h5Data.data = MOTE_GUIDE_URL;
                h5Data.showProcess = true;
                h5Data.showNav = true;
                h5Data.title = "模特指引";
                intent.putExtra(H5Activity.H5Data.H5_DATA, h5Data);
                startActivity(intent);
                break;
            case R.id.aboutus:
                Intent aboutUsIntent = new Intent(this, H5Activity.class);
                H5Activity.H5Data aboutUsData = new H5Activity.H5Data();
                aboutUsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                aboutUsData.data = ABOUT_APP;
                aboutUsData.showProcess = true;
                aboutUsData.showNav = true;
                aboutUsData.title = "模特指引";
                aboutUsIntent.putExtra(H5Activity.H5Data.H5_DATA, aboutUsData);
                startActivity(aboutUsIntent);
                break;

        }

    }
}

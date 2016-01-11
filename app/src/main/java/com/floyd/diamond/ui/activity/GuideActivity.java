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

    private static final String MOTE_GUIDE_URL = "http://test.static.penghongwo.com/4.html";
    private static final String ABOUT_APP = "http://test.static.penghongwo.com/1.html";
    private static final String SELLER_GUIDE_URL = "http://test.static.penghongwo.com/7.html";
    private static final String RENZHEN_URL = "http://test.static.penghongwo.com/3.html";

    private View moteGuideLayout;
    private View aboutUsLayout;
    private View renzhengLayout;
    private View sellerGuideLayout;
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

        renzhengLayout = findViewById(R.id.renzheng_layout);
        renzhengLayout.setOnClickListener(this);

        sellerGuideLayout = findViewById(R.id.seller_guide_layout);
        sellerGuideLayout.setOnClickListener(this);
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
                aboutUsData.title = "关于App";
                aboutUsIntent.putExtra(H5Activity.H5Data.H5_DATA, aboutUsData);
                startActivity(aboutUsIntent);
                break;
            case R.id.renzheng_layout:
                Intent renzhengIntent = new Intent(this, H5Activity.class);
                H5Activity.H5Data renzhengData = new H5Activity.H5Data();
                renzhengData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                renzhengData.data = RENZHEN_URL;
                renzhengData.showProcess = true;
                renzhengData.showNav = true;
                renzhengData.title = "认证说明";
                renzhengIntent.putExtra(H5Activity.H5Data.H5_DATA, renzhengData);
                startActivity(renzhengIntent);
                break;
            case R.id.seller_guide_layout:
                Intent sellerGuideIntent = new Intent(this, H5Activity.class);
                H5Activity.H5Data sellerData = new H5Activity.H5Data();
                sellerData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                sellerData.data = SELLER_GUIDE_URL;
                sellerData.showProcess = true;
                sellerData.showNav = true;
                sellerData.title = "商家指引";
                sellerGuideIntent.putExtra(H5Activity.H5Data.H5_DATA, sellerData);
                startActivity(sellerGuideIntent);
                break;

        }

    }
}

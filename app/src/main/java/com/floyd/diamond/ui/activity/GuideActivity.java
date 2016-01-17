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

    private static final String STATIC_HOST = "http://test.static.penghongwo.com/";
    //联系我们
    private static final String CONTACT_URL = STATIC_HOST + "1.html";
    //关于我们
    private static final String ABOUT_US = STATIC_HOST + "2.html";
    //模特索引
    private static final String MOTE_GUIDE_URL = STATIC_HOST + "3.html";
    //商家索引
    private static final String SELLER_GUIDE_URL = STATIC_HOST + "4.html";
    //图标说明
    private static final String ICON_ULR = STATIC_HOST + "5.html";
    //新商家QA
    private static final String NEWSELLER_QA = STATIC_HOST + "6.html";
    //注册说明
    private static final String REG_URL = STATIC_HOST + "7.html";

    private View moteGuideLayout;
    private View sellerGuideLayout;
    private View sellerQaLayout;
    private View contactUsLayout;
    private View iconLayout;
    private View regLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caozuozhiyin);

        findViewById(R.id.left).setOnClickListener(this);
        init();
    }

    public void init() {
        moteGuideLayout = findViewById(R.id.mote_guide_layout);
        moteGuideLayout.setOnClickListener(this);
        sellerGuideLayout = findViewById(R.id.seller_guide_layout);
        sellerGuideLayout.setOnClickListener(this);
        sellerQaLayout = findViewById(R.id.seller_qa_layout);
        sellerQaLayout.setOnClickListener(this);
        contactUsLayout = findViewById(R.id.contact_us_layout);
        contactUsLayout.setOnClickListener(this);
        iconLayout = findViewById(R.id.icon_layout);
        iconLayout.setOnClickListener(this);
        regLayout = findViewById(R.id.reg_layout);
        regLayout.setOnClickListener(this);

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
            case R.id.seller_qa_layout:
                Intent newSellerQAIntent = new Intent(this, H5Activity.class);
                H5Activity.H5Data newSellerQAData = new H5Activity.H5Data();
                newSellerQAData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                newSellerQAData.data = NEWSELLER_QA;
                newSellerQAData.showProcess = true;
                newSellerQAData.showNav = true;
                newSellerQAData.title = "新商家QA";
                newSellerQAIntent.putExtra(H5Activity.H5Data.H5_DATA, newSellerQAData);
                startActivity(newSellerQAIntent);
                break;
            case R.id.contact_us_layout:
                Intent contactUsIntent = new Intent(this, H5Activity.class);
                H5Activity.H5Data contactUsData = new H5Activity.H5Data();
                contactUsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                contactUsData.data = CONTACT_URL;
                contactUsData.showProcess = true;
                contactUsData.showNav = true;
                contactUsData.title = "联系我们";
                contactUsIntent.putExtra(H5Activity.H5Data.H5_DATA, contactUsData);
                startActivity(contactUsIntent);
                break;
            case R.id.icon_layout:
                Intent iconIntent = new Intent(this, H5Activity.class);
                H5Activity.H5Data iconData = new H5Activity.H5Data();
                iconData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                iconData.data = ICON_ULR;
                iconData.showProcess = true;
                iconData.showNav = true;
                iconData.title = "图标解释";
                iconIntent.putExtra(H5Activity.H5Data.H5_DATA, iconData);
                startActivity(iconIntent);
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
            case R.id.reg_layout:
                Intent regIntent = new Intent(this, H5Activity.class);
                H5Activity.H5Data regData = new H5Activity.H5Data();
                regData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                regData.data = SELLER_GUIDE_URL;
                regData.showProcess = true;
                regData.showNav = true;
                regData.title = "注册协议";
                regIntent.putExtra(H5Activity.H5Data.H5_DATA, regData);
                startActivity(regIntent);
                break;

        }

    }
}

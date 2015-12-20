package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.LoginVO;

/**
 * Created by Administrator on 2015/11/28.
 */
public class SettingPersonInfoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SettingPersonInfoActivity";
    private TextView ziliao;
    private TextView phoneNum;
    private TextView message;
    private TextView clear;
    private TextView suggest;
    private TextView tuijian;
    private TextView aboutUs;
    private TextView noLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        //初始化设置
        init();
    }

    public void init() {
        ziliao = ((TextView) findViewById(R.id.ziliao));//个人资料
        phoneNum = ((TextView) findViewById(R.id.phoneNum));//手机号
        message = ((TextView) findViewById(R.id.message));//消息通知
        clear = ((TextView) findViewById(R.id.clear));//清除缓存
        suggest = ((TextView) findViewById(R.id.suggest));//意见反馈
        tuijian = ((TextView) findViewById(R.id.tuijian));//推荐给朋友
        aboutUs = ((TextView) findViewById(R.id.aboutus));//关于我们
        noLogin = ((TextView) findViewById(R.id.noLogin));//退出登录
        ziliao.setOnClickListener(this);
        noLogin.setOnClickListener(this);
        this.findViewById(R.id.left).setOnClickListener(this);

        LoginVO vo = LoginManager.getLoginInfo(this);
        //Log.i(TAG, vo.toString());
        phoneNum.setText(vo.user.phoneNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ziliao:
                Intent ziliaoIntent = new Intent(this, PersonInfoActivity.class);
                ziliaoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ziliaoIntent);
                break;
            case R.id.noLogin:
                PrefsTools.setStringPrefs(this, LoginManager.LOGIN_INFO, "");
                break;
            case R.id.left:
                this.finish();
                break;
        }

    }
}

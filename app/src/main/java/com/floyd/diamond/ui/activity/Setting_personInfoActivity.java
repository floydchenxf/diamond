package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/28.
 */
public class Setting_personInfoActivity extends Activity {
    private TextView ziliao,phoneNum,message,clear,suggest,tuijian,aboutUs,noLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);

        //初始化设置
        init();
    }

    public void init(){
        ziliao= ((TextView) findViewById(R.id.ziliao));//个人资料
        phoneNum= ((TextView) findViewById(R.id.phoneNum));//手机号
        message= ((TextView) findViewById(R.id.message));//消息通知
        clear= ((TextView) findViewById(R.id.clear));//清除缓存
        suggest= ((TextView) findViewById(R.id.suggest));//意见反馈
        tuijian= ((TextView) findViewById(R.id.tuijian));//推荐给朋友
        aboutUs= ((TextView) findViewById(R.id.aboutus));//关于我们
        noLogin= ((TextView) findViewById(R.id.noLogin));//退出登录

        //跳转到个人资料设置界面
        ziliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting_personInfoActivity.this,PersonInfoActivity.class));
            }
        });

        //跳转到意见反馈界面
        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting_personInfoActivity.this,SuggestActivity.class));
            }
        });
    }
}

package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ModelInfoActivity extends Activity{
    private TextView back,moreInfo;
    private CheckBox careCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelinfo_layout);

        initdata();

        setData();
    }

    public void initdata(){
        moreInfo= ((TextView) findViewById(R.id.moreInfo));//头像
        back= ((TextView) findViewById(R.id.back));//返回箭头
        careCount= ((CheckBox) findViewById(R.id.careCount));//关注次数
        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击跳转到模特的详情界面
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModelInfoActivity.this, ModelPersonActivity.class));
            }
        });

    }

    public void setData(){
        String likecount=getIntent().getStringExtra("likeCount");//获取上个界面传过来的关注次数
        careCount.setText("关注度"+192);
    }
}

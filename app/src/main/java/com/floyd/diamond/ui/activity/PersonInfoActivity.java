package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.floyd.diamond.R;

/**
 * Created by Administrator on 2015/11/28.
 */
public class PersonInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
    }

    //跳转到个人资料修改界面
    public void click(View view){
        startActivity(new Intent(this,PersonModifyActivity.class));
    }
}

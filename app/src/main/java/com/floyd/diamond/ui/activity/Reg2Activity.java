package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.LoginManager;

public class Reg2Activity extends Activity implements View.OnClickListener {

    private TextView backView;
    private EditText checkCodeView;
    private TextView checkCodeButtonView;
    private EditText userNickView;
    private EditText passwordView;
    private TextView regButton;
    private EditText phoneNumView;
    private int time = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String phoneNumber;
    private int checkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg2);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        checkType = getIntent().getIntExtra("checkType", 0);

        backView = (TextView) findViewById(R.id.back);
        checkCodeView = (EditText) findViewById(R.id.check_code);
        checkCodeButtonView = (TextView) findViewById(R.id.check_code_button);
        userNickView = (EditText) findViewById(R.id.user_nick);
        passwordView = (EditText) findViewById(R.id.password);
        regButton = (TextView) findViewById(R.id.next_step);
        backView.setOnClickListener(this);
        regButton.setOnClickListener(this);
        checkCodeButtonView.setOnClickListener(this);
        regButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.check_code_button:
                checkCodeButtonView.setEnabled(false);
                LoginManager.fetchVerifyCodeJob(phoneNumber).startUI(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(Reg2Activity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        checkCodeButtonView.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(Reg2Activity.this, s, Toast.LENGTH_SHORT).show();
                        checkCodeButtonView.setEnabled(false);
                        checkCodeButtonView.setBackgroundResource(R.drawable.common_round_bg);
                        checkCodeButtonView.setText("60秒后重新获取");
                        time = 60;
                        while (time >= 0) {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (time <= 0) {
                                        checkCodeButtonView.setEnabled(true);
                                        checkCodeButtonView.setBackgroundResource(R.drawable.common_round_blue_bg);
                                        checkCodeButtonView.setText("获取验证码");
                                        return;
                                    }
                                    time--;
                                    checkCodeButtonView.setText(time + "秒后重新获取");

                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
            case R.id.next_step:
                //TODO 需要扩展
//                LoginManager.createLoginJob(phoneNumber,)
                break;
        }
    }
}

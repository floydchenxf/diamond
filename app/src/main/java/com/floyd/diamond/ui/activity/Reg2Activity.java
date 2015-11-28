package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.AccountType;
import com.floyd.diamond.biz.ApiResult;
import com.floyd.diamond.biz.LoginManager;

public class Reg2Activity extends Activity implements View.OnClickListener {

    private static final String TAG = "Reg2Activity";

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
        checkType = getIntent().getIntExtra("checkType", 1);

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


    private void doUpdateTime(final int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = time;
                if (k <= 0) {
                    checkCodeButtonView.setEnabled(true);
                    checkCodeButtonView.setBackgroundResource(R.drawable.common_round_blue_bg);
                    checkCodeButtonView.setText("获取验证码");
                    return;
                }
                k--;
                checkCodeButtonView.setText(k + "秒后重新获取");
                doUpdateTime(k);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.check_code_button:
                checkCodeButtonView.setEnabled(false);
                LoginManager.fetchVerifyCodeJob(phoneNumber).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(Reg2Activity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        checkCodeButtonView.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Boolean s) {
                        if (s) {
                            checkCodeButtonView.setEnabled(false);
                            checkCodeButtonView.setBackgroundResource(R.drawable.common_round_bg);
                            checkCodeButtonView.setText("60秒后重新获取");
                            doUpdateTime(60);
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
            case R.id.next_step:
                AccountType type = AccountType.COMMON;
                if (checkType == 1) {
                    type = AccountType.COMMON;
                } else if (checkType == 2) {
                    type = AccountType.SELLER;
                }

                String usernick = userNickView.getText().toString();
                if (TextUtils.isEmpty(usernick)) {
                    Toast.makeText(this, "请输入别名", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password= passwordView.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                String smsCode = checkCodeView.getText().toString();
                if (TextUtils.isEmpty(smsCode)) {
                    Toast.makeText(this, "请输入校验码", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginManager.regUserJob(phoneNumber, usernick, password, type, smsCode).startUI(new ApiCallback<ApiResult<Boolean>>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(Reg2Activity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "--------"+code+"----"+errorInfo);
                    }

                    @Override
                    public void onSuccess(ApiResult<Boolean> s) {
                        Log.e(TAG, "--------"+s);
                        if (s.isSuccess()) {
                            Toast.makeText(Reg2Activity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Reg2Activity.this, s.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
        }
    }
}

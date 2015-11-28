package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.LoginManager;

public class ResetPasswordActivity extends Activity implements View.OnClickListener {

    private TextView backView;
    private EditText phoneNumView;
    private EditText checkCodeView;
    private TextView checkCodeButtonView;
    private EditText newPasswordView;
    private TextView finishButtonView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        backView = (TextView) findViewById(R.id.back);
        phoneNumView = (EditText) findViewById(R.id.user_name);
        checkCodeView = (EditText) findViewById(R.id.check_code);
        checkCodeButtonView = (TextView) findViewById(R.id.check_code_button);
        newPasswordView = (EditText) findViewById(R.id.new_password);
        finishButtonView = (TextView) findViewById(R.id.finish);

        backView.setOnClickListener(this);
        phoneNumView.setOnClickListener(this);
        checkCodeView.setOnClickListener(this);
        checkCodeButtonView.setOnClickListener(this);
        newPasswordView.setOnClickListener(this);
        finishButtonView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_code_button:
                checkCodeButtonView.setEnabled(false);
                String phoneNumber = phoneNumView.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    checkCodeButtonView.setEnabled(true);
                    return;
                }

                LoginManager.fetchVerifyCodeJob(phoneNumber).startUI(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(ResetPasswordActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        checkCodeButtonView.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(ResetPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                        checkCodeButtonView.setEnabled(false);
                        checkCodeButtonView.setBackgroundResource(R.drawable.common_round_bg);
                        checkCodeButtonView.setText("60秒后重新获取");
                        time = 60;
                        while(time >= 0) {
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
            case R.id.finish:
                final String pn1 = phoneNumView.getText().toString();
                if (TextUtils.isEmpty(pn1)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String smsCode = checkCodeView.getText().toString();
                if (TextUtils.isEmpty(smsCode)) {
                    Toast.makeText(this, "请输入短信验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String newPwd = newPasswordView.getText().toString();
                if (TextUtils.isEmpty(newPwd)) {
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginManager.fetchResetPwdJob(pn1, newPwd, smsCode).startUI(new ApiCallback<String>() {
                    @Override
                    public void onError(int code, final String errorInfo) {
                        Toast.makeText(ResetPasswordActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(ResetPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });


                break;
            case R.id.back:
                this.finish();
                break;
            default:
        }

    }
}

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
import com.floyd.diamond.biz.manager.LoginManager;

public class SetReturnItemMobileActivity extends Activity implements View.OnClickListener {

    private EditText phoneNumView;
    private EditText checkCodeView;
    private TextView checkCodeButtonView;
    private TextView finishButtonView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_return_item_mobile);
        findViewById(R.id.title_back).setOnClickListener(this);
        phoneNumView = (EditText) findViewById(R.id.user_name);
        checkCodeView = (EditText) findViewById(R.id.check_code);
        checkCodeButtonView = (TextView) findViewById(R.id.check_code_button);
        finishButtonView = (TextView) findViewById(R.id.finish);

        phoneNumView.setOnClickListener(this);
        checkCodeView.setOnClickListener(this);
        checkCodeButtonView.setOnClickListener(this);
        finishButtonView.setOnClickListener(this);
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
            case R.id.check_code_button:
                checkCodeButtonView.setEnabled(false);
                String phoneNumber = phoneNumView.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    checkCodeButtonView.setEnabled(true);
                    return;
                }

                LoginManager.fetchVerifyCodeJob(phoneNumber).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(SetReturnItemMobileActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        checkCodeButtonView.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Boolean s) {
                        Toast.makeText(SetReturnItemMobileActivity.this, s + "", Toast.LENGTH_SHORT).show();
                        checkCodeButtonView.setEnabled(false);
                        checkCodeButtonView.setBackgroundResource(R.drawable.common_round_bg);
                        checkCodeButtonView.setText("60秒后重新获取");
                        doUpdateTime(60);
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

//                LoginManager.fetchResetPwdJob(pn1, newPwd, smsCode).startUI(new ApiCallback<Boolean>() {
//                    @Override
//                    public void onError(int code, final String errorInfo) {
//                        Toast.makeText(ResetPasswordActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSuccess(Boolean s) {
//                        Toast.makeText(ResetPasswordActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onProgress(int progress) {
//
//                    }
//                });


                break;
            case R.id.title_back:
                this.finish();
                break;
            default:
        }

    }

}

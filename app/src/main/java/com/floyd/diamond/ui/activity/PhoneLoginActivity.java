package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.ApiResult;
import com.floyd.diamond.biz.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;

public class PhoneLoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "PhoneLoginActivity";

    private EditText userNameView;
    private EditText passwordView;
    private TextView loginView;
    private TextView backView;
    private TextView regView;
    private TextView forgotPwdView;
    private TextView regTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        backView = (TextView) findViewById(R.id.back);
        regView = (TextView) findViewById(R.id.regButton);
        userNameView = (EditText) findViewById(R.id.user_name);
        passwordView = (EditText) findViewById(R.id.password);
        forgotPwdView = (TextView) findViewById(R.id.forgot_pwd);
        regTextView = (TextView) findViewById(R.id.reg_text);
        backView.setOnClickListener(this);
        regView.setOnClickListener(this);
        loginView = (TextView) findViewById(R.id.login);
        loginView.setOnClickListener(this);
        forgotPwdView.setOnClickListener(this);
        regTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String userName = userNameView.getText().toString();
                String password = passwordView.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(PhoneLoginActivity.this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginManager.createLoginJob(userName, password, 1).startUI(new ApiCallback<ApiResult<LoginVO>>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(PhoneLoginActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "----" + errorInfo);
                    }

                    @Override
                    public void onSuccess(ApiResult<LoginVO> result) {
                        if (result.isSuccess()) {
                            Toast.makeText(PhoneLoginActivity.this, result.msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PhoneLoginActivity.this, result.msg, Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "----" + result);
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });

                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.regButton:
            case R.id.reg_text:
                Intent it = new Intent(PhoneLoginActivity.this, RegActivity.class);
                startActivity(it);
                this.finish();
                break;
            case R.id.forgot_pwd:
                Intent resetIntent = new Intent(PhoneLoginActivity.this, ResetPasswordActivity.class);
                startActivity(resetIntent);
                this.finish();
                break;
            default:
        }

    }
}

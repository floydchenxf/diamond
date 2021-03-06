package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.event.LoginEvent;

import de.greenrobot.event.EventBus;

public class PhoneLoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "PhoneLoginActivity";

    private EditText userNameView;
    private EditText passwordView;
    private TextView loginView;
    private LinearLayout backView;
    private TextView regView;
    private TextView forgotPwdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        backView = (LinearLayout) findViewById(R.id.back);
        regView = (TextView) findViewById(R.id.regButton);
        userNameView = (EditText) findViewById(R.id.user_name);
        passwordView = (EditText) findViewById(R.id.password);
        forgotPwdView = (TextView) findViewById(R.id.forgot_pwd);
        backView.setOnClickListener(this);
        regView.setOnClickListener(this);
        loginView = (TextView) findViewById(R.id.login);
        loginView.setOnClickListener(this);
        forgotPwdView.setOnClickListener(this);
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

                LoginManager.createLoginJob(this, userName, password, 1, LoginManager.getDeviceId(PhoneLoginActivity.this)).startUI(new ApiCallback<LoginVO>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(PhoneLoginActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "----" + errorInfo);
                    }

                    @Override
                    public void onSuccess(LoginVO result) {
                        Toast.makeText(PhoneLoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                        LoginEvent loginEvent = new LoginEvent();
                        loginEvent.id = result.id;
                        loginEvent.usernick = result.user.nickname;
                        loginEvent.token = result.token;
                        EventBus.getDefault().post(loginEvent);
                        finish();
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

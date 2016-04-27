package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;

/**
 * Created by hy on 2016/4/13.
 */
public class ModifyActivity extends Activity implements View.OnClickListener {
    private EditText oldPass,checkCode;
    private TextView checkCodeBtn,modify_next,back,oldPhone;
    private String phoneNumber;
    private LoginVO loginVO;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        init();
    }

    public void init(){
        loginVO = LoginManager.getLoginInfo(this);

        oldPass= ((EditText) findViewById(R.id.oldPass));
        checkCode= ((EditText) findViewById(R.id.check_code));
        checkCodeBtn= ((TextView) findViewById(R.id.check_code_button));
        modify_next= ((TextView) findViewById(R.id.modify_next));
        back= ((TextView) findViewById(R.id.back));
        oldPhone= ((TextView) findViewById(R.id.oldPhone));

        phoneNumber =getIntent().getStringExtra("phoneNum");
        if (loginVO.isModel()){
            String phoneNumText=phoneNumber.trim().replace(loginVO.user.phoneNumber.substring(3,7),"****");
            oldPhone.setText(phoneNumText);
        }else {
            String phoneNumText=phoneNumber.trim().replace(loginVO.user.phoneNumber.substring(3,7),"****");
            oldPhone.setText(phoneNumText);
        }


        back.setOnClickListener(this);
        checkCodeBtn.setOnClickListener(this);
        modify_next.setOnClickListener(this);

        checkCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    if (!oldPass.getText().toString().isEmpty()) {
                        modify_next.setBackgroundResource(R.drawable.common_round_blue_bg);
                    } else {
                        modify_next.setBackgroundResource(R.drawable.gray_button_bg_nor);
                    }
                } else {
                    modify_next.setBackgroundResource(R.drawable.gray_button_bg_nor);
                }
            }
        });
        oldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    if (!checkCode.getText().toString().isEmpty()) {
                        modify_next.setBackgroundResource(R.drawable.common_round_blue_bg);
                    } else {
                        modify_next.setBackgroundResource(R.drawable.gray_button_bg_nor);
                    }
                } else {
                    modify_next.setBackgroundResource(R.drawable.gray_button_bg_nor);
                }
            }
        });

        checkCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.check_code_button:
                checkCodeBtn.setEnabled(false);
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(this, "未获取到手机号", Toast.LENGTH_SHORT).show();
                    checkCodeBtn.setEnabled(true);
                    return;
                }

                LoginManager.fetchVerifyCodeJob(phoneNumber).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(ModifyActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        checkCodeBtn.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Boolean s) {
//                        Toast.makeText(ModifyActivity.this, s+"", Toast.LENGTH_SHORT).show();
                        checkCodeBtn.setEnabled(false);
                        checkCodeBtn.setBackgroundResource(R.drawable.common_round_bg);
                        checkCodeBtn.setText("60秒后重新获取");
                        doUpdateTime(60);
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
            case R.id.modify_next:
                String oldPassword=oldPass.getText().toString();
                String checkCodeNum=checkCode.getText().toString();
                if (oldPassword.isEmpty()){
                    Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                }else if (checkCodeNum.isEmpty()){
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(ModifyActivity.this,ModifyActivity2.class);
                    intent.putExtra("oldNum",phoneNumber);
                    intent.putExtra("oldPass",oldPassword);
                    intent.putExtra("oldCode",checkCodeNum);
                    startActivity(intent);
                }
                break;
        }
    }
    private void doUpdateTime(final int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = time;
                if (k <= 0) {
                    checkCodeBtn.setEnabled(true);
                    checkCodeBtn.setBackgroundResource(R.drawable.common_round_blue_bg);
                    checkCodeBtn.setText("获取验证码");
                    return;
                }
                k--;
                checkCodeBtn.setText(k + "秒后重新获取");
                doUpdateTime(k);
            }
        }, 1000);
    }

}

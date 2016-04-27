package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.ModifyBean;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.APIError;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.channel.request.HttpMethod;
import com.floyd.diamond.ui.view.UIAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hy on 2016/4/13.
 */
public class ModifyActivity2 extends Activity implements View.OnClickListener {
    private TextView back,confirm,checkCodeBtn;
    private EditText newPhonenum,newPass,newCheckcode;
    private String newPhone;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify2);

        init();
    }

    public void init(){
        back= ((TextView) findViewById(R.id.back));
        confirm= ((TextView) findViewById(R.id.confirm));
        checkCodeBtn= ((TextView) findViewById(R.id.check_code_button));
        newPhonenum= ((EditText) findViewById(R.id.newphoneNum));
        newPass= ((EditText) findViewById(R.id.newPass));
        newCheckcode= ((EditText) findViewById(R.id.new_check_code));

        back.setOnClickListener(this);
        checkCodeBtn.setOnClickListener(this);
        confirm.setOnClickListener(this);

        newPhonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    checkCodeBtn.setBackgroundResource(R.drawable.common_round_blue_bg);
                    if (!newPass.getText().toString().isEmpty()) {
                        if (!newCheckcode.getText().toString().isEmpty()) {
                            confirm.setBackgroundResource(R.drawable.common_round_blue_bg);
                        } else {
                            confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                        }
                    } else {
                        confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                    }
                } else {
                    checkCodeBtn.setBackgroundResource(R.drawable.gray_button_bg_nor);
                    confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                }

            }
        });

        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    if (!newPhonenum.getText().toString().isEmpty()) {
                        if (!newCheckcode.getText().toString().isEmpty()) {
                            confirm.setBackgroundResource(R.drawable.common_round_blue_bg);
                        } else {
                            confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                        }
                    } else {
                        confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                    }
                } else {
                    confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                }

            }
        });
        newCheckcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    if (!newPass.getText().toString().isEmpty()) {
                        if (!newPass.getText().toString().isEmpty()) {
                            confirm.setBackgroundResource(R.drawable.common_round_blue_bg);
                        } else {
                            confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                        }
                    } else {
                        confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                    }
                } else {
                    confirm.setBackgroundResource(R.drawable.gray_button_bg_nor);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.check_code_button:
                checkCodeBtn.setEnabled(false);
                newPhone=newPhonenum.getText().toString();
                if (TextUtils.isEmpty(newPhone)) {
                    Toast.makeText(this, "未获取到手机号", Toast.LENGTH_SHORT).show();
                    checkCodeBtn.setEnabled(true);
                    return;
                }

                LoginManager.fetchVerifyCodeJob(newPhone).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(ModifyActivity2.this, errorInfo, Toast.LENGTH_SHORT).show();
                        checkCodeBtn.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Boolean s) {
//                        Toast.makeText(ModifyActivity2.this, s+"", Toast.LENGTH_SHORT).show();
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
            case R.id.confirm:
                String newPassword=newPass.getText().toString();
                String newCodeNum=newCheckcode.getText().toString();
                newPhone=newPhonenum.getText().toString();
                if (newPhone.isEmpty()){
                    Toast.makeText(this, "请输入新账号", Toast.LENGTH_SHORT).show();
                }else if (newPassword.isEmpty()){
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                }else if (newCodeNum.isEmpty()){
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }else{

                    loadData(newPassword,newCodeNum);
                }
                break;

        }
    }


    public void loadData(String newPass,String newCode){
        String oldNum=getIntent().getStringExtra("oldNum");
        String oldPass=getIntent().getStringExtra("oldPass");
        String oldCode=getIntent().getStringExtra("oldCode");
        String token=LoginManager.getLoginInfo(this).token;
        LoginManager.modifyNum(oldNum,oldPass,oldCode,newPhone,newPass,newCode,token).startUI(new ApiCallback<Boolean>() {
            @Override
            public void onError(int code, String errorInfo) {
                Toast.makeText(ModifyActivity2.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                AlertDialog.Builder builder = new UIAlertDialog.Builder(ModifyActivity2.this);
                View view = LayoutInflater.from(ModifyActivity2.this).inflate(R.layout.tologin_dialog, null);
                view.findViewById(R.id.conifrm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ModifyActivity2.this,"密码错误，请重新登录",Toast.LENGTH_SHORT).show();
                        PrefsTools.setStringPrefs(ModifyActivity2.this, LoginManager.LOGIN_INFO, "");
                        PrefsTools.setStringPrefs(ModifyActivity2.this, SellerManager.SELLER_INFO, "");
                        PrefsTools.setStringPrefs(ModifyActivity2.this, MoteManager.MOTE_INFO, "");
                        Intent it = new Intent(ModifyActivity2.this, MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
                    }
                });
                builder.setView(view);
                builder.show();
            }

            @Override
            public void onProgress(int progress) {

            }
        });
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

package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.seller.SellerWalletRecordActivity;

public class AlipayActivity extends Activity implements View.OnClickListener {

    public static final String REMIND_FEE_KEY = "remind_fee_key";

    private Dialog dataLoadingDialog;

    private TextView alipayIdView;
    private TextView remindMoneyView;
    private EditText drawFeeView;
    private EditText drawPasswordView;
    private EditText smsCodeView;
    private TextView smsFetchView;
    private TextView drawButton;

    private TextView titleNameView;
    private TextView rightView;

    private LoginVO loginVO;
    private Handler mHandler = new Handler(Looper.getMainLooper());

//    private DataLoadingView dataLoadingView;

    private float remindFee;

    private void doUpdateTime(final int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = time;
                if (k <= 0) {
                    smsFetchView.setEnabled(true);
                    smsFetchView.setBackgroundResource(R.drawable.common_round_blue_bg);
                    smsFetchView.setText("获取验证码");
                    return;
                }
                k--;
                smsFetchView.setText(k + "秒后重新获取");
                doUpdateTime(k);
            }
        }, 1000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
//        dataLoadingView = new DefaultDataLoadingView();
//        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        loginVO = LoginManager.getLoginInfo(this);

        remindFee = getIntent().getFloatExtra(REMIND_FEE_KEY, 0f);

        findViewById(R.id.title_back).setOnClickListener(this);
        titleNameView = (TextView) findViewById(R.id.title_name);
        titleNameView.setText(R.string.draw_money);
        titleNameView.setVisibility(View.VISIBLE);

        rightView = (TextView) findViewById(R.id.right);
        if (loginVO.isModel()){
            rightView.setText("提现记录");
        }else{
            rightView.setText("账户明细");
        }

        rightView.setOnClickListener(this);
        rightView.setVisibility(View.VISIBLE);

        alipayIdView = (TextView) findViewById(R.id.alipay_id_view);
        remindMoneyView = (TextView) findViewById(R.id.remind_money_view);
        drawFeeView = (EditText) findViewById(R.id.draw_fee_view);
        drawPasswordView = (EditText) findViewById(R.id.draw_password_view);
        smsCodeView = (EditText) findViewById(R.id.sms_code_view);
        smsFetchView = (TextView) findViewById(R.id.sms_fetch_view);
        drawButton = (TextView) findViewById(R.id.draw_button);

        drawButton.setOnClickListener(this);
        smsFetchView.setOnClickListener(this);
        fillData();
    }

    private void fillData() {
        alipayIdView.setText("支付宝帐号:" + loginVO.user.alipayId);
        remindMoneyView.setText("余额:" + remindFee);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right:
                if (loginVO.isModel()) {
                    Intent payRecordIntent = new Intent(this, MoteWalletRecordsActivity.class);
                    startActivity(payRecordIntent);
                } else {
                    Intent sellerWalletRecordIntent = new Intent(this, SellerWalletRecordActivity.class);
                    startActivity(sellerWalletRecordIntent);
                }
                break;
            case R.id.title_back:
                this.finish();
                break;
            case R.id.sms_fetch_view:
                LoginManager.fetchVerifyCodeJob(loginVO.user.phoneNumber).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        Toast.makeText(AlipayActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        smsFetchView.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Boolean s) {
                        if (s) {
                            smsFetchView.setEnabled(false);
                            smsFetchView.setBackgroundResource(R.drawable.common_round_bg);
                            smsFetchView.setText("60秒后重新获取");
                            doUpdateTime(60);
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });
                break;
            case R.id.draw_button:
                String drawFee = drawFeeView.getText().toString();
                if (TextUtils.isEmpty(drawFee)) {
                    Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                String drawPassword = drawPasswordView.getText().toString();
                if (TextUtils.isEmpty(drawPassword)) {
                    Toast.makeText(this, "请输入提现密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                String smsCode = smsCodeView.getText().toString();
                if (TextUtils.isEmpty(smsCode)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataLoadingDialog.show();
                MoteManager.applyReduceCash(drawFee, smsCode, drawPassword, loginVO.token).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        if (!AlipayActivity.this.isFinishing()) {
                            dataLoadingDialog.dismiss();
                            Toast.makeText(AlipayActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (!AlipayActivity.this.isFinishing()) {
                            dataLoadingDialog.dismiss();
                            if (aBoolean) {
                                smsCodeView.setText("");
                                drawPasswordView.setText("");
                                drawFeeView.setText("");
                                Toast.makeText(AlipayActivity.this, "提现成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });

                break;
            case R.id.act_ls_fail_layout:
                fillData();
                break;
        }

    }
}

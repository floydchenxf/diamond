package com.floyd.diamond.ui.seller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.seller.SellerWalletVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.activity.AlipayActivity;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;

public class SellerWalletActivity extends Activity implements View.OnClickListener {

    private DataLoadingView dataLoadingView;
    private Dialog dataloadingDialog;
    private LoginVO loginVO;

    private TextView selfBuyMoneyTotalView;//自购总金额
    private TextView remindFeeView;//帐号余额
    private TextView unFinishMoneyView;//未完结任务金额
    private TextView selfBuyNumTotalView;//模特自购数量
    private TextView shotFeeTotalView;//佣金
    private TextView addCashTotalView;//累计充值金额
    private TextView addCashButton;
    private SellerWalletVO sellerWalletVO;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_wallet);
        dataloadingDialog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        loginVO = LoginManager.getLoginInfo(this);
        initView();
        loadData(true);
    }

    private void loadData(final boolean isFirst) {
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataloadingDialog.show();
        }

        SellerManager.getSellerWallet(loginVO.token).startUI(new ApiCallback<SellerWalletVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!SellerWalletActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataloadingDialog.dismiss();
                        Toast.makeText(SellerWalletActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onSuccess(SellerWalletVO sellerWalletVO) {
                if (!SellerWalletActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataloadingDialog.dismiss();
                    }

                    SellerWalletActivity.this.sellerWalletVO = sellerWalletVO;
                    remindFeeView.setText(sellerWalletVO.remindFee+"");
                    selfBuyMoneyTotalView.setText(sellerWalletVO.selfBuyMoneyTotal+"");
                    shotFeeTotalView.setText(sellerWalletVO.shotFeeTotal+"");
                    unFinishMoneyView.setText(sellerWalletVO.unFinishMoney+"");
                    selfBuyNumTotalView.setText(sellerWalletVO.selfBuyNumTotal+"");
                    addCashTotalView.setText(sellerWalletVO.addCashTotal+"");
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    private void initView() {
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView titleNameView = (TextView) findViewById(R.id.title_name);
        titleNameView.setText(R.string.wallet);
        titleNameView.setVisibility(View.VISIBLE);
        TextView rightView = (TextView) findViewById(R.id.right);
        rightView.setText(R.string.draw_money);
        rightView.setVisibility(View.VISIBLE);
        rightView.setOnClickListener(this);

        shotFeeTotalView = (TextView) findViewById(R.id.shot_fee_total_view);
        selfBuyMoneyTotalView = (TextView) findViewById(R.id.self_buy_money_total_view);
        remindFeeView = (TextView) findViewById(R.id.remind_fee_view);
        unFinishMoneyView = (TextView) findViewById(R.id.unfinish_money_view);
        selfBuyNumTotalView = (TextView) findViewById(R.id.self_buy_num_total_view);
        addCashTotalView = (TextView) findViewById(R.id.add_cash_total_view);
        addCashButton = (TextView) findViewById(R.id.add_cash_button);
        addCashButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                loadData(true);
                break;
            case R.id.right:
                Intent drawIntent = new Intent(this, AlipayActivity.class);
                drawIntent.putExtra(AlipayActivity.REMIND_FEE_KEY, sellerWalletVO.remindFee);
                startActivity(drawIntent);
                break;
        }

    }
}

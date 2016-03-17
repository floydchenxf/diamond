package com.floyd.diamond.ui.activity;

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
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteWalletVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;

/**
 * Created by floyd on 15-12-30.
 */
public class MoteWalletSummaryActivity extends Activity implements View.OnClickListener {

    private DataLoadingView dataLoadingView;
    private Dialog dataloadingDialog;
    private LoginVO loginVO;

    private TextView shotFeeView;
    private TextView selfBuyView;
    private TextView remindFeeView;
    private TextView totalReduceMoneyView;
    private TextView finishNumView;
    private TextView unFinishMoneyView;
    private TextView drawMoneyButton;

    private MoteWalletVO moteWalletVO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_summary);
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

        MoteManager.getMoteWallet(loginVO.token).startUI(new ApiCallback<MoteWalletVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!MoteWalletSummaryActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataloadingDialog.dismiss();
                        Toast.makeText(MoteWalletSummaryActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onSuccess(MoteWalletVO moteWalletVO) {
                if (!MoteWalletSummaryActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataloadingDialog.dismiss();
                    }

                    MoteWalletSummaryActivity.this.moteWalletVO = moteWalletVO;
                    shotFeeView.setText(moteWalletVO.shotFee+"");
                    selfBuyView.setText(moteWalletVO.totalSelfBuyFee+"");
                    remindFeeView.setText(moteWalletVO.remindFee+"");
                    totalReduceMoneyView.setText(moteWalletVO.totalReduceMoney+"");
                    finishNumView.setText(moteWalletVO.finishNum+"");
                    unFinishMoneyView.setText(moteWalletVO.unFinishMoney+"");
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

        shotFeeView = (TextView) findViewById(R.id.shot_fee_view);
        selfBuyView = (TextView) findViewById(R.id.self_bug_money_view);
        remindFeeView = (TextView) findViewById(R.id.remind_fee_view);
        totalReduceMoneyView = (TextView) findViewById(R.id.total_reduce_money_view);
        finishNumView = (TextView) findViewById(R.id.finish_num_view);
        unFinishMoneyView = (TextView) findViewById(R.id.unfinish_money_view);
        drawMoneyButton = (TextView) findViewById(R.id.draw_money_button);

        drawMoneyButton.setOnClickListener(this);
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
            case R.id.draw_money_button:
                Intent drawIntent = new Intent(this, AlipayActivity.class);
                drawIntent.putExtra(AlipayActivity.REMIND_FEE_KEY, moteWalletVO.remindFee);
                startActivity(drawIntent);
                finish();
                break;
        }

    }
}

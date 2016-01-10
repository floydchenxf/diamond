package com.floyd.diamond.ui.seller;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.seller.SellerWalletRecordVO;
import com.floyd.diamond.biz.vo.seller.SellerWalletSummaryVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.SellerWalletRecordAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.List;

public class SellerWalletRecordActivity extends Activity implements View.OnClickListener {

    private CheckedTextView allStatusView;
    private CheckedTextView publishTaskStatusView;
    private CheckedTextView selfBuyStatusView;
    private CheckedTextView drawMoneyStatusView;
    private CheckedTextView addCashStatusView;

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private Dialog dataLoadingDailog;
    private DataLoadingView dataLoadingView;
    private ImageLoader mImageLoader;

    private SellerWalletStatus walletStatus = SellerWalletStatus.ALL;
    private static int PAGE_SIZE = 10;
    private int pageNo = 1;
    private SellerWalletRecordAdapter adapter;
    private boolean isClear;

    private TextView emptyView;
    private TextView titlenameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_wallet_record);

        titlenameView = (TextView)findViewById(R.id.title_name);
        titlenameView.setText(R.string.pay_record);
        titlenameView.setVisibility(View.VISIBLE);

        dataLoadingDailog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        allStatusView = (CheckedTextView) findViewById(R.id.all_status);
        publishTaskStatusView = (CheckedTextView) findViewById(R.id.publish_task_status);
        selfBuyStatusView = (CheckedTextView) findViewById(R.id.self_buy_status);
        drawMoneyStatusView = (CheckedTextView) findViewById(R.id.draw_money_status);
        addCashStatusView = (CheckedTextView) findViewById(R.id.add_cash_status);

        allStatusView.setOnClickListener(this);
        publishTaskStatusView.setOnClickListener(this);
        selfBuyStatusView.setOnClickListener(this);
        drawMoneyStatusView.setOnClickListener(this);
        addCashStatusView.setOnClickListener(this);

        emptyView = (TextView) findViewById(R.id.empty_info);

        findViewById(R.id.title_back).setOnClickListener(this);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.seller_money_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                pageNo++;
                isClear = false;
                loadData(false);
                mPullToRefreshListView.onRefreshComplete(false, false);
            }

            @Override
            public void onPullUpToRefresh() {
                pageNo++;
                isClear = false;
                loadData(false);
                mPullToRefreshListView.onRefreshComplete(false, false);
            }
        });

        mListView = mPullToRefreshListView.getRefreshableView();
        adapter = new SellerWalletRecordAdapter(this);
        mListView.setAdapter(adapter);
        loadData(true);
    }

    private void loadData(final boolean isFirst) {
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDailog.show();
        }
        LoginVO loginVO = LoginManager.getLoginInfo(this);
        SellerManager.getSellerWalletRecord(walletStatus.code, pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<SellerWalletSummaryVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!SellerWalletRecordActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDailog.dismiss();
                    }
                    Toast.makeText(SellerWalletRecordActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(SellerWalletSummaryVO sellerWalletSummaryVO) {
                if (!SellerWalletRecordActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDailog.dismiss();
                    }
                    List<SellerWalletRecordVO> tasks = sellerWalletSummaryVO.dataList;
                    if ((tasks == null || tasks.isEmpty()) && pageNo == 1) {
                        emptyView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    } else {
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        adapter.addAll(tasks, isClear);
                    }
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_status:
                walletStatus = SellerWalletStatus.ALL;
                allStatusView.setChecked(true);
                publishTaskStatusView.setChecked(false);
                selfBuyStatusView.setChecked(false);
                drawMoneyStatusView.setChecked(false);
                addCashStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.publish_task_status:
                walletStatus = SellerWalletStatus.PUBLISH_TASK;
                allStatusView.setChecked(false);
                publishTaskStatusView.setChecked(true);
                selfBuyStatusView.setChecked(false);
                drawMoneyStatusView.setChecked(false);
                addCashStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.self_buy_status:
                walletStatus = SellerWalletStatus.SELF_BUY;
                allStatusView.setChecked(false);
                publishTaskStatusView.setChecked(false);
                selfBuyStatusView.setChecked(true);
                drawMoneyStatusView.setChecked(false);
                addCashStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.draw_money_status:
                walletStatus = SellerWalletStatus.DRAW_MONEY;
                allStatusView.setChecked(false);
                publishTaskStatusView.setChecked(false);
                selfBuyStatusView.setChecked(false);
                drawMoneyStatusView.setChecked(true);
                addCashStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.add_cash_status:
                walletStatus = SellerWalletStatus.ADD_CASH;
                allStatusView.setChecked(false);
                publishTaskStatusView.setChecked(false);
                selfBuyStatusView.setChecked(false);
                drawMoneyStatusView.setChecked(false);
                addCashStatusView.setChecked(true);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.title_back:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                loadData(true);
                break;
        }
    }
}

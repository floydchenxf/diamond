package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteWalletPageVO;
import com.floyd.diamond.biz.vo.mote.MoteWalletRecordVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.adapter.WalletRecordAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.List;

public class MoteWalletRecordsActivity extends Activity implements View.OnClickListener {

    private static final int PAGE_SIZE = 10;
    private DataLoadingView dataLoadingView;

    private Dialog dataloadingDialog;
    private int pageNo = 1;

    private LoginVO loginVO;

    private PullToRefreshListView walletPullRefreshListView;
    private ListView mListView;
    private TextView emptyView;
    private WalletRecordAdapter adapter;
    private boolean clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_wallet_records);
        dataloadingDialog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        loginVO = LoginManager.getLoginInfo(this);
        initView();
        loadData(true);
    }

    private void initView() {
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView titleNameView = (TextView) findViewById(R.id.title_name);
        titleNameView.setText("提现状态");
        titleNameView.setVisibility(View.VISIBLE);
        walletPullRefreshListView = (PullToRefreshListView) findViewById(R.id.wallet_list);
        walletPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        walletPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                walletPullRefreshListView.onRefreshComplete(false, false);
            }

            @Override
            public void onPullUpToRefresh() {
                pageNo++;
                clear = false;
                loadData(false);
                walletPullRefreshListView.onRefreshComplete(false, false);
            }
        });
        mListView = walletPullRefreshListView.getRefreshableView();
        emptyView = (TextView) findViewById(R.id.empty_info);
        adapter = new WalletRecordAdapter(this);
        mListView.setAdapter(adapter);
    }

    public void loadData(final boolean isFirst) {
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataloadingDialog.show();
        }

        MoteManager.fetchWalletRecords(pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<MoteWalletPageVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (isFirst) {
                    dataLoadingView.loadFail();
                } else {
                    dataloadingDialog.dismiss();
                    Toast.makeText(MoteWalletRecordsActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(MoteWalletPageVO moteWalletPageVO) {
                if (isFirst) {
                    dataLoadingView.loadSuccess();
                } else {
                    dataloadingDialog.dismiss();
                }
                List<MoteWalletRecordVO> walletRecords = moteWalletPageVO.dataList;
                adapter.addAll(walletRecords, clear);
                if (adapter.getWalletRecords() == null || adapter.getWalletRecords().isEmpty()) {
                    walletPullRefreshListView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    walletPullRefreshListView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
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
            case R.id.title_back:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                pageNo = 1;
                clear = true;
                loadData(true);
                break;
        }

    }
}

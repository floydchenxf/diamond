package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

public class MoteDetailActivity extends Activity implements View.OnClickListener {

    private NetworkImageView headBgView;
    private NetworkImageView headView;
    private TextView jinyanView;
    private TextView agreeView;
    private TextView moreInfoView;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private CheckedTextView guanzhuView;
    private TextView emptyView;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_detail);
        loadingDialog = new Dialog(this, R.style.data_load_dialog);
        headBgView = (NetworkImageView) findViewById(R.id.head_bg);
        headView = (NetworkImageView) findViewById(R.id.head);
        jinyanView = (TextView) findViewById(R.id.jinyan);
        agreeView = (TextView) findViewById(R.id.agree);
        guanzhuView = (CheckedTextView) findViewById(R.id.guanzhu);
        moreInfoView = (TextView) findViewById(R.id.more_info);
        emptyView = (TextView) findViewById(R.id.empty_info);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.detail_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {

            }

            @Override
            public void onPullUpToRefresh() {

            }
        });

        guanzhuView.setOnClickListener(this);
        moreInfoView.setOnClickListener(this);

        loadingDialog.show();
        loadData();
    }

    private void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_info:
                Intent it = new Intent(this, MoteMoreInfoActivity.class);
                startActivity(it);
                break;
            case R.id.guanzhu:
                if (!this.isFinishing()) {
                    loadingDialog.show();
                }
                doGuanzhu();
                break;
        }

    }

    private void doGuanzhu() {

    }
}

package com.floyd.diamond.ui.seller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.constants.SellerTaskStatus;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskItemVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.SellerTaskAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.List;

public class SellerTaskActivity extends Activity implements View.OnClickListener {

    private CheckedTextView allStatusView;
    private CheckedTextView doingStatusView;
    private CheckedTextView confirmStatusView;
    private CheckedTextView doneStatusView;

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private Dialog dataLoadingDailog;
    private DataLoadingView dataLoadingView;
    private ImageLoader mImageLoader;

    private SellerTaskStatus taskStatus = SellerTaskStatus.ALL;
    private static int PAGE_SIZE = 10;
    private int pageNo = 1;
    private SellerTaskAdapter adapter;
    private boolean isClear;

    private TextView emptyView;
    private float density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task);
        density = this.getResources().getDisplayMetrics().density;

        dataLoadingDailog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        allStatusView = (CheckedTextView) findViewById(R.id.all_status);
        doingStatusView = (CheckedTextView) findViewById(R.id.doing_status);
        confirmStatusView = (CheckedTextView) findViewById(R.id.confirm_status);
        doneStatusView = (CheckedTextView) findViewById(R.id.done_status);

        allStatusView.setOnClickListener(this);
        doingStatusView.setOnClickListener(this);
        confirmStatusView.setOnClickListener(this);
        doneStatusView.setOnClickListener(this);

        emptyView = (TextView) findViewById(R.id.empty_info);

        allStatusView.setOnClickListener(this);
        doingStatusView.setOnClickListener(this);
        confirmStatusView.setOnClickListener(this);
        doneStatusView.setOnClickListener(this);
        findViewById(R.id.title_back).setOnClickListener(this);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.my_task_list);
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
        adapter = new SellerTaskAdapter(this, mImageLoader, new SellerTaskAdapter.StatusCallback() {
            @Override
            public void doCallback(View v, int status) {

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SellerTaskItemVO itemVO = adapter.getData().get(position - 1);
                Intent it = new Intent(SellerTaskActivity.this, SellerTaskDetailActivity.class);
                it.putExtra(SellerTaskDetailActivity.TASK_ID_PARAM, itemVO.id);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });
        mListView.setAdapter(adapter);
        loadData(true);
    }

    private void loadData(final boolean isFirst) {
        if (!LoginManager.isLogin(this)) {
            return;
        }

        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDailog.show();
        }
        LoginVO loginVO = LoginManager.getLoginInfo(this);
        SellerManager.getSellerTaskList(taskStatus, pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<SellerTaskVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!SellerTaskActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDailog.dismiss();
                    }
                    Toast.makeText(SellerTaskActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(SellerTaskVO moteTaskVO) {
                if (!SellerTaskActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDailog.dismiss();
                    }
                    List<SellerTaskItemVO> tasks = moteTaskVO.dataList;
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
                taskStatus = SellerTaskStatus.ALL;
                allStatusView.setChecked(true);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.doing_status:
                taskStatus = SellerTaskStatus.DOING;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(true);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.confirm_status:
                taskStatus = SellerTaskStatus.CONFIRM;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(true);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.done_status:
                taskStatus = SellerTaskStatus.DONE;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(true);
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

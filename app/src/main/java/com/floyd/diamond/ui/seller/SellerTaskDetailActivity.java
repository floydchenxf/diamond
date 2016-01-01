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
import com.floyd.diamond.biz.constants.SellerTaskDetailStatus;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.seller.SellerTaskDetailVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.SellerTaskDetailAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.seller.process.SellerTaskProcessActivity;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.List;

/**
 * Created by floyd on 15-12-26.
 */
public class SellerTaskDetailActivity extends Activity implements View.OnClickListener {

    public static final String TASK_ID_PARAM = "TASK_ID_PARAM";
    private CheckedTextView allStatusView;
    private CheckedTextView doingStatusView;
    private CheckedTextView confirmStatusView;
    private CheckedTextView doneStatusView;

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private Dialog dataLoadingDailog;
    private DataLoadingView dataLoadingView;
    private ImageLoader mImageLoader;

    private SellerTaskDetailStatus taskStatus = SellerTaskDetailStatus.ALL;
    private static int PAGE_SIZE = 10;
    private int pageNo = 1;
    private SellerTaskDetailAdapter adapter;
    private boolean isClear;

    private long taskId;

    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_detail);
        taskId = getIntent().getLongExtra(TASK_ID_PARAM, 0l);

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
                loadData();
                mPullToRefreshListView.onRefreshComplete(false, false);
            }

            @Override
            public void onPullUpToRefresh() {
                pageNo++;
                isClear = false;
                loadData();
                mPullToRefreshListView.onRefreshComplete(false, false);
            }
        });

        mListView = mPullToRefreshListView.getRefreshableView();
        adapter = new SellerTaskDetailAdapter(this, mImageLoader);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SellerTaskDetailVO sellerTaskDetailVO = adapter.getData().get(position - 1);
                Intent it = new Intent(SellerTaskDetailActivity.this, SellerTaskProcessActivity.class);
                it.putExtra(SellerTaskProcessActivity.SELLER_MOTE_TASK_ID, sellerTaskDetailVO.id);
                startActivity(it);
            }
        });
        mListView.setAdapter(adapter);
        firstLoadData();
    }

    private void firstLoadData() {
        if (!LoginManager.isLogin(this)) {
            return;
        }

        dataLoadingView.startLoading();
        LoginVO loginVO = LoginManager.getLoginInfo(this);
        SellerManager.getSellerTaskDetailList(taskId, taskStatus, pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<List<SellerTaskDetailVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!SellerTaskDetailActivity.this.isFinishing()) {
                    dataLoadingView.loadFail();
                    Toast.makeText(SellerTaskDetailActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(List<SellerTaskDetailVO> sellerTaskVO) {
                if (!SellerTaskDetailActivity.this.isFinishing()) {
                    dataLoadingView.loadSuccess();
                    List<SellerTaskDetailVO> tasks = sellerTaskVO;
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


    private void loadData() {
        if (!LoginManager.isLogin(this)) {
            return;
        }

        dataLoadingDailog.show();
        LoginVO loginVO = LoginManager.getLoginInfo(this);
        SellerManager.getSellerTaskDetailList(taskId, taskStatus, pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<List<SellerTaskDetailVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!SellerTaskDetailActivity.this.isFinishing()) {
                    dataLoadingDailog.dismiss();
                    Toast.makeText(SellerTaskDetailActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(List<SellerTaskDetailVO> moteTaskVO) {
                if (!SellerTaskDetailActivity.this.isFinishing()) {
                    dataLoadingDailog.dismiss();
                    List<SellerTaskDetailVO> tasks = moteTaskVO;
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
                taskStatus = SellerTaskDetailStatus.ALL;
                allStatusView.setChecked(true);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData();
                break;
            case R.id.doing_status:
                taskStatus = SellerTaskDetailStatus.DOING;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(true);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData();
                break;
            case R.id.confirm_status:
                taskStatus = SellerTaskDetailStatus.CONFIRM;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(true);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData();
                break;
            case R.id.done_status:
                taskStatus = SellerTaskDetailStatus.DONE;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(true);
                pageNo = 1;
                isClear = true;
                dataLoadingDailog.show();
                loadData();
                break;
            case R.id.title_back:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                firstLoadData();
                break;
        }

    }
}

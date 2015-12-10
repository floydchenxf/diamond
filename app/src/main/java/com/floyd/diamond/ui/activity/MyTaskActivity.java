package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.constants.MoteTaskStatus;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteTaskVO;
import com.floyd.diamond.biz.vo.TaskItemVO;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.MyTaskAdapter;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.List;

public class MyTaskActivity extends Activity implements View.OnClickListener {

    private CheckedTextView allStatusView;
    private CheckedTextView doingStatusView;
    private CheckedTextView confirmStatusView;
    private CheckedTextView doneStatusView;

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private Dialog dataLoadingDailog;
    private ImageLoader mImageLoader;

    private MoteTaskStatus taskStatus = MoteTaskStatus.ALL;
    private static int PAGE_SIZE = 10;
    private int pageNo = 1;
    private MyTaskAdapter adapter;
    private boolean isClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);

        dataLoadingDailog = new Dialog(this, R.style.data_load_dialog);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        allStatusView = (CheckedTextView) findViewById(R.id.all_status);
        doingStatusView = (CheckedTextView) findViewById(R.id.doing_status);
        confirmStatusView = (CheckedTextView) findViewById(R.id.confirm_status);
        doneStatusView = (CheckedTextView) findViewById(R.id.done_status);

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
            }

            @Override
            public void onPullUpToRefresh() {
                pageNo++;
                isClear = false;
                loadData();
            }
        });

        mListView = mPullToRefreshListView.getRefreshableView();
        adapter = new MyTaskAdapter(this, mImageLoader);
        mListView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        if (!LoginManager.isLogin(this)) {
            return;
        }

        dataLoadingDailog.show();
        LoginVO loginVO = LoginManager.getLoginInfo(this);
        MoteManager.fetchMyTasks(taskStatus, pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<MoteTaskVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!MyTaskActivity.this.isFinishing()) {
                    dataLoadingDailog.dismiss();
                    Toast.makeText(MyTaskActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(MoteTaskVO moteTaskVO) {
                if (!MyTaskActivity.this.isFinishing()) {
                    dataLoadingDailog.dismiss();
                    List<TaskItemVO> tasks = moteTaskVO.dataList;
                    adapter.addAll(tasks, isClear);
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
                taskStatus = MoteTaskStatus.ALL;
                allStatusView.setChecked(true);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                break;
            case R.id.doing_status:
                taskStatus = MoteTaskStatus.DOING;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(true);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                break;
            case R.id.confirm_status:
                taskStatus = MoteTaskStatus.CONFIRM;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(true);
                doneStatusView.setChecked(false);
                pageNo = 1;
                isClear = true;
                break;
            case R.id.done_status:
                taskStatus = MoteTaskStatus.DONE;
                allStatusView.setChecked(false);
                doingStatusView.setChecked(false);
                confirmStatusView.setChecked(false);
                doneStatusView.setChecked(true);
                pageNo = 1;
                isClear = true;
                break;
            case R.id.title_back:
                this.finish();
                break;
        }

    }
}
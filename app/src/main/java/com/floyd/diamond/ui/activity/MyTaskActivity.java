package com.floyd.diamond.ui.activity;

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
import com.floyd.diamond.biz.constants.MoteTaskStatus;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteTaskVO;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.event.TaskEvent;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.MyTaskAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 点击我的任务模特端跳转的任务界面
 */

public class MyTaskActivity extends Activity implements View.OnClickListener {

    private CheckedTextView allStatusView;
    private CheckedTextView doingStatusView;
    private CheckedTextView confirmStatusView;
    private CheckedTextView doneStatusView;

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private Dialog dataLoadingDailog;
    private DataLoadingView dataLoadingView;
    private ImageLoader mImageLoader;

    private MoteTaskStatus taskStatus = MoteTaskStatus.ALL;
    private static int PAGE_SIZE = 10;
    private int pageNo = 1;
    private MyTaskAdapter adapter;
    private boolean isClear;

    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);

        EventBus.getDefault().register(this);
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
        adapter = new MyTaskAdapter(this, mImageLoader);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskItemVO itemVO = adapter.getData().get(position - 1);
                Intent it = new Intent(MyTaskActivity.this, NewTaskActivity.class);
                it.putExtra(NewTaskActivity.TASK_TYPE_ITEM_ID, itemVO.id);
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
        MoteManager.fetchMyTasks(taskStatus, pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<MoteTaskVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!MyTaskActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDailog.dismiss();
                    }
                    Toast.makeText(MyTaskActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(MoteTaskVO moteTaskVO) {
                if (!MyTaskActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDailog.dismiss();
                    }
                    List<TaskItemVO> tasks = moteTaskVO.dataList;
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

    @Subscribe
    public void onEventMainThread(TaskEvent event) {
        if (!isFinishing()) {
            adapter.updateStatus(event);
        }
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
                dataLoadingDailog.show();
                loadData(false);
                break;
            case R.id.doing_status:
                taskStatus = MoteTaskStatus.DOING;
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
                taskStatus = MoteTaskStatus.CONFIRM;
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
                taskStatus = MoteTaskStatus.DONE;
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

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

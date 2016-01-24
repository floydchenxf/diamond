package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.MoteTaskVO;
import com.floyd.diamond.biz.vo.mote.MoteTypeTaskVO;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.event.AcceptTaskEvent;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.MoteTaskTypeAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MoteTaskTypeActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ProductTypeActivity";
    private static final int PAGE_SIZE = 10;

    public static final int FEMALE_PRODUCT_TYPE = 1;
    public static final int MALE_PRODUCT_TYPE = 2;
    public static final int BABY_PRODUCT_TYPE = 3;
    public static final int MULTI_PRODUCT_TYPE = 4;
    public static final String PRODUCT_TYPE_KEY = "PRODUCT_TYPE_KEY";

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private TextView productTypeNameView;
    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;
    private ImageLoader mImageLoader;

    private  MoteTaskTypeAdapter moteTaskTypeAdapter;
    private TextView emptyView;

    private boolean isClear;
    private int moteTaskType = 1;
    private int pageNo = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);
        findViewById(R.id.title_back).setOnClickListener(this);
        EventBus.getDefault().register(this);

        this.mImageLoader = ImageLoaderFactory.createImageLoader();

        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.product_type_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
//                dataLoadingDialog.show();
//                pageNo++;
//                isClear = false;
//                loadData(false);
                mPullToRefreshListView.onRefreshComplete(false, true);
            }

            @Override
            public void onPullUpToRefresh() {
                dataLoadingDialog.show();
                pageNo++;
                isClear = false;
                loadData(false);
                mPullToRefreshListView.onRefreshComplete(false, true);
            }
        });
        productTypeNameView = (TextView) findViewById(R.id.product_type);
        moteTaskType = getIntent().getIntExtra("PRODUCT_TYPE_KEY", 1);
        if (moteTaskType == 1) {
            productTypeNameView.setText(R.string.female_type);
        } else if (moteTaskType == 2) {
            productTypeNameView.setText(R.string.male_type);
        } else if (moteTaskType == 3) {
            productTypeNameView.setText(R.string.baby_type);
        } else if (moteTaskType == 4) {
            productTypeNameView.setText(R.string.multi_type);
        }

        mListView = mPullToRefreshListView.getRefreshableView();
        emptyView = (TextView)findViewById(R.id.empty_info);
        moteTaskTypeAdapter = new MoteTaskTypeAdapter(this, mImageLoader, new MoteTaskTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, TaskItemVO taskItemVO) {
                Intent it = new Intent(MoteTaskTypeActivity.this, NewTaskActivity.class);
                it.putExtra(NewTaskActivity.TASK_TYPE_ITEM_ID, taskItemVO.id);
                MoteTaskTypeActivity.this.startActivity(it);
            }
        });

        mListView.setAdapter(moteTaskTypeAdapter);
        loadData(true);
    }

    private void loadData(final boolean isFirst) {
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        LoginVO loginVO = LoginManager.getLoginInfo(this);
        String token = loginVO == null?"":loginVO.token;
        MoteManager.fetchTaskList(moteTaskType, pageNo, PAGE_SIZE, token).startUI(new ApiCallback<MoteTaskVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!MoteTaskTypeActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadFail();
                    } else {
                        dataLoadingDialog.dismiss();
                        Toast.makeText(MoteTaskTypeActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onSuccess(MoteTaskVO moteTaskVO) {
                if (!MoteTaskTypeActivity.this.isFinishing()) {
                    if (isFirst) {
                        dataLoadingView.loadSuccess();
                    } else {
                        dataLoadingDialog.dismiss();
                    }
                }

                List<TaskItemVO> taskItemVOs = moteTaskVO.dataList;
                List<MoteTypeTaskVO> dataList = moteTaskTypeAdapter.getData();
                if ((taskItemVOs == null || taskItemVOs.isEmpty()) && pageNo == 1){
                    emptyView.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);

                } else {
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);

                    List<MoteTypeTaskVO> typeTasks = new ArrayList<MoteTypeTaskVO>();
                    int idx = 0;
                    for (TaskItemVO vo: taskItemVOs) {
                        if (idx % 2 == 0) {
                            MoteTypeTaskVO moteTypeTaskVO = new MoteTypeTaskVO();
                            moteTypeTaskVO.productItemVO1 = vo;
                            typeTasks.add(moteTypeTaskVO);
                        } else {
                            typeTasks.get(idx/2).productItemVO2 = vo;
                        }

                        idx++;
                    }

                    if (!typeTasks.isEmpty()) {
                        moteTaskTypeAdapter.addAll(typeTasks, isClear);
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
            case R.id.title_back:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                isClear = true;
                pageNo = 1;
                loadData(true);
                break;
        }

    }


    @Subscribe
    public void onEventMainThread(AcceptTaskEvent event) {
        Log.i(TAG, "-----------accept event fire");
        if (!MoteTaskTypeActivity.this.isFinishing()) {
            long taskId = event.taskId;
            moteTaskTypeAdapter.updateAcceptStatus(taskId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

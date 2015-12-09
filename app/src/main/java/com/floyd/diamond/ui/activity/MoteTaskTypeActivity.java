package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.IMChannel;
import com.floyd.diamond.IMImageCache;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteTaskVO;
import com.floyd.diamond.biz.vo.MoteTypeTaskVO;
import com.floyd.diamond.biz.vo.TaskItemVO;
import com.floyd.diamond.ui.adapter.MoteTaskTypeAdapter;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

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
    private Dialog loadingDialog;
    private ImageLoader mImageLoader;

    private  MoteTaskTypeAdapter moteTaskTypeAdapter;

    private int moteTaskType = 1;
    private int pageNo = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);
        findViewById(R.id.title_back).setOnClickListener(this);

        RequestQueue mQueue = Volley.newRequestQueue(this);
        IMImageCache wxImageCache = IMImageCache.findOrCreateCache(
                IMChannel.getApplication(), EnvConstants.imageRootPath);
        this.mImageLoader = new ImageLoader(mQueue, wxImageCache);
        mImageLoader.setBatchedResponseDelay(0);

        loadingDialog = new Dialog(this, R.style.data_load_dialog);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.product_type_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                loadingDialog.show();
                pageNo++;
                loadData();
                mPullToRefreshListView.onRefreshComplete(false, true);
            }

            @Override
            public void onPullUpToRefresh() {
                loadingDialog.show();
                pageNo++;
                loadData();
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
        moteTaskTypeAdapter = new MoteTaskTypeAdapter(this, mImageLoader, new MoteTaskTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, TaskItemVO taskItemVO) {
                Intent it = new Intent(MoteTaskTypeActivity.this, NewTaskActivity.class);
                it.putExtra(NewTaskActivity.TASK_TYPE_ITEM_OBJECT, taskItemVO);
                MoteTaskTypeActivity.this.startActivity(it);
            }
        });

        mListView.setAdapter(moteTaskTypeAdapter);

        loadingDialog.show();
        loadData();
    }

    private void loadData() {
        LoginVO loginVO = LoginManager.getLoginInfo(this);
        if (loginVO == null) {
            this.loadingDialog.dismiss();
            Toast.makeText(this, "未登录状态无法获取数据", Toast.LENGTH_SHORT).show();
            return;
        }
        MoteManager.fetchTaskList(moteTaskType, pageNo, PAGE_SIZE, loginVO.token).startUI(new ApiCallback<MoteTaskVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (!MoteTaskTypeActivity.this.isFinishing()) {
                    MoteTaskTypeActivity.this.loadingDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(MoteTaskVO moteTaskVO) {
                if (!MoteTaskTypeActivity.this.isFinishing()) {
                    MoteTaskTypeActivity.this.loadingDialog.dismiss();
                }

                List<TaskItemVO> taskItemVOs = moteTaskVO.dataList;
                if (taskItemVOs != null && !taskItemVOs.isEmpty()) {
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
                        moteTaskTypeAdapter.addAll(typeTasks, false);
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
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

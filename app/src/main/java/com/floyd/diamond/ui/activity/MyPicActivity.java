package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.TaskPicsVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.adapter.TaskPicAdapter;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.multiimage.MultiImageActivity;
import com.floyd.diamond.ui.multiimage.base.MulitImageVO;
import com.floyd.diamond.ui.multiimage.base.PicViewObject;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MyPicActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MyPicActivity";

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private TextView emptyView;
    private TextView titleNameView;
    private long moteId;
    private ImageLoader mImageLoader;
    private int pageNo = 1;
    private static int PAGE_SIZE = 10;

    private List<TaskPicsVO> taskPicsList;
    private TaskPicAdapter taskPicAdapter;

    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pic);
        this.mImageLoader = ImageLoaderFactory.createImageLoader();
        moteId = LoginManager.getLoginInfo(this).user.id;
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        emptyView = (TextView) findViewById(R.id.empty_info);
        titleNameView = (TextView) findViewById(R.id.title_name);
        titleNameView.setVisibility(View.VISIBLE);
        titleNameView.setText("我的图库");
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.detail_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                pageNo++;
                mPullToRefreshListView.onRefreshComplete(false, true);
                loadData();

            }

            @Override
            public void onPullUpToRefresh() {
                pageNo++;
                mPullToRefreshListView.onRefreshComplete(false, true);
                loadData();
            }
        });


        taskPicAdapter = new TaskPicAdapter(MyPicActivity.this, mImageLoader, new TaskPicAdapter.TaskPicItemClick() {
            @Override
            public void onItemClick(int position, View v) {
                TaskPicsVO taskPicsVO = taskPicsList.get(position);
                List<MoteTaskPicVO> pics = taskPicsVO.taskPics;
                List<PicViewObject> picViewList = new ArrayList<PicViewObject>();
                for (MoteTaskPicVO taskPic : pics) {
                    PicViewObject picViewObject = new PicViewObject();
                    picViewObject.setPicId(taskPic.id);
                    picViewObject.setPicPreViewUrl(taskPic.getPreviewImageUrl());
                    picViewObject.setPicUrl(taskPic.getDetailImageUrl());
                    picViewObject.setPicType(PicViewObject.IMAGE);
                    picViewList.add(picViewObject);
                }
                MulitImageVO mulitImageVO = new MulitImageVO(0, picViewList);
                Intent it = new Intent(MyPicActivity.this, MultiImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MultiImageActivity.MULIT_IMAGE_VO, mulitImageVO);
                it.putExtra(MultiImageActivity.MULIT_IMAGE_VO, bundle);
                it.putExtra(MultiImageActivity.MULIT_IMAGE_PICK_MODE,
                        MultiImageActivity.MULIT_IMAGE_PICK_MODE_PREVIEW);
                startActivity(it);
            }
        });
        mListView.setAdapter(taskPicAdapter);

        firstLoadData();
    }

    private void loadData(ApiCallback<List<TaskPicsVO>> callback) {
        LoginVO vo = LoginManager.getLoginInfo(this);
        if (vo.isModel()) {
            MoteManager.fetchMoteTaskPics(moteId, pageNo, PAGE_SIZE, vo.token).startUI(callback);
        } else {
            SellerManager.getSellerTaskPics(pageNo, PAGE_SIZE, vo.token).startUI(callback);
        }
    }

    private void loadData() {
        dataLoadingDialog.show();
        loadData(new ApiCallback<List<TaskPicsVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                dataLoadingDialog.dismiss();
                if (MyPicActivity.this.taskPicsList == null || MyPicActivity.this.taskPicsList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSuccess(List<TaskPicsVO> taskPicsVOs) {
                dataLoadingDialog.dismiss();
                taskPicAdapter.addAll(taskPicsVOs, false);
                MyPicActivity.this.taskPicsList = taskPicAdapter.getData();
                if (MyPicActivity.this.taskPicsList == null || MyPicActivity.this.taskPicsList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                }

                Log.i(TAG, "kkkk--------------map:" + taskPicsVOs);

            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }


    private void firstLoadData() {
        dataLoadingView.startLoading();
        loadData(new ApiCallback<List<TaskPicsVO>>() {
            @Override
            public void onError(int code, String errorInfo) {
                dataLoadingView.loadFail();
                if (MyPicActivity.this.taskPicsList == null || MyPicActivity.this.taskPicsList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSuccess(List<TaskPicsVO> taskPicsVOs) {
                dataLoadingView.loadSuccess();
                taskPicAdapter.addAll(taskPicsVOs, false);
                MyPicActivity.this.taskPicsList = taskPicAdapter.getData();
                if (MyPicActivity.this.taskPicsList == null || MyPicActivity.this.taskPicsList.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                }

                Log.i(TAG, "kkkk--------------map:" + taskPicsVOs);
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
                firstLoadData();
                break;
        }

    }
}

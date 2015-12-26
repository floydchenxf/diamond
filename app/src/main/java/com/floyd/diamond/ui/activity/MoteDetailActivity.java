package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteDetailInfoVO;
import com.floyd.diamond.biz.vo.mote.MoteTaskPicVO;
import com.floyd.diamond.biz.vo.mote.TaskPicsVO;
import com.floyd.diamond.event.LoginEvent;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MoteDetailActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MoteDetailActivity";

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
    private TextView backView;
    private long moteId;
    private ImageLoader mImageLoader;
    private TextView usernickView;
    private int pageNo = 1;
    private static int PAGE_SIZE = 10;

    private List<TaskPicsVO> taskPicsList;
    TaskPicAdapter taskPicAdapter;

    private DataLoadingView dataLoadingView;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean success = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_detail);
        this.mImageLoader = ImageLoaderFactory.createImageLoader();
        moteId = getIntent().getLongExtra("moteId", 0);
        loadingDialog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        headBgView = (NetworkImageView) findViewById(R.id.head_bg);
        headView = (NetworkImageView) findViewById(R.id.head);
        backView = (TextView) findViewById(R.id.back);
        jinyanView = (TextView) findViewById(R.id.jinyan);
        agreeView = (TextView) findViewById(R.id.agree);
        guanzhuView = (CheckedTextView) findViewById(R.id.guanzhu);
        moreInfoView = (TextView) findViewById(R.id.more_info);
        emptyView = (TextView) findViewById(R.id.empty_info);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.detail_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        usernickView = (TextView) findViewById(R.id.usernick);
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


        taskPicAdapter = new TaskPicAdapter(MoteDetailActivity.this, mImageLoader, new TaskPicAdapter.TaskPicItemClick() {
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
                Intent it = new Intent(MoteDetailActivity.this, MultiImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MultiImageActivity.MULIT_IMAGE_VO, mulitImageVO);
                it.putExtra(MultiImageActivity.MULIT_IMAGE_VO, bundle);
                it.putExtra(MultiImageActivity.MULIT_IMAGE_PICK_MODE,
                        MultiImageActivity.MULIT_IMAGE_PICK_MODE_PREVIEW);
                startActivity(it);
            }
        });
        mListView.setAdapter(taskPicAdapter);

        guanzhuView.setOnClickListener(this);
        moreInfoView.setOnClickListener(this);

        backView.setOnClickListener(this);

        EventBus.getDefault().register(this);
        loadData();
    }

    private void loadData() {
        success = true;
        dataLoadingView.startLoading();
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await(10000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            dataLoadingView.loadSuccess();
                        } else {
                            dataLoadingView.loadFail();
                        }
                    }
                });
            }
        }).start();


        if (!LoginManager.isLogin(this)) {
            countDownLatch.countDown();
            countDownLatch.countDown();
        } else {
            LoginVO vo = LoginManager.getLoginInfo(this);
            MoteManager.fetchMoteDetailInfo(moteId, vo.token).startUI(new ApiCallback<MoteDetailInfoVO>() {
                @Override
                public void onError(int code, String errorInfo) {
                    countDownLatch.countDown();
                    success = false;
                }

                @Override
                public void onSuccess(MoteDetailInfoVO vo) {
                    countDownLatch.countDown();
                    String imageUrl = vo.getPreviewImageUrl();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        headView.setDefaultImageResId(R.drawable.head);
                        headView.setImageUrl(imageUrl, mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.getCircleBitmap(bitmap, MoteDetailActivity.this.getResources().getDimension(R.dimen.cycle_head_image_size));
                            }
                        });
                        headBgView.setDefaultImageResId(R.drawable.head);
                        headBgView.setImageUrl(vo.getDetailImageUrl(), mImageLoader);
                    }

                    usernickView.setText(vo.nickname);

                    boolean isFollow = vo.isFollow;
                    if (isFollow) {
                        guanzhuView.setText("已关注");
                        guanzhuView.setChecked(true);
                    } else {
                        int num = vo.followNum;
                        guanzhuView.setText("关注度:" + num);
                        guanzhuView.setChecked(false);
                        guanzhuView.setOnClickListener(MoteDetailActivity.this);
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });
            MoteManager.fetchMoteTaskPics(moteId, pageNo, PAGE_SIZE, vo.token).startUI(new ApiCallback<List<TaskPicsVO>>() {
                @Override
                public void onError(int code, String errorInfo) {
                    countDownLatch.countDown();
                    success = false;
                    if (MoteDetailActivity.this.taskPicsList == null || MoteDetailActivity.this.taskPicsList.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onSuccess(List<TaskPicsVO> taskPicsVOs) {
                    countDownLatch.countDown();
                    taskPicAdapter.addAll(taskPicsVOs, false);
                    MoteDetailActivity.this.taskPicsList = taskPicAdapter.getData();
                    if (MoteDetailActivity.this.taskPicsList == null || MoteDetailActivity.this.taskPicsList.isEmpty()) {
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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_info:
                Intent it = new Intent(this, ModelPersonActivity.class);
                it.putExtra("moteId",moteId);
                startActivity(it);
                break;
            case R.id.guanzhu:
                doGuanzhu();
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.act_ls_fail_layout:
                loadData();
                break;
        }

    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
    }

    private void doGuanzhu() {
        if (LoginManager.isLogin(this)) {
            loadingDialog.show();
            LoginVO vo = LoginManager.getLoginInfo(this);
            MoteManager.addFollow(moteId, vo.token).startUI(new ApiCallback<Boolean>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MoteDetailActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    Toast.makeText(MoteDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                    guanzhuView.setText("已关注");
                    guanzhuView.setChecked(true);
                    guanzhuView.setOnClickListener(null);
                }

                @Override
                public void onProgress(int progress) {

                }
            });


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

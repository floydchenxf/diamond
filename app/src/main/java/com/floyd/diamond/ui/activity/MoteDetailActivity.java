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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.IMChannel;
import com.floyd.diamond.IMImageCache;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.constants.EnvConstants;
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
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

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
    private TextView share;

    private List<TaskPicsVO> taskPicsList;
    private TaskPicAdapter taskPicAdapter;

    private DataLoadingView dataLoadingView;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean success = true;

    private UMSocialService mShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_detail);

        // 得到UM的社会化分享组件
        mShare = UMServiceFactory.getUMSocialService("com.umeng.share");
        //设置分享内容
        setShareContent();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        IMImageCache wxImageCache = IMImageCache.findOrCreateCache(
                IMChannel.getApplication(), EnvConstants.imageRootPath);
        this.mImageLoader = new ImageLoader(mQueue, wxImageCache);
        mImageLoader.setBatchedResponseDelay(0);

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
        share= ((TextView) findViewById(R.id.share));
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configPlatform();
            }
        });
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh() {
                pageNo++;
                mPullToRefreshListView.onRefreshComplete(false, true);
                loadData(false);

            }

            @Override
            public void onPullUpToRefresh() {
                pageNo++;
                mPullToRefreshListView.onRefreshComplete(false, true);
                loadData(false);
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
        loadData(true);
    }

    // SSO授权回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mShare.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    // 设置分享内容的方法
    private void setShareContent() {
        // 分享字符串
        mShare.setShareContent("来自“全民模特”的分享");
        // 设置分享图片, 参数2为图片的url地址
        mShare.setShareMedia(new UMImage(MoteDetailActivity.this,
                "http://img4.duitang.com/uploads/item/201201/04/20120104223901_Cku8d.thumb.600_0.jpg"));
    }

    // 用来配置各个平台的SDKF
    private void configPlatform() {

        // 添加微信平台
        addWXPlatform();
        // 添加QQ平台
        addQQZonePlatform();

        // 设置分享面板上的分享平台
        mShare.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT
        );


        // 打开分享面板
        // mShare.openShare(this, false);//系统默认的
        startActivity(new Intent(MoteDetailActivity.this, DialogActivity.class));


    }

    // 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
    // image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
    // 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
    // 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
    private void addQQZonePlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MoteDetailActivity.this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://img4.duitang.com/uploads/item/201201/04/20120104223901_Cku8d.thumb.600_0.jpg");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                MoteDetailActivity.this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    // 添加微信分享平台
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        // String appId = "wx967daebe835fbeac";
        // String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
        String appId="wx6f4a5ebb3d2cd11e";
        String appSecret="9603f3903c1dab2b494de93c04c9026a";
//        String appId="wxd570a10aaf918fa7";
//        String appSecret="d4624c36b6795d1 d99dcf0547af5443d";

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(MoteDetailActivity.this, appId,
                appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(MoteDetailActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }



    private void loadData(final boolean isFirst) {
        success = true;
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            loadingDialog.show();
        }
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
                            if (isFirst) {
                                dataLoadingView.loadSuccess();
                            } else {
                                loadingDialog.dismiss();
                            }
                        } else {
                            if (isFirst) {
                                dataLoadingView.loadFail();
                            } else {
                                loadingDialog.dismiss();
                            }
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
                loadData(true);
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

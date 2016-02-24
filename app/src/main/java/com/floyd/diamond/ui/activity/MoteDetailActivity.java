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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.MoteDetail1;
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
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MoteDetailActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MoteDetailActivity";

    public static final String MOTE_ID = "moteId";

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
    private LinearLayout backView;
    private long moteId;
    private ImageLoader mImageLoader;
    private TextView usernickView;
    private int pageNo = 1;
    private static int PAGE_SIZE = 10;
    private LinearLayout share;

    private List<TaskPicsVO> taskPicsList;
    private TaskPicAdapter taskPicAdapter;

    private DataLoadingView dataLoadingView;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean success = true;

    private UMSocialService mShare;

    private TextView jingyanzhi, manyidu;

    private MoteDetailInfoVO infoVO;

    private boolean isFollow;//关注


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_detail);

        // 得到UM的社会化分享组件
        mShare = UMServiceFactory.getUMSocialService("com.umeng.share");
        //设置分享内容
        setShareContent();

        infoVO = new MoteDetailInfoVO();

        this.mImageLoader = ImageLoaderFactory.createImageLoader();
        moteId = getIntent().getLongExtra(MOTE_ID, 0);

        if (GlobalParams.isDebug){
            Log.e("moteId",moteId+"");
        }
        loadingDialog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        headBgView = (NetworkImageView) findViewById(R.id.head_bg);
        headBgView.setDefaultImageResId(R.drawable.head_lay);
        headView = (NetworkImageView) findViewById(R.id.head);
        headView.setDefaultImageResId(R.drawable.head);
        backView = (LinearLayout) findViewById(R.id.back);
        jinyanView = (TextView) findViewById(R.id.jinyan);
        agreeView = (TextView) findViewById(R.id.agree);
        guanzhuView = (CheckedTextView) findViewById(R.id.guanzhu);
        moreInfoView = (TextView) findViewById(R.id.more_info);
        emptyView = (TextView) findViewById(R.id.empty_info);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.detail_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        usernickView = (TextView) findViewById(R.id.usernick);
        share = ((LinearLayout) findViewById(R.id.share));
        jingyanzhi = ((TextView) findViewById(R.id.jinyan));
        manyidu = ((TextView) findViewById(R.id.agree));
        configPlatform();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.openShare(MoteDetailActivity.this, false);//系统默认的
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
            public void onItemClick(int position, int picNum, View v) {
                if (!LoginManager.isLogin(MoteDetailActivity.this)) {
                    return;
                }

                TaskPicsVO taskPicsVO = taskPicsList.get(position);
                List<MoteTaskPicVO> pics = taskPicsVO.taskPics;
                List<PicViewObject> picViewList = new ArrayList<PicViewObject>();
                for (MoteTaskPicVO taskPic : pics) {
                    PicViewObject picViewObject = new PicViewObject();
                    picViewObject.setPicId(taskPic.id);
                    picViewObject.setPicPreViewUrl(taskPic.getPreviewImageUrl());
                    picViewObject.setPicUrl(taskPic.getDetailImageUrl());
                    picViewObject.setPicType(PicViewObject.IMAGE);
                    picViewObject.setExtData(taskPic.id + "");
                    picViewList.add(picViewObject);
                }
                MulitImageVO mulitImageVO = new MulitImageVO(picNum, picViewList);
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
        mShare.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        // 设置分享图片, 参数2为图片的url地址
        mShare.setShareMedia(new UMImage(MoteDetailActivity.this,
                R.drawable.icon));

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
    }

    // 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
    // image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
    // 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
    // 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
    private void addQQZonePlatform() {
//        String appId = "100424468";
//        String appKey = "c7394704798a158208a74ab60104f0ba";
        String appId = "1104979541";
        String appKey = "uMcMgTs7XX85f4eO";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MoteDetailActivity.this,
                appId, appKey);
//        qqSsoHandler.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
//        //设置title
//        qqSsoHandler.setTitle("来自“全民模特”的分享");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                MoteDetailActivity.this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        //设置分享title
        qqShareContent.setTitle("来自“全民模特”的分享");
        //设置分享图片
        qqShareContent.setShareImage(new UMImage(MoteDetailActivity.this, R.drawable.icon));
        //设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
        mShare.setShareMedia(qqShareContent);

        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享文字
        qzone.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        //设置点击消息的跳转URL
        qzone.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
        qzone.setTitle("来自“全民模特”的分享");
        //设置分享图片
        qzone.setShareImage(new UMImage(MoteDetailActivity.this, R.drawable.icon));
        mShare.setShareMedia(qzone);
    }

    //添加微信分享平台
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

        String appId = "wx6f4a5ebb3d2cd11e";
        String appSecret = "64710023bac7d1b314c1b3ed3db5949d";

        UMWXHandler wxHandler = new UMWXHandler(MoteDetailActivity.this, appId, appSecret);
        wxHandler.addToSocialSDK();

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        //设置title
        weixinContent.setTitle("来自“全民模特”的分享");
        //设置分享内容跳转URL
        weixinContent.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
        //设置分享图片
        weixinContent.setShareImage(new UMImage(MoteDetailActivity.this,R.drawable.icon));
        mShare.setShareMedia(weixinContent);
        // 添加微信平台

        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        //设置朋友圈title
        circleMedia.setTitle("来自“全民模特”的分享");
        circleMedia.setShareImage(new UMImage(MoteDetailActivity.this, R.drawable.icon));
        circleMedia.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
        mShare.setShareMedia(circleMedia);

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


        LoginVO vo = LoginManager.getLoginInfo(this);
        if (GlobalParams.isDebug) {
            Log.e("moteId_near", moteId + "");
        }

        String token = vo == null?"":vo.token;
        MoteManager.fetchMoteDetailInfo(moteId, token).startUI(new ApiCallback<MoteDetail1>() {
            @Override
            public void onError(int code, String errorInfo) {
                countDownLatch.countDown();
                success = false;
            }

            @Override
            public void onSuccess(final MoteDetail1 vo) {
                countDownLatch.countDown();

                if (GlobalParams.isDebug){
                    Log.e("TAG_detail",vo.getFollowNum()+"");
                }

                String imageUrl = vo.getAvartUrl();
                if (!TextUtils.isEmpty(imageUrl)) {
                    headView.setImageUrl(imageUrl, mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.getCircleBitmap(bitmap, MoteDetailActivity.this.getResources().getDimension(R.dimen.cycle_head_image_size));
                        }
                    });
                    headBgView.setImageUrl(vo.getAvartUrl(), mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.fastBlur(MoteDetailActivity.this, bitmap, 12);
                        }
                    });
                }

                usernickView.setText(vo.getNickname());

                isFollow = vo.isFollow;
                if (isFollow) {
                    guanzhuView.setText("已关注");
                    guanzhuView.setChecked(true);
                } else {
//                    int num = infoVO.getFollowNum();
                    guanzhuView.setText("关注度:" + vo.getFollowNum());
                    guanzhuView.setChecked(false);
                }

                jingyanzhi.setText("经验值：" + vo.getOrderNum());
                if (vo.goodeEvalRate==0){
                    manyidu.setText("满意度：" + vo.goodeEvalRate);
                }else{
                    manyidu.setText("满意度：" + vo.goodeEvalRate+"%");
                }

                guanzhuView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doGuanzhu(vo.getFollowNum());
                    }
                });

            }

            @Override
            public void onProgress(int progress) {

            }
        });
        MoteManager.fetchMoteTaskPics(moteId, pageNo, PAGE_SIZE).startUI(new ApiCallback<List<TaskPicsVO>>() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_info:
                Intent it = new Intent(this, ModelPersonActivity.class);
                it.putExtra("moteId", moteId);
                startActivity(it);
                break;
//            case R.id.guanzhu:
//                doGuanzhu();
//                break;
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

    private void doGuanzhu(final int number) {
        if (!LoginManager.isLogin(this)) {
            return;
        }
        loadingDialog.show();
        LoginVO vo = LoginManager.getLoginInfo(this);
        if (!isFollow) {
            MoteManager.addFollow(moteId, vo.token).startUI(new ApiCallback<Integer>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MoteDetailActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Integer aBoolean) {
                    Toast.makeText(MoteDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                    guanzhuView.setText("已关注");
                    guanzhuView.setChecked(true);
                    isFollow = true;
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        } else {
            List<Long> moteIds = Arrays.asList(new Long[]{moteId});
            MoteManager.cancelFollow(moteIds, vo.token).startUI(new ApiCallback<Boolean>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MoteDetailActivity.this, "取消关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Boolean num) {
                    Toast.makeText(MoteDetailActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                    guanzhuView.setText("关注度:" +number);
                    if (GlobalParams.isDebug){
                        Log.e("TAG",infoVO.getFollowNum()+"");
                    }
                    guanzhuView.setChecked(false);
                    isFollow = false;
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

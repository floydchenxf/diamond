package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.IMChannel;
import com.floyd.diamond.IMImageCache;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.MoteDetail;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteDetailInfoVO;
import com.floyd.diamond.ui.DialogCreator;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ModelPersonActivity extends Activity {
    private UMSocialService mShare;
    private LinearLayout share;
    private SocializeListeners.UMShareBoardListener listener;
    private CheckedTextView careCount;
    private Dialog loadingDialog;
    private NetworkImageView headView, headView_bg;
    private long moteId;
    private TextView nickname, jianyanzhi, manyidu, age, gender, height, shapes, area;
    private ImageLoader mImageLoader;
    private MoteDetailInfoVO vo;
    private RequestQueue queue;
    private MoteDetail moteDetail;
    private LoginVO loginVO;
    private boolean isFollow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modelperson);

        // 得到UM的社会化分享组件
        mShare = UMServiceFactory.getUMSocialService("com.umeng.share");

        setShareContent();

        init();

        loadData();
    }

    public void init() {
        vo = new MoteDetailInfoVO();
        loginVO = LoginManager.getLoginInfo(ModelPersonActivity.this);
        queue = Volley.newRequestQueue(this);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        IMImageCache wxImageCache = IMImageCache.findOrCreateCache(
                IMChannel.getApplication(), EnvConstants.imageRootPath);
        this.mImageLoader = new ImageLoader(mQueue, wxImageCache);
        mImageLoader.setBatchedResponseDelay(0);

        LinearLayout back = ((LinearLayout) findViewById(R.id.back));//返回
        share = ((LinearLayout) findViewById(R.id.share));//分享按钮
        careCount = ((CheckedTextView) findViewById(R.id.careCount2));//关注次数
        careCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGuanzhu();
            }
        });
        headView = ((NetworkImageView) findViewById(R.id.touxiang));//头像
        headView_bg = ((NetworkImageView) findViewById(R.id.touxiang_bg));//头像背景
        nickname = ((TextView) findViewById(R.id.nickname2));//昵称
        jianyanzhi = ((TextView) findViewById(R.id.jingyanzhi));
        manyidu = ((TextView) findViewById(R.id.manyidu));
        gender = ((TextView) findViewById(R.id.gender_model));
        age = ((TextView) findViewById(R.id.age_model));
        height = ((TextView) findViewById(R.id.height_model));
        shapes = ((TextView) findViewById(R.id.shapes_model));
        area = ((TextView) findViewById(R.id.area_model));
        loadingDialog = new Dialog(this, R.style.data_load_dialog);
        loadingDialog = DialogCreator.createDataLoadingDialog(this);
        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击弹出分享面板
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configPlatform();
            }
        });

    }

    public void loadData() {
        moteId = getIntent().getLongExtra("moteId", 0);

        String url = APIConstants.HOST + APIConstants.API_MOTE_DETAIL_INFO;

        final String token = loginVO == null ? "" : loginVO.token;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug) {
                    Log.e("MoteDetail", response);
                }

                Gson gson = new Gson();
                moteDetail = gson.fromJson(response, MoteDetail.class);

                //绑定数据
                nickname.setText(moteDetail.getData().getNickname());
                age.setText(moteDetail.getData().getAge() + "");
                int genderNum = moteDetail.getData().getGender();
                if (genderNum == 0) {
                    gender.setText("女");
                } else if (genderNum == 1) {
                    gender.setText("男");
                }

                height.setText(moteDetail.getData().getHeight() + "");
                float shape = ((float) moteDetail.getData().getShape());
                if (shape == 1) {
                    shapes.setText("骨感");
                } else if (shape == 2) {
                    shapes.setText("标致");
                } else if (shape == 3) {
                    shapes.setText("丰满");
                }

                area.setText(moteDetail.getData().getArea());
                jianyanzhi.setText(moteDetail.getData().getOrderNum() + "");
                if (moteDetail.getData().getGoodeEvalRate() == 0) {
                    manyidu.setText(moteDetail.getData().getGoodeEvalRate() + "");
                } else {
                    manyidu.setText(moteDetail.getData().getGoodeEvalRate() + "%");
                }

                isFollow = moteDetail.getData().isIsFollow();
                if (isFollow) {
                    careCount.setText("已关注");
                    careCount.setChecked(true);
                } else {
                    int num = moteDetail.getData().getFollowNum();
                    careCount.setText("关注度:" + num);
                    careCount.setChecked(false);
                    // careCount.setOnClickListener((View.OnClickListener) ModelPersonActivity.this);
                }
                String imageUrl = moteDetail.getData().getAvartUrl() + "!v400";
//                MyImageLoader loader=new MyImageLoader(queue,imageUrl,headView,ModelPersonActivity.this);
//                MyImageLoader loader1=new MyImageLoader(queue,imageUrl,headView_bg,ModelPersonActivity.this);
                if (!TextUtils.isEmpty(imageUrl)) {
//                    headView.setDefaultImageResId(R.drawable.head);
                    headView.setImageUrl(imageUrl, mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.getCircleBitmap(bitmap, ModelPersonActivity.this.getResources().getDimension(R.dimen.cycle_head_image_size));
                        }
                    });
//                    headView_bg.setDefaultImageResId(R.drawable.head);
                    headView_bg.setImageUrl(imageUrl, mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.fastBlur(bitmap, 12);
                        }
                    });
                    //headView_bg.setBackgroundResource(R.color.headview_bf);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ModelPersonActivity.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<>();
                params.put("id", moteId + "");
                params.put("token", token);
                return params;
            }
        };

        queue.add(request);
    }

//    private void loadData() {
//        moteId=getIntent().getLongExtra("moteId",0);
//
//        if (GlobalParams.isDebug){
//            Log.e("moteId",moteId+"");
//        }
//        final CountDownLatch countDownLatch = new CountDownLatch(2);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    countDownLatch.await(2000, TimeUnit.MILLISECONDS);
//                } catch (InterruptedException e) {
//                }
//
//                loadingDialog.dismiss();
//
//            }
//        }).start();
//
//
//        if (!LoginManager.isLogin(this)) {
//            countDownLatch.countDown();
//            countDownLatch.countDown();
//        } else {
//            LoginVO vo = LoginManager.getLoginInfo(this);
//            MoteManager.fetchMoteDetailInfo(moteId, vo.token).startUI(new ApiCallback<MoteDetailInfoVO>() {
//                @Override
//                public void onError(int code, String errorInfo) {
//                    countDownLatch.countDown();
//                }
//
//                @Override
//                public void onSuccess(MoteDetailInfoVO vo) {
//                    countDownLatch.countDown();
//                    String imageUrl = vo.getPreviewImageUrl();
//                    if (!TextUtils.isEmpty(imageUrl)) {
//                        headView.setDefaultImageResId(R.drawable.head);
//                        headView.setImageUrl(imageUrl, mImageLoader, new BitmapProcessor() {
//                            @Override
//                            public Bitmap processBitmpa(Bitmap bitmap) {
//                                return ImageUtils.getCircleBitmap(bitmap, ModelPersonActivity.this.getResources().getDimension(R.dimen.cycle_head_image_size));
//                            }
//                        });
//                        headView_bg.setDefaultImageResId(R.drawable.head);
//                        headView_bg.setImageUrl(vo.getDetailImageUrl(), mImageLoader);
//                    }
//
//                    nickname.setText(vo.nickname);
//
//                    boolean isFollow = vo.isFollow;
//                    if (isFollow) {
//                        careCount.setText("已关注");
//                        careCount.setChecked(true);
//                    } else {
//                        int num = vo.followNum;
//                        careCount.setText("关注度:" + num);
//                        careCount.setChecked(false);
//                       // careCount.setOnClickListener((View.OnClickListener) ModelPersonActivity.this);
//                    }
//
////                    age.setText(vo.getAge());
////
////                    gender.setText(vo.getGender());
////
////                    height.setText(vo.getHeight());
////
////                    area.setText(vo.getAddress());
//                }
//
//                @Override
//                public void onProgress(int progress) {
//
//                }
//            });
//        }
//
//    }


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
        mShare.setShareMedia(new UMImage(ModelPersonActivity.this,
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


        // 打开分享面板
        mShare.openShare(this, false);//系统默认的
        // startActivity(new Intent(ModelPersonActivity.this, DialogActivity.class));


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
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ModelPersonActivity.this,
                appId, appKey);
//        qqSsoHandler.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
//        //设置title
//        qqSsoHandler.setTitle("来自“全民模特”的分享");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                ModelPersonActivity.this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        //设置分享文字
        qqShareContent.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        //设置分享title
        qqShareContent.setTitle("来自“全民模特”的分享");
        //设置分享图片
        qqShareContent.setShareImage(new UMImage(ModelPersonActivity.this, R.drawable.icon));
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
        qzone.setShareImage(new UMImage(ModelPersonActivity.this, R.drawable.icon));
        mShare.setShareMedia(qzone);
    }

    //添加微信分享平台
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

        String appId = "wx787a58aec93342a4";
        String appSecret = "b9b9b224459a65766b0b3396c8f8cf1e";

        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        //设置分享文字
        weixinContent.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        //设置title
        weixinContent.setTitle("来自“全民模特”的分享");
        //设置分享内容跳转URL
        weixinContent.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
        //设置分享图片
        weixinContent.setShareImage(new UMImage(ModelPersonActivity.this, R.drawable.icon));
        mShare.setShareMedia(weixinContent);
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(ModelPersonActivity.this, appId,
                appSecret);
        wxHandler.addToSocialSDK();

        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("基于移动互联网，构建颜值经济平台，聚集高颜值美女，帅哥，儿童，让颜值成为生产力，让颜值更有价值！圆你一个模特梦，让人人都有机会成为网络红人的平台！\n" +
                "在全民模特APP只要你有颜值！而颜值越用越亮！真正做到轻松赚钱，还能成为网红！");
        //设置朋友圈title
        circleMedia.setTitle("来自“全民模特”的分享");
        circleMedia.setShareImage(new UMImage(ModelPersonActivity.this, R.drawable.icon));
        circleMedia.setTargetUrl("https://mp.weixin.qq.com/s?__biz=MzA3MzUxMjE0Nw==&mid=402986533&idx=1&sn=d503481e7048afe058d7b7b19613919d&scene=0&previewkey=Y0AJm9zrE7wRVUc950Fuc8NS9bJajjJKzz%2F0By7ITJA%3D&uin=NTgzMTExODgw&key=710a5d99946419d9b3463e089f1d876636ceb5d18633bbe0cb595068d607d845498fc2369c6cb0fb9af4c15c0132292e&devicetype=iMac14%2C2+OSX+OSX+10.11.1+build%2815B42%29&version=11000003&lang=zh_CN&pass_ticket=fsq9NnAUofrE%2FMjugdWnmN1G2g9xOx1w2bLs%2BwX9n2wOSxs8FzTcB4eb5CHVLpyy");
        mShare.setShareMedia(circleMedia);

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(ModelPersonActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void doGuanzhu() {
        if (!LoginManager.isLogin(this)) {
            return;
        }
        loadingDialog.show();
        LoginVO vo = LoginManager.getLoginInfo(this);
        if (!isFollow) {
            MoteManager.addFollow(moteId, vo.token).startUI(new ApiCallback<Integer>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(ModelPersonActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!ModelPersonActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Integer aBoolean) {
                    Toast.makeText(ModelPersonActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    if (!ModelPersonActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                    careCount.setText("已关注");
                    careCount.setChecked(true);
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
                    Toast.makeText(ModelPersonActivity.this, "取消关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!ModelPersonActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Boolean num) {
                    Toast.makeText(ModelPersonActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    if (!ModelPersonActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                    careCount.setText("关注度:" + moteDetail.getData().getFollowNum());
                    careCount.setChecked(false);
                    isFollow = false;
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        }
    }


}



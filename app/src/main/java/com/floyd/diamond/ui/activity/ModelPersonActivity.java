package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.MoteDetailInfoVO;
import com.floyd.diamond.biz.vo.TaskPicsVO;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ModelPersonActivity extends Activity {
    private UMSocialService mShare;
    private LinearLayout share;
    private SocializeListeners.UMShareBoardListener listener;
    private CheckBox careCount;
    private Dialog loadingDialog;
    private NetworkImageView headView,headView_bg;
    private long moteId;
    private TextView nickname,jianyanzhi,manyidu,age,gender,height,shapes,area;
    private ImageLoader mImageLoader;
    private MoteDetailInfoVO vo;

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
        vo=new MoteDetailInfoVO();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        IMImageCache wxImageCache = IMImageCache.findOrCreateCache(
                IMChannel.getApplication(), EnvConstants.imageRootPath);
        this.mImageLoader = new ImageLoader(mQueue, wxImageCache);
        mImageLoader.setBatchedResponseDelay(0);

        LinearLayout back = ((LinearLayout) findViewById(R.id.back));//返回
        share = ((LinearLayout) findViewById(R.id.share));//分享按钮
        careCount= ((CheckBox) findViewById(R.id.careCount2));//关注次数
        careCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGuanzhu();
            }
        });
        headView= ((NetworkImageView) findViewById(R.id.touxiang));//头像
        headView_bg= ((NetworkImageView) findViewById(R.id.touxiang_bg));//头像背景
        nickname= ((TextView) findViewById(R.id.nickname2));//昵称
        jianyanzhi= ((TextView) findViewById(R.id.jingyanzhi));
        manyidu= ((TextView) findViewById(R.id.manyidu));
        gender= ((TextView) findViewById(R.id.gender_model));
        age= ((TextView) findViewById(R.id.age_model));
        height= ((TextView) findViewById(R.id.height_model));
        shapes= ((TextView) findViewById(R.id.shapes_model));
        area= ((TextView) findViewById(R.id.area_model));
        loadingDialog = new Dialog(this, R.style.data_load_dialog);
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

    private void loadData() {
        moteId=getIntent().getLongExtra("moteId",0);

        if (GlobalParams.isDebug){
            Log.e("moteId",moteId+"");
        }
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await(2000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }

                loadingDialog.dismiss();

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
                                return ImageUtils.getCircleBitmap(bitmap, ModelPersonActivity.this.getResources().getDimension(R.dimen.cycle_head_image_size));
                            }
                        });
                        headView_bg.setDefaultImageResId(R.drawable.head);
                        headView_bg.setImageUrl(vo.getDetailImageUrl(), mImageLoader);
                    }

                    nickname.setText(vo.nickname);

                    boolean isFollow = vo.isFollow;
                    if (isFollow) {
                        careCount.setText("已关注");
                        careCount.setChecked(true);
                    } else {
                        int num = vo.followNum;
                        careCount.setText("关注度:" + num);
                        careCount.setChecked(false);
                       // careCount.setOnClickListener((View.OnClickListener) ModelPersonActivity.this);
                    }

//                    age.setText(vo.getAge());
//
//                    gender.setText(vo.getGender());
//
//                    height.setText(vo.getHeight());
//
//                    area.setText(vo.getAddress());
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        }

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
        mShare.setShareMedia(new UMImage(ModelPersonActivity.this,
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
        mShare.openShare(this, false);//系统默认的
      // startActivity(new Intent(ModelPersonActivity.this, DialogActivity.class));


    }


    // 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
    // image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
    // 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
    // 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
    private void addQQZonePlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ModelPersonActivity.this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://img4.duitang.com/uploads/item/201201/04/20120104223901_Cku8d.thumb.600_0.jpg");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                ModelPersonActivity.this, appId, appKey);
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
        UMWXHandler wxHandler = new UMWXHandler(ModelPersonActivity.this, appId,
                appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(ModelPersonActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void doGuanzhu() {
        if (LoginManager.isLogin(this)) {
            LoginVO vo = LoginManager.getLoginInfo(this);
            MoteManager.addFollow(moteId, vo.token).startUI(new ApiCallback<Boolean>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(ModelPersonActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!ModelPersonActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    Toast.makeText(ModelPersonActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    if (!ModelPersonActivity.this.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                    careCount.setText("已关注");
                    careCount.setChecked(true);
                    careCount.setOnClickListener(null);
                }

                @Override
                public void onProgress(int progress) {

                }
            });


        }
    }


}



package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.floyd.diamond.R;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.LoginVO;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * Created by Administrator on 2015/11/28.
 */
public class SettingPersonInfoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SettingPersonInfoActivity";
    private TextView ziliao;
    private TextView phoneNum;
    private TextView message;
    private TextView clear;
    private TextView suggest;
    private TextView tuijian;
    private TextView aboutUs;
    private TextView noLogin;
    private UMSocialService mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);

        // 得到UM的社会化分享组件
        mShare = UMServiceFactory.getUMSocialService("com.umeng.share");
        //设置分享内容
        setShareContent();

        //初始化设置
        init();
    }

    public void init() {
        ziliao = ((TextView) findViewById(R.id.ziliao));//个人资料
        phoneNum = ((TextView) findViewById(R.id.phoneNum));//手机号
        message = ((TextView) findViewById(R.id.message));//消息通知
        clear = ((TextView) findViewById(R.id.clear));//清除缓存
        suggest = ((TextView) findViewById(R.id.suggest));//意见反馈
        tuijian = ((TextView) findViewById(R.id.tuijian));//推荐给朋友
        aboutUs = ((TextView) findViewById(R.id.aboutus));//关于我们
        noLogin = ((TextView) findViewById(R.id.noLogin));//退出登录
        ziliao.setOnClickListener(this);
        noLogin.setOnClickListener(this);
        this.findViewById(R.id.left).setOnClickListener(this);

        LoginVO vo = LoginManager.getLoginInfo(this);
        phoneNum.setText(vo.user.phoneNumber);

        tuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configPlatform();
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ziliao:
                Intent ziliaoIntent = new Intent(this, PersonInfoActivity.class);
                ziliaoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ziliaoIntent);
                break;
            case R.id.noLogin:
                PrefsTools.setStringPrefs(this, LoginManager.LOGIN_INFO, "");
                break;
            case R.id.left:
                this.finish();
                break;

        }

    }


    // 设置分享内容的方法
    private void setShareContent() {
        // 分享字符串
        mShare.setShareContent("来自“全民模特”的分享");
        // 设置分享图片, 参数2为图片的url地址
        mShare.setShareMedia(new UMImage(SettingPersonInfoActivity.this,
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
        startActivity(new Intent(SettingPersonInfoActivity.this, DialogActivity.class));


    }

    // 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
    // image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
    // 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
    // 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
    private void addQQZonePlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(SettingPersonInfoActivity.this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://img4.duitang.com/uploads/item/201201/04/20120104223901_Cku8d.thumb.600_0.jpg");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                SettingPersonInfoActivity.this, appId, appKey);
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
        UMWXHandler wxHandler = new UMWXHandler(SettingPersonInfoActivity.this, appId,
                appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(SettingPersonInfoActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }


}

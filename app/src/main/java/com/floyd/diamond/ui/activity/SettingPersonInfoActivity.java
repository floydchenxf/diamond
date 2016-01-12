package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.JobFactory;
import com.floyd.diamond.biz.constants.EnvConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.FileUtils;
import com.floyd.diamond.biz.tools.PrefsTools;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.UserVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.seller.SellerPersonInfoActivity;
import com.floyd.diamond.ui.view.UIAlertDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.io.File;

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
    private TextView fileSizeView;
    private UMSocialService mShare;
    private LoginVO loginVO;
    private Switch msgSwitch;

    private Dialog dataloadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        dataloadingDialog = DialogCreator.createDataLoadingDialog(this);

        // 得到UM的社会化分享组件
        mShare = UMServiceFactory.getUMSocialService("com.umeng.share");
        //设置分享内容
        setShareContent();

        //初始化设置
        init();

        AsyncJob<String> job = new AsyncJob<String>() {
            @Override
            public void start(ApiCallback<String> callback) {
                File file = new File(EnvConstants.imageRootPath);
                if (!file.exists()) {
                    callback.onError(1, "not exists");
                    return;
                }

                long size = 0;
                try {
                    size = FileUtils.getFileSize(file);
                } catch (Exception e) {
                    callback.onError(2, e.getMessage());
                    return;
                }
                float a = size / (1024 * 1024);
                callback.onSuccess(a + "M");
            }
        };

        job.threadOn().startUI(new ApiCallback<String>() {
            @Override
            public void onError(int code, String errorInfo) {
                Toast.makeText(SettingPersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s) {
                fileSizeView.setText(s);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
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
        fileSizeView = (TextView) findViewById(R.id.file_size_view);
        ziliao.setOnClickListener(this);
        noLogin.setOnClickListener(this);
        clear.setOnClickListener(this);
        suggest.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        this.findViewById(R.id.left).setOnClickListener(this);
        msgSwitch = (Switch)findViewById(R.id.msg_switch_view);

        msgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingPersonInfoActivity.this.setMsgSetting(isChecked);
            }
        });

        loginVO = LoginManager.getLoginInfo(this);
        if (loginVO.user.msgSwitch == 1) {
            msgSwitch.setChecked(true);
        } else {
            msgSwitch.setChecked(false);
        }
        phoneNum.setText(loginVO.user.phoneNumber);

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

    public void setMsgSetting(boolean checked) {
        dataloadingDialog.show();
        if (checked) {
            loginVO.user.msgSwitch = 1;
        } else {
            loginVO.user.msgSwitch = 0;
        }
        MoteManager.updateMoteInfo(loginVO.user, loginVO.token).startUI(new ApiCallback<UserVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                dataloadingDialog.dismiss();
                Toast.makeText(SettingPersonInfoActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(UserVO userVO) {
                dataloadingDialog.dismiss();
                loginVO.user.msgSwitch = userVO.msgSwitch;
                LoginManager.saveLoginInfo(SettingPersonInfoActivity.this, loginVO);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ziliao:
                if (loginVO.isModel()) {
                    Intent ziliaoIntent = new Intent(this, PersonInfoActivity.class);
                    ziliaoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ziliaoIntent);
                } else {
                    Intent sellerInfoIntent = new Intent(this, SellerPersonInfoActivity.class);
                    sellerInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(sellerInfoIntent);
                }
                break;
            case R.id.clear:
                UIAlertDialog.Builder clearBuilder = new UIAlertDialog.Builder(this);
                clearBuilder.setMessage("亲！您确认清除缓存？")
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                        JobFactory.createJob(EnvConstants.imageRootPath).map(new Func<String, File>() {
                                            @Override
                                            public File call(String s) {
                                                File f = new File(s);
                                                FileUtils.deleteFile(f);
                                                f.mkdir();
                                                return f;
                                            }
                                        }).threadOn().startUI(new ApiCallback<File>() {
                                            @Override
                                            public void onError(int code, String errorInfo) {

                                            }

                                            @Override
                                            public void onSuccess(File file) {
                                                fileSizeView.setText("0.0M");
                                            }

                                            @Override
                                            public void onProgress(int progress) {

                                            }
                                        });
                                    }
                                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog2 = clearBuilder.create();
                dialog2.show();
                break;
            case R.id.noLogin:
                UIAlertDialog.Builder builder = new UIAlertDialog.Builder(this);
                builder.setMessage("亲！您确认要退出登录？")
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                        PrefsTools.setStringPrefs(SettingPersonInfoActivity.this, LoginManager.LOGIN_INFO, "");
                                        PrefsTools.setStringPrefs(SettingPersonInfoActivity.this, SellerManager.SELLER_INFO, "");
                                        PrefsTools.setStringPrefs(SettingPersonInfoActivity.this, MoteManager.MOTE_INFO, "");
                                        Intent it = new Intent(SettingPersonInfoActivity.this, MainActivity.class);
                                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(it);
                                    }
                                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.left:
                this.finish();
                break;
            case R.id.suggest:
                Intent suggestIntent = new Intent(SettingPersonInfoActivity.this, FeedbackActivity.class);
                startActivity(suggestIntent);
                break;
            case R.id.aboutus:
                Intent aboutUsIntent = new Intent(SettingPersonInfoActivity.this, AboutUsActivity.class);
                startActivity(aboutUsIntent);
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
        String appId = "wx6f4a5ebb3d2cd11e";
        String appSecret = "9603f3903c1dab2b494de93c04c9026a";
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

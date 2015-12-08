package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Administrator on 2015/11/29.
 */
public class DialogActivity extends Activity {
    private UMSocialService mShare;
    private ImageView back;
    private TextView weixin,pengyouquan,QQ,QQZone,tengxunweibo,xinlangweibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        // 得到UM的社会化分享组件
        mShare = UMServiceFactory.getUMSocialService("com.umeng.share");

        init();

        //设置dialog之外的区域不可点击
        setFinishOnTouchOutside(false);

        setShareContent();
    }


    public void init() {

        //撤销
        back = ((ImageView) findViewById(R.id.delete));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //微信
        weixin= ((TextView) findViewById(R.id.weixin));
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.directShare(DialogActivity.this, SHARE_MEDIA.WEIXIN, mShareListener);
            }
        });

        //朋友圈
        pengyouquan= ((TextView) findViewById(R.id.pengyouquan));
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.directShare(DialogActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE, mShareListener);
            }
        });

        //腾讯微博
        tengxunweibo= ((TextView) findViewById(R.id.tengxunweibo));
        tengxunweibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.directShare(DialogActivity.this, SHARE_MEDIA.TENCENT, mShareListener);
            }
        });


        //新浪微博
        xinlangweibo= ((TextView) findViewById(R.id.xinlangweibo));
        xinlangweibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.directShare(DialogActivity.this, SHARE_MEDIA.SINA, mShareListener);
            }
        });

        //QQ
        QQ= ((TextView) findViewById(R.id.QQ));
        QQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.directShare(DialogActivity.this, SHARE_MEDIA.QQ, mShareListener);
            }
        });

        //QQZone
        QQZone= ((TextView) findViewById(R.id.QQZone));
        QQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShare.directShare(DialogActivity.this, SHARE_MEDIA.QZONE, mShareListener);
            }
        });

    }

    private void setShareContent() {
        // 分享字符串
        mShare.setShareContent("来自“全民模特”的分享");
        // 设置分享图片, 参数2为图片的url地址
        mShare.setShareMedia(new UMImage(DialogActivity.this,
                "http://www.umeng.com/images/pic/banner_module_social.png"));
    }

    /**
     * 分享监听器
     */
    SocializeListeners.SnsPostListener mShareListener = new SocializeListeners.SnsPostListener() {


        @Override
        public void onStart() {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int stCode,
                               SocializeEntity entity) {
            if (stCode == 200) {
                Toast.makeText(DialogActivity.this, "分享成功", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(DialogActivity.this,
                        "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };


}



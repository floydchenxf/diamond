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
import com.floyd.diamond.biz.vo.MoteDetailInfoHelpVO;
import com.floyd.diamond.biz.vo.MoteDetailInfoVO;
import com.floyd.diamond.biz.vo.MoteTaskPicVO;
import com.floyd.diamond.event.LoginEvent;
import com.floyd.pullrefresh.widget.PullToRefreshBase;
import com.floyd.pullrefresh.widget.PullToRefreshListView;

import java.util.List;
import java.util.Map;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_detail);

        RequestQueue mQueue = Volley.newRequestQueue(this);
        IMImageCache wxImageCache = IMImageCache.findOrCreateCache(
                IMChannel.getApplication(), EnvConstants.imageRootPath);
        this.mImageLoader = new ImageLoader(mQueue, wxImageCache);
        mImageLoader.setBatchedResponseDelay(0);

        moteId = getIntent().getLongExtra("moteId",0);
        loadingDialog = new Dialog(this, R.style.data_load_dialog);
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

            }

            @Override
            public void onPullUpToRefresh() {

            }
        });

        guanzhuView.setOnClickListener(this);
        moreInfoView.setOnClickListener(this);

        backView.setOnClickListener(this);

        EventBus.getDefault().register(this);
        loadingDialog.show();
        loadData();
    }

    private void loadData() {
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
        } else {
            LoginVO vo = LoginManager.getLoginInfo(this);
            MoteManager.fetchMoteDetailInfo(moteId,vo.token).startUI(new ApiCallback<MoteDetailInfoHelpVO>() {
                @Override
                public void onError(int code, String errorInfo) {
                    countDownLatch.countDown();
                }

                @Override
                public void onSuccess(MoteDetailInfoHelpVO moteDetailInfoVO) {
                    countDownLatch.countDown();
                    MoteDetailInfoVO vo = moteDetailInfoVO.moteInfo;
                    String imageUrl = vo.avartUrl;
                    if (!TextUtils.isEmpty(imageUrl)) {
                        headView.setDefaultImageResId(R.drawable.head);
                        headView.setImageUrl(imageUrl, mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.getCircleBitmap(bitmap, MoteDetailActivity.this.getResources().getDimension(R.dimen.cycle_head_image_size));
                            }
                        });
                        headBgView.setDefaultImageResId(R.drawable.head);
                        headBgView.setImageUrl(imageUrl, mImageLoader);
                    }

                    usernickView.setText(vo.nickname);

                    boolean isFollow = moteDetailInfoVO.isFollow;
                    if (isFollow) {
                        guanzhuView.setText("已关注");
                        guanzhuView.setChecked(true);
                    } else {
                        int num = vo.followNum;
                        guanzhuView.setText("关注度:"+num);
                        guanzhuView.setChecked(false);
                        guanzhuView.setOnClickListener(MoteDetailActivity.this);
                    }



                }

                @Override
                public void onProgress(int progress) {

                }
            });
            MoteManager.fetchMoteTaskPics(moteId, 1, 10, vo.token).startUI(new ApiCallback<List<Map<String, List<MoteTaskPicVO>>>>() {
                @Override
                public void onError(int code, String errorInfo) {
                    countDownLatch.countDown();
                }

                @Override
                public void onSuccess(List<Map<String, List<MoteTaskPicVO>>> maps) {
                    countDownLatch.countDown();
                    if (maps == null||maps.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                    }
                    Log.i(TAG, "kkkk--------------map:"+maps);
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
                Intent it = new Intent(this, MoteMoreInfoActivity.class);
                startActivity(it);
                break;
            case R.id.guanzhu:
                if (!this.isFinishing()) {
                    loadingDialog.show();
                }
                doGuanzhu();
                break;
            case R.id.back:
                this.finish();
                break;
        }

    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
    }

    private void doGuanzhu() {
        if (LoginManager.isLogin(this)) {
            LoginVO vo = LoginManager.getLoginInfo(this);
            MoteManager.addFollow(moteId,vo.token).startUI(new ApiCallback<Boolean>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(MoteDetailActivity.this, "关注失败:"+errorInfo, Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.hide();
                    }
                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    Toast.makeText(MoteDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    if (!MoteDetailActivity.this.isFinishing()) {
                        loadingDialog.hide();
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

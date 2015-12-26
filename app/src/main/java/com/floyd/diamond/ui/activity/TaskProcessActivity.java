package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.anim.LsLoadingView;
import com.floyd.diamond.ui.fragment.FinishCallback;
import com.floyd.diamond.ui.fragment.ProcessGoodsOperateFragment;
import com.floyd.diamond.ui.fragment.ProcessUploadImageFragment;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;

public class TaskProcessActivity extends Activity implements View.OnClickListener {

    public static final String MOTE_TASK_ID = "moteTaskId";
    //-----------------------任务信息---------------------//
    private TextView taskInfoSummaryView;
    private View taskInfoDetailLayout;
    private NetworkImageView taskImageView;
    private TextView titleView;
    private TextView shotRequiredView;
    private TextView priceView;
    private TextView shotFeeView;
    private TextView addressView;
    private TextView goodsLinkView;

    //----------------------接单信息----------------------//
    private TextView acceptTimeView;
    private View acceptStatusLayout;

    //----------------------订单号-----------------------//
    private View editConfirmOrderNoLayout;
    private TextView confirmOrderNoTextView; //不是当前输入订单状态
    private EditText confirmOrderNoEditView; //输入订单状态
    private TextView confirmButton;
    private TextView confirmTimeView; //确认时间
    private TextView dropOrderNoView; //放弃订单

    //-----------------图片-------------------------//

    private Dialog dataLoadingDailog;
    private DataLoadingView dataLoadingView;

    //loading
    private FrameLayout mActLsloading;
    private View mActLsFailLayoutView;
    private TextView mActLsFailTv;
    private LsLoadingView mLsLoadingView;
    private View mLoading_container;

    private TaskProcessVO taskProcessVO;

    private ImageLoader mImageLoader;

    private Long moteTaskId;

    private Handler mHandler = new Handler();

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            long times = taskProcessVO.moteTask.acceptedTime;
            long now = System.currentTimeMillis();
            long leftTimes = 30 * 60 - (now - times) / 1000;
            if (leftTimes < 0) {
                //
                editConfirmOrderNoLayout.setVisibility(View.GONE);
                confirmOrderNoTextView.setVisibility(View.VISIBLE);
                dropOrderNoView.setVisibility(View.GONE);
                confirmOrderNoTextView.setText("任务终止");
                return;
            }

            confirmTimeView.setText("请在<font color=\"red\">" + leftTimes + "</font>秒内完成下单并输入订单号");
            dropOrderNoView.setVisibility(View.VISIBLE);
            mHandler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_process);
        moteTaskId = getIntent().getLongExtra(MOTE_TASK_ID, 0l);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        dataLoadingDailog = DialogCreator.createDataLoadingDialog(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        initTaskInfoView();
        initAcceptView();
        initOrderNoView();
        dataLoadingView.startLoading();
        loadData();
    }

    private void initOrderNoView() {
        editConfirmOrderNoLayout = findViewById(R.id.edit_confirm_order_layout);
        confirmOrderNoEditView = (EditText) findViewById(R.id.order_no);
        confirmButton = (TextView) findViewById(R.id.confirm_order_button);
        confirmTimeView = (TextView) findViewById(R.id.confirm_time);
        confirmOrderNoTextView = (TextView) findViewById(R.id.confirm_order_id);
        dropOrderNoView = (TextView) findViewById(R.id.drop_order);
        confirmButton.setOnClickListener(this);
        dropOrderNoView.setOnClickListener(this);
    }

    private void initAcceptView() {
        acceptStatusLayout = findViewById(R.id.accept_status_layout);
        acceptTimeView = (TextView) findViewById(R.id.accept_time);
    }

    private void loadData() {
        LoginVO vo = LoginManager.getLoginInfo(this);
        MoteManager.fetchTaskProcess(moteTaskId, vo.token).startUI(new ApiCallback<TaskProcessVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                dataLoadingView.loadFail();
            }

            @Override
            public void onSuccess(TaskProcessVO taskProcessVO) {
                dataLoadingView.loadSuccess();
                TaskProcessActivity.this.taskProcessVO = taskProcessVO;
                fillTaskInfo(taskProcessVO);
                fillAcceptStatus(taskProcessVO);
                fillOrderStatus(taskProcessVO);

                int status = taskProcessVO.moteTask.status;
                if (status >= 2) {
                    initAndFillUploadPic(taskProcessVO);
                }

                if (status >= 4) {
                    initAndFillGoodsOperate(taskProcessVO);
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    private void initAndFillGoodsOperate(TaskProcessVO taskProcessVO) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment goodsProcessFragment = fragmentManager.findFragmentById(R.id.goods_process);
        if (goodsProcessFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            goodsProcessFragment = ProcessGoodsOperateFragment.newInstance(taskProcessVO);
            fragmentTransaction.add(R.id.goods_process, goodsProcessFragment);
            fragmentTransaction.commit();
        }
    }


    private void initAndFillUploadPic(final TaskProcessVO taskProcessVO) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment uploadPicFragment = fragmentManager.findFragmentById(R.id.upload_pic);
        if (uploadPicFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            uploadPicFragment = ProcessUploadImageFragment.newInstance(taskProcessVO, new FinishCallback() {
                @Override
                public void doFinish() {
                    initAndFillGoodsOperate(taskProcessVO);
                }
            });
            fragmentTransaction.add(R.id.upload_pic, uploadPicFragment);
            fragmentTransaction.commit();
        }
    }

    private void fillOrderStatus(TaskProcessVO taskProcessVO) {
        int status = taskProcessVO.moteTask.status;
        if (status == 1) {
            //计算时间
            editConfirmOrderNoLayout.setVisibility(View.VISIBLE);
            confirmOrderNoTextView.setVisibility(View.GONE);
            mHandler.post(timer);
        } else if (status == 2) {
            long orderNoTime = taskProcessVO.moteTask.orderNoTime;
            String orderNo = taskProcessVO.moteTask.orderNo;
            String dateStr = DateUtil.getDateStr(orderNoTime);
            editConfirmOrderNoLayout.setVisibility(View.VISIBLE);
            confirmOrderNoTextView.setVisibility(View.GONE);
            confirmTimeView.setText(dateStr);
            confirmOrderNoEditView.setText(orderNo);
        } else {
            long orderNoTime = taskProcessVO.moteTask.orderNoTime;
            String dateStr = DateUtil.getDateStr(orderNoTime);
            String orderNo = taskProcessVO.moteTask.orderNo;
            editConfirmOrderNoLayout.setVisibility(View.GONE);
            confirmOrderNoTextView.setVisibility(View.VISIBLE);
            dropOrderNoView.setVisibility(View.GONE);
            confirmTimeView.setText(dateStr);
            confirmOrderNoTextView.setText(orderNo);
        }

    }

    private void fillAcceptStatus(TaskProcessVO taskProcessVO) {
        long time = taskProcessVO.moteTask.acceptedTime;
        String dateStr = DateUtil.getDateStr(time);
        acceptTimeView.setText(dateStr);
    }

    private void fillTaskInfo(TaskProcessVO taskProcessVO) {
        TaskItemVO itemVo = taskProcessVO.task;
        taskImageView.setImageUrl(itemVo.getPreviewImageUrl(), mImageLoader);
        addressView.setText(itemVo.areaName);
        priceView.setText(itemVo.price + "");
        shotFeeView.setText(itemVo.shotFee + "");
        shotRequiredView.setText(itemVo.shotDesc);
        titleView.setText(itemVo.title);
    }

    private void initTaskInfoView() {
        taskInfoSummaryView = (TextView) findViewById(R.id.task_info_summary);
        taskInfoDetailLayout = findViewById(R.id.task_info_detail_layout);
        taskInfoSummaryView.setVisibility(View.VISIBLE);
        taskInfoDetailLayout.setVisibility(View.GONE);
        taskImageView = (NetworkImageView) findViewById(R.id.task_image);
        priceView = (TextView) findViewById(R.id.goods_price);
        shotFeeView = (TextView) findViewById(R.id.goods_shot_fee);
        addressView = (TextView) findViewById(R.id.goods_address);
        titleView = (TextView) findViewById(R.id.task_name);
        shotRequiredView = (TextView) findViewById(R.id.task_shot_required);
        goodsLinkView = (TextView) findViewById(R.id.goods_link);

        taskInfoSummaryView.setOnClickListener(this);
        findViewById(R.id.jiantou_up).setOnClickListener(this);
        goodsLinkView.setOnClickListener(this);
    }

    private void confirmOrderNo(long moteTaskId, String orderNo, String token) {
        MoteManager.addOrderNo(moteTaskId, orderNo, token).startUI(new ApiCallback<Boolean>() {
            @Override
            public void onError(int code, String errorInfo) {
                Toast.makeText(TaskProcessActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                initAndFillUploadPic(taskProcessVO);
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
            case R.id.jiantou_up:
                Drawable down = null;
                if(android.os.Build.VERSION.SDK_INT >= 21){
                    down = getResources().getDrawable(R.drawable.jiantou_down, getTheme());
                } else {
                    down = getResources().getDrawable(R.drawable.jiantou_down);
                }
                down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
                taskInfoSummaryView.setCompoundDrawables(null, null, down, null);
                taskInfoDetailLayout.setVisibility(View.GONE);
                acceptStatusLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.task_info_summary:
                taskInfoSummaryView.setCompoundDrawables(null, null, null, null);
                taskInfoDetailLayout.setVisibility(View.VISIBLE);
                acceptStatusLayout.setVisibility(View.GONE);
                break;
            case R.id.goods_link:
                String url = taskProcessVO.task.url;
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(TaskProcessActivity.this, "无效商品链接!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.confirm_order_button:
                long moteTaskId = taskProcessVO.moteTask.id;
                LoginVO vo = LoginManager.getLoginInfo(this);
                String token = vo.token;
                String orderNo = confirmOrderNoEditView.getText().toString();
                if (TextUtils.isEmpty(orderNo)) {
                    Toast.makeText(this, "请输入订单号", Toast.LENGTH_SHORT).show();
                    return;
                }

                confirmOrderNo(moteTaskId, orderNo, token);
                break;
            case R.id.drop_order:
                break;
            case R.id.act_ls_fail_layout:
                loadData();
                break;
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(timer);
    }
}

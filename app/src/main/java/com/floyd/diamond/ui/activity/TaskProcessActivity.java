package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.floyd.diamond.event.TaskEvent;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.fragment.FinishCallback;
import com.floyd.diamond.ui.fragment.ProcessGoodsOperateFragment;
import com.floyd.diamond.ui.fragment.ProcessUploadImageFragment;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;

import de.greenrobot.event.EventBus;

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

    //--------------确认收货---------------//
    private LinearLayout confirmGoodsLayout;
    private TextView confirmGoodsTimeView;
    private TextView confirmGoodsDescView;


    private Dialog dataLoadingDailog;
    private DataLoadingView dataLoadingView;

    private TaskProcessVO taskProcessVO;

    private ImageLoader mImageLoader;

    private Long moteTaskId;

    private TextView jiantou;

    private Handler mHandler = new Handler();

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            long times = taskProcessVO.moteTask.acceptedTime;
            int status = taskProcessVO.moteTask.status;
            long now = System.currentTimeMillis();
            long leftTimes = 30 * 60 - (now - times) / 1000;
            if (leftTimes <= 0) {
                editConfirmOrderNoLayout.setVisibility(View.GONE);
                confirmOrderNoTextView.setVisibility(View.VISIBLE);
                dropOrderNoView.setVisibility(View.GONE);
                String datetime = DateUtil.getDateStr(System.currentTimeMillis());
                confirmTimeView.setText(datetime);
                confirmOrderNoTextView.setText("任务终止");
                return;
            }

            if (status == 1) {
                String t = DateUtil.getDateBefore(taskProcessVO.moteTask.acceptedTime);
                confirmTimeView.setText(Html.fromHtml("请在<font color=\"red\">" + t + "</font>内完成下单并输入订单号"));
                dropOrderNoView.setVisibility(View.VISIBLE);
                mHandler.postDelayed(this, 1000);
            }

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
        initGoodsConfirmView();
        loadData(true);
    }

    private void initGoodsConfirmView() {
        confirmGoodsLayout = (LinearLayout) findViewById(R.id.confirm_goods_layout);
        confirmGoodsTimeView = (TextView) findViewById(R.id.confirm_goods_time_view);
        confirmGoodsDescView = (TextView) findViewById(R.id.confirm_goods_view);
        confirmGoodsLayout.setVisibility(View.GONE);
    }

    private void initOrderNoView() {
        editConfirmOrderNoLayout = findViewById(R.id.edit_confirm_order_layout);
        confirmOrderNoEditView = (EditText) findViewById(R.id.order_no);
        confirmButton = (TextView) findViewById(R.id.confirm_order_button);
        confirmButton.setBackgroundResource(R.drawable.common_round_red_bg);
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

    private void loadData(final boolean isFirst) {
        LoginVO vo = LoginManager.getLoginInfo(this);
        if (isFirst) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDailog.show();
        }
        MoteManager.fetchTaskProcess(moteTaskId, vo.token).startUI(new ApiCallback<TaskProcessVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (TaskProcessActivity.this.isFinishing()) {
                    return;
                }

                if (isFirst && !TaskProcessActivity.this.isFinishing()) {
                    dataLoadingView.loadFail();
                } else {
                    dataLoadingDailog.dismiss();
                }
            }

            @Override
            public void onSuccess(TaskProcessVO taskProcessVO) {
                if (TaskProcessActivity.this.isFinishing()) {
                    return;
                }

                if (isFirst) {
                    dataLoadingView.loadSuccess();
                } else {
                    dataLoadingDailog.dismiss();
                }
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

                if (status >=5 && status < 8) {
                    confirmGoodsLayout.setVisibility(View.VISIBLE);
                    String dateStr = DateUtil.getDateStr(System.currentTimeMillis());
                    confirmGoodsTimeView.setText(dateStr);
                    confirmGoodsDescView.setText("等待商家确认");
                }

                if (status >=8 || taskProcessVO.moteTask.finishStatus == 1) {
                    confirmGoodsLayout.setVisibility(View.VISIBLE);
                    String dateStr = DateUtil.getDateStr(taskProcessVO.moteTask.finishStatusTime);
                    confirmGoodsTimeView.setText(dateStr);
                    confirmGoodsDescView.setText("商家已确认");
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        goodsProcessFragment = ProcessGoodsOperateFragment.newInstance(taskProcessVO, new FinishCallback() {
            @Override
            public void doFinish(int type) {
                loadData(false);
            }
        });
        if (goodsProcessFragment == null) {
            fragmentTransaction.add(R.id.goods_process, goodsProcessFragment);
        } else {
            fragmentTransaction.replace(R.id.goods_process, goodsProcessFragment);
        }
        fragmentTransaction.commit();
    }


    private void initAndFillUploadPic(final TaskProcessVO taskProcessVO) {
        dropOrderNoView.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment uploadPicFragment = fragmentManager.findFragmentById(R.id.upload_pic);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        uploadPicFragment = ProcessUploadImageFragment.newInstance(taskProcessVO, false, new FinishCallback() {
            @Override
            public void doFinish(int type) {
                loadData(false);
            }
        });
        if (uploadPicFragment == null) {
            fragmentTransaction.add(R.id.upload_pic, uploadPicFragment);
        } else {
            fragmentTransaction.replace(R.id.upload_pic, uploadPicFragment);
        }
        fragmentTransaction.commit();
    }

    private void fillOrderStatus(TaskProcessVO taskProcessVO) {
        int status = taskProcessVO.moteTask.status;
        if (status == 1) {
            //计算时间
            editConfirmOrderNoLayout.setVisibility(View.VISIBLE);
            confirmOrderNoTextView.setVisibility(View.GONE);
            confirmButton.setText("确认");
            confirmButton.setTextColor(Color.WHITE);
            confirmButton.setBackgroundResource(R.drawable.common_round_red_bg);
            mHandler.post(timer);
        } else if (status == 2) {
            long orderNoTime = taskProcessVO.moteTask.orderNoTime;
            String orderNo = taskProcessVO.moteTask.orderNo;
            String dateStr = DateUtil.getDateStr(orderNoTime);
            editConfirmOrderNoLayout.setVisibility(View.VISIBLE);
            confirmOrderNoTextView.setVisibility(View.GONE);
            confirmTimeView.setText(dateStr);
            confirmOrderNoEditView.setText(orderNo);
            confirmButton.setText("修改");
            confirmButton.setTextColor(Color.WHITE);
            confirmButton.setBackgroundResource(R.drawable.common_round_blue_bg);
        } else {
            long orderNoTime = taskProcessVO.moteTask.orderNoTime;
            String dateStr = DateUtil.getDateStr(orderNoTime);
            String orderNo = taskProcessVO.moteTask.orderNo;
            editConfirmOrderNoLayout.setVisibility(View.GONE);
            confirmOrderNoTextView.setVisibility(View.VISIBLE);
            dropOrderNoView.setVisibility(View.GONE);
            confirmTimeView.setText(dateStr);
            confirmOrderNoTextView.setText(orderNo);
            confirmButton.setText("确认");
            confirmButton.setTextColor(Color.WHITE);
            confirmButton.setBackgroundResource(R.drawable.common_round_blue_bg);
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
        taskInfoDetailLayout.setVisibility(View.VISIBLE);
        jiantou= ((TextView) findViewById(R.id.jiantou_hide));
        jiantou.setVisibility(View.INVISIBLE);
        taskImageView = (NetworkImageView) findViewById(R.id.task_image);
        taskImageView.setDefaultImageResId(R.drawable.tupian);
        priceView = (TextView) findViewById(R.id.goods_price);
        shotFeeView = (TextView) findViewById(R.id.goods_shot_fee);
        addressView = (TextView) findViewById(R.id.goods_address);
        titleView = (TextView) findViewById(R.id.task_name);
        shotRequiredView = (TextView) findViewById(R.id.task_shot_required);
        goodsLinkView = (TextView) findViewById(R.id.goods_link);

        taskInfoSummaryView.setOnClickListener(this);
        findViewById(R.id.jiantou_upTask).setOnClickListener(this);
        goodsLinkView.setOnClickListener(this);
    }

    private void confirmOrderNo(long moteTaskId, String orderNo, String token) {
        dataLoadingDailog.show();
        MoteManager.addOrderNo(moteTaskId, orderNo, token).startUI(new ApiCallback<Boolean>() {
            @Override
            public void onError(int code, String errorInfo) {
                dataLoadingDailog.dismiss();
                Toast.makeText(TaskProcessActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                dataLoadingDailog.dismiss();
                taskProcessVO.moteTask.status = 2;
                loadData(false);
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
                TaskEvent taskEvent = new TaskEvent();
                taskEvent.moteTaskId = moteTaskId;
                taskEvent.status = taskProcessVO.moteTask.status;
                taskEvent.finishStatus = taskProcessVO.moteTask.finishStatus;
                EventBus.getDefault().post(taskEvent);
                this.finish();
                break;
            case R.id.jiantou_upTask:
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
                jiantou.setVisibility(View.INVISIBLE);
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
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(url);
//                intent.setData(content_url);
//                startActivity(intent);

                Intent goodsItemIntent = new Intent(TaskProcessActivity.this, H5Activity.class);
                H5Activity.H5Data goodsData = new H5Activity.H5Data();
                goodsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                goodsData.data = url;
                goodsData.showProcess = true;
                goodsData.showNav = true;
                goodsData.title = "商品";
                goodsData.canZoom=true;
                goodsItemIntent.putExtra(H5Activity.H5Data.H5_DATA, goodsData);
                startActivity(goodsItemIntent);
                goodsData.title = "商品详情";
                goodsItemIntent.putExtra(H5Activity.H5Data.H5_DATA, goodsData);
                startActivity(goodsItemIntent);
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(url);
//                intent.setData(content_url);
//                startActivity(intent);
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

                if (orderNo.length() < 11) {
                    Toast.makeText(this, "订单号不能小于11位", Toast.LENGTH_SHORT).show();
                    return;
                }

                confirmOrderNo(moteTaskId, orderNo, token);
                break;
            case R.id.drop_order:
                dataLoadingDailog.show();
                long taskId = taskProcessVO.task.id;
                LoginVO loginVO = LoginManager.getLoginInfo(this);
                MoteManager.giveupMoteTask(taskProcessVO.moteTask.id, loginVO.token).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        dataLoadingDailog.dismiss();
                        Toast.makeText(TaskProcessActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        dataLoadingDailog.dismiss();
                        Intent myTaskIntent = new Intent(TaskProcessActivity.this, MyTaskActivity.class);
                        myTaskIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myTaskIntent);
                    }

                    @Override
                    public void onProgress(int progress) {

                    }
                });


                break;
            case R.id.act_ls_fail_layout:
                loadData(true);
                break;
        }

    }

    public void onBackPressed() {
        super.onBackPressed();

        TaskEvent taskEvent = new TaskEvent();
        taskEvent.moteTaskId = moteTaskId;
        taskEvent.status = taskProcessVO.moteTask.status;
        taskEvent.finishStatus = taskProcessVO.moteTask.finishStatus;
        EventBus.getDefault().post(taskEvent);
    }

    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(timer);
    }
}

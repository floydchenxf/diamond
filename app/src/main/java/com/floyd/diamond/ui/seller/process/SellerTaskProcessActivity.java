package com.floyd.diamond.ui.seller.process;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.fragment.FinishCallback;
import com.floyd.diamond.ui.fragment.ProcessUploadImageFragment;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.view.UIAlertDialog;

/**
 * Created by floyd on 15-12-27.
 */
public class SellerTaskProcessActivity extends Activity implements View.OnClickListener {

    private static final String SELLER_MOTE_TASK_ID = "SELLER_MOTE_TASK_ID";

    private long moteTaskId;
    private ImageLoader mImageLoader;
    private DataLoadingView dataLoadingView;
    private Dialog dataLoadingDailog;

    private TaskProcessVO taskProcessVO;

    //----------------------接单信息----------------------//
    private TextView acceptTimeView;
    private View acceptStatusLayout;

    //----------------------订单号-----------------------//
    private TextView confirmOrderNoTextView; //不是当前输入订单状态
    private TextView confirmButton;
    private TextView confirmTimeView; //确认时间

    //----------------------商品处理------------------------//
    private TextView goodsProcessTimeTextView;
    private TextView goodsOrderTypeView;
    private TextView goodsOrderNoView;
    private View goodsProcessLayout;
    private View line4;
    private View line5;

    //-------------------mote信息-------------------------//
    private TextView moteInfoSummaryView; //mote资料
    private View moteDetailInfoLayout;//mote详情信息
    private NetworkImageView headImage;//头像
    private TextView nicknameView;//别名
    private TextView experienceView;//经验
    private TextView agreeKeyView;//满意度
    private CheckedTextView guanzhuView;//关注
    private ImageView jiantouView;


    private CheckedTextView finishView;
    //--------------------任务完成-------------------------//
    private View finishLayout;

    private LoginVO loginVO;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_process);
        moteTaskId = getIntent().getLongExtra(SELLER_MOTE_TASK_ID, 0l);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        dataLoadingDailog = DialogCreator.createDataLoadingDialog(this);
        loginVO = LoginManager.getLoginInfo(this);
        findViewById(R.id.title_back).setOnClickListener(this);
        initMoteInfo();
        initAcceptView();
        initOrderNoView();
        line4 = findViewById(R.id.line4);
        line5 = findViewById(R.id.line5);
        line4.setVisibility(View.GONE);
        line5.setVisibility(View.GONE);
        initGoodsLayout();
        initTaskFinishLayout();
        dataLoadingView.startLoading();
        loadData(true);
    }

    private void initTaskFinishLayout() {
        finishLayout = findViewById(R.id.confirm_task_layout);
        finishView = (CheckedTextView) findViewById(R.id.finish_button);
        finishView.setChecked(false);
        finishView.setTextColor(Color.parseColor("#999999"));
        finishView.setOnClickListener(null);
    }

    private void initGoodsLayout() {
        goodsProcessLayout = findViewById(R.id.goods_process_layout);
        goodsProcessLayout.setVisibility(View.GONE);
        goodsProcessTimeTextView = (TextView) findViewById(R.id.goods_process_time);
        goodsOrderNoView = (TextView) findViewById(R.id.goods_order_no_view);
        goodsOrderTypeView = (TextView) findViewById(R.id.goods_order_type_view);
    }

    /**
     * 模特信息
     */
    private void initMoteInfo() {
        moteInfoSummaryView = (TextView) findViewById(R.id.mote_info_summary);
        moteDetailInfoLayout = findViewById(R.id.mote_info_detail_layout);
        headImage = (NetworkImageView) findViewById(R.id.mote_head_image);
        nicknameView = (TextView) findViewById(R.id.mote_nickname_view);
        agreeKeyView = (TextView) findViewById(R.id.agree_key_view);
        experienceView = (TextView) findViewById(R.id.experience_view);
        moteDetailInfoLayout.setVisibility(View.GONE);
        findViewById(R.id.jiantou_up).setOnClickListener(this);
    }

    private void initAcceptView() {
        acceptStatusLayout = findViewById(R.id.accept_status_layout);
        acceptTimeView = (TextView) findViewById(R.id.accept_time);
    }

    private void initOrderNoView() {
        confirmButton = (TextView) findViewById(R.id.confirm_order_button);
        confirmTimeView = (TextView) findViewById(R.id.confirm_time);
        confirmOrderNoTextView = (TextView) findViewById(R.id.confirm_order_id);
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
                if (SellerTaskProcessActivity.this.isFinishing()) {
                    return;
                }

                if (isFirst && !SellerTaskProcessActivity.this.isFinishing()) {
                    dataLoadingView.loadFail();
                } else {
                    dataLoadingDailog.dismiss();
                }
            }

            @Override
            public void onSuccess(TaskProcessVO taskProcessVO) {
                if (SellerTaskProcessActivity.this.isFinishing()) {
                    return;
                }

                if (isFirst && !SellerTaskProcessActivity.this.isFinishing()) {
                    dataLoadingView.loadSuccess();
                } else {
                    dataLoadingDailog.dismiss();
                }

                SellerTaskProcessActivity.this.taskProcessVO = taskProcessVO;
                fillMoteInfo(taskProcessVO);
                fillAcceptStatus(taskProcessVO);
                fillOrderStatus(taskProcessVO);

                int status = taskProcessVO.moteTask.status;
                if (status > 2) {
                    initAndFillUploadPic(taskProcessVO);
                }

                if (status > 4) {
                    initAndFillGoodsOperate(taskProcessVO);
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    private void fillMoteInfo(TaskProcessVO taskProcessVO) {

    }

    private void fillOrderStatus(TaskProcessVO taskProcessVO) {
        long orderNoTime = taskProcessVO.moteTask.orderNoTime;
        String dateStr = DateUtil.getDateStr(orderNoTime);
        String orderNo = taskProcessVO.moteTask.orderNo;
        confirmTimeView.setText(dateStr);
        confirmOrderNoTextView.setText(orderNo);
    }

    private void fillAcceptStatus(TaskProcessVO taskProcessVO) {
        long time = taskProcessVO.moteTask.acceptedTime;
        String dateStr = DateUtil.getDateStr(time);
        acceptTimeView.setText(dateStr);
    }

    private void initAndFillUploadPic(final TaskProcessVO taskProcessVO) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment uploadPicFragment = fragmentManager.findFragmentById(R.id.upload_pic);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        uploadPicFragment = ProcessUploadImageFragment.newInstance(taskProcessVO, new FinishCallback() {
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

    private void initAndFillGoodsOperate(TaskProcessVO taskProcessVO) {
        line4.setVisibility(View.VISIBLE);
        goodsProcessLayout.setVisibility(View.VISIBLE);
        int status = taskProcessVO.moteTask.status;
        if (status == 5 || taskProcessVO.moteTask.selfBuyTime != 0) {
            String time = DateUtil.getDateStr(taskProcessVO.moteTask.selfBuyTime);
            goodsProcessTimeTextView.setText(time);
            goodsOrderTypeView.setVisibility(View.GONE);
            goodsOrderNoView.setText("模特已购");
        }

        if (status == 6 || taskProcessVO.moteTask.returnItemTime != 0) {
            String time = DateUtil.getDateStr(taskProcessVO.moteTask.returnItemTime);
            goodsProcessTimeTextView.setText(time);
            goodsOrderTypeView.setVisibility(View.VISIBLE);
            goodsOrderTypeView.setText("承运来源：" + taskProcessVO.moteTask.expressCompanyId);
            goodsOrderNoView.setText("运单编号：" + taskProcessVO.moteTask.orderNo);
        }

        if (status > 4 && status < 7) {
            line5.setVisibility(View.VISIBLE);
            finishLayout.setVisibility(View.VISIBLE);
            finishView.setChecked(true);
            finishView.setTextColor(Color.WHITE);
            finishView.setOnClickListener(this);
        }

        if (status == 7 || status == 8) {
            finishView.setChecked(false);
            finishView.setTextColor(Color.parseColor("#999999"));
            finishView.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_button:
                UIAlertDialog.Builder builder = new UIAlertDialog.Builder(this);
                builder.setMessage("选择不满意，此模特以后将无法承接您的任务!")
                        .setCancelable(true)
                        .setNegativeButton("不满意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dataLoadingDailog.show();
                                SellerManager.finishAndApproveMoteTask(taskProcessVO.moteTask.id, 2, loginVO.token).startUI(new ApiCallback<Boolean>() {
                                    @Override
                                    public void onError(int code, String errorInfo) {
                                        dataLoadingDailog.dismiss();
                                        Toast.makeText(SellerTaskProcessActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        dataLoadingDailog.dismiss();
                                        loadData(false);
                                    }

                                    @Override
                                    public void onProgress(int progress) {

                                    }
                                });

                            }
                        })
                        .setPositiveButton("满意",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                        dataLoadingDailog.show();
                                        SellerManager.finishAndApproveMoteTask(taskProcessVO.moteTask.id, 1, loginVO.token).startUI(new ApiCallback<Boolean>() {
                                            @Override
                                            public void onError(int code, String errorInfo) {
                                                dataLoadingDailog.dismiss();
                                                Toast.makeText(SellerTaskProcessActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                dataLoadingDailog.dismiss();
                                                loadData(false);
                                            }

                                            @Override
                                            public void onProgress(int progress) {

                                            }
                                        });
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.jiantou_up:
                Drawable down = null;
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    down = getResources().getDrawable(R.drawable.jiantou_down, getTheme());
                } else {
                    down = getResources().getDrawable(R.drawable.jiantou_down);
                }
                down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
                moteInfoSummaryView.setCompoundDrawables(null, null, down, null);
                moteDetailInfoLayout.setVisibility(View.GONE);
                break;
            case R.id.mote_info_summary:
                moteInfoSummaryView.setCompoundDrawables(null, null, null, null);
                moteDetailInfoLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.act_ls_fail_layout:
                loadData(true);
                break;


        }

    }
}

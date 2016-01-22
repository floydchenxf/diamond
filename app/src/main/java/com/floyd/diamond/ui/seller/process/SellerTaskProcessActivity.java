package com.floyd.diamond.ui.seller.process;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.MoteDetail1;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.DateUtil;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.UserVO;
import com.floyd.diamond.biz.vo.process.TaskProcessVO;
import com.floyd.diamond.event.SellerTaskEvent;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.activity.ExpressActivity;
import com.floyd.diamond.ui.activity.MoteDetailActivity;
import com.floyd.diamond.ui.fragment.FinishCallback;
import com.floyd.diamond.ui.fragment.ProcessUploadImageFragment;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.view.UIAlertDialog;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by floyd on 15-12-27.
 */
public class SellerTaskProcessActivity extends Activity implements View.OnClickListener {

    public static final String SELLER_MOTE_TASK_ID = "SELLER_MOTE_TASK_ID";

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
    private View line1;
    private View orderLayout;

    //-------------------mote信息-------------------------//
    private TextView moteInfoSummaryView; //mote资料
    private View moteDetailInfoLayout;//mote详情信息
    private NetworkImageView headImage;//头像
    private TextView nicknameView;//别名
    private TextView experienceView;//经验
    private TextView agreeKeyView;//满意度
    private CheckedTextView guanzhuView;//关注
    private ImageView jiantouView;

    private View waitLine;
    private View waitLayout;
    private TextView waitTimeView;
    private TextView waitStatusView;


    private CheckedTextView finishView;
    //--------------------任务完成-------------------------//
    private View finishLayout;

    private LoginVO loginVO;
    private float oneDp;

    private MoteDetail1 moteDetail;
    private boolean isFollow;
    private TextView titleNameView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_task_process);
        titleNameView = (TextView)findViewById(R.id.title_name);
        titleNameView.setText("任务进度");
        titleNameView.setVisibility(View.VISIBLE);

        oneDp = this.getResources().getDimension(R.dimen.one_dp);
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
        initWaitLayout();
        hiddenWaitLayout();
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
        finishLayout.setVisibility(View.GONE);
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
        headImage.setDefaultImageResId(R.drawable.tupian);
        nicknameView = (TextView) findViewById(R.id.mote_nickname_view);
        agreeKeyView = (TextView) findViewById(R.id.agree_key_view);
        experienceView = (TextView) findViewById(R.id.experience_view);
        moteDetailInfoLayout.setVisibility(View.GONE);
        guanzhuView = (CheckedTextView) findViewById(R.id.guanzhu);
        findViewById(R.id.jiantou_up).setOnClickListener(this);
        moteInfoSummaryView.setOnClickListener(this);
    }

    private void initAcceptView() {
        acceptStatusLayout = findViewById(R.id.accept_status_layout);
        acceptTimeView = (TextView) findViewById(R.id.accept_time);
    }

    private void initOrderNoView() {
        orderLayout = findViewById(R.id.confirm_order_layout);
        orderLayout.setVisibility(View.GONE);
        line1 = findViewById(R.id.line1);
        line1.setVisibility(View.GONE);
        confirmButton = (TextView) findViewById(R.id.confirm_order_button);
        confirmTimeView = (TextView) findViewById(R.id.confirm_time);
        confirmOrderNoTextView = (TextView) findViewById(R.id.confirm_order_id);
    }

    private void hiddenWaitLayout() {
        waitLayout.setVisibility(View.GONE);
        waitLine.setVisibility(View.GONE);
    }

    private void showWaitLayout(String time, String statusName) {
        waitLine.setVisibility(View.VISIBLE);
        waitLayout.setVisibility(View.VISIBLE);
        waitTimeView.setText(time);
        waitStatusView.setText(statusName);
    }

    private void initWaitLayout() {
        waitLine = this.findViewById(R.id.wait_line);
        waitLayout = this.findViewById(R.id.wait_layout);
        waitTimeView = (TextView) this.findViewById(R.id.wait_time_view);
        waitStatusView = (TextView) this.findViewById(R.id.wait_status_view);
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

                int status = taskProcessVO.moteTask.status;
                fillAcceptStatus(taskProcessVO);
                if (status > 1) {
                    fillOrderStatus(taskProcessVO);
                }

                if (status > 2) {
                    initAndFillUploadPic(taskProcessVO);
                }

                if (status > 4) {
                    initAndFillGoodsOperate(taskProcessVO);
                }

                if (status <= 1) {
                    String dateTime = DateUtil.getDateStr(System.currentTimeMillis());
                    showWaitLayout(dateTime, "等待模特上传订单");
                } else if (status > 1 && status <=3) {
                    String dateTime = DateUtil.getDateStr(System.currentTimeMillis());
                    showWaitLayout(dateTime, "等待模特上传照片");
                } else if (status == 4) {
                    String dateTime = DateUtil.getDateStr(System.currentTimeMillis());
                    showWaitLayout(dateTime, "等待模特上传单号");
                } else {
                    hiddenWaitLayout();
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    private void fillMoteInfo(TaskProcessVO taskProcessVO) {
        dataLoadingDailog.show();
        UserVO userVO = taskProcessVO.user;
        MoteManager.fetchMoteDetailInfo(userVO.id, LoginManager.getLoginInfo(this).token).startUI(new ApiCallback<MoteDetail1>() {
            @Override
            public void onError(int code, String errorInfo) {
                dataLoadingDailog.dismiss();
                Toast.makeText(SellerTaskProcessActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                moteDetailInfoLayout.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(MoteDetail1 vo) {
                dataLoadingDailog.dismiss();
                SellerTaskProcessActivity.this.moteDetail = vo;
                String imageUrl = vo.getAvartUrl();
                if (!TextUtils.isEmpty(imageUrl)) {
                    headImage.setImageUrl(vo.getPreviewUrl(), mImageLoader, new BitmapProcessor() {
                        @Override
                        public Bitmap processBitmpa(Bitmap bitmap) {
                            return ImageUtils.getCircleBitmap(bitmap, 90 * oneDp);
                        }
                    });
                    headImage.setOnClickListener(SellerTaskProcessActivity.this);
                    nicknameView.setText(vo.getNickname());
                }

                isFollow = vo.isFollow;
                if (isFollow) {
                    guanzhuView.setText("已关注");
                    guanzhuView.setTextColor(Color.WHITE);
                    guanzhuView.setChecked(true);
                } else {
                    Object num = vo.getFollowNum();
                    guanzhuView.setText("关注度:" + num);
                    guanzhuView.setTextColor(Color.parseColor("#666666"));
                    guanzhuView.setChecked(false);
                }

                guanzhuView.setOnClickListener(SellerTaskProcessActivity.this);
                experienceView.setText("经验值：" + vo.getOrderNum());
                agreeKeyView.setText("满意度：" + vo.goodeEvalRate);
                moteDetailInfoLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }

    private void fillOrderStatus(TaskProcessVO taskProcessVO) {
        line1.setVisibility(View.VISIBLE);
        orderLayout.setVisibility(View.VISIBLE);
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
        uploadPicFragment = ProcessUploadImageFragment.newInstance(taskProcessVO, true, new FinishCallback() {
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
            goodsOrderNoView.setOnClickListener(null);
        }

        if (status == 6 || taskProcessVO.moteTask.returnItemTime != 0) {
            String time = DateUtil.getDateStr(taskProcessVO.moteTask.returnItemTime);
            goodsProcessTimeTextView.setText(time);
            goodsOrderTypeView.setVisibility(View.VISIBLE);
            goodsOrderTypeView.setText("承运来源：" + taskProcessVO.moteTask.expressCompanyId);
            goodsOrderNoView.setText("运单编号：" + taskProcessVO.moteTask.expressNo);
            goodsOrderNoView.setOnClickListener(this);
        }

        if (status > 4 && status < 7) {
            line5.setVisibility(View.VISIBLE);
            finishLayout.setVisibility(View.VISIBLE);
            finishView.setChecked(true);
            finishView.setTextColor(Color.WHITE);
            finishView.setOnClickListener(this);
        }

        if (status == 7 || status == 8 || taskProcessVO.moteTask.finishStatus == 1) {
            finishLayout.setVisibility(View.VISIBLE);
            finishView.setChecked(false);
            line5.setVisibility(View.VISIBLE);
            finishView.setTextColor(Color.parseColor("#999999"));
            finishView.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish_button:
                UIAlertDialog.Builder builder = new UIAlertDialog.Builder(this);
                SpannableString message = new SpannableString("您选择不满意，此模特以后将无法承接您的任务!");
                message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setMessage(message)
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
                                        SellerTaskEvent event = new SellerTaskEvent();
                                        event.finishStatus = 1;
                                        event.status = 8;
                                        event.moteTaskId = taskProcessVO.moteTask.id;
                                        EventBus.getDefault().post(event);
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
                fillMoteInfo(taskProcessVO);
                break;
            case R.id.act_ls_fail_layout:
                loadData(true);
                break;
            case R.id.title_back:
                this.finish();
                break;
            case R.id.mote_head_image:
                Intent moteDetailIntent = new Intent(SellerTaskProcessActivity.this, MoteDetailActivity.class);
                moteDetailIntent.putExtra(MoteDetailActivity.MOTE_ID, taskProcessVO.moteTask.userId);
                startActivity(moteDetailIntent);
                break;
            case R.id.guanzhu:
                doGuanzhu();
                break;
            case R.id.goods_order_no_view:
                Intent orderIntent = new Intent(this, ExpressActivity.class);
                orderIntent.putExtra(ExpressActivity.EXPRESS_MOTE_TASK_ID, taskProcessVO.moteTask.id);
                startActivity(orderIntent);
                break;
        }

    }

    private void doGuanzhu() {
        if (!LoginManager.isLogin(this)) {
            return;
        }
        dataLoadingDailog.show();
        LoginVO vo = LoginManager.getLoginInfo(this);
        if (!isFollow) {
            MoteManager.addFollow(taskProcessVO.user.id, vo.token).startUI(new ApiCallback<Integer>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(SellerTaskProcessActivity.this, "关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!SellerTaskProcessActivity.this.isFinishing()) {
                        dataLoadingDailog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Integer num) {
                    Toast.makeText(SellerTaskProcessActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    if (!SellerTaskProcessActivity.this.isFinishing()) {
                        dataLoadingDailog.dismiss();
                    }
                    guanzhuView.setText("已关注");
                    guanzhuView.setChecked(true);
                    guanzhuView.setTextColor(Color.WHITE);
                    isFollow = true;
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        } else {
            List<Long> moteIds = Arrays.asList(new Long[]{taskProcessVO.user.id});
            MoteManager.cancelFollow(moteIds, vo.token).startUI(new ApiCallback<Boolean>() {
                @Override
                public void onError(int code, String errorInfo) {
                    Toast.makeText(SellerTaskProcessActivity.this, "取消关注失败:" + errorInfo, Toast.LENGTH_SHORT).show();
                    if (!SellerTaskProcessActivity.this.isFinishing()) {
                        dataLoadingDailog.dismiss();
                    }
                }

                @Override
                public void onSuccess(Boolean num) {
                    Toast.makeText(SellerTaskProcessActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    if (!SellerTaskProcessActivity.this.isFinishing()) {
                        dataLoadingDailog.dismiss();
                    }
                    guanzhuView.setText("关注度:" + moteDetail.followNum);
                    guanzhuView.setTextColor(Color.parseColor("#666666"));
                    guanzhuView.setChecked(false);
                    isFollow = false;
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        }

    }
}

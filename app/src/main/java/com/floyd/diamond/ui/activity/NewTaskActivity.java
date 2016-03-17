package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.biz.vo.mote.TaskItemVO;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.floyd.diamond.ui.loading.DefaultDataLoadingView;
import com.floyd.diamond.ui.multiimage.MultiImageActivity;
import com.floyd.diamond.ui.multiimage.base.MulitImageVO;
import com.floyd.diamond.ui.multiimage.base.PicViewObject;
import com.floyd.diamond.ui.view.UIAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class NewTaskActivity extends Activity implements View.OnClickListener {

    public static final String TASK_TYPE_ITEM_OBJECT = "task_type_item_object";
    public static final String TASK_TYPE_ITEM_ID = "TASK_TYPE_ITEM_ID";

    private ImageLoader mImageLoader;
    private LinearLayout backView;
    private TextView shotDescView;
    private TextView taskProductNameView;

    private TextView priceView;
    private TextView shotFeeView;
    private TextView shotAreaIdView;
    private TextView selfBuyRateView;

    private NetworkImageView taskImageView;
    private ImageView defaultImageView;

    private CheckedTextView newTaskButton;

    private TaskItemVO taskItemVO;
    private Dialog dataLoadingDialog;
    private DataLoadingView dataLoadingView;

    private String imageUrl;
    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        dataLoadingDialog = DialogCreator.createDataLoadingDialog(this);
        dataLoadingView = new DefaultDataLoadingView();
        dataLoadingView.initView(findViewById(R.id.act_lsloading), this);
        taskId = getIntent().getLongExtra(TASK_TYPE_ITEM_ID, 0l);

        initView();
        loadData(true);
    }

    private void loadData(final boolean firstLoad) {
        if (firstLoad) {
            dataLoadingView.startLoading();
        } else {
            dataLoadingDialog.show();
        }

        LoginVO loginVO = LoginManager.getLoginInfo(this);
        String token = loginVO == null ? "" : loginVO.token;

        MoteManager.getDetailByTaskId(taskId, token).startUI(new ApiCallback<TaskItemVO>() {
            @Override
            public void onError(int code, String errorInfo) {
                if (firstLoad) {
                    dataLoadingView.loadFail();
                } else {
                    dataLoadingDialog.dismiss();
                }
                Toast.makeText(NewTaskActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(TaskItemVO taskItemVO) {
                if (firstLoad) {
                    dataLoadingView.loadSuccess();
                } else {
                    dataLoadingDialog.dismiss();
                }
                NewTaskActivity.this.taskItemVO = taskItemVO;

                shotDescView.setText(taskItemVO.shotDesc);
                taskProductNameView.setText(taskItemVO.title);
                priceView.setText("商品售价：" + taskItemVO.price + "");
                shotFeeView.setText("酬金：" + taskItemVO.shotFee + "");
                if (taskItemVO.areaName!=null){
                    shotAreaIdView.setText("所在地：" + taskItemVO.areaName);
                }else {
                    shotAreaIdView.setText("所在地：" );
                }
                selfBuyRateView.setText("自购价格：" + taskItemVO.selfBuyOff + "");

                //任务状态为1的时候为红色，2位不可接灰色（选中为红色）
                if (taskItemVO.acceptStauts==1) {
                    newTaskButton.setOnClickListener(NewTaskActivity.this);
                    newTaskButton.setChecked(true);
                    newTaskButton.setTextColor(Color.WHITE);
                } else if (taskItemVO.acceptStauts==2){
                    newTaskButton.setOnClickListener(null);
                    newTaskButton.setTextColor(Color.parseColor("#666666"));
                    newTaskButton.setChecked(false);
                }


                taskImageView.setDefaultImageResId(R.drawable.tupian);
                taskImageView.setImageUrl(taskItemVO.getDetailImageUrl(), mImageLoader, new BitmapProcessor() {
                    @Override
                    public Bitmap processBitmpa(Bitmap bitmap) {
                        defaultImageView.setVisibility(View.GONE);
                        return bitmap;
                    }
                });
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }


    private void initView() {
        backView = (LinearLayout) findViewById(R.id.back);
        shotDescView = (TextView) findViewById(R.id.task_shot_required);
        taskProductNameView = (TextView) findViewById(R.id.task_product_name);
        priceView = (TextView) findViewById(R.id.price);
        shotFeeView = (TextView) findViewById(R.id.shot_fee);
        shotAreaIdView = (TextView) findViewById(R.id.shot_area_id);
        selfBuyRateView = (TextView) findViewById(R.id.self_buy_rate);
        defaultImageView = (ImageView) findViewById(R.id.default_image);
        taskImageView = (NetworkImageView) findViewById(R.id.task_image);
        newTaskButton = (CheckedTextView) findViewById(R.id.new_task_button);
        LinearLayout click= ((LinearLayout) findViewById(R.id.new_task_button_layout));
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","点击不跳转！");
            }
        });

        backView.setOnClickListener(this);
        taskImageView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.task_image:
                List<PicViewObject> picViewList = new ArrayList<PicViewObject>();
                PicViewObject picObject = new PicViewObject();
                picObject.setPicPreViewUrl(taskItemVO.getPreviewImageUrl());
                picObject.setPicUrl(taskItemVO.getDetailImageUrl());
                picObject.setPicId(1l);
                picObject.setPicType(PicViewObject.IMAGE);
                picViewList.add(picObject);
                MulitImageVO mulitImageVO = new MulitImageVO(0, picViewList);
                Intent it = new Intent(NewTaskActivity.this, MultiImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MultiImageActivity.MULIT_IMAGE_VO, mulitImageVO);
                it.putExtra(MultiImageActivity.MULIT_IMAGE_VO, bundle);
                it.putExtra(MultiImageActivity.MULIT_IMAGE_PICK_MODE,
                        MultiImageActivity.MULIT_IMAGE_PICK_MODE_PREVIEW);
                startActivity(it);
                break;
            case R.id.new_task_button:
                boolean logined = LoginManager.isLogin(this);
                if (!logined) {
                    return;
                }
                dataLoadingDialog.show();
                LoginVO loginVO = LoginManager.getLoginInfo(this);
                MoteManager.fetchNewTask(taskId, loginVO.token).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        if (!NewTaskActivity.this.isFinishing()) {
                            dataLoadingDialog.dismiss();
                            Toast.makeText(NewTaskActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (!NewTaskActivity.this.isFinishing()) {
                            dataLoadingDialog.dismiss();
                        }

                        newTaskButton.setChecked(false);
                        newTaskButton.setTextColor(Color.parseColor("#666666"));
                        newTaskButton.setEnabled(false);
                        UIAlertDialog.Builder builder = new UIAlertDialog.Builder(NewTaskActivity.this);
                        SpannableString message = new SpannableString("亲，您已抢单成功，请半小时之内完成下单并去任务列表填写订单号");
                        message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setMessage(message)
                                .setCancelable(true)
                                .setPositiveButton(R.string.confirm_order,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.dismiss();
                                            }
                                        }).setNegativeButton("商品链接", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = taskItemVO.url;
                                if (TextUtils.isEmpty(url)) {
                                    Toast.makeText(NewTaskActivity.this, "无效商品链接!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Intent goodsItemIntent = new Intent(NewTaskActivity.this, H5Activity.class);
                                H5Activity.H5Data goodsData = new H5Activity.H5Data();
                                goodsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                                goodsData.data = url;
                                goodsData.showProcess = true;
                                //判断是否在app中打开网页
                                goodsData.showNav = true;
                                goodsData.title = "商品详情";
                                goodsItemIntent.putExtra(H5Activity.H5Data.H5_DATA, goodsData);
                                startActivity(goodsItemIntent);
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
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

    public void onDestory() {
        super.onDestroy();
    }
}

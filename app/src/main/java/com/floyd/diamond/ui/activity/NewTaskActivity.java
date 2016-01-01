package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.floyd.diamond.event.AcceptTaskEvent;
import com.floyd.diamond.ui.DialogCreator;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.multiimage.MultiImageActivity;
import com.floyd.diamond.ui.multiimage.base.MulitImageVO;
import com.floyd.diamond.ui.multiimage.base.PicViewObject;
import com.floyd.diamond.ui.view.UIAlertDialog;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class NewTaskActivity extends Activity implements View.OnClickListener {

    public static final String TASK_TYPE_ITEM_OBJECT = "task_type_item_object";

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
    private Dialog loadingDialog;

    private String imageUrl;
    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        mImageLoader = ImageLoaderFactory.createImageLoader();
        loadingDialog = DialogCreator.createDataLoadingDialog(this);

        taskItemVO = (TaskItemVO) getIntent().getSerializableExtra(TASK_TYPE_ITEM_OBJECT);
        backView = (LinearLayout) findViewById(R.id.back);
        backView.setOnClickListener(this);

        shotDescView = (TextView) findViewById(R.id.task_shot_required);
        shotDescView.setText(taskItemVO.shotDesc);

        taskProductNameView = (TextView) findViewById(R.id.task_product_name);
        taskProductNameView.setText(taskItemVO.title);

        priceView = (TextView) findViewById(R.id.price);
        shotFeeView = (TextView) findViewById(R.id.shot_fee);
        shotAreaIdView = (TextView) findViewById(R.id.shot_area_id);
        selfBuyRateView = (TextView) findViewById(R.id.self_buy_rate);

        priceView.setText(taskItemVO.price + "");
        shotFeeView.setText(taskItemVO.shotFee + "");
        shotAreaIdView.setText(taskItemVO.areaName);
        selfBuyRateView.setText(taskItemVO.selfBuyRate + "");


        defaultImageView = (ImageView) findViewById(R.id.default_image);
        taskImageView = (NetworkImageView) findViewById(R.id.task_image);
        taskImageView.setOnClickListener(this);

        newTaskButton = (CheckedTextView) findViewById(R.id.new_task_button);
        if (!taskItemVO.isAccepted) {
            newTaskButton.setOnClickListener(this);
            newTaskButton.setChecked(true);
            newTaskButton.setTextColor(Color.WHITE);
        } else {
            newTaskButton.setTextColor(Color.parseColor("#666666"));
            newTaskButton.setChecked(false);
        }
        imageUrl = taskItemVO.getDetailImageUrl();
        taskId = taskItemVO.id;
    }

    @Override
    protected void onResume() {
        super.onResume();

        taskImageView.setImageUrl(imageUrl, mImageLoader, new BitmapProcessor() {
            @Override
            public Bitmap processBitmpa(Bitmap bitmap) {
                defaultImageView.setVisibility(View.GONE);
                return bitmap;
            }
        });

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
                loadingDialog.show();
                LoginVO loginVO = LoginManager.getLoginInfo(this);
                if (loginVO == null) {
                    Toast.makeText(this, "未登录不能抢单", Toast.LENGTH_SHORT).show();
                    return;
                }
                MoteManager.fetchNewTask(taskId, loginVO.token).startUI(new ApiCallback<Boolean>() {
                    @Override
                    public void onError(int code, String errorInfo) {
                        if (!NewTaskActivity.this.isFinishing()) {
                            loadingDialog.dismiss();
                            Toast.makeText(NewTaskActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (!NewTaskActivity.this.isFinishing()) {
                            loadingDialog.dismiss();
                        }

                        newTaskButton.setChecked(false);
                        newTaskButton.setTextColor(Color.parseColor("#666666"));
                        newTaskButton.setEnabled(false);
                        EventBus.getDefault().post(new AcceptTaskEvent(taskItemVO.id));

                        UIAlertDialog.Builder builder = new UIAlertDialog.Builder(NewTaskActivity.this);
                        builder.setMessage("亲！您已抢单，请半小时之内完成下单并去任务列表填写订单号")
                                .setCancelable(true)
                                .setPositiveButton(R.string.confirm,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
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
        }

    }

    public void onDestory() {
        super.onDestroy();
    }
}

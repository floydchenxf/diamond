package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
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
import com.floyd.diamond.bean.CorrectTime;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.MoteManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.DateUtil;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private String resultUrl;

    private TextView timeView;
    static long minute = -1;
    static long second = -1;
    static long hour = -1;
    final static String tag = "tag";
    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            System.out.println("handle!");
            if (hour == 0) {
                if (minute == 0) {
                    if (second == 0) {
                        timeView.setText("Time out !");
                        timeView.setVisibility(View.GONE);
                        //任务状态为1的时候为红色，2位不可接灰色（选中为红色）
                        if (taskItemVO.acceptStauts == 1) {
                            newTaskButton.setOnClickListener(NewTaskActivity.this);
                            newTaskButton.setChecked(true);
                            newTaskButton.setTextColor(Color.WHITE);
                            newTaskButton.setText("抢单");
//                            showQiangdanDialog();
                        } else if (taskItemVO.acceptStauts == 2) {
                            newTaskButton.setOnClickListener(NewTaskActivity.this);
                            newTaskButton.setTextColor(Color.parseColor("#666666"));
                            newTaskButton.setChecked(false);
                            newTaskButton.setText("抢单");
                        }
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        if (timerTask != null) {
                            timerTask = null;
                        }
                    } else {
                        second--;
                        setTime(hour, minute, second);
                    }
                } else {
                    if (second == 0) {
                        //hour==0,second==0,判断分钟来决定倒计时的时间
//                        if (minute==5){
//                            correctTime();
////                            loadData(false);
//                        }
                        second = 59;
                        minute--;
                        setTime(hour, minute, second);
                    } else {
                        second--;
                        setTime(hour, minute, second);
                    }
                }
            } else {
                if (minute == 0) {
                    if (second == 0) {
                        second = 59;
                        minute = 59;
                        hour--;
                        setTime(hour, minute, second);
                    } else {
                        second--;
                        setTime(hour, minute, second);
                    }
                } else {
                    if (second == 0) {
                        second = 59;
                        minute--;
                        setTime(hour, minute, second);

                    } else {
                        second--;
                        setTime(hour, minute, second);
                    }
                }

            }

        }

        ;
    };


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

                if (GlobalParams.isDebug) {
                    String result = DateUtil.getDateTime(taskItemVO.databaseTime);
                    Log.e("TAG", DateUtil.getDateTime(taskItemVO.futurePublishTime) + "");
                }

                if (firstLoad) {
                    dataLoadingView.loadSuccess();
                } else {
                    dataLoadingDialog.dismiss();
                }
                NewTaskActivity.this.taskItemVO = taskItemVO;

                shotDescView.setText(taskItemVO.shotDesc);
                taskProductNameView.setText(taskItemVO.title);
                priceView.setText("商品售价：" + taskItemVO.price + "");
                shotFeeView.setText("积分：" + taskItemVO.shotFee + "");
                if (taskItemVO.areaName != null) {
                    shotAreaIdView.setText("所在地：" + taskItemVO.areaName);
                } else {
                    shotAreaIdView.setText("所在地：");
                }
                selfBuyRateView.setText("自购折扣：" + taskItemVO.selfBuyOff + "");

                if (taskItemVO.diffTime <= 0) {
                    timeView.setVisibility(View.GONE);
                    //任务状态为1的时候为红色，2位不可接灰色（选中为红色）
                    if (taskItemVO.acceptStauts == 1) {
                        newTaskButton.setOnClickListener(NewTaskActivity.this);
                        newTaskButton.setChecked(true);
                        newTaskButton.setText("抢单");
                        newTaskButton.setTextColor(Color.WHITE);
                    } else if (taskItemVO.acceptStauts == 2) {
                        newTaskButton.setOnClickListener(NewTaskActivity.this);
                        newTaskButton.setTextColor(Color.parseColor("#666666"));
                        newTaskButton.setChecked(false);
                        newTaskButton.setText("抢单");
                    }

                } else {
                    timeView.setVisibility(View.VISIBLE);
                    getTimer(DateUtil.getDateTime(taskItemVO.diffTime));
                    if (taskItemVO.isCustomTask) {
                        newTaskButton.setOnClickListener(NewTaskActivity.this);
                        newTaskButton.setText("查看任务要求");
                        newTaskButton.setChecked(true);
                        newTaskButton.setTextColor(Color.WHITE);
                    } else {
                        newTaskButton.setOnClickListener(NewTaskActivity.this);
                        newTaskButton.setText("预览");
                        newTaskButton.setChecked(true);
                        newTaskButton.setTextColor(Color.WHITE);
                    }
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

    /**
     * 矫正时间
     */

    public void correctTime(){
        String token=LoginManager.getLoginInfo(NewTaskActivity.this).token;
        if (GlobalParams.isDebug){
            Log.e("TAG",taskItemVO.moteTaskId+"id1"+taskItemVO.id);
        }
        SellerManager.correctTime(token,taskItemVO.id+"").startUI(new ApiCallback<CorrectTime>() {
            @Override
            public void onError(int code, String errorInfo) {

                Log.e("TAG",errorInfo);

            }

            @Override
            public void onSuccess(CorrectTime correctTime) {

                if (correctTime.isSuccess()){
                    getTimer(correctTime.getData()+"");
                }
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

        timeView= ((TextView) findViewById(R.id.timer));
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/quartzregulardb.ttf");
        timeView.setTypeface(typeFace);

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
                if (taskItemVO.isTimerTask){
                    if (!(timeView.getText().toString().equals("Time out !"))){
                        //倒计时
                        if (taskItemVO.isCustomTask){
                            //自定义任务，跳转h5的webview
                            String url = taskItemVO.h5Url;
                            if (GlobalParams.isDebug){
                                Log.e("TAG",url+"h5");
                            }
                            if (TextUtils.isEmpty(url)) {
                                Toast.makeText(NewTaskActivity.this, "无效链接!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent goodsItemIntent = new Intent(NewTaskActivity.this, H5Activity.class);
                            H5Activity.H5Data goodsData = new H5Activity.H5Data();
                            goodsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                            goodsData.data = url;
                            goodsData.showProcess = true;
                            //判断是否在app中打开网页
                            goodsData.showNav = true;
                            if (taskItemVO.isCustomTask){
                                goodsData.title = "任务要求";
                            }else{
                                goodsData.title = "商品详情";
                            }

                            goodsItemIntent.putExtra(H5Activity.H5Data.H5_DATA, goodsData);
                            startActivity(goodsItemIntent);

                        }else{
                            //跳转普通链接
                            String url = taskItemVO.url;
                            if (GlobalParams.isDebug){
                                Log.e("TAG",url+"p");
                            }
                            if (TextUtils.isEmpty(url)) {
                                Toast.makeText(NewTaskActivity.this, "无效链接!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent goodsItemIntent = new Intent(NewTaskActivity.this, H5Activity.class);
                            H5Activity.H5Data goodsData = new H5Activity.H5Data();
                            goodsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                            goodsData.data = url;
                            goodsData.showProcess = true;
                            //判断是否在app中打开网页
                            goodsData.showNav = true;
                            if (taskItemVO.isCustomTask){
                                goodsData.title = "任务要求";
                            }else{
                                goodsData.title = "商品详情";
                            }

                            goodsItemIntent.putExtra(H5Activity.H5Data.H5_DATA, goodsData);
                            startActivity(goodsItemIntent);
                        }
                    }else{
                        boolean logined = LoginManager.isLogin(this);
                        if (!logined) {
                            return;
                        }
                        dataLoadingDialog.show();

                        showQiangdanDialog();
                    }

                }else{
//                    if (taskItemVO.acceptStauts==2){
//                        Log.e("TAG","灰色按钮");
//                    }else{
                        boolean logined = LoginManager.isLogin(this);
                        if (!logined) {
                            return;
                        }
                        dataLoadingDialog.show();

                        showQiangdanDialog();
//                    }

                }

                break;
            case R.id.act_ls_fail_layout:
                loadData(true);
                break;
        }

    }

    public  void showQiangdanDialog(){
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
                String tiaozhuan;
                if (taskItemVO.isCustomTask){
                    tiaozhuan= "任务要求";
                }else{
                   tiaozhuan = "商品详情";
                }
                UIAlertDialog.Builder builder = new UIAlertDialog.Builder(NewTaskActivity.this);
                SpannableString message = new SpannableString("亲，您已抢单成功，请半小时之内完成下单并去任务列表填写订单号");
                message.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                message.setSpan(new ForegroundColorSpan(Color.parseColor("#d4377e")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setMessage(message)
                        .setTitle("抢单成功")
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm_order,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(tiaozhuan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (taskItemVO.isCustomTask) {
                            resultUrl = taskItemVO.h5Url;
                        } else {
                            resultUrl = taskItemVO.url;
                        }

                        if (TextUtils.isEmpty(resultUrl)) {
                            Toast.makeText(NewTaskActivity.this, "无效链接!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent goodsItemIntent = new Intent(NewTaskActivity.this, H5Activity.class);
                        H5Activity.H5Data goodsData = new H5Activity.H5Data();
                        goodsData.dataType = H5Activity.H5Data.H5_DATA_TYPE_URL;
                        goodsData.data = resultUrl;
                        goodsData.showProcess = true;
                        //判断是否在app中打开网页
                        goodsData.showNav = true;
                        if (taskItemVO.isCustomTask) {
                            goodsData.title = "任务要求";
                        } else {
                            goodsData.title = "商品详情";
                        }
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
    };

    public void getTimer(String diffTime){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        try {
            d1 = df.parse(diffTime);
//            Date d2 = df.parse(databaseTime+"");

            if (GlobalParams.isDebug){
                Log.e("TAG_time",d1+"");
            }
//
            long diff = d1.getTime() ;//这样得到的差值是微秒级别

            long days =diff / (1000 * 60 * 60 * 24);

            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);

            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);

            long seconds=(diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*((1000* 60)))/1000;

            if (hour == -1 && minute == -1 && second == -1) {
                Intent intent = getIntent();
                //倒计时赋值
               long[] times = {days*24+hours,minutes,seconds};
                hour = times[0];
                minute = times[1];
                second = times[2];
            }

            timeView.setText(hour + ":" + minute + ":" + second);

            timerTask = new TimerTask() {

                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            };

            timer = new Timer();
            timer.schedule(timerTask, 0, 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        Log.v(tag, "log---------->onDestroy!");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        hour = -1;
        minute = -1;
        second = -1;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.v(tag, "log---------->onStart!");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v(tag, "log---------->onStop!");
        super.onStop();
    }


    @Override
    protected void onRestart() {
        Log.v(tag, "log---------->onRestart!");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.v(tag, "log---------->onPause!");
        super.onPause();
    }

    public void setTime(long hour1, long minute1, long second1) {
        if (hour1 >= 10) {
            if (minute1 >= 10) {
                if (second1 >= 10) {
                    timeView.setText(hour1 + ":" + minute1 + ":" + second1);
                } else {
                    timeView.setText(hour1 + ":" + minute1 + ":0" + second1);
                }
            } else {
                if (second1 >= 10) {
                    timeView.setText(hour1 + ":0" + minute1 + ":" + second1);
                } else {
                    timeView.setText(hour1 + ":0" + minute1 + ":0" + second1);
                }
            }
        } else {
            if (minute1 >= 10) {
                if (second1 >= 10) {
                    timeView.setText("0" + hour1 + ":" + minute1 + ":" + second1);
                } else {
                    timeView.setText("0" + hour1 + ":" + minute1 + ":0" + second1);
                }
            } else {
                if (second1 >= 10) {
                    timeView.setText("0" + hour1 + ":0" + minute1 + ":" + second1);
                } else {
                    timeView.setText("0" + hour1 + ":0" + minute1 + ":0" + second1);
                }
            }
        }
    }
}

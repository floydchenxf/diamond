package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.BitmapProcessor;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.VipDays;
import com.floyd.diamond.bean.VipInfos;
import com.floyd.diamond.bean.VipMoney;
import com.floyd.diamond.bean.VipObject;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.tools.ImageUtils;
import com.floyd.diamond.ui.ImageLoaderFactory;
import com.floyd.diamond.ui.view.UIAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hy on 2016/3/21.
 */
public class VipOneActivity extends Activity implements View.OnClickListener {

    private LinearLayout back, vip_layout, maijiaxiu, cekuan;
    private boolean isChecked_01, isChecked_02;
    private ImageView vip_maijaixiu, vip_cekaun,vip_logo;
    private TextView confirm;
    private NetworkImageView image_01, image_02;
    private TextView title_01, title_02, content_01, content_02;
    private List<VipObject.DataEntity> dataEntityList;
    private ImageLoader mImageLoader;
    private List<String> choices;
    private TextView day_01,day_02,cancle_button;
    private String day01,day02;
    private boolean isOpen_01,isOpen_02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_layout_01);

        init();

        loadData();
    }

    public void init() {
        mImageLoader=ImageLoaderFactory.createImageLoader();
        choices=new ArrayList<>();
        back = ((LinearLayout) findViewById(R.id.back));
        vip_layout = ((LinearLayout) findViewById(R.id.vip_layout));
        maijiaxiu = ((LinearLayout) findViewById(R.id.maijiaxiu));
        cekuan = ((LinearLayout) findViewById(R.id.cekuan));
        vip_maijaixiu = ((ImageView) findViewById(R.id.dui_vip1));
        vip_cekaun = ((ImageView) findViewById(R.id.dui_vip2));
        confirm = ((TextView) findViewById(R.id.confirm_open));
        image_01 = ((NetworkImageView) findViewById(R.id.vip_image_01));
        image_02 = ((NetworkImageView) findViewById(R.id.vip_image_02));
        title_01 = ((TextView) findViewById(R.id.vip_title_01));
        title_02 = ((TextView) findViewById(R.id.vip_title_02));
        content_01 = ((TextView) findViewById(R.id.vip_content_01));
        content_02 = ((TextView) findViewById(R.id.vip_content_02));
        vip_logo= ((ImageView) findViewById(R.id.vip_vip));

        back.setOnClickListener(this);
        vip_layout.setOnClickListener(this);
        maijiaxiu.setOnClickListener(this);
        cekuan.setOnClickListener(this);
        confirm.setOnClickListener(this);
        vip_logo.setOnClickListener(this);

    }

    public void loadData() {

        String token = LoginManager.getLoginInfo(this).token;

        long userId=LoginManager.getLoginInfo(this).id;

        SellerManager.getVipInfo(token).startUI(new ApiCallback<List<VipObject.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {
                Log.e("TAG", errorInfo);

            }

            @Override
            public void onSuccess(List<VipObject.DataEntity> vipObject) {

                dataEntityList=vipObject;
                for (VipObject.DataEntity vipOb:dataEntityList){
                    if (vipOb.getId()==1){
                        title_01.setText(vipOb.getVipName());
                        content_01.setText(vipOb.getVipDescription());
                        image_01.setImageUrl(vipOb.getVipPicUrl(), mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.getOriginRoundBitmap(bitmap, 3 * 1);
                            }
                        });
                    }else if (vipOb.getId()==2){
                        title_02.setText(vipOb.getVipName());
                        content_02.setText(vipOb.getVipDescription());
                        image_02.setImageUrl(vipOb.getVipPicUrl(), mImageLoader, new BitmapProcessor() {
                            @Override
                            public Bitmap processBitmpa(Bitmap bitmap) {
                                return ImageUtils.getOriginRoundBitmap(bitmap, 3 * 1);
                            }
                        });
                    }
                }

            }

            @Override
            public void onProgress(int progress) {

            }
        });

        SellerManager.getVipDays(token,userId).startUI(new ApiCallback<List<VipDays.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(List<VipDays.DataEntity> dataEntities) {

                if (dataEntities.size()!=0){
                    if (dataEntities.size()==1){
                        for (VipDays.DataEntity dataEntity:dataEntities){
                            if (dataEntity.getVipTypeId()==1){
                                day01=dataEntity.getLeftDays()+"";
                                isOpen_01=true;
                                isOpen_02=false;
                            }else if (dataEntity.getVipTypeId()==2){
                                day02=dataEntity.getLeftDays()+"";
                                isOpen_02=true;
                                isOpen_01=false;
                            }
                        }
                    }else if (dataEntities.size()==2){
                        for (VipDays.DataEntity dataEntity:dataEntities){
                            if (dataEntity.getVipTypeId()==1){
                                day01=dataEntity.getLeftDays()+"";
                                isOpen_01=true;
                            }else if (dataEntity.getVipTypeId()==2){
                                day02=dataEntity.getLeftDays()+"";
                                isOpen_02=true;
                            }
                        }
                    }
                }
                if (isOpen_01||isOpen_02){
                    vip_logo.setImageResource(R.drawable.open_vip);
                    showDays(day01, day02);
                }else{
                    vip_logo.setImageResource(R.drawable.notopen_vip);
                }
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.maijiaxiu:
                if (isChecked_01) {
                    vip_maijaixiu.setVisibility(View.INVISIBLE);
                    isChecked_01 = false;
                    if (choices.contains("1")){
                        choices.remove("1");
                    }
                } else {
                    vip_maijaixiu.setVisibility(View.VISIBLE);
                    isChecked_01 = true;
                    choices.add("1");
                }
                if (isChecked_02 || isChecked_01) {
                    confirm.setBackgroundResource(R.drawable.common_round_blue_bg);
                } else {
                    confirm.setBackgroundResource(R.drawable.common_round_gray_bg);
                }

                break;
            case R.id.cekuan:
                if (isChecked_02) {
                    vip_cekaun.setVisibility(View.INVISIBLE);
                    isChecked_02 = false;
                    if (choices.contains("2")){
                        choices.remove("2");
                    }
                } else {
                    vip_cekaun.setVisibility(View.VISIBLE);
                    isChecked_02 = true;
                    choices.add("2");
                }
                if (isChecked_02 || isChecked_01) {
                    confirm.setBackgroundResource(R.drawable.common_round_blue_bg);
                } else {
                    confirm.setBackgroundResource(R.drawable.common_round_gray_bg);
                }

                break;
            case R.id.confirm_open:
                if (GlobalParams.isDebug){
                    Log.e("TAG",choices.toString().substring(1,choices.toString().length()-1));
                }
                if (isChecked_01 || isChecked_02) {
                    //跳转开通第二个界面
//                    Toast.makeText(VipOneActivity.this,"跳转！！！",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VipOneActivity.this, VipTwoActivity.class);
                    intent.putExtra("vips",choices.toString().substring(1,choices.toString().length()-1));
                    startActivity(intent);
                } else {
                    Toast.makeText(VipOneActivity.this, "请选择您要开通的会员类别！！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.vip_vip:
                if (isOpen_02||isOpen_01){
                    showDays(day01,day02);
                }else{
                    Toast.makeText(VipOneActivity.this,"很抱歉，您尚未开通会员！",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

//    public void showYuanBao(){
//        Toast toast = new Toast(VipOneActivity.this);
//        View view = LayoutInflater.from(VipOneActivity.this).inflate(R.layout.toast_yuanbao_layout, null);
//        toast.setView(view);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
//    }

    /**
     * 弹出会员时间框
     */
    public void showDays(String day1,String day2){
        final AlertDialog.Builder builder = new UIAlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.days_layout, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        day_01= ((TextView) view.findViewById(R.id.day_1));
        day_02= ((TextView) view.findViewById(R.id.day_2));
        LinearLayout day1_layout= ((LinearLayout) view.findViewById(R.id.day1_layout));
        LinearLayout day2_layout= ((LinearLayout) view.findViewById(R.id.day2_layout));


        if (isOpen_01){
            day1_layout.setVisibility(View.VISIBLE);
            day_01.setText(day1);
        }

        if (isOpen_02){
            day2_layout.setVisibility(View.VISIBLE);
            day_02.setText(day2);
        }

        cancle_button= ((TextView) view.findViewById(R.id.cancle));
        cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}

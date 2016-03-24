package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.bean.DLCondition;
import com.floyd.diamond.bean.VipMoney;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.ui.view.UIAlertDialog;

import java.util.List;

/**
 * Created by hy on 2016/3/21.
 */
public class VipTwoActivity extends Activity implements View.OnClickListener {
    private LinearLayout back, year, halfyear, season, month;
    private TextView day_M1, all_M1, day_M2, all_M2, day_M3, all_M3, day_M4, all_M4;
    private TextView weixin_pay, zhifubao_pay, cancle_pay, payMoney;
    private AlertDialog dialog;
    private int allM1, allM2, allM3, allM4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_two);

        init();

        loadData();
    }

    public void init() {
        back = ((LinearLayout) findViewById(R.id.back));
        day_M1 = ((TextView) findViewById(R.id.day_money_01));
        all_M1 = ((TextView) findViewById(R.id.allMoney_01));

        day_M2 = ((TextView) findViewById(R.id.day_money_02));
        all_M2 = ((TextView) findViewById(R.id.allMoney_02));

        day_M3 = ((TextView) findViewById(R.id.day_money_03));
        all_M3 = ((TextView) findViewById(R.id.allMoney_03));

        day_M4 = ((TextView) findViewById(R.id.day_money_04));
        all_M4 = ((TextView) findViewById(R.id.allMoney_04));

        year = ((LinearLayout) findViewById(R.id.vip_year));
        halfyear = ((LinearLayout) findViewById(R.id.vip_halfyear));
        season = ((LinearLayout) findViewById(R.id.vip_season));
        month = ((LinearLayout) findViewById(R.id.vip_month));

        back.setOnClickListener(this);

        year.setOnClickListener(this);
        halfyear.setOnClickListener(this);
        season.setOnClickListener(this);
        month.setOnClickListener(this);
    }

    public void loadData() {
        String token = LoginManager.getLoginInfo(this).token;
        String choices = getIntent().getStringExtra("vips").trim().replace(" ", "");
        SellerManager.getVipMoney(token, choices).startUI(new ApiCallback<List<VipMoney.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(List<VipMoney.DataEntity> dataEntities) {

                allM1 = dataEntities.get(3).getPrice() / 10000;
                allM2 = dataEntities.get(2).getPrice() / 10000;
                allM3 = dataEntities.get(1).getPrice() / 10000;
                allM4 = dataEntities.get(0).getPrice() / 10000;

                day_M4.setText("日均" + allM4 / dataEntities.get(0).getCycle() + "元");
                all_M4.setText(allM4 + "");

                day_M3.setText("日均" + allM3 / dataEntities.get(1).getCycle() + "元");
                all_M3.setText(allM3 + "");

                day_M2.setText("日均" + allM2 / dataEntities.get(2).getCycle() + "元");
                all_M2.setText(allM2 + "");

                day_M1.setText("日均" + allM1 / dataEntities.get(3).getCycle() + "元");
                all_M1.setText(allM1 + "");

            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }

    ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.vip_year:
                showVipDialog(allM1);
                break;
            case R.id.vip_halfyear:
                showVipDialog(allM2);
                break;
            case R.id.vip_season:
                showVipDialog(allM3);
                break;
            case R.id.vip_month:
                showVipDialog(allM4);
                break;
        }

    }

    public void showVipDialog(int money) {
//        final AlertDialog.Builder builder=new UIAlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        final AlertDialog.Builder builder = new UIAlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.vipmoney_layout, null);
        builder.setView(view);
//        builder.setNegativeButton("取消购买", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        weixin_pay = ((TextView) view.findViewById(R.id.weixin_pay));
        zhifubao_pay = ((TextView) view.findViewById(R.id.zhifubao_pay));
        cancle_pay = ((TextView) view.findViewById(R.id.cancel_pay));
        payMoney = ((TextView) view.findViewById(R.id.pay_money));
        payMoney.setText("￥"+money+"");
        cancle_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        weixin_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VipTwoActivity.this, "跳转微信支付...", Toast.LENGTH_SHORT).show();
            }
        });
        zhifubao_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VipTwoActivity.this, "跳转支付宝支付...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 支付成功弹出大元宝提示
     */
    public void showYuanBao(){
        Toast toast = new Toast(VipTwoActivity.this);
        View view = LayoutInflater.from(VipTwoActivity.this).inflate(R.layout.toast_yuanbao_layout, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

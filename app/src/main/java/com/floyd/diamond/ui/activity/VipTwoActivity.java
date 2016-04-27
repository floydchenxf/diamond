package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.aync.ApiCallback;
import com.floyd.diamond.aync.AsyncJob;
import com.floyd.diamond.aync.Func;
import com.floyd.diamond.aync.HttpJobFactory;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.PingPP;
import com.floyd.diamond.bean.VipMoney;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.func.StringFunc;
import com.floyd.diamond.biz.manager.JsonHttpJobFactory;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.manager.SellerManager;
import com.floyd.diamond.biz.parser.AbstractJsonParser;
import com.floyd.diamond.channel.request.HttpMethod;
import com.floyd.diamond.ui.view.UIAlertDialog;
import com.google.gson.Gson;
import com.pingplusplus.android.PaymentActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hy on 2016/3/21.
 */
public class VipTwoActivity extends Activity implements View.OnClickListener {
    private LinearLayout back, year, halfyear, season, month;
    private TextView day_M1, all_M1, day_M2, all_M2, day_M3, all_M3, day_M4, all_M4;
    private TextView weixin_pay, zhifubao_pay, cancle_pay, payMoney;
    private AlertDialog dialog;
    private int allM1, allM2, allM3, allM4;
    private String token;
    private int REQUEST_CODE_PAYMENT = 1;
    private String choices;
    private String data;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_two);

        init();

        loadData();
    }

    public void init() {
        queue = Volley.newRequestQueue(this);
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
        choices = getIntent().getStringExtra("vips").trim().replace(" ", "");
        SellerManager.getVipMoney(token, choices).startUI(new ApiCallback<List<VipMoney.DataEntity>>() {
            @Override
            public void onError(int code, String errorInfo) {

            }

            @Override
            public void onSuccess(List<VipMoney.DataEntity> dataEntities) {

                allM1 = dataEntities.get(3).getPrice() / 1000;
                allM2 = dataEntities.get(2).getPrice() / 1000;
                allM3 = dataEntities.get(1).getPrice() / 1000;
                allM4 = dataEntities.get(0).getPrice() / 1000;

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
                showVipDialog(allM1, 12 * 30);
                break;
            case R.id.vip_halfyear:
                showVipDialog(allM2, 6 * 30);
                break;
            case R.id.vip_season:
                showVipDialog(allM3, 3 * 30);
                break;
            case R.id.vip_month:
                showVipDialog(allM4, 1 * 30);
                break;
        }

    }

    public void showVipDialog(final float money, final int cycle) {
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
        payMoney.setText("￥" + money + "");
        cancle_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        weixin_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChargeObj("wx", money + "", cycle + "", choices);
                Toast.makeText(VipTwoActivity.this, "跳转微信支付...", Toast.LENGTH_SHORT).show();
            }
        });
        zhifubao_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChargeObj("alipay", money + "", cycle + "", choices);
                Toast.makeText(VipTwoActivity.this, "跳转支付宝支付...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 支付成功弹出大元宝提示
     */
    public void showYuanBao() {
        Toast toast = new Toast(VipTwoActivity.this);
        View view = LayoutInflater.from(VipTwoActivity.this).inflate(R.layout.toast_yuanbao_layout, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void getChargeObj(final String channel, final String money, final String cycle, final String vipTypeIds) {

//        //按键点击之后的禁用，防止重复点击
//        weixin_pay.setOnClickListener(null);
//        zhifubao_pay.setOnClickListener(null);


        token = LoginManager.getLoginInfo(VipTwoActivity.this).token;
        String url = APIConstants.HOST + APIConstants.API_PINGPP;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int index = response.indexOf(":");
                int last = response.lastIndexOf(",");
                data = response.substring(index+1, last);

                Log.e("TAG_Charge", data);

                Intent intent = new Intent(VipTwoActivity.this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("channel", channel);
                params.put("money", money + "");
                params.put("vipTypeIds", vipTypeIds);
                params.put("cycle", cycle);
                return params;
            }
        };
        queue.add(request);

//      SellerManager.getVipType(token,channel,money,vipTypeIds,cycle).startUI(new ApiCallback<PingPP.DataEntity>() {
//          @Override
//          public void onError(int code, String errorInfo) {
//
//          }
//
//          @Override
//          public void onSuccess(PingPP.DataEntity dataEntity) {
//              if (dataEntity==null){
//                  Toast.makeText(VipTwoActivity.this, "获取充   值信息失败，请稍后再试", Toast.LENGTH_SHORT).show();
//              }else{
//                  data=dataEntity.toString();
//
//                  Log.e("TAG",dataEntity.toString());
//
//                  Intent intent = new Intent(VipTwoActivity.this, PaymentActivity.class);
//                  intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
//                  startActivityForResult(intent, REQUEST_CODE_PAYMENT);
//              }
//
//          }
//
//          @Override
//          public void onProgress(int progress) {
//
//          }
//      });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
//        if (requestCode == REQUEST_CODE_PAYMENT) {
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getExtras().getString("pay_result");
            /* 处理返回值
             * "success" - 支付成功
             * "fail"    - 支付失败
             * "cancel"  - 取消支付
             * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
             */
            if (result.equals("cancel")) {
                Toast.makeText(VipTwoActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
            } else if (result.equals("fail")) {
                Toast.makeText(VipTwoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            } else if (result.equals("success")) {
                finish();
//                startActivity(new Intent(VipTwoActivity.this,VipOneActivity.class));
                showYuanBao();
            } else if (result.equals("invalid")) {
                Toast.makeText(VipTwoActivity.this, "支付插件未安装", Toast.LENGTH_SHORT).show();
            }
            String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
            String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            Log.e("TAG_aplpay", errorMsg + "=========" + extraMsg+resultCode+""+data);
            Log.e("TAG_wx", result);
//                showMsg(result, errorMsg, extraMsg);
        }
    }

//    }
}

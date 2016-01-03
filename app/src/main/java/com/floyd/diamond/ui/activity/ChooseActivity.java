package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.floyd.diamond.R;
import com.floyd.diamond.bean.ChoiceCondition;
import com.floyd.diamond.bean.ChooseCondition;
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.SeekBarPressure;
import com.floyd.diamond.bean.SeekBarPressure1;
import com.floyd.diamond.bean.SeekBarPressure2;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/24.
 */
public class ChooseActivity extends Activity {
    private LinearLayout back;
    private ImageView moreProvince, jiantou_up;
    private LinearLayout changeProvince;
    private CheckBox allChoose, beijing, shanghai, tianjin, chongqing, jiangsu, zhejiang, liaoning, heilongjiang, jilin, shandong, anhui, hebei, henan, hubei,
            hunan, jiangxi, shanxi3, shanxi1, sichuan, qinghai, hainan, guangdong, guizhou, fujian, taiwan, gansu, yunnan, neimenggu, ningxia, xinjiang, xizang,
            guangxi, xianggang, aomen, gugan, biaozhi, fengman;
    private SeekBarPressure seekBarAge;
    private SeekBarPressure2 seekBarCredit;
    private SeekBarPressure1 seekBarHeight;
    private boolean isScreen;
    private RadioButton boy, girl;
    private ChooseCondition chooseCondition;
    private List<String> shapesList;
    private List<String> provincesList;
    private TextView search;
    private RequestQueue queue;
    private LoginVO vo;
    private ChoiceCondition.DataEntity dataEntity;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseactivity);
        initView();

        setData();
    }

    //获取默认的筛选条件
    public void setData(){
        String url= APIConstants.HOST+APIConstants.API_GET_MOTE_FILTER;
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug){
                    Log.e("TAG_shaixuan",response);
                }
                Gson gson=new Gson();
                ChoiceCondition choiceCondition=gson.fromJson(response,ChoiceCondition.class);

                dataEntity=choiceCondition.getData();
                handler.sendEmptyMessage(1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ChooseActivity.this,"请检查网络连接...",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<>();
                params.put("token",vo.token );
                return params;
            }
        };

        queue.add(request);
    }

    //初始化操作
    public void initView() {
        vo = LoginManager.getLoginInfo(this);
        chooseCondition = new ChooseCondition();
        chooseCondition.setCreditMax(100);
        queue= Volley.newRequestQueue(this);
        shapesList = new ArrayList<>();
        provincesList = new ArrayList<>();
        search = ((TextView) findViewById(R.id.search));
        boy = ((RadioButton) findViewById(R.id.boy));
        girl = ((RadioButton) findViewById(R.id.girl));
        back = ((LinearLayout) findViewById(R.id.guide));
        moreProvince = ((ImageView) findViewById(R.id.moreProvince));
        jiantou_up = ((ImageView) findViewById(R.id.jiantou_up));
        allChoose = ((CheckBox) findViewById(R.id.allChoose));
        changeProvince = ((LinearLayout) findViewById(R.id.changeProvince));
        changeProvince.setVisibility(View.GONE);
        beijing = ((CheckBox) findViewById(R.id.beijing));
        shanghai = ((CheckBox) findViewById(R.id.shanghai));
        tianjin = ((CheckBox) findViewById(R.id.tianjin));
        chongqing = ((CheckBox) findViewById(R.id.chongqing));
        jiangsu = ((CheckBox) findViewById(R.id.jiangsu));
        zhejiang = ((CheckBox) findViewById(R.id.zhejiang));
        liaoning = ((CheckBox) findViewById(R.id.liaoning));
        heilongjiang = ((CheckBox) findViewById(R.id.heilongjiang));
        jilin = ((CheckBox) findViewById(R.id.jilin));
        shandong = ((CheckBox) findViewById(R.id.shandong));
        anhui = ((CheckBox) findViewById(R.id.anhui));
        hebei = ((CheckBox) findViewById(R.id.hebei));
        henan = ((CheckBox) findViewById(R.id.henan));
        hubei = ((CheckBox) findViewById(R.id.hubei));
        hunan = ((CheckBox) findViewById(R.id.hunan));
        jiangxi = ((CheckBox) findViewById(R.id.jiangxi));
        shanxi3 = ((CheckBox) findViewById(R.id.shanxi3));
        shanxi1 = ((CheckBox) findViewById(R.id.shanxi1));
        sichuan = ((CheckBox) findViewById(R.id.sichuan));
        qinghai = ((CheckBox) findViewById(R.id.qinghai));
        hainan = ((CheckBox) findViewById(R.id.hainan));
        guangdong = ((CheckBox) findViewById(R.id.guangdong));
        guizhou = ((CheckBox) findViewById(R.id.guizhou));
        fujian = ((CheckBox) findViewById(R.id.fujian));
        taiwan = ((CheckBox) findViewById(R.id.taiwan));
        gansu = ((CheckBox) findViewById(R.id.gansu));
        yunnan = ((CheckBox) findViewById(R.id.yunnan));
        neimenggu = ((CheckBox) findViewById(R.id.neimenggu));
        ningxia = ((CheckBox) findViewById(R.id.ningxia));
        xinjiang = ((CheckBox) findViewById(R.id.xinjiang));
        xizang = ((CheckBox) findViewById(R.id.xizang));
        guangxi = ((CheckBox) findViewById(R.id.guangxi));
        xianggang = ((CheckBox) findViewById(R.id.xianggang));
        aomen = ((CheckBox) findViewById(R.id.aomen));
        seekBarAge = (SeekBarPressure) findViewById(R.id.seekBar_age);
        seekBarHeight = ((SeekBarPressure1) findViewById(R.id.seekBar_height));
        seekBarCredit = ((SeekBarPressure2) findViewById(R.id.seekBar_manyi));
        gugan = ((CheckBox) findViewById(R.id.gugan));
        biaozhi = ((CheckBox) findViewById(R.id.biaozhi));
        fengman = ((CheckBox) findViewById(R.id.fengman));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, ChooseResultActivity.class);
                intent.putExtra("chooseCondition", (Serializable) dataEntity);
                startActivity(intent);
            }
        });

        seekBarAge.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {
                isScreen = true;
            }

            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {

                chooseCondition.setAgeMin((int) progressLow);

                chooseCondition.setAgeMax((int) progressHigh);

            }

            @Override
            public void onProgressAfter() {
                isScreen = false;
            }
        });
        seekBarHeight.setOnSeekBarChangeListener(new SeekBarPressure1.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {

            }

            @Override
            public void onProgressChanged(SeekBarPressure1 seekBar, double progressLow, double progressHigh) {

                chooseCondition.setHeightMin((int) progressLow);

                chooseCondition.setHeightMax((int) progressHigh);
            }

            @Override
            public void onProgressAfter() {

            }
        });

        seekBarCredit.setOnSeekBarChangeListener(new SeekBarPressure2.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {

            }

            @Override
            public void onProgressChanged(SeekBarPressure2 seekBar, double progressLow, double progressHigh) {

                chooseCondition.setCreditMin((int) progressLow);

//                chooseCondition.setCreditMax(100);
            }

            @Override
            public void onProgressAfter() {

            }
        });

        //点击返回上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击弹出更多的省
        moreProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreProvince.setVisibility(View.INVISIBLE);
                changeProvince.setVisibility(View.VISIBLE);
            }
        });
        //点击返回原样
        jiantou_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProvince.setVisibility(View.GONE);
                moreProvince.setVisibility(View.VISIBLE);
            }
        });

        //点击所有的区域都被选中
        if (GlobalParams.isDebug) {
            Log.e("allChoose", allChoose.isChecked() + "");
        }

        allChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allChoose.isChecked()) {
                    beijing.setChecked(true);
                    shanghai.setChecked(true);
                    tianjin.setChecked(true);
                    chongqing.setChecked(true);
                    jiangsu.setChecked(true);
                    zhejiang.setChecked(true);
                    liaoning.setChecked(true);
                    heilongjiang.setChecked(true);
                    jilin.setChecked(true);
                    shandong.setChecked(true);
                    anhui.setChecked(true);
                    hebei.setChecked(true);
                    henan.setChecked(true);
                    hubei.setChecked(true);
                    hunan.setChecked(true);
                    jiangxi.setChecked(true);
                    shanxi3.setChecked(true);
                    shanxi1.setChecked(true);
                    sichuan.setChecked(true);
                    qinghai.setChecked(true);
                    hainan.setChecked(true);
                    guangdong.setChecked(true);
                    guizhou.setChecked(true);
                    fujian.setChecked(true);
                    taiwan.setChecked(true);
                    gansu.setChecked(true);
                    yunnan.setChecked(true);
                    neimenggu.setChecked(true);
                    ningxia.setChecked(true);
                    xinjiang.setChecked(true);
                    xizang.setChecked(true);
                    guangxi.setChecked(true);
                    xianggang.setChecked(true);
                    aomen.setChecked(true);
                } else {
                    beijing.setChecked(false);
                    shanghai.setChecked(false);
                    tianjin.setChecked(false);
                    chongqing.setChecked(false);
                    jiangsu.setChecked(false);
                    zhejiang.setChecked(false);
                    liaoning.setChecked(false);
                    heilongjiang.setChecked(false);
                    jilin.setChecked(false);
                    shandong.setChecked(false);
                    anhui.setChecked(false);
                    hebei.setChecked(false);
                    henan.setChecked(false);
                    hubei.setChecked(false);
                    hunan.setChecked(false);
                    jiangxi.setChecked(false);
                    shanxi3.setChecked(false);
                    shanxi1.setChecked(false);
                    sichuan.setChecked(false);
                    qinghai.setChecked(false);
                    hainan.setChecked(false);
                    guangdong.setChecked(false);
                    guizhou.setChecked(false);
                    fujian.setChecked(false);
                    taiwan.setChecked(false);
                    gansu.setChecked(false);
                    yunnan.setChecked(false);
                    neimenggu.setChecked(false);
                    ningxia.setChecked(false);
                    xinjiang.setChecked(false);
                    xizang.setChecked(false);
                    guangxi.setChecked(false);
                    xianggang.setChecked(false);
                    aomen.setChecked(false);
                }

            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boy.isChecked()) {
                    chooseCondition.setGender(1);
                }
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (girl.isChecked()) {
                    chooseCondition.setGender(0);
                }
            }
        });

        gugan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gugan.isChecked()) {
                    shapesList.add(gugan.getTag() + "");
                } else {
                    shapesList.remove(gugan.getTag() + "");
                }

            }
        });


        biaozhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (biaozhi.isChecked()) {
                    shapesList.add(biaozhi.getTag() + "");
                } else {
                    shapesList.remove(biaozhi.getTag() + "");
                }

            }
        });

        fengman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fengman.isChecked()) {
                    shapesList.add(fengman.getTag() + "");
                } else {
                    shapesList.remove(fengman.getTag() + "");
                }

            }
        });

        chooseCondition.setShapesList(shapesList);

        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (beijing.isChecked()) {
                    provincesList.add(beijing.getTag() + "");

                } else {
                    provincesList.remove(beijing.getTag() + "");
                }

            }
        });

        shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    provincesList.add(shanghai.getTag() + "");

                } else {
                    provincesList.remove(shanghai.getTag() + "");
                }

            }
        });
        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tianjin.isChecked()) {
                    provincesList.add(tianjin.getTag() + "");

                } else {
                    provincesList.remove(tianjin.getTag() + "");
                }

            }
        });
        chongqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chongqing.isChecked()) {
                    provincesList.add(chongqing.getTag() + "");

                } else {
                    provincesList.remove(chongqing.getTag() + "");
                }

            }
        });

        jiangsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangsu.isChecked()) {
                    provincesList.add(jiangsu.getTag() + "");
                } else {
                    provincesList.remove(jiangsu.getTag() + "");
                }

            }
        });

        zhejiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zhejiang.isChecked()) {
                    provincesList.add(zhejiang.getTag() + "");
                } else {
                    provincesList.remove(zhejiang.getTag() + "");
                }

            }
        });

        liaoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (liaoning.isChecked()) {
                    provincesList.add(liaoning.getTag() + "");
                } else {
                    provincesList.remove(liaoning.getTag() + "");
                }

            }
        });

        heilongjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (heilongjiang.isChecked()) {
                    provincesList.add(heilongjiang.getTag() + "");
                } else {
                    provincesList.remove(heilongjiang.getTag() + "");
                }

            }
        });

        jilin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jilin.isChecked()) {
                    provincesList.add(jilin.getTag() + "");
                } else {
                    provincesList.remove(jilin.getTag() + "");
                }

            }
        });

        shandong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shandong.isChecked()) {
                    provincesList.add(shandong.getTag() + "");
                } else {
                    provincesList.remove(shandong.getTag() + "");
                }

            }
        });

        anhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (anhui.isChecked()) {
                    provincesList.add(anhui.getTag() + "");
                } else {
                    provincesList.remove(anhui.getTag() + "");
                }

            }
        });

        hebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hebei.isChecked()) {
                    provincesList.add(hebei.getTag() + "");
                } else {
                    provincesList.remove(hebei.getTag() + "");
                }

            }
        });

        henan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (henan.isChecked()) {
                    provincesList.add(henan.getTag() + "");
                } else {
                    provincesList.remove(henan.getTag() + "");
                }

            }
        });

        hubei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hubei.isChecked()) {
                    provincesList.add(hubei.getTag() + "");
                } else {
                    provincesList.remove(hubei.getTag() + "");
                }

            }
        });

        hunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hunan.isChecked()) {
                    provincesList.add(hunan.getTag() + "");
                } else {
                    provincesList.remove(hunan.getTag() + "");
                }

            }
        });

        jiangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangxi.isChecked()) {
                    provincesList.add(jiangxi.getTag() + "");
                } else {
                    provincesList.remove(jiangxi.getTag() + "");
                }

            }
        });

        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    provincesList.add(tianjin.getTag() + "");
                } else {
                    provincesList.remove(tianjin.getTag() + "");
                }

            }
        });

        shanxi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi3.isChecked()) {
                    provincesList.add(shanxi3.getTag() + "");
                } else {
                    provincesList.remove(shanxi3.getTag() + "");
                }

            }
        });

        shanxi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi1.isChecked()) {
                    provincesList.add(shanxi1.getTag() + "");
                } else {
                    provincesList.remove(shanxi1.getTag() + "");
                }

            }
        });

        sichuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sichuan.isChecked()) {
                    provincesList.add(sichuan.getTag() + "");
                } else {
                    provincesList.remove(sichuan.getTag() + "");
                }

            }
        });

        qinghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qinghai.isChecked()) {
                    provincesList.add(qinghai.getTag() + "");
                } else {
                    provincesList.remove(qinghai.getTag() + "");
                }

            }
        });

        hainan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hainan.isChecked()) {
                    provincesList.add(hainan.getTag() + "");
                } else {
                    provincesList.remove(hainan.getTag() + "");
                }

            }
        });

        guangdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangdong.isChecked()) {
                    provincesList.add(guangdong.getTag() + "");
                } else {
                    provincesList.remove(guangdong.getTag() + "");
                }

            }
        });

        guizhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guizhou.isChecked()) {
                    provincesList.add(guizhou.getTag() + "");
                } else {
                    provincesList.remove(guizhou.getTag() + "");
                }

            }
        });

        fujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fujian.isChecked()) {
                    provincesList.add(fujian.getTag() + "");
                } else {
                    provincesList.remove(fujian.getTag() + "");
                }

            }
        });

        taiwan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (taiwan.isChecked()) {
                    provincesList.add(taiwan.getTag() + "");
                } else {
                    provincesList.remove(taiwan.getTag() + "");
                }

            }
        });

        gansu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gansu.isChecked()) {
                    provincesList.add(gansu.getTag() + "");
                } else {
                    provincesList.remove(gansu.getTag() + "");
                }

            }
        });

        yunnan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yunnan.isChecked()) {
                    provincesList.add(yunnan.getTag() + "");
                } else {
                    provincesList.remove(yunnan.getTag() + "");
                }

            }
        });
        neimenggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (neimenggu.isChecked()) {
                    provincesList.add(neimenggu.getTag() + "");
                } else {
                    provincesList.remove(neimenggu.getTag() + "");
                }

            }
        });

        ningxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ningxia.isChecked()) {
                    provincesList.add(ningxia.getTag() + "");
                } else {
                    provincesList.remove(ningxia.getTag() + "");
                }

            }
        });

        xinjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xinjiang.isChecked()) {
                    provincesList.add(xinjiang.getTag() + "");
                } else {
                    provincesList.remove(xinjiang.getTag() + "");
                }

            }
        });

        xizang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xizang.isChecked()) {
                    provincesList.add(xizang.getTag() + "");
                } else {
                    provincesList.remove(xizang.getTag() + "");
                }

            }
        });

        guangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangxi.isChecked()) {
                    provincesList.add(guangxi.getTag() + "");
                } else {
                    provincesList.remove(guangxi.getTag() + "");
                }

            }
        });

        xianggang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xianggang.isChecked()) {
                    provincesList.add(xianggang.getTag() + "");
                } else {
                    provincesList.remove(xianggang.getTag() + "");
                }

            }
        });

        aomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aomen.isChecked()) {
                    provincesList.add(aomen.getTag() + "");
                } else {
                    provincesList.remove(aomen.getTag() + "");
                }

            }
        });


        chooseCondition.setProvincesList(provincesList);
    }


}

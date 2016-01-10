package com.floyd.diamond.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.floyd.diamond.bean.GlobalParams;
import com.floyd.diamond.bean.SeekBarPressure;
import com.floyd.diamond.bean.SeekBarPressure1;
import com.floyd.diamond.bean.SeekBarPressure2;
import com.floyd.diamond.biz.constants.APIConstants;
import com.floyd.diamond.biz.manager.LoginManager;
import com.floyd.diamond.biz.vo.LoginVO;
import com.floyd.diamond.ui.loading.DataLoadingView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/5.
 */
public class ChooseActivity1 extends Activity {
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
    private List<String> shapesList;
    private List<String> provincesList;
    private TextView search;
    private RequestQueue queue;
    private LoginVO vo;
    private ChoiceCondition.DataEntity dataEntity;
    private List<String> shapes;
    private List<String> areaids;
    private DataLoadingView dataLoadingView;
    private boolean isFirstShape = true;
    private boolean isFirshArea = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            clickCondition();

            dataEntity.setAreaids(areaids);
            dataEntity.setShapes(shapes);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseactivity);

        initView();

        if (vo == null) {
            setData();
        } else {
            setDataUser();
        }

    }

    //获取默认的筛选条件(未登录的)
    public void setData() {
        String url = APIConstants.HOST + APIConstants.API_GET_MOTE_FILTER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug) {
                    Log.e("weidenglu", response);
                }
                Gson gson = new Gson();
                ChoiceCondition choiceCondition = gson.fromJson(response, ChoiceCondition.class);

                dataEntity = choiceCondition.getData();

                seekBarAge.setProgressLow(dataEntity.getAgeMin());
                seekBarAge.setProgressHigh(dataEntity.getAgeMax());

                seekBarHeight.setProgressHigh(dataEntity.getHeightMax() - 80);
                seekBarHeight.setProgressLow(dataEntity.getHeightMin() - 80);

                seekBarCredit.setProgressLow(dataEntity.getCreditMin());

                handler.sendEmptyMessage(1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ChooseActivity1.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();
            }
        })
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                //在这里设置需要post的参数
//                Map<String, String> params = new HashMap<>();
//                params.put("token",vo.token );
//                return params;
//            }
//        }
                ;

        queue.add(request);
    }

    //获取保存的筛选条件（已登录）
    public void setDataUser() {

        if (GlobalParams.isDebug) {
            Log.e("token", vo.token + "");
        }
        String url = APIConstants.HOST + APIConstants.API_GET_MOTE_FILTER;
        StringRequest requestLogin = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalParams.isDebug) {
                    Log.e("TAG_denglu", response);
                }
                Gson gson = new Gson();
                ChoiceCondition choiceCondition = gson.fromJson(response, ChoiceCondition.class);

                dataEntity = choiceCondition.getData();

                seekBarAge.setProgressLow(dataEntity.getAgeMin());
                seekBarAge.setProgressHigh(dataEntity.getAgeMax());

                seekBarHeight.setProgressHigh(dataEntity.getHeightMax() - 80);
                seekBarHeight.setProgressLow(dataEntity.getHeightMin() - 80);

                seekBarCredit.setProgressLow(dataEntity.getCreditMin());
//
//                if (GlobalParams.isDebug){
//                    Log.e("TAG_huoqu",dataEntity.getShapes().toString());
//                }


                List<ChoiceCondition.DataEntity.ShapesListEntity> shapesLists = dataEntity.getShapesList();
                if (dataEntity.getShapes() != null) {
                    for (int i = 0; i < shapesLists.size(); i++) {
                        if (shapesLists.get(i).getId() == 1) {
                            if (dataEntity.getShapes().contains(1)) {
                                gugan.setChecked(true);
                            } else {
                                gugan.setChecked(false);
                            }
                        } else if (shapesLists.get(i).getId() == 2) {
                            if (dataEntity.getShapes().contains(2)) {
                                biaozhi.setChecked(true);
                            } else {
                                biaozhi.setChecked(false);
                            }
                        } else if (shapesLists.get(i).getId() == 3) {
                            if (dataEntity.getShapes().contains(3)) {
                                fengman.setChecked(true);
                            } else {
                                fengman.setChecked(false);
                            }
                        }
                    }
                }


                List<ChoiceCondition.DataEntity.AreaListEntity> areaLists = dataEntity.getAreaList();
                if (dataEntity.getAreaids() != null) {
                    for (int i = 0; i < areaLists.size(); i++) {
                        if (areaLists.get(i).getId() == 110000) {
                            if (dataEntity.getAreaids().contains(110000)) {
                                beijing.setChecked(true);
                            } else {
                                beijing.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 120000) {
                            if (dataEntity.getAreaids().contains(120000)) {
                                tianjin.setChecked(true);
                            } else {
                                tianjin.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 130000) {
                            if (dataEntity.getAreaids().contains(130000)) {
                                hebei.setChecked(true);
                            } else {
                                hebei.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 140000) {
                            if (dataEntity.getAreaids().contains(140000)) {
                                shanxi1.setChecked(true);
                            } else {
                                shanxi1.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 150000) {
                            if (dataEntity.getAreaids().contains(150000)) {
                                neimenggu.setChecked(true);
                            } else {
                                neimenggu.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 210000) {
                            if (dataEntity.getAreaids().contains(210000)) {
                                liaoning.setChecked(true);
                            } else {
                                liaoning.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 220000) {
                            if (dataEntity.getAreaids().contains(220000)) {
                                jilin.setChecked(true);
                            } else {
                                jilin.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 230000) {
                            if (dataEntity.getAreaids().contains(230000)) {
                                heilongjiang.setChecked(true);
                            } else {
                                heilongjiang.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 310000) {
                            if (dataEntity.getAreaids().contains(310000)) {
                                shanghai.setChecked(true);
                            } else {
                                shanghai.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 320000) {
                            if (dataEntity.getAreaids().contains(320000)) {
                                jiangsu.setChecked(true);
                            } else {
                                jiangsu.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 330000) {
                            if (dataEntity.getAreaids().contains(330000)) {
                                zhejiang.setChecked(true);
                            } else {
                                zhejiang.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 340000) {
                            if (dataEntity.getAreaids().contains(340000)) {
                                anhui.setChecked(true);
                            } else {
                                anhui.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 350000) {
                            if (dataEntity.getAreaids().contains(350000)) {
                                fujian.setChecked(true);
                            } else {
                                fujian.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 360000) {
                            if (dataEntity.getAreaids().contains(360000)) {
                                jiangxi.setChecked(true);
                            } else {
                                jiangxi.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 370000) {
                            if (dataEntity.getAreaids().contains(370000)) {
                                shandong.setChecked(true);
                            } else {
                                shandong.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 410000) {
                            if (dataEntity.getAreaids().contains(410000)) {
                                henan.setChecked(true);
                            } else {
                                henan.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 420000) {
                            if (dataEntity.getAreaids().contains(420000)) {
                                hubei.setChecked(true);
                            } else {
                                hubei.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 430000) {
                            if (dataEntity.getAreaids().contains(430000)) {
                                hunan.setChecked(true);
                            } else {
                                hunan.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 440000) {
                            if (dataEntity.getAreaids().contains(440000)) {
                                guangdong.setChecked(true);
                            } else {
                                guangdong.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 450000) {
                            if (dataEntity.getAreaids().contains(450000)) {
                                guangxi.setChecked(true);
                            } else {
                                hainan.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 460000) {
                            if (dataEntity.getAreaids().contains(460000)) {
                                hainan.setChecked(true);
                            } else {
                                hainan.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 500000) {
                            if (dataEntity.getAreaids().contains(500000)) {
                                chongqing.setChecked(true);
                            } else {
                                chongqing.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 510000) {
                            if (dataEntity.getAreaids().contains(510000)) {
                                sichuan.setChecked(true);
                            } else {
                                sichuan.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 520000) {
                            if (dataEntity.getAreaids().contains(520000)) {
                                guizhou.setChecked(true);
                            } else {
                                guizhou.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 530000) {
                            if (dataEntity.getAreaids().contains(530000)) {
                                yunnan.setChecked(true);
                            } else {
                                yunnan.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 540000) {
                            if (dataEntity.getAreaids().contains(540000)) {
                                xizang.setChecked(true);
                            } else {
                                xizang.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 610000) {
                            if (dataEntity.getAreaids().contains(610000)) {
                                shanxi3.setChecked(true);
                            } else {
                                shanxi3.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 620000) {
                            if (dataEntity.getAreaids().contains(620000)) {
                                gansu.setChecked(true);
                            } else {
                                gansu.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 630000) {
                            if (dataEntity.getAreaids().contains(630000)) {
                                qinghai.setChecked(true);
                            } else {
                                qinghai.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 640000) {
                            if (dataEntity.getAreaids().contains(640000)) {
                                ningxia.setChecked(true);
                            } else {
                                ningxia.setChecked(false);
                            }
                        } else if (areaLists.get(i).getId() == 650000) {
                            if (dataEntity.getAreaids().contains(650000)) {
                                xinjiang.setChecked(true);
                            } else {
                                xinjiang.setChecked(false);
                            }
                        }

                    }
                }

                handler.sendEmptyMessage(1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ChooseActivity1.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();
            }
        })
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                //在这里设置需要post的参数
//                Map<String, String> params = new HashMap<>();
//                params.put("token",vo.token );
//                return params;
//            }
//        }
                ;

        queue.add(requestLogin);
    }

    //初始化操作
    public void initView() {
        vo = LoginManager.getLoginInfo(this);
//        chooseCondition = new ChooseCondition();
//        chooseCondition.setCreditMax(100);
        dataEntity = new ChoiceCondition.DataEntity();
        shapes = new ArrayList<>();
        areaids = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
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

                if (GlobalParams.isDebug){
                    Log.e("dataEntity",dataEntity.toString());
                }

                if (dataEntity != null) {
                    if (vo != null) {
                        String saveUrl = APIConstants.HOST + APIConstants.API_SAVE_MOTE_FILTER;
                        StringRequest submitCondition = new StringRequest(Request.Method.POST, saveUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (GlobalParams.isDebug) {
                                    Log.e("TAG_submit", response);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                //在这里设置需要post的参数
                                Map<String, String> params = new HashMap<>();
                                params.put("gender", dataEntity.getGender() + "");
                                params.put("ageMin", dataEntity.getAgeMin() + "");
                                params.put("ageMax", dataEntity.getAgeMax() + "");
                                params.put("heightMin", dataEntity.getHeightMin() + "");
                                params.put("heightMax", dataEntity.getHeightMax() + "");
                                params.put("creditMin", dataEntity.getCreditMin() + "");
                                params.put("creditMax", dataEntity.getCreditMax() + "");
                                params.put("shapes", dataEntity.getShapes().toString().substring(1, dataEntity.getShapes().toString().length() - 1).replace(" ", "") + "");
                                params.put("areaids", dataEntity.getAreaids().toString().substring(1, dataEntity.getAreaids().toString().length() - 1).replace(" ", "") + "");
                                params.put("token", vo.token + "");
                                return params;
                            }
                        };

                        queue.add(submitCondition);
                    }


                    Intent intent = new Intent(ChooseActivity1.this, ChooseResultActivity.class);
                    intent.putExtra("chooseCondition", (Serializable) dataEntity);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ChooseActivity1.this, "请检查网络连接...", Toast.LENGTH_LONG).show();
                }

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

    }

    public void clickCondition() {
        seekBarAge.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {
                isScreen = true;
            }

            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {

                dataEntity.setAgeMin((int) progressLow);

                dataEntity.setAgeMax((int) progressHigh);

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

                dataEntity.setHeightMin((int) progressLow);

                dataEntity.setHeightMax((int) progressHigh);
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

                dataEntity.setCreditMin((int) progressLow);

//                dataEntity.setCreditMax(100);
            }

            @Override
            public void onProgressAfter() {

            }
        });


        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boy.isChecked()) {
                    dataEntity.setGender(1);
                }
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (girl.isChecked()) {
                    dataEntity.setGender(0);
                }
            }
        });


        gugan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gugan.isChecked()) {
                    shapes.add(1 + "");
                } else {
                    shapes.remove(1 + "");
                }

            }
        });


        biaozhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (biaozhi.isChecked()) {
                    shapes.add(2 + "");
                } else {
                    shapes.remove(2 + "");
                }

            }
        });

        fengman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fengman.isChecked()) {
                    shapes.add(3 + "");
                } else {
                    shapes.remove(3 + "");
                }

            }
        });


        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (beijing.isChecked()) {
                    areaids.add(beijing.getTag() + "");

                } else {
                    areaids.remove(beijing.getTag() + "");
                }

            }
        });

        shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    areaids.add(shanghai.getTag() + "");

                } else {
                    areaids.remove(shanghai.getTag() + "");
                }

            }
        });
        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tianjin.isChecked()) {
                    areaids.add(tianjin.getTag() + "");

                } else {
                    areaids.remove(tianjin.getTag() + "");
                }

            }
        });
        chongqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chongqing.isChecked()) {
                    areaids.add(chongqing.getTag() + "");

                } else {
                    areaids.remove(chongqing.getTag() + "");
                }

            }
        });

        jiangsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangsu.isChecked()) {
                    areaids.add(jiangsu.getTag() + "");
                } else {
                    areaids.remove(jiangsu.getTag() + "");
                }

            }
        });

        zhejiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (zhejiang.isChecked()) {
                    areaids.add(zhejiang.getTag() + "");
                } else {
                    areaids.remove(zhejiang.getTag() + "");
                }

            }
        });

        liaoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (liaoning.isChecked()) {
                    areaids.add(liaoning.getTag() + "");
                } else {
                    areaids.remove(liaoning.getTag() + "");
                }

            }
        });

        heilongjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (heilongjiang.isChecked()) {
                    areaids.add(heilongjiang.getTag() + "");
                } else {
                    areaids.remove(heilongjiang.getTag() + "");
                }

            }
        });

        jilin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jilin.isChecked()) {
                    areaids.add(jilin.getTag() + "");
                } else {
                    areaids.remove(jilin.getTag() + "");
                }

            }
        });

        shandong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shandong.isChecked()) {
                    areaids.add(shandong.getTag() + "");
                } else {
                    areaids.remove(shandong.getTag() + "");
                }

            }
        });

        anhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (anhui.isChecked()) {
                    areaids.add(anhui.getTag() + "");
                } else {
                    areaids.remove(anhui.getTag() + "");
                }

            }
        });

        hebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hebei.isChecked()) {
                    areaids.add(hebei.getTag() + "");
                } else {
                    areaids.remove(hebei.getTag() + "");
                }

            }
        });

        henan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (henan.isChecked()) {
                    areaids.add(henan.getTag() + "");
                } else {
                    areaids.remove(henan.getTag() + "");
                }

            }
        });

        hubei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hubei.isChecked()) {
                    areaids.add(hubei.getTag() + "");
                } else {
                    areaids.remove(hubei.getTag() + "");
                }

            }
        });

        hunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hunan.isChecked()) {
                    areaids.add(hunan.getTag() + "");
                } else {
                    areaids.remove(hunan.getTag() + "");
                }

            }
        });

        jiangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jiangxi.isChecked()) {
                    areaids.add(jiangxi.getTag() + "");
                } else {
                    areaids.remove(jiangxi.getTag() + "");
                }

            }
        });

        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanghai.isChecked()) {
                    areaids.add(tianjin.getTag() + "");
                } else {
                    areaids.remove(tianjin.getTag() + "");
                }

            }
        });

        shanxi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi3.isChecked()) {
                    areaids.add(shanxi3.getTag() + "");
                } else {
                    areaids.remove(shanxi3.getTag() + "");
                }

            }
        });

        shanxi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shanxi1.isChecked()) {
                    areaids.add(shanxi1.getTag() + "");
                } else {
                    areaids.remove(shanxi1.getTag() + "");
                }

            }
        });

        sichuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sichuan.isChecked()) {
                    areaids.add(sichuan.getTag() + "");
                } else {
                    areaids.remove(sichuan.getTag() + "");
                }

            }
        });

        qinghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qinghai.isChecked()) {
                    areaids.add(qinghai.getTag() + "");
                } else {
                    areaids.remove(qinghai.getTag() + "");
                }

            }
        });

        hainan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hainan.isChecked()) {
                    areaids.add(hainan.getTag() + "");
                } else {
                    areaids.remove(hainan.getTag() + "");
                }

            }
        });

        guangdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangdong.isChecked()) {
                    areaids.add(guangdong.getTag() + "");
                } else {
                    areaids.remove(guangdong.getTag() + "");
                }

            }
        });

        guizhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guizhou.isChecked()) {
                    areaids.add(guizhou.getTag() + "");
                } else {
                    areaids.remove(guizhou.getTag() + "");
                }

            }
        });

        fujian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fujian.isChecked()) {
                    areaids.add(fujian.getTag() + "");
                } else {
                    areaids.remove(fujian.getTag() + "");
                }

            }
        });

        taiwan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (taiwan.isChecked()) {
                    areaids.add(taiwan.getTag() + "");
                } else {
                    areaids.remove(taiwan.getTag() + "");
                }

            }
        });

        gansu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gansu.isChecked()) {
                    areaids.add(gansu.getTag() + "");
                } else {
                    areaids.remove(gansu.getTag() + "");
                }

            }
        });

        yunnan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yunnan.isChecked()) {
                    areaids.add(yunnan.getTag() + "");
                } else {
                    areaids.remove(yunnan.getTag() + "");
                }

            }
        });
        neimenggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (neimenggu.isChecked()) {
                    areaids.add(neimenggu.getTag() + "");
                } else {
                    areaids.remove(neimenggu.getTag() + "");
                }

            }
        });

        ningxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ningxia.isChecked()) {
                    areaids.add(ningxia.getTag() + "");
                } else {
                    areaids.remove(ningxia.getTag() + "");
                }

            }
        });

        xinjiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xinjiang.isChecked()) {
                    areaids.add(xinjiang.getTag() + "");
                } else {
                    areaids.remove(xinjiang.getTag() + "");
                }

            }
        });

        xizang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xizang.isChecked()) {
                    areaids.add(xizang.getTag() + "");
                } else {
                    areaids.remove(xizang.getTag() + "");
                }

            }
        });

        guangxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (guangxi.isChecked()) {
                    areaids.add(guangxi.getTag() + "");
                } else {
                    areaids.remove(guangxi.getTag() + "");
                }

            }
        });

        xianggang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (xianggang.isChecked()) {
                    areaids.add(xianggang.getTag() + "");
                } else {
                    areaids.remove(xianggang.getTag() + "");
                }

            }
        });

        aomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aomen.isChecked()) {
                    areaids.add(aomen.getTag() + "");
                } else {
                    areaids.remove(aomen.getTag() + "");
                }

            }
        });


    }


}

